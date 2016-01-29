package com.reviewmerce.exchange.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.exchange.AnalyticsApplication;
import com.reviewmerce.exchange.ListView.BankAdapter;
import com.reviewmerce.exchange.ListView.BankExchangeAdapter;
import com.reviewmerce.exchange.MainActivity;
import com.reviewmerce.exchange.commonData.BankExchangeItem;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread_v2;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.custom.MyMenuBtn;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;
import com.reviewmerce.exchange.publicClass.NetworkInfo;
import com.reviewmerce.exchange.testClass.BankSortedData;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BankFragment extends baseOnebuyFragment implements AdapterView.OnItemSelectedListener {
    GlobalVar mGlobalVar=null;
    NationDataLab mNationLab = null;
    BankAdapter mBankAdapter;
    ListView mListView;
    private ImageView mButtonRefresh;
    MyMenuBtn mButtonMenu;
    TextView tvInfo,tvAd, tvSeperator;
    Rect mNationSelectRect;
    private Button mBtnNationSelect;
//    private int m_nPreTouchPosX = 0;
    private Handler mHandlerHttp;                   // API 통신 쓰레드 핸들
    private boolean m_bConnectThreadRunning=false;
    private boolean mFirstDataGet = false;
    private TextView mButtonExchangeType;
    private TextView mButtonCurrencyType;
    private ArrayList<BankSortedData> mDataArray = null;
    BankDataLab mBankDataLab;
    private LinearLayout mLayout;
    View headerView;
    ImageView ivHeader1;
    ImageView ivHeader2;
    ImageView ivHeader3;

    private ImageView mBackgroundView;
    private Bitmap mBackgroundBitmap=null;

    private boolean bAnimationRunning;
    private Tracker mTracker;
    public BankFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bank, container, false);
        mGlobalVar = GlobalVar.get();
        mNationLab = NationDataLab.get(null);
        setExchangeType(v);
        setCurrencyType(v);
        setBankdataList(v);
        setMenuButton(v);
        mNationSelectRect = new Rect();
        mBtnNationSelect = (Button)v.findViewById(R.id.btnNationSelect);
        tvInfo = (TextView)v.findViewById(R.id.tvInfo);
        tvAd = (TextView)v.findViewById(R.id.tvAd);
        tvSeperator = (TextView)v.findViewById(R.id.tvSeperator);
       // mLayout = (LinearLayout)v.findViewById(R.id.mainLayout_bank);
        mBackgroundView = (ImageView)v.findViewById(R.id.ivScreen_bank);
        mButtonRefresh = (ImageView) v.findViewById(R.id.btnRefresh);
        mButtonRefresh.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mButtonRefresh.setImageResource(R.drawable.ic_refresh1);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
                                                  if((mGlobalVar.getNetworkMode()== false) && (bWifi == false)){
                                                      AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                                                      String sMsg = String.format("모바일 데이터를 사용하지 않도록 설정되어 있습니다. 그래도 모바일 데이터를 사용 하시겠습니까?");
                                                      alt_bld.setMessage(sMsg).setCancelable(
                                                              false).setPositiveButton("Yes",
                                                              new DialogInterface.OnClickListener() {
                                                                  public void onClick(DialogInterface dialog, int id) {
                                                                      refresh();
                                                                  }
                                                              }).setNegativeButton("No",
                                                              new DialogInterface.OnClickListener() {
                                                                  public void onClick(DialogInterface dialog, int id) {
                                                                      // Action for 'NO' Button
                                                                      dialog.cancel();

                                                                  }
                                                              });
                                                      alt_bld.show();
                                                  }
                                                  else
                                                      refresh();

                                              }
                                          }
        );
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.bankMainLinear);
        linearLayout.setOnTouchListener(touchMainListener);
        /*
        new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    m_nPreTouchPosX = (int) event.getX();
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int nTouchPosX = (int) event.getX();

                    if (nTouchPosX < m_nPreTouchPosX - BasicInfo.g_nMovePos)   // 오른쪽
                    {
                        //    Intent intent = new Intent(getActivity(), BankActivity.class);
                        //    startActivity(intent);
                        mCallback.chgFragment(1, 1);
                    } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                        mCallback.chgFragment(1, -1);

                        //MovewPreviousView();
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return true;
            }
        });
        */
        mHandlerHttp = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BasicInfo.TYPE_BANK:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정
                        mFirstDataGet = true;
                        m_bConnectThreadRunning = false;
//                        mDataArray = mBankDataLab.getSortedData();
                        setValue();
                        mBankDataLab.saveData();
                        stopAnimation();
                        break;
                    case BasicInfo.TYPE_ANIMATION_REFRESH:
                        int nDrawable = (int)msg.obj;
                        mButtonRefresh.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        mButtonRefresh.setImageResource(nDrawable);
                        break;
                }
            }
        };
        initData();

        // offline 일 때 교통정리


        int nCount = 0;
        nCount = mBankDataLab.loadData();
        mFirstDataGet = true;
        setValue();

        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
        if((bWifi == true) || (mGlobalVar.getNetworkMode() == true))
            refresh();

        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        Log.i(BasicInfo.TAG, "Setting screen name: " + "BankFragment");
        mTracker.setScreenName("Screen" + "BankFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        return v;
    }
    View.OnTouchListener touchMainListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                m_nPreTouchPosX = (int) event.getX();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int nTouchPosX = (int) event.getX();

                if (nTouchPosX < m_nPreTouchPosX - BasicInfo.g_nMovePos)   // 오른쪽
                {
                    //                     recycleView(mLayout);
                    mCallback.chgFragment(1, 1);
                } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                    //                       recycleView(mLayout);
                    mCallback.chgFragment(1, -1);
                }
                else
                {
                    mBtnNationSelect.getHitRect(mNationSelectRect);
                    if((int)event.getY() <= mNationSelectRect.bottom )
                    {
                        mCallback.makeNationDialog(1);
                    }
                }
                m_nPreTouchPosX = nTouchPosX;
            }
            return false;
        }

    };
    public void setValue()
    {
        mBankDataLab.sortToSortedData();
        mBankAdapter.chgSortedData(mBankDataLab.getSortedData());
        mBankAdapter.notifyDataSetInvalidated();
        tvInfo.setText(mBankDataLab.getRepresentDate() + " " + mBankDataLab.getRepresentTime() + " (" + mBankDataLab.getRepresentBank() + " 기준)");
    }
    public void refresh() {


        if (m_bConnectThreadRunning == false) {
            startAnimation();
            mBankDataLab.clearData();
            m_bConnectThreadRunning = true;
            ConnectMonitorThread_v2 thread = new ConnectMonitorThread_v2(BasicInfo.ApiURL, getActivity(), mHandlerHttp, BasicInfo.TYPE_BANK);
            thread.setCurrency(mNationLab.getCurrencyCodeInEng());
            thread.start();
        }
    }
    public void setBankdataList(View v)
    {
        mListView = (ListView) v.findViewById(R.id.listBank);
        mListView.setSelector(R.drawable.transparent_selector);
        mBankAdapter = new BankAdapter(getActivity());
        headerView = getActivity().getLayoutInflater().inflate(R.layout.fragment_header_bank,null,false);

        ivHeader1 = (ImageView)headerView.findViewById(R.id.ivHeaderBank);
        ivHeader2 = (ImageView)headerView.findViewById(R.id.ivHeaderValue);
        ivHeader3 = (ImageView)headerView.findViewById(R.id.ivHeaderIndex);
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.bankheaderLayout);
        //   layout.setBackgroundColor();
       //headerView.setBackgroundColor(mGlobalVar.getGraphColor());
  //      headerView.setBackgroundResource( R.drawable.header);
        mListView.addHeaderView(headerView);

        mListView.setAdapter(mBankAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //           BankItem curItem = (BankItem) mBankAdapter.getItem(position);
                //            String[] curData = curItem.getData();

                //   Toast.makeText(getApplicationContext(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();

            }

        });


    }

    public void setCurrencyType(View v)
    {
        mButtonCurrencyType = (TextView)v.findViewById(R.id.btnCurrency);

        mButtonCurrencyType.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       /*
                                                       if(bDoingChgCurrency==false)
                                                       {
                                                           bDoingChgCurrency = true;
                                                           final CurrencyAdapter adapter = mNationLab.getCurrencyAdapter(getActivity());
                                                           AlertDialog.Builder alert =
                                                                   new AlertDialog.Builder(getActivity()).//setView(dialogView).
                                                                           setAdapter(adapter, new DialogInterface.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                           CurrencyItem item = (CurrencyItem) (adapter.getItem(which));
                                                                           chgCurrency(item.getData(0));

                                                                           int nCount = 0;
                                                                           nCount = mBankDataLab.loadData();
                                                                           if (nCount > 0) {
                                                                               setValue();
                                                                           }
                                                                           Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
                                                                           if ((bWifi == true) || (mGlobalVar.getNetworkMode())) {
                                                                               refresh();
                                                                           }
                                                                           bDoingChgCurrency = false;

                                                                       }
                                                                   });
                                                           ;
                                                           alert.setOnKeyListener(new Dialog.OnKeyListener() {

                                                               @Override
                                                               public boolean onKey(DialogInterface arg0, int keyCode,
                                                                                    KeyEvent event) {
                                                                   // TODO Auto-generated method stub
                                                                   if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                                       bDoingChgCurrency = false;
                                                                       return false;

                                                                   }
                                                                   return true;
                                                               }
                                                           });
                                                           alert.show();

                                                       }
                                                           */
                                                       mCallback.makeNationDialog(1);
//*/
                                                   }
                                               }
        );
    }
    public void chgCurrency(String sCurrency)
    {
        try {
            mNationLab.chgCurrency(sCurrency);

            //mLayout.setBackgroundResource(GlobalVar.mBackgroundId);
            mButtonCurrencyType.setTextColor(mNationLab.getGraphTextColor());
            mButtonExchangeType.setTextColor(mNationLab.getGraphTextColor());
            tvSeperator.setBackgroundColor(mNationLab.getGraphTextColor());
            tvAd.setTextColor(mNationLab.getGraphTextColor());
            if((mNationLab.getCurrencyCodeInEng().indexOf("JPY")>=0) || (mNationLab.getCurrencyCodeInEng().indexOf("IDR")>=0) ||
                    (mNationLab.getCurrencyCodeInEng().indexOf("VND")>=0) || (mNationLab.getCurrencyCodeInEng().indexOf("KHR")>=0))
                mButtonCurrencyType.setText( mNationLab.getCountryNameInKorean()+ " " + mNationLab.getCurrencyCodeInEng() + "(100)");
            else
                mButtonCurrencyType.setText( mNationLab.getCountryNameInKorean()+ " " + mNationLab.getCurrencyCodeInEng());

            mButtonExchangeType.setText(mGlobalVar.getExchangeType());

            tvInfo.setTextColor(mNationLab.getGraphTextColor());
            chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + mNationLab.getBackgroundFile(), 2);
            if(mBackgroundBitmap==null)
            {
                ((MainActivity)getActivity()).CopyAssets("image/background");
                chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + mNationLab.getBackgroundFile(), 2);
            }
            mBackgroundView.setImageBitmap(mBackgroundBitmap);

            Bitmap bmp = mNationLab.getPurseBar1Bitmap();
            ivHeader1.setImageBitmap(bmp);
            ivHeader2.setImageBitmap(bmp);
            ivHeader3.setImageBitmap(bmp);

            mCallback.saveOptionData();

            Log.i(BasicInfo.TAG, "Change Currency: " + mNationLab.getCurrencyCodeInEng());
            mTracker.setScreenName("Change Currency : " + mNationLab.getCurrencyCodeInEng());
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
        catch (Exception e)
        {
            Log.e(BasicInfo.TAG, e.toString());
        }

    }
    public void chgBackground(String sFilename,int nType)
    {
        switch (nType) {
            case 1:
                try {
                    if (mBackgroundBitmap != null)
                        mBackgroundBitmap.recycle();
                    AssetManager assetMgr = getActivity().getAssets();
                    InputStream is = assetMgr.open(sFilename);
                    mBackgroundBitmap = BitmapFactory.decodeStream(is);
                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try
                {
                    if(mBackgroundBitmap != null) {
                        mBackgroundBitmap.recycle();
                        mBackgroundBitmap = null;
                    }
                    mBackgroundBitmap = BitmapFactory.decodeFile(sFilename);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }
    public void setExchangeType(View v)
    {
        mButtonExchangeType = (TextView)v.findViewById(R.id.btnExchangeType);
        mButtonExchangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///*
                final BankExchangeAdapter adapter = new BankExchangeAdapter(getActivity());

                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(1)));
                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(2)));
                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(3)));
                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(4)));
                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(5)));
                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(6)));
                adapter.addItem(new BankExchangeItem(mBankDataLab.getExchangeType_name(7)));
                ContextThemeWrapper cw = new ContextThemeWrapper(getActivity(),R.style.BankAlertDialogTheme);
                AlertDialog.Builder alert =
                        //new AlertDialog.Builder(cw).//setView(dialogView).
                        new AlertDialog.Builder(getActivity()).
                                setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BankExchangeItem item = (BankExchangeItem) (adapter.getItem(which));
                                mButtonExchangeType.setText(item.getData(0));
                                mGlobalVar.setExchangeType(item.getData(0));

                                setValue();
                                mCallback.saveOptionData();
                            }
                        });
                ;

                alert.show();
                //*/

            };

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void initData()
    {
        mBankDataLab = BankDataLab.get(null);

//        mBankDataLab.setExchangeType((String) mButtonExchangeType.getText());

        chgCurrency(mNationLab.getCurrencyCodeInEng());

    }
    public void setMenuButton(View v)
    {
        mButtonMenu = (MyMenuBtn)v.findViewById(R.id.btnMenu);
        mButtonMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mCallback.chgFragment(5, 1);
                }
                return true;

            }
        });
    }
    public void startAnimation()
    {
        AnimationThread mThreadRefresh = new AnimationThread( getActivity(), mHandlerHttp);
        mThreadRefresh.start();
    }
    public void stopAnimation()
    {
        bAnimationRunning = false;
    }
    public class AnimationThread extends Thread {
        int nDuration = 100;
        final int MaxImage=8;
        Handler mParentHandler;
        Context mParentContext;
        final int imageId[] = {R.drawable.ic_refresh1,
                R.drawable.ic_refresh2,
                R.drawable.ic_refresh3,
                R.drawable.ic_refresh4,
                R.drawable.ic_refresh5,
                R.drawable.ic_refresh6,
                R.drawable.ic_refresh7,
                R.drawable.ic_refresh8};
        int nCurrentIndex = 0 ;

        public AnimationThread(Context ctx, Handler handler) {
            mParentHandler = handler;
            mParentContext = ctx;
        }
        public void run()
        {
            bAnimationRunning = true;

            while(bAnimationRunning)
            {
                Message msg = Message.obtain();
                msg.obj = imageId[nCurrentIndex];
                msg.what =  BasicInfo.TYPE_ANIMATION_REFRESH;
                mParentHandler.sendMessage(msg);


                nCurrentIndex ++;
                if(nCurrentIndex >= MaxImage)
                    nCurrentIndex = 0;
                try {
                    Thread.sleep(nDuration);
                }
                catch (InterruptedException ex)
                {
                    Log.e("Animation","Animation Thread Exception");
                }
            }

            Message msg = Message.obtain();
            msg.obj = imageId[nCurrentIndex];
            msg.what =  BasicInfo.TYPE_ANIMATION_REFRESH;
            msg.obj = imageId[0];
            mParentHandler.sendMessage(msg);
        }
    }
    /*
    public void makeNationDialog()
    {
        try {
            if(getActivity()!=null) {
                //               /*
                if(NationFragment.m_bRunning == false) {
                    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                    NationFragment nf = new NationFragment();
                    nf.setCallFragmentId(1);
                    String sz = "hi";
                    nf.show(fm, sz);
                }
            }
        }
        catch(Exception e)
        {
            Log.e(BasicInfo.TAG,"makeDialog Err");
        }
    }
    */
    public void OnDialogOkListener(String sValue)
    {
        chgCurrency(sValue);

        int nCount = 0;
        nCount = mBankDataLab.loadData();
        if (nCount > 0) {
            setValue();
        }
        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
        if ((bWifi == true) || (mGlobalVar.getNetworkMode())) {
            refresh();
        }
    }
}
