package com.reviewmerce.exchange.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.exchange.AnalyticsApplication;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.BankAdapter;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.ListView.PurseAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.custom.MyMenuBtn;
import com.reviewmerce.exchange.publicClass.ExchangeDataLab;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NetworkInfo;
import com.reviewmerce.exchange.publicClass.PurseDataLab;
import com.reviewmerce.exchange.publicClass.SwipeDetector;
import com.reviewmerce.exchange.testClass.BankSortedData;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PurseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurseFragment extends baseOnebuyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PurseDataLab mPurseDatalab=null;
    private LiveDataLab mLiveDatalab=null;
    // TODO: Rename and change types of parameters

    GlobalVar mGlobalVar = null;
    Handler mHandlerHttp;
    private boolean m_bConnectThreadRunning = false;
    TextView mtvCurrency, mtvConvertedValue, mtvDate;
    private ImageView mBackgroundView;
    private int mStatusView;
    private Bitmap mBackgroundBitmap=null;
    private boolean bIsKeyboard = false;
    /////////////////////////////////////////////////////////////////////////////
    FrameLayout mFrameLayout;
    LinearLayout mParentLinearLayout;
    LinearLayout mBudgetLayout, mMainLayout,mInsertLayout;
    private ImageView mButtonRefresh;
    MyMenuBtn mButtonMenu;
    ArrayList<PurseData> listPurseData;
    PurseData delPurseData;
    double mResultVal = .0f;

    ListView mMain_lvBody;
    PurseAdapter mMainlistAdapter;
    ImageView mMain_ivHeader1,mMain_ivHeader2;
    TextView mMain_tvBudgetDate, mMain_tvBudgetValue, mMain_tvResultTitle,mMain_tvResultKRWValue, mMain_tvResuleValueKRWMark, mMain_tvResultValue, mMain_tvResuleValueMark;


    ImageView mBudget_ivHeader;
    TextView mBudget_tvDate1, mBudget_tvDate2, mBudget_etValue, mBudget_etValue_kor;


    ImageView mInsert_ivHeader;
    TextView  mInsert_tvItemValue,mInsert_tvDateValue,mInsert_tvInputValue;
    TextView  mInsert_tvItemTitle,mInsert_tvDateTitle,mInsert_tvInputTitle;
    /////////////////////////////////////////////////////////////////////////////
    private Boolean bDoingChgCurrency = false;
    private boolean bAnimationRunning;
    private Tracker mTracker;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurseFragment newInstance(String param1, String param2) {
        PurseFragment fragment = new PurseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PurseFragment() {
        // Required empty public constructor
    }

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

        mStatusView = 0;
        mGlobalVar = GlobalVar.get();
        prepareListData();
        initLinearView(v);
        initFragmentView(v);
        setMenuButton(v);
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.purseMainLinear);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    m_nPreTouchPosX = (int) event.getX();
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int nTouchPosX = (int) event.getX();

                    if (nTouchPosX < m_nPreTouchPosX - BasicInfo.g_nMovePos)   // 오른쪽
                    {
                        //                     recycleView(mLayout);
                        mCallback.chgFragment(2, 1);
                    } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                        //                       recycleView(mLayout);
                        mCallback.chgFragment(2, -1);
                    }

                    m_nPreTouchPosX = nTouchPosX;
                    if ((mStatusView == 1) || (mStatusView == 2)) {
                        if (bIsKeyboard == true) {
                            closeKeyboard();
                        }
                        else {
                            changeView(0);
                        }
                    }
                }
                return false;
            }
        });
        mHandlerHttp = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BasicInfo.TYPE_PURSE:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정
                        m_bConnectThreadRunning = false;
                        mLiveDatalab.saveLiveData_DB();
                        saveData();
                        dataToView();
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

        mPurseDatalab = PurseDataLab.get(getActivity());
        mLiveDatalab = LiveDataLab.get(getActivity());

        chgCurrency(mGlobalVar.getCountryNameInKorean());
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

    private void initFragmentView(View v)
    {
        mBackgroundView = (ImageView)v.findViewById(R.id.ivScreen);
        mButtonRefresh = (ImageView)v.findViewById(R.id.btnRefresh);
        mButtonRefresh.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mButtonRefresh.setImageResource(R.drawable.ic_refresh1);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
                                                  if ((mGlobalVar.getNetworkMode() == false) && (bWifi == false)) {
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
                                                  } else
                                                      refresh();

                                              }
                                          }
        );
        mtvCurrency = (TextView)v.findViewById(R.id.tvCurrency);

        mtvCurrency.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //if(bDoingChgCurrency==false)
                                               {
                                                   bDoingChgCurrency = true;
                                                   Resources res = getActivity().getResources();
                                                   final CurrencyAdapter adapter = mGlobalVar.getCurrencyAdapter(getActivity());
//                                               adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.kor), "KOR", "대한민국"));
                                                   AlertDialog.Builder alert =
                                                           new AlertDialog.Builder(getActivity()).//setView(dialogView).
                                                                   setAdapter(adapter, new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialog, int which) {
                                                                   CurrencyItem item = (CurrencyItem) (adapter.getItem(which));
                                                                   chgCurrency(item.getData(0));
                                                                   saveData();
                                                                   dataToView();
                                                                   bDoingChgCurrency = false;

                                                               }
                                                           });
                                                   ;
                                                   alert.show();

                                               }
//*/
                                           }
                                       }
        );
        mtvConvertedValue = (TextView)v.findViewById(R.id.tvConverted_value);
        mtvDate = (TextView)v.findViewById(R.id.tvDate);


    }
    private void initLinearView(View v)
    {
        LayoutInflater infalInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mFrameLayout = new FrameLayout(getActivity());

        mParentLinearLayout = (LinearLayout)v.findViewById(R.id.purseMainLinear);
        mBudgetLayout = (LinearLayout)infalInflater.inflate(R.layout.fragment_purse_budget, null);
        mBudgetLayout.setVisibility(View.GONE);
        mMainLayout = (LinearLayout)infalInflater.inflate(R.layout.fragment_purse_main, null);
        mInsertLayout = (LinearLayout)infalInflater.inflate(R.layout.fragment_purse_insertitem, null);
        mInsertLayout.setVisibility(View.GONE);

        mFrameLayout.addView(mMainLayout);
        mFrameLayout.addView(mBudgetLayout);
        mFrameLayout.addView(mInsertLayout);
        mParentLinearLayout.addView(mFrameLayout);

        initMainLayoutView(mMainLayout);
        initBudgetLayoutView(mBudgetLayout);
        initInsertLayoutView(mInsertLayout);
    }
    private void initMainLayoutView(View v)
    {
        mMain_ivHeader1 = (ImageView)mMainLayout.findViewById(R.id.ivHeader1);

        mMain_ivHeader1.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   changeView(1);
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
                                                   changeView(2);
                                                   GregorianCalendar today = new GregorianCalendar();
                                                   int year = today.get(today.YEAR);
                                                   int month = today.get(today.MONTH) + 1;
                                                   int day = today.get(today.DAY_OF_MONTH);
                                                   Date time = today.getTime();

                                                   mInsert_tvInputValue.setText("");
                                                   mInsert_tvDateValue.setText(String.format("%2d/%2d", month,day));
                                                   mInsert_tvItemValue.setText("");
                                                   mInsertLayout.setPadding(0, 60, 0, 0);
                                               }
                                           }
        );
     //   mMain_ivHeader3 = (ImageView)mMainLayout.findViewById(R.id.ivHeader3);
        mMain_tvBudgetDate = (TextView)v.findViewById(R.id.tv_purse_budget_date);
        mMain_tvResultTitle = (TextView)v.findViewById(R.id.tv_purse_result_title);
        mMain_tvBudgetValue = (TextView)v.findViewById(R.id.tv_purse_budget_value);

        mMain_tvResultKRWValue  = (TextView)v.findViewById(R.id.tv_purse_result_value_krw);
        mMain_tvResuleValueKRWMark = (TextView)v.findViewById(R.id.tv_purse_result_value_krw_mark);
        mMain_tvResultValue = (TextView)v.findViewById(R.id.tv_purse_result_value);
        mMain_tvResuleValueMark  = (TextView)v.findViewById(R.id.tv_purse_result_value_mark);

        initMainListView(v);
    }
    public void initMainListView(View v)
    {
        mMain_lvBody = (ListView) v.findViewById(R.id.tv_purse_listview);

        mMainlistAdapter = new PurseAdapter(getActivity());
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.bankheaderLayout);
        //   layout.setBackgroundColor();
        mMain_lvBody.setAdapter(mMainlistAdapter);
        final SwipeDetector swipeDetector = new SwipeDetector();
        mMain_lvBody.setOnTouchListener(swipeDetector);
        mMain_lvBody.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (swipeDetector.swipeDetected()) {

                    PurseData pd = (PurseData) parent.getItemAtPosition(position);
                    delPurseData = pd;
                    deleteOk_dialog();
                    //PurseData item = new PurseData(nIndex,sTitle,sDate,sTime,dValue);


                } else {
                    // do the onItemClick action
                }
            }
        });
        mMain_lvBody.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    PurseData pd = (PurseData) parent.getItemAtPosition(position);
                    delPurseData = pd;


                    return true;
                } else {
                    // do the onItemLongClick action
                    return true;
                }
            }
        });
    }
    private void initBudgetLayoutView(View v)
    {
        mBudget_ivHeader = (ImageView)v.findViewById(R.id.ivHeader);

        mBudget_ivHeader.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    changeView(0);
                                                    saveData();
                                                    dataToView();
                                                }
                                            }
        );
        mBudget_ivHeader.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    changeView(0);

                    return true;
                } else {
                    return false;
                }
            }
        });
        mBudget_tvDate1 = (TextView)v.findViewById(R.id.tv_purse_budget_date1);
        mBudget_tvDate2 = (TextView)v.findViewById(R.id.tv_purse_budget_date2);
        mBudget_etValue = (TextView)v.findViewById(R.id.et_purse_budget_value);
        mBudget_etValue_kor = (TextView)v.findViewById(R.id.et_purse_budget_value_kor);

        GregorianCalendar today = new GregorianCalendar();
        final int todayYear = today.get(today.YEAR);
        final int todayMonth = today.get(today.MONTH) + 1;
        final int todayDay = today.get(today.DAY_OF_MONTH);

        mBudget_tvDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nYear = mPurseDatalab.getBeginDate_year();

                int nMonth = mPurseDatalab.getBeginDate_month();
                int nDay = mPurseDatalab.getBeginDate_day();
                if ((nYear == 0) || (nMonth == 0) || (nDay == 0)) {
                    nYear = todayYear;
                    nMonth = todayMonth;
                    nDay = todayDay;
                }
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mPurseDatalab.setBeginDate(year, monthOfYear + 1, dayOfMonth);
                                dataToView();
                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, nYear, nMonth - 1, nDay);
                alert.show();
            }
        });
        mBudget_tvDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nYear = mPurseDatalab.getEndDate_year();
                int nMonth = mPurseDatalab.getEndDate_month();
                int nDay = mPurseDatalab.getEndDate_day();
                if ((nYear == 0) || (nMonth == 0) || (nDay == 0)) {
                    nYear = todayYear;
                    nMonth = todayMonth;
                    nDay = todayDay;
                }
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mPurseDatalab.setEndDate(year, monthOfYear + 1, dayOfMonth);
                                dataToView();
                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, nYear, nMonth - 1, nDay);
                alert.show();
            }
        });
        mBudget_etValue.setText("");
        mBudget_etValue.setInputType(0);
        mBudget_etValue.requestFocus();
        mBudget_etValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBudget_etValue.setText("");
                mBudget_etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


                //       mBudget_etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                mBudget_etValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mBudget_etValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:        // 완료
                    case EditorInfo.IME_ACTION_GO:          // 이동
                    case EditorInfo.IME_ACTION_NEXT:      // 다음
                    case EditorInfo.IME_ACTION_NONE:     // 엔터
                    case EditorInfo.IME_ACTION_SEARCH:  // 돋보기
                    case EditorInfo.IME_ACTION_SEND:    // 전송
                    case EditorInfo.IME_ACTION_UNSPECIFIED:     // 확인
                        closeKeyboard();
                        String sNumber = mBudget_etValue.getText().toString();//.replaceAll("\\D", "");
                        double dChangeVal = .0f;
                        dChangeVal = getExchangeVal(mGlobalVar.getCurrencyCodeInEng(), Double.valueOf(sNumber), true);
                        String strVal = String.format("%.2f", dChangeVal) + " 원";
                        mBudget_etValue_kor.setText(strVal);
                        mPurseDatalab.setBudget(dChangeVal);

                        break;
                }
                return false;
            }
        });
        mBudget_etValue_kor.setText("");
        mBudget_etValue_kor.setInputType(0);
        mBudget_etValue_kor.requestFocus();
        mBudget_etValue_kor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBudget_etValue_kor.setText("");
                mBudget_etValue_kor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


                //       mBudget_etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                mBudget_etValue_kor.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mBudget_etValue_kor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 요기서 입력된 이벤트가 무엇인지 찾아서 실행해 줌
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_GO:          // 이동
                    case EditorInfo.IME_ACTION_NEXT:      // 다음
                    case EditorInfo.IME_ACTION_NONE:     // 엔터
                    case EditorInfo.IME_ACTION_SEARCH:  // 돋보기
                    case EditorInfo.IME_ACTION_SEND:    // 전송
                    case EditorInfo.IME_ACTION_UNSPECIFIED:     // 확인
                        closeKeyboard();
                        String sNumber = mBudget_etValue_kor.getText().toString();//.replaceAll("\\D", "");
                        mPurseDatalab.setBudget(Double.valueOf(sNumber));

                        double dExchangeBudget = .0f;
                        dExchangeBudget = getExchangeVal(mGlobalVar.getCurrencyCodeInEng(), mPurseDatalab.getBudget(), false);

                        String strVal = String.format("%.2f", dExchangeBudget) + " " + mGlobalVar.getCurrencyChar();
                        mBudget_etValue.setText(strVal);

                        break;
                }
                return false;
            }
        });

    }
    private void initInsertLayoutView(View v)
    {
        mInsert_ivHeader = (ImageView)v.findViewById(R.id.ivHeader);


        mInsert_ivHeader.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String sItem = "";
                                                    if(mInsert_tvItemValue.getText().length() > 0 )
                                                        sItem = mInsert_tvItemValue.getText().toString();
                                                    double dValue=0;
                                                    if(mInsert_tvInputValue.getText().length() > 0 )
                                                        dValue = Double.valueOf(mInsert_tvInputValue.getText().toString());
                                                    else
                                                    {
                                                        changeView(0);
                                                        closeKeyboard();
                                                        return ;
                                                    }
                                                    String sDate = mInsert_tvDateValue.getText().toString();
                                                    DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                                                    DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
                                                    try {
                                                        Date newDate = fm.parse(sDate);
                                                        sDate = fmNew.format(newDate);
                                                    }
                                                    catch(Exception e)
                                                    {
                                                    }
                                                    mPurseDatalab.addItem(sItem, sDate, "000000", dValue, mGlobalVar.getCurrencyCodeInEng());
                                                    changeView(0);
                                                    closeKeyboard();
                                                    saveData();
                                                    dataToView();


                                                }
                                            }
        );

        mInsert_ivHeader.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    changeView(0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        mInsert_tvItemValue = (TextView)v.findViewById(R.id.tv_purse_item_input);
        mInsert_tvDateValue = (TextView)v.findViewById(R.id.tv_purse_date_input);
        mInsert_tvInputValue = (TextView)v.findViewById(R.id.tv_purse_value_input);

        mInsert_tvItemTitle = (TextView)v.findViewById(R.id.tv_purse_item_title);
        mInsert_tvDateTitle = (TextView)v.findViewById(R.id.tv_purse_date_title);
        mInsert_tvInputTitle = (TextView)v.findViewById(R.id.tv_purse_value_title);

        mInsert_tvItemValue.setText("");
        mInsert_tvItemValue.setInputType(0);
        mInsert_tvItemValue.requestFocus();
        mInsert_tvItemValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInsert_tvItemValue.setText("");
                mInsert_tvItemValue.setInputType(InputType.TYPE_CLASS_TEXT);

                mInsert_tvItemValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mInsert_tvItemValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 요기서 입력된 이벤트가 무엇인지 찾아서 실행해 줌
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                        closeKeyboard();
                        return true;
//                        break;
                }
                return false;
            }
        });

        GregorianCalendar today = new GregorianCalendar();
        final int todayYear = today.get(today.YEAR);
        final int todayMonth = today.get(today.MONTH) + 1;
        final int todayDay = today.get(today.DAY_OF_MONTH);
        mInsert_tvDateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nYear = mPurseDatalab.getBeginDate_year();

                int nMonth = mPurseDatalab.getBeginDate_month();
                int nDay = mPurseDatalab.getBeginDate_day();
                if ((nYear == 0) || (nMonth == 0) || (nDay == 0)) {
                    nYear = todayYear;
                    nMonth = todayMonth;
                    nDay = todayDay;
                }
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String strVal = String.valueOf(year)+ "/" +String.valueOf(monthOfYear+1)+ "/"+String.valueOf(dayOfMonth);
                                mInsert_tvDateValue.setText(strVal); ;

                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, nYear, nMonth - 1, nDay);
                alert.show();
            }
        });

        mInsert_tvInputValue.setText("");
        mInsert_tvInputValue.setInputType(0);
        mInsert_tvInputValue.requestFocus();
        mInsert_tvInputValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInsert_tvInputValue.setText("");
                mInsert_tvInputValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                mInsert_tvInputValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mInsert_tvInputValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 요기서 입력된 이벤트가 무엇인지 찾아서 실행해 줌
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:

                        closeKeyboard();
                        return true;
//                        break;
                }
                return false;
            }
        });
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
            mGlobalVar.chgCurrency(sCurrncy);

            mtvCurrency.setTextColor(mGlobalVar.getGraphTextColor());
            mtvCurrency.setTextColor(mGlobalVar.getGraphTextColor());

            if((mGlobalVar.getCurrencyCodeInEng().indexOf("JPY")>=0) || (mGlobalVar.getCurrencyCodeInEng().indexOf("IDR")>=0) ||
                    (mGlobalVar.getCurrencyCodeInEng().indexOf("VND")>=0) || (mGlobalVar.getCurrencyCodeInEng().indexOf("KHR")>=0))
                mtvCurrency.setText(mGlobalVar.getCountryNameInKorean() + " " + mGlobalVar.getCurrencyCodeInEng() + "(100)");
            else
                mtvCurrency.setText(mGlobalVar.getCountryNameInKorean() + " " + mGlobalVar.getCurrencyCodeInEng());

            chgBackground(BasicInfo.InternalPath + "image/flag/" + mGlobalVar.getBackgroundFile(), 2);
            mBackgroundView.setImageBitmap(mBackgroundBitmap);
            mMain_ivHeader1.setImageResource(mGlobalVar.getPurseBarId1());
            mMain_ivHeader2.setImageResource(mGlobalVar.getPurseBarId2());


            mBudget_ivHeader.setImageResource(mGlobalVar.getPurseBarId1());
            mInsert_ivHeader.setImageResource(mGlobalVar.getPurseBarId2());

            mMain_tvResultTitle.setBackgroundColor(mGlobalVar.getPurseTextColor());
            mMain_tvResultKRWValue.setBackgroundColor(mGlobalVar.getPurseTextColor2());
            mMain_tvResuleValueKRWMark.setBackgroundColor(mGlobalVar.getPurseTextColor());
            mMain_tvResultValue.setBackgroundColor(mGlobalVar.getPurseTextColor2());
            mMain_tvResuleValueMark.setBackgroundColor(mGlobalVar.getPurseTextColor());


            mInsert_tvItemTitle.setBackgroundColor(mGlobalVar.getPurseTextColor2());
            mInsert_tvDateTitle.setBackgroundColor(mGlobalVar.getPurseTextColor());
            mInsert_tvInputTitle.setBackgroundColor(mGlobalVar.getPurseTextColor2());
            mtvConvertedValue.setTextColor(mGlobalVar.getGraphTextColor());
            mtvDate.setTextColor(mGlobalVar.getGraphTextColor());
            mCallback.saveOptionData();

            Log.i(BasicInfo.TAG, "Change Currency: " + mGlobalVar.getCurrencyCodeInEng());
            mTracker.setScreenName("Change Currency : " + mGlobalVar.getCurrencyCodeInEng());
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
    public void dataToView()
    {
        calcResult();

        mMainlistAdapter.setListItems(mPurseDatalab.getData());
        mMainlistAdapter.setCurrencyStr(mGlobalVar.getCurrencyChar());
        mMainlistAdapter.notifyDataSetChanged();

        String strVal =  String.format("%.2f",mPurseDatalab.getBudget()) + " 원";
        mBudget_etValue_kor.setText(strVal);
        double dExchangeBudget = .0f;
        dExchangeBudget = getExchangeVal(mGlobalVar.getCurrencyCodeInEng(), mPurseDatalab.getBudget(), false);
        strVal =  String.format("%.2f", dExchangeBudget) + " " + mGlobalVar.getCurrencyChar();
        mBudget_etValue.setText(strVal);
        strVal = String.valueOf(mPurseDatalab.getBeginDate_month())+ "/"+String.valueOf(mPurseDatalab.getBeginDate_day());
        mBudget_tvDate1.setText(strVal);
        strVal = String.valueOf(mPurseDatalab.getEndDate_month())+ "/"+String.valueOf(mPurseDatalab.getEndDate_day());
        mBudget_tvDate2.setText(strVal);
        strVal = String.valueOf(mPurseDatalab.getBeginDate_month())+ "/"+String.valueOf(mPurseDatalab.getBeginDate_day() + " ~ " +
                mPurseDatalab.getEndDate_month())+ "/"+String.valueOf(mPurseDatalab.getEndDate_day());
        mMain_tvBudgetDate.setText(strVal);
        strVal = String.format("%.2f",mPurseDatalab.getBudget()) + " 원";
        mMain_tvBudgetValue.setText(strVal);

        ///////////////////////////////////////////////////////////////////////////////
        double dResultVal = getExchangeVal(mGlobalVar.getCurrencyCodeInEng(),mResultVal,false);

        mMain_tvResultKRWValue.setText(String.format("%.2f", mResultVal));
        mMain_tvResuleValueKRWMark.setText("원 ");
        mMain_tvResultValue.setText(String.format("%.2f", dResultVal));
        mMain_tvResuleValueMark.setText(mGlobalVar.getCurrencyChar() + " ");

        double dConvertedVal = .0f;
        if((mGlobalVar.getCurrencyCodeInEng().indexOf("JPY")>=0) || (mGlobalVar.getCurrencyCodeInEng().indexOf("IDR")>=0) ||
                (mGlobalVar.getCurrencyCodeInEng().indexOf("VND")>=0) || (mGlobalVar.getCurrencyCodeInEng().indexOf("KHR")>=0))
            dConvertedVal= mLiveDatalab.getCurrencyValue(mGlobalVar.getCurrencyCodeInEng())*100;
        else
            dConvertedVal= mLiveDatalab.getCurrencyValue(mGlobalVar.getCurrencyCodeInEng());

        mtvConvertedValue.setText(String.format("%.2f", dConvertedVal) + "원");
        mtvDate.setText(mLiveDatalab.getLiveDate_Time(mGlobalVar.getCurrencyCodeInEng()) + " (외환은행 기준)");

    }

    public void calcResult()
    {
        double dCurrencyValue = .0f;
        if(listPurseData == null)
            listPurseData = new ArrayList<PurseData>();
        else
            listPurseData.clear();
        double dExchangeVal;
        double  dTotalVal=.0f;
        for(PurseData p : mPurseDatalab.getData())
        {
            dExchangeVal = p.getValue();
            dExchangeVal = getExchangeVal(p.getCurrencyCode(),dExchangeVal,true);
            dTotalVal += dExchangeVal;
        }
        mResultVal = mPurseDatalab.getBudget() - dTotalVal;
    }


    private void deleteOk_dialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
        String sMsg = String.format("%d번째 \"%s (%s%.2f)\"를 삭제 하시겠습니까?",
                delPurseData.getIndex()+1,delPurseData.getTitle(),mGlobalVar.getCurrencyChar(delPurseData.getCurrencyCode()),delPurseData.getValue() );
        alt_bld.setMessage(sMsg).setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPurseDatalab.delItem(delPurseData);
                        listPurseData.remove(delPurseData);
                        saveData();
                        dataToView();
                        mMain_lvBody.clearChoices();
                        mMainlistAdapter.notifyDataSetChanged();
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
            ConnectMonitorThread thread = new ConnectMonitorThread(BasicInfo.ApiURL, getActivity(), mHandlerHttp, BasicInfo.TYPE_PURSE);
            thread.setCurrency(mGlobalVar.getCurrencyCodeInEng());
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
        enableDisableViewGroup(mMainLayout,false);
        enableDisableViewGroup(mBudgetLayout,false);
        enableDisableViewGroup(mInsertLayout,false);
        mMainLayout.setVisibility(View.GONE);
        mBudgetLayout.setVisibility(View.GONE);
        mInsertLayout.setVisibility(View.GONE);
        mStatusView = nType;
        switch (nType)
        {
            case 0:         // 메인화면
                mMainLayout.setVisibility(View.VISIBLE);
                enableDisableViewGroup(mMainLayout, true);
                break;
            case 1:         // 예산입력
                mBudgetLayout.setVisibility(View.VISIBLE);
                enableDisableViewGroup(mBudgetLayout, true);
                break;
            case 2:         // 지출입력
                mBudgetLayout.setVisibility(View.VISIBLE);
                mInsertLayout.setVisibility(View.VISIBLE);
                enableDisableViewGroup(mBudgetLayout, true);
                enableDisableViewGroup(mInsertLayout,true);
                break;
            case 3:         //
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
}
