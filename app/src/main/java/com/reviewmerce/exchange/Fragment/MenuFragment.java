package com.reviewmerce.exchange.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.BankAdapter;
import com.reviewmerce.exchange.ListView.BankExchangeAdapter;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.BankExchangeItem;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.custom.MyMenuBtn;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.testClass.BankSortedData;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuFragment extends baseOnebuyFragment implements AdapterView.OnItemSelectedListener {
    GlobalVar mGlobalVar=null;
    BankAdapter mBankAdapter;
    ListView mListView;
    ImageButton mButtonRefresh;
    MyMenuBtn mButtonMenu;
    TextView tvInfo,tvAd;
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
    private ImageView mBackgroundView;
    private Bitmap mBackgroundBitmap=null;
    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        mGlobalVar = GlobalVar.get();
        setExchangeType(v);
        setCurrencyType(v);
        setBankdataList(v);
        setMenuButton(v);
        tvInfo = (TextView)v.findViewById(R.id.tvInfo);
        tvAd = (TextView)v.findViewById(R.id.tvAd);
       // mLayout = (LinearLayout)v.findViewById(R.id.mainLayout_bank);
        mBackgroundView = (ImageView)v.findViewById(R.id.ivScreen_bank);
        mButtonRefresh = (ImageButton) v.findViewById(R.id.btnRefresh);
        mButtonRefresh.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  refresh();
                                              }
                                          }
        );
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.bankMainLinear);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
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
                        mCallback.chgFragment(5, 1);
                    } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                        mCallback.chgFragment(5, -1);

                        //MovewPreviousView();
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return true;
            }
        });
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
                        break;
                }
            }
        };
        initData();
        int nCount = 0;
        nCount = mBankDataLab.loadData();
        // offline 일 때 교통정리
        refresh();
        if(nCount>0)
        {
            mFirstDataGet = true;
            setValue();
        }
        else if(nCount == 0)
            refresh();
        return v;
    }
    public void setValue()
    {
        mBankDataLab.sortToSortedData();
        mBankAdapter.chgSortedData(mBankDataLab.getSortedData());
        mBankAdapter.notifyDataSetInvalidated();
        tvInfo.setText(mBankDataLab.getRepresentDate() + " " + mBankDataLab.getRepresentTime() + " (" + mBankDataLab.getRepresentBank() + " 기준)");
    }
    public void refresh() {


        if (m_bConnectThreadRunning == false) {
//            if (!NetworkInfo.Is3GAvailable(getActivity()) && !NetworkInfo.IsWifiAvailable(getActivity()))
//                return;
            mBankDataLab.clearData();
            m_bConnectThreadRunning = true;
            ConnectMonitorThread thread = new ConnectMonitorThread(BasicInfo.ApiURL, getActivity(), mHandlerHttp, BasicInfo.TYPE_BANK);
            thread.setCurrency(mGlobalVar.getCurrencyCodeInEng());
            thread.start();
        }
    }
    public void setBankdataList(View v)
    {
        mListView = (ListView) v.findViewById(R.id.listBank);
        mBankAdapter = new BankAdapter(getActivity());
        headerView = getActivity().getLayoutInflater().inflate(R.layout.fragment_header_bank,null,false);
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.bankheaderLayout);
        //   layout.setBackgroundColor();
       headerView.setBackgroundColor(mGlobalVar.getGraphColor());
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
                                                       final CurrencyAdapter adapter = mGlobalVar.getCurrencyAdapter(getActivity());
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
                                                                       if (nCount == 0)
                                                                           refresh();
                                                                   }
                                                               });
                                                       ;

                                                       alert.show();
//*/
                                                   }
                                               }
        );
    }
    public void chgCurrency(String sCurrency)
    {
        try {
            mGlobalVar.chgCurrency(sCurrency);

            //mLayout.setBackgroundResource(GlobalVar.mBackgroundId);
            mButtonCurrencyType.setTextColor(mGlobalVar.getGraphTextColor());
            mButtonExchangeType.setTextColor(mGlobalVar.getGraphTextColor());
            tvAd.setTextColor(mGlobalVar.getGraphTextColor());
            if((mGlobalVar.getCurrencyCodeInEng().indexOf("JPY")>=0) || (mGlobalVar.getCurrencyCodeInEng().indexOf("IDR")>=0))
                mButtonCurrencyType.setText(mGlobalVar.getCountryNameInKorean() + " " + mGlobalVar.getCurrencyCodeInEng() + "(100)");
            else
                mButtonCurrencyType.setText(mGlobalVar.getCountryNameInKorean() + " " + mGlobalVar.getCurrencyCodeInEng());

            mButtonExchangeType.setText(mGlobalVar.getExchangeType());
       // onebuy     headerView.setBackgroundColor(GlobalVar.mGraphFillColor);
            tvInfo.setTextColor(mGlobalVar.getGraphTextColor());
            chgBackground(BasicInfo.InternalPath + "image/flag/" + mGlobalVar.getBackgroundFile(), 2);
            mBackgroundView.setImageBitmap(mBackgroundBitmap);
            headerView.setBackgroundColor(mGlobalVar.getGraphColor()); // onebuy
            mCallback.saveOptionData();
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
        mBankDataLab = BankDataLab.get(getActivity());

//        mBankDataLab.setExchangeType((String) mButtonExchangeType.getText());

        chgCurrency(mGlobalVar.getCurrencyCodeInEng());

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
                    Toast.makeText(getActivity(), "Menu", Toast.LENGTH_SHORT).show();
                }
                return true;

            }
        });
    }
}
