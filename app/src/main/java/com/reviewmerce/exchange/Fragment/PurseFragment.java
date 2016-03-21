package com.reviewmerce.exchange.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.exchange.AnalyticsApplication;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread_v2;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.Activity.MainActivity;
import com.reviewmerce.exchange.ListView.PurseAdapter_2;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.custom.MyMenuBtn;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;
import com.reviewmerce.exchange.publicClass.NetworkInfo;
import com.reviewmerce.exchange.publicClass.PurseDataLab;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PurseFragment extends baseOnebuyFragment implements baseOnebuyFragment.PurseInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PurseDataLab mPurseDatalab=null;
    private LiveDataLab mLiveDatalab=null;
    // TODO: Rename and change types of parameters

    GlobalVar mGlobalVar = null;
    NationDataLab mNationLab = null;
    Handler mHandlerHttp;
    private boolean m_bConnectThreadRunning = false;
   // TextView mtvCurrency, mtvConvertedValue, mtvDate;//, tvSeperator;
    PurseBudgetDialogFragment mDialogPurseBudget=null;
    PurseInputDialogFragment mDialogPurseInput=null;
//    Rect mNationSelectRect;
//    private Button mBtnNationSelect;
    private ImageView mBackgroundView;
    private int mStatusView;
    private Bitmap mBackgroundBitmap=null;
    private Bitmap mBar1Bitmap = null;
    private Bitmap mBar2Bitmap = null;
    private boolean bIsKeyboard = false;
 //   private FrameLayout mTopLayout;
   // /////////////////////////////////////////////////////////////////////////////
    FrameLayout mFrameLayout;
    LinearLayout mParentLinearLayout;
    LinearLayout mMainLayout;

 //   MyMenuBtn mButtonMenu;
    ArrayList<PurseData> listPurseData;
    PurseData delPurseData;
    double mResultValKRW = .0f;
    double mResultVal = .0f;
    RecyclerView mMain_lvBody;
    private PurseAdapter_2 mMainlistAdapter;
    ImageView mMain_ivHeader1,mMain_ivHeader2;
    TextView mMain_tvBudgetDate, mMain_tvBudgetValue, mMain_tvResultTitle,mMain_tvResultKRWValue, mMain_tvResuleValueKRWMark, mMain_tvResultValue, mMain_tvResuleValueMark;
    ImageView mMain_ivCat;
/////////////////////////////////////////////////////////////////////////////
    private boolean bAnimationRunning;
    private Tracker mTracker;
    private OnFragmentInteractionListener mListener;
    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_purse, container, false);
//        mNationSelectRect = new Rect();
        mStatusView = 0;
        mGlobalVar = GlobalVar.get();
        mNationLab = NationDataLab.get(null);
        prepareListData();
        initLinearView(v);
        initFragmentView(v);
//        setMenuButton(v);



        mHandlerHttp = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BasicInfo.TYPE_MONITOR_LIVE:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정
                        m_bConnectThreadRunning = false;
                        mLiveDatalab.saveLiveData_DB();
       //                 saveData();
                        dataToView();
                        stopAnimation();
                        break;
                    case BasicInfo.TYPE_ANIMATION_REFRESH:
                        int nDrawable = (int)msg.obj;

                        break;
                }
            }
        };

        mPurseDatalab = PurseDataLab.get(null);
        mLiveDatalab = LiveDataLab.get(null);

        calcBudgetOldVer();
        chgCurrency(mNationLab.getCurrencyCodeInEng());
        dataToView();

        // offline 모드일 때 교통정리
        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
        if((mGlobalVar.getNetworkMode() == true) || (bWifi == true))
            refresh();


        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        Log.i(BasicInfo.TAG, "Setting screen name: " + "PurseFragment");
        mTracker.setScreenName("Screen" + "PurseFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //testFunc();
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
                    mCallback.chgFragment(BasicInfo.FRAGMENT_MOVE, 1);
                } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                    //                       recycleView(mLayout);
                    mCallback.chgFragment(BasicInfo.FRAGMENT_MOVE, -1);
                }
                m_nPreTouchPosX = nTouchPosX;
            }
            return false;
        }

    };


    @Override
    public void onResume() {
        mCallback.chgBottombar(BasicInfo.FRAGMENT_EXCHANGE_PURSE,"Purse");
        super.onResume();
    }
    private void initFragmentView(View v)
    {
        mBackgroundView = (ImageView)v.findViewById(R.id.ivScreen_graph);

    }
    private void initLinearView(View v)
    {
        LayoutInflater infalInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mFrameLayout = new FrameLayout(getActivity());
        mParentLinearLayout = (LinearLayout)v.findViewById(R.id.purseMainLinear);
        mMainLayout = (LinearLayout) infalInflater.inflate(R.layout.fragment_purse_main, null);

        mFrameLayout.addView(mMainLayout);
        mParentLinearLayout.addView(mFrameLayout);
        initMainLayoutView(mMainLayout);
            }
    private void initMainLayoutView(View v)
    {
        mMain_ivHeader1 = (ImageView)mMainLayout.findViewById(R.id.ivHeader1);

        mMain_ivHeader1.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   changeView(BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET);
                                               }
                                           }
        );
        mMain_ivHeader2 = (ImageView)mMainLayout.findViewById(R.id.ivHeader2);

        mMain_ivHeader2.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   /*
                                                   mMainLayout.setVisibility(View.GONE);
                                                   mBudgetLayout.setVisibility(View.GONE);
                                                   mInsertLayout.setVisibility(View.VISIBLE);
                                                   */
                                                   changeView(BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT);
                                               }
                                           }
        );
        mMain_tvBudgetDate = (TextView)v.findViewById(R.id.tv_purse_budget_date);
        mMain_tvResultTitle = (TextView)v.findViewById(R.id.tv_purse_result_title);
        mMain_tvBudgetValue = (TextView)v.findViewById(R.id.tv_purse_budget_value);

        mMain_tvResultKRWValue  = (TextView)v.findViewById(R.id.tv_purse_result_value_krw);
        mMain_tvResuleValueKRWMark = (TextView)v.findViewById(R.id.tv_purse_result_value_krw_mark);
        mMain_tvResultValue = (TextView)v.findViewById(R.id.tv_purse_result_value);
        mMain_tvResuleValueMark  = (TextView)v.findViewById(R.id.tv_purse_result_value_mark);

        mMain_ivCat = (ImageView)v.findViewById(R.id.ivCat);
        mMain_ivCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sFile = BasicInfo.InternalPath + "/image/meow.mp3";
                try {
                    playAudio(sFile);
                } catch (Exception e) {
                }
            }
        });
        initMainListView(v);
        LinearLayout layoutMain = (LinearLayout)mMainLayout.findViewById(R.id.purseLayout_main);
        layoutMain.setOnTouchListener(touchMainListener);
    }
    public void initMainListView(View v)
    {
        mMain_lvBody = (RecyclerView) v.findViewById(R.id.tv_purse_listview);

        //LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
  //      mMain_lvBody.setHasFixedSize(true);
        mMain_lvBody.setLayoutManager(layoutManager);
    }

    private void prepareListData() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public void chgCurrency(String sCurrncy)
    {

        try {
            mNationLab.chgCurrency(sCurrncy);

//            mtvCurrency.setTextColor(mNationLab.getGraphTextColor());
//            mtvCurrency.setTextColor(mNationLab.getGraphTextColor());
//
//            if((mNationLab.getCurrencyCodeInEng().indexOf("JPY")>=0) || (mNationLab.getCurrencyCodeInEng().indexOf("IDR")>=0) ||
//                    (mNationLab.getCurrencyCodeInEng().indexOf("VND")>=0) || (mNationLab.getCurrencyCodeInEng().indexOf("KHR")>=0))
//                mtvCurrency.setText(mNationLab.getCountryNameInKorean() + " " + mNationLab.getCurrencyCodeInEng() + "(100)");
//            else
//                mtvCurrency.setText(mNationLab.getCountryNameInKorean() + " " + mNationLab.getCurrencyCodeInEng());

            chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + mNationLab.getBackgroundFile(), 2);
            if(mBackgroundBitmap==null)
            {
                ((MainActivity)getActivity()).CopyAssets("image/background");
                chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + mNationLab.getBackgroundFile(), 2);
            }
            mBackgroundView.setImageBitmap(mBackgroundBitmap);
            mBar1Bitmap = mNationLab.getPurseBar1Bitmap();
            mBar2Bitmap = mNationLab.getPurseBar2Bitmap();

//            mtvConvertedValue.setTextColor(mNationLab.getGraphTextColor());
      //      tvSeperator.setBackgroundColor(mNationLab.getGraphTextColor());
//            mtvDate.setTextColor(mNationLab.getGraphTextColor());
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

    public void saveData()
    {
        if(mPurseDatalab != null)
            mPurseDatalab.savePurseData_DB();
    }
    public void loadData()
    {
        if(mPurseDatalab != null) {
            mPurseDatalab.loadPurseData_DB();
        if(mLiveDatalab != null)
        {
            mLiveDatalab.loadLiveData_DB();
        }

            dataToView();
        }
    }
    @Override
    public void applyData_to_view(int nFragment)
    {
        switch(nFragment)
        {
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT:
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT:
                mDialogPurseInput = null;
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET:
                mDialogPurseBudget = null;
                break;
        }
        dataToView();
    }
    public void dataToView() {
        calcResult();
        PurseAdapter_2 adapter = new PurseAdapter_2(getContext(),mPurseDatalab.getData(),R.layout.list_purse);
        adapter.setCallback(this);

        mMain_lvBody.setAdapter(adapter);
//        int height = mMain_lvBody.getHeight();
//        mMain_lvBody.scrollTo(0,height);

      //  PurseAdapter_2 adapter = (PurseAdapter_2)mMain_lvBody.getAdapter();

     //   adapter.setListItems(mPurseDatalab.getData());
    //    adapter.setCurrencyStr(mNationLab.getCurrencyChar());
   //     adapter.notifyDataSetChanged();

        String strVal = String.valueOf(mPurseDatalab.getBeginDate_month())+ "/"+String.valueOf(mPurseDatalab.getBeginDate_day() + " ~ " +
                mPurseDatalab.getEndDate_month())+ "/"+String.valueOf(mPurseDatalab.getEndDate_day());
        mMain_tvBudgetDate.setText(strVal);
        strVal = String.format("%.2f",mPurseDatalab.getBudgetKRW()) + " 원";
        mMain_tvBudgetValue.setText(strVal);

        ///////////////////////////////////////////////////////////////////////////////
        //
       // double dResultVal = getExchangeVal(mPurseDatalab.getBudgetCurrency(),mResultValKRW,false);          // 남은 원화의 환율 변환값

        ///////////////////////////////////////////////////////////////////////////////

        mMain_tvResultKRWValue.setText(String.format("%.2f", mResultValKRW));
        mMain_tvResuleValueKRWMark.setText("원 ");
        mMain_tvResultValue.setText(String.format("%.2f", mResultVal));
        mMain_tvResuleValueMark.setText(mNationLab.getCurrencyChar(mPurseDatalab.getBudgetCurrency()) + " ");

        double dConvertedVal = .0f;
        if((mPurseDatalab.getBudgetCurrency().indexOf("JPY")>=0) || (mPurseDatalab.getBudgetCurrency().indexOf("IDR")>=0) ||
                (mPurseDatalab.getBudgetCurrency().indexOf("VND")>=0) || (mPurseDatalab.getBudgetCurrency().indexOf("KHR")>=0))
            dConvertedVal= mLiveDatalab.getCurrencyValue(mPurseDatalab.getBudgetCurrency())*100;
        else
            dConvertedVal= mLiveDatalab.getCurrencyValue(mPurseDatalab.getBudgetCurrency());

//        mtvConvertedValue.setText(String.format("%.2f", dConvertedVal) + " 원");
//        mtvDate.setText(mLiveDatalab.getLiveDate_Time(mNationLab.getCurrencyCodeInEng()) + " (외환은행 기준)");

    }

    public void calcResult()
    {
        double dCurrencyValue = .0f;
        if(listPurseData == null)
            listPurseData = new ArrayList<PurseData>();
        else
            listPurseData.clear();
        double dExchangeVal;
        double  dTotalValKRW=.0f;
        for(PurseData p : mPurseDatalab.getData())
        {
            dExchangeVal = p.getValue();
            dExchangeVal = getExchangeVal(p.getCurrencyCode(),dExchangeVal,true);
            dTotalValKRW += dExchangeVal;
        }
        mResultValKRW = mPurseDatalab.getBudgetKRW() - dTotalValKRW;
        double dTotalAnotherVal = getExchangeVal(mPurseDatalab.getBudgetCurrency(),dTotalValKRW,false);
        mResultVal = mPurseDatalab.getBudget() - dTotalAnotherVal;
    }
    public void deleteItem(int nPosition)
    {
        PurseAdapter_2 adapter = (PurseAdapter_2)mMain_lvBody.getAdapter();
        delPurseData = (PurseData)adapter.getItem(nPosition);
        deleteOk_dialog();
    }
    public void editItem(int nPosition)
    {
        PurseAdapter_2 adapter = (PurseAdapter_2)mMain_lvBody.getAdapter();
        delPurseData = (PurseData)adapter.getItem(nPosition);
        changeView(BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT);
    }

    private void deleteOk_dialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
        String sMsg = String.format("%d번째 \"%s (%s%.2f)\"를 삭제 하시겠습니까?",
                delPurseData.getIndex()+1,delPurseData.getTitle(),mNationLab.getCurrencyChar(delPurseData.getCurrencyCode()),delPurseData.getValue() );
        alt_bld.setMessage(sMsg).setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPurseDatalab.delItem(delPurseData);
                        listPurseData.remove(delPurseData);
                        saveData();
                        dataToView();
                 //      mMain_lvBody.clearChoices();
                       // mMainlistAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("정말 지우시겠습니까?");
        // Icon for AlertDialog

        alert.show();
    }
    public void refresh() {

        if(m_bConnectThreadRunning == false) {
            startAnimation();
            mLiveDatalab.removeAllLiveItem();
            m_bConnectThreadRunning = true;
            ConnectMonitorThread_v2 thread = new ConnectMonitorThread_v2(BasicInfo.ApiURL, getActivity(), mHandlerHttp, BasicInfo.TYPE_PURSE);
            thread.setCurrency(mNationLab.getCurrencyCodeInEng());
            thread.start();

        }
    }
    public double getExchangeVal(String sCurrency,double dVal, boolean bGetKor)
    {
        double dExchangeVal = mLiveDatalab.getCurrencyValue(sCurrency);
    //    if((sCurrency.indexOf("JPY")>=0) || (sCurrency.indexOf("IDR")>=0))
    //        dExchangeVal = dExchangeVal / 100;

        double dExchangeBudget = .0f;
        if(bGetKor)
        {
            if(dExchangeVal != .0f)
                dExchangeBudget =  dVal * dExchangeVal;
            else
                dExchangeBudget = .0f;
        }
        else {
            if (dExchangeVal != .0f)
                dExchangeBudget = dVal / dExchangeVal;
            else
                dExchangeBudget = .0f;
        }
        return dExchangeBudget;
    }
    private void closeKeyboard()
    {
        try {
            if(getActivity().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.i(BasicInfo.TAG, e.toString());
        }

        bIsKeyboard = false;
    }

    private void changeView(int nType)
    {
        mStatusView = nType;
        switch (nType)
        {
            case 0:         // 메인화면
                mMainLayout.setVisibility(View.VISIBLE);
                enableDisableViewGroup(mMainLayout, true);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET:         // 예산입력
                if(mDialogPurseBudget!=null)
                    break;
                Bundle args = new Bundle();
                args.putString("title", "initsearch");
                args.putInt("type", BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET);

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                mDialogPurseBudget = new PurseBudgetDialogFragment();
                mDialogPurseBudget.setResultCallback((baseOnebuyFragment.PurseInterface)this);
                mDialogPurseBudget.setArguments(args);
                mDialogPurseBudget.show(fm, "hi");
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT:         // 지출입력
                if(mDialogPurseInput!=null)
                    break;
                args = new Bundle();
                args.putString("title", "initsearch");
                args.putInt("type", BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT);

                fm = getActivity().getSupportFragmentManager();
                mDialogPurseInput = new PurseInputDialogFragment();
                mDialogPurseInput.setResultCallback((baseOnebuyFragment.PurseInterface) this);
                mDialogPurseInput.setArguments(args);

                mDialogPurseInput.show(fm, "hi");

                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT:         // 입력 내용 변경
                if(mDialogPurseInput!=null)
                    break;
                args = new Bundle();
                args.putString("title", "initsearch");
                args.putInt("type", BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT);
                args.putParcelable("editdata", delPurseData);

                fm = getActivity().getSupportFragmentManager();
                mDialogPurseInput = new PurseInputDialogFragment();
                mDialogPurseInput.setResultCallback((baseOnebuyFragment.PurseInterface)this);
                mDialogPurseInput.setArguments(args);
                mDialogPurseInput.show(fm, "hi");
                break;

        }

        closeKeyboard();
    }
    public void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {


            View view = viewGroup.getChildAt(i);
            if(enabled == true)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
            view.setEnabled(enabled);

            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }
    /*
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
    */
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



    public void OnDialogOkListener(int nType, String sValue)
    {
        if(nType == BasicInfo.FRAGMENT_EXCHANGE_PURSE)
        {
            chgCurrency(sValue);
            saveData();
            dataToView();
        }
        if(nType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET)
        {
            if(mDialogPurseBudget != null)
            {
                mDialogPurseBudget.changeCurrency(sValue);
            }
        }
        else if((nType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT) || (nType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT))
        {
            if(mDialogPurseInput != null)
            {
                mDialogPurseInput.changeCurrency(sValue);
            }
        }




    }
    private void playAudio(String url) throws Exception
    {


        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.setVolume(0.1f,0.1f);
        mediaPlayer.start();
    }
    private void killMediaPlayer()
    {
        if(mediaPlayer != null) {
            try
            {
                mediaPlayer.release();
            } catch (Exception e)
            {
            }
        }
    }
    private void calcBudgetOldVer()
    {
        double dBudget = mPurseDatalab.getBudget();
        double dBudgetKr = mPurseDatalab.getBudgetKRW();
        if((dBudgetKr!=.0f) && (dBudget == .0f))
        {
            dBudget = getExchangeVal(mPurseDatalab.getBudgetCurrency(),dBudgetKr,false);
        }
        mPurseDatalab.setBudget(dBudget);
    }
}
