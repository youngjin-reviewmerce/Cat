package com.reviewmerce.exchange.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.custom.WrapperEditText;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;
import com.reviewmerce.exchange.publicClass.PurseDataLab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class PurseBudgetDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView mBudget_ivHeader;
    TextView mBudget_tvDate1, mBudget_tvDate2;
    WrapperEditText mBudget_etValue, mBudget_etValue_kor;
    TextView mBudget_tvNation,mBudget_tvNationTitle;

    private baseOnebuyFragment.FragmentChangeListener mCallback;
    private baseOnebuyFragment.PurseInterface mCallback_result;
    private GlobalVar mGlobalVar=null;
    private PurseDataLab mPurseDatalab=null;
    private LiveDataLab mLiveDatalab = null;
    private NationDataLab mNationLab = null;
    private String mStartDate,mEndDate;
    private int mBeginDate_year,mBeginDate_month,mBeginDate_day;
    private int mEndDate_year,mEndDate_month,mEndDate_day;
    private double mBudget,mBudgetKRW;
    private boolean bIsKeyboard = false;
    private int argType;

    //private PurseData argEditData;
    private String mCurrency;
    // The request code must be 0 or greater.
    public PurseBudgetDialogFragment() {
        // Required empty public constructor
        mBudget=.0f;
        mBudgetKRW=.0f;

        mBeginDate_year=0;
        mBeginDate_month=0;
        mBeginDate_day=0;
        mEndDate_year=0;
        mEndDate_month=0;
        mEndDate_day=0;
    }
    @Override
    public void onStop() {
        mGlobalVar.removeExtandPage(GlobalVar.EXTANDPAGE_TYPE_CALC);
        mCallback.saveOptionData();
        super.onStop();

    }
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            mCallback = (baseOnebuyFragment.FragmentChangeListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        Bundle args = getArguments();
        argType = args.getInt("type");

        mGlobalVar = GlobalVar.get();
        View v = inflater.inflate(R.layout.fragment_purse_budget, container, false);
        initControlValue(v);
// remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mGlobalVar = GlobalVar.get();
        mPurseDatalab = PurseDataLab.get(null);
        mLiveDatalab = LiveDataLab.get(null);
        mNationLab = NationDataLab.get(null);
        loadData();
        initBudgetLayoutView(v);


        // remove dialog background
        //       getDialog().getWindow().setBackgroundDrawable(
        //               new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mGlobalVar.addExtandPage(GlobalVar.EXTANDPAGE_TYPE_CALC);
        //mCallback.saveOptionData();
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        int dialogWidth = (int)(width*0.9); // specify a value here
        int dialogHeight = (int)(height*0.5); // specify a value here

        if(dialogHeight<600)
            dialogHeight=600;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);


        //  setButton();
        // ... other stuff you want to do in your onStart() method
    }
    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }

    private void setButton()
    {

    }

    public void changeCurrency(String sCurrency)
    {
        mCurrency = sCurrency;
        String sCurrencyChg = mNationLab.getCountryNameInKorean(mCurrency) + " (" + mCurrency + ")";
        mBudget_tvNation.setText(sCurrencyChg);

        //double dExchangeBudget = getExchangeVal(mCurrency, mPurseDatalab.getBudgetKRW(), false);
        //mBudget = Double.valueOf(dExchangeBudget);
        //mBudget_etValue.setText(mBudget);
    }
    public void initControlValue(View v)
    {

    }
    View.OnClickListener chkTaxListener = new View.OnClickListener()
    {
            @Override
            public void onClick(View v) {

            }
    };
    @Override
    public void onClick(View v) {

    }
    private void initBudgetLayoutView(View v)
    {

        mBudget_tvDate1 = (TextView)v.findViewById(R.id.tv_purse_budget_date1);
        mBudget_tvDate2 = (TextView)v.findViewById(R.id.tv_purse_budget_date2);
        mBudget_etValue = (WrapperEditText)v.findViewById(R.id.et_purse_budget_value);
        mBudget_etValue_kor = (WrapperEditText)v.findViewById(R.id.et_purse_budget_value_kor);
        mBudget_tvNationTitle = (TextView)v.findViewById(R.id.tv_purse_budget_nation_title);
        mBudget_tvNation = (TextView)v.findViewById(R.id.tv_purse_budget_nation);
        String sCurrencyChg = mNationLab.getCountryNameInKorean(mCurrency) + " (" + mCurrency + ")";
        mBudget_tvNation.setText(sCurrencyChg);
        mBudget_tvNation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.makeNationDialog(BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET);
            }
        });

        initHeaderView(v);
        initDateView(v);
        initBudgetView(v);
        initBudgetKRWView(v);
        inputToView();
    }
    private void initHeaderView(View v)
    {
        mBudget_ivHeader = (ImageView)v.findViewById(R.id.ivHeader);

        mBudget_ivHeader.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (mPurseDatalab != null) {
                                                        saveData();
                                                    }
                                                    mCallback_result.applyData_to_view(BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET);
                                                    closeKeyboard();
                                                    dismiss();
                                                }
                                            }
        );
        mBudget_ivHeader.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mCallback_result.applyData_to_view(BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET);

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onPause() {
        mCallback_result.applyData_to_view(BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET);
        super.onPause();
    }
    private void initDateView(View v)
    {
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
                                mBeginDate_year = year;
                                mBeginDate_month = monthOfYear + 1;
                                mBeginDate_day = dayOfMonth;

                                String strVal = String.format("%04d/%02d/%02d", mBeginDate_year, mBeginDate_month, mBeginDate_day);
                                mBudget_tvDate1.setText(strVal);
                                //mCallback_result.applyData_to_view();
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
                                //mPurseDatalab.setEndDate(year, monthOfYear + 1, dayOfMonth);
                                mEndDate_year = year;
                                mEndDate_month = monthOfYear + 1;
                                mEndDate_day = dayOfMonth;
                                String strVal = String.format("%04d/%02d/%02d", mEndDate_year, mEndDate_month, mEndDate_day);
                                mBudget_tvDate2.setText(strVal);
                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, nYear, nMonth - 1, nDay);
                alert.show();
            }
        });
    }
    private void initBudgetKRWView(View v)
    {

        mBudget_etValue_kor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   closeKeyboard();
                mBudget_etValue_kor.setFocusable(false);           // 클릭시 자동으로 키보드가 올라오는 기능 막기
                mBudget_etValue_kor.setClickable(false);
                mBudget_etValue_kor.setText("");
                mBudget_etValue_kor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                mBudget_etValue_kor.setFocusableInTouchMode(true);
                mBudget_etValue_kor.setFocusable(true);

                //       mBudget_etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                mBudget_etValue_kor.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mBudget_etValue_kor.setKeyImeChangeListener(new WrapperEditText.KeyImeChange() {
            @Override
            public boolean onKeyIme(int keyCode, KeyEvent event) {
                if (bIsKeyboard == true) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            if (bIsKeyboard == true) {
                                closeKeyboard();//bIsKeyboard = false;// 상위로 메시지 넘김
                            }
                        }

                    }
                    return true;
                }
                return false;
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
                        String sNumber;
                        try {
                            sNumber = mBudget_etValue_kor.getText().toString();//.replaceAll("\\D", "");
                            if(sNumber.length()<=0)
                                break;
                        }
                        catch (Exception e)
                        {
                            sNumber = "0";
                        }
                        mPurseDatalab.setBudget(Double.valueOf(sNumber));
                        double dExchangeBudget = .0f;
                        dExchangeBudget = getExchangeVal(mCurrency, mPurseDatalab.getBudget(), false);
                        mBudgetKRW = Double.valueOf(sNumber);
                        mBudget = Double.valueOf(dExchangeBudget);

                        String strVal = String.format("%.2f", dExchangeBudget) + " " + mNationLab.getCurrencyChar(mCurrency);
                        mBudget_etValue.setText(strVal);
                        mBudget_etValue_kor.setText(sNumber + " 원");
                        break;
                }
                return false;
            }
        });
    }
    private void initBudgetView(View v)
    {

                ////////////////////
                mBudget_etValue.setFocusable(false);           // 클릭시 자동으로 키보드가 올라오는 기능 막기
                mBudget_etValue.setClickable(false);

                mBudget_etValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                        mBudget_etValue.setText("");
                        mBudget_etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        mBudget_etValue.setFocusableInTouchMode(true);
                        mBudget_etValue.setFocusable(true);

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
                                if(sNumber.length()<=0)
                                    break;
                                double dChangeVal = .0f;
                                dChangeVal = getExchangeVal(mCurrency, Double.valueOf(sNumber), true);

                                mBudget = Double.valueOf(sNumber);
                                mBudgetKRW = Double.valueOf(dChangeVal);

                                String strVal = String.format("%.2f", dChangeVal) + " 원";
                                //String strVal = String.format("%.2f", dExchangeBudget) + " " + mNationLab.getCurrencyChar();
                                mBudget_etValue.setText(sNumber + " " + mNationLab.getCurrencyChar(mCurrency));
                                mBudget_etValue_kor.setText(strVal);

                                break;
                        }
                        return false;
                    }
                });

            }

    private void closeKeyboard()
    {
        try {
            //        mInsert_tvInputValue;
            //        mInsert_tvItemValue;
            mBudget_etValue.clearFocus();
            mBudget_etValue.setFocusableInTouchMode(false);
            mBudget_etValue.setFocusable(false);


            mBudget_etValue_kor.clearFocus();
            mBudget_etValue_kor.setFocusableInTouchMode(false);
            mBudget_etValue_kor.setFocusable(false);
            if(getActivity().getCurrentFocus().getWindowToken() != null) {
                mBudget_etValue.setInputType(InputType.TYPE_NULL);
                mBudget_etValue_kor.setInputType(InputType.TYPE_NULL);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.i(BasicInfo.TAG, e.toString());
        }

        bIsKeyboard = false;
    }

    public void setResultCallback(baseOnebuyFragment.PurseInterface callback) {
        mCallback_result = callback;
    }

    public double getExchangeVal(String sCurrency, double dVal, boolean bGetKor) {
        double dExchangeVal = mLiveDatalab.getCurrencyValue(sCurrency);
        /*
        if((sCurrency.indexOf("JPY")>=0) || (sCurrency.indexOf("IDR")>=0) ||
                (sCurrency.indexOf("VND")>=0) || (sCurrency.indexOf("KHR")>=0))
            dExchangeVal= mLiveDatalab.getCurrencyValue(mNationLab.getCurrencyCodeInEng())*100;
*/
        double dExchangeBudget = .0f;
        if (bGetKor) {
            if (dExchangeVal != .0f)
                dExchangeBudget = dVal * dExchangeVal;
            else
                dExchangeBudget = .0f;
        } else {
            if (dExchangeVal != .0f)
                dExchangeBudget = dVal / dExchangeVal;
            else
                dExchangeBudget = .0f;
        }
        return dExchangeBudget;
    }

    public void inputToView() {
        String strVal = String.format("%.2f", mBudgetKRW) + " 원";
        mBudget_etValue_kor.setText(strVal);
        strVal = String.format("%.2f", mBudget) + " " + mNationLab.getCurrencyChar(mCurrency);
        mBudget_etValue.setText(strVal);

        strVal = String.format("%04d/%02d/%02d", mBeginDate_year, mBeginDate_month, mBeginDate_day);
        mBudget_tvDate1.setText(strVal);

        strVal = String.format("%04d/%02d/%02d", mEndDate_year, mEndDate_month, mEndDate_day);
        mBudget_tvDate2.setText(strVal);

    }

    public void saveData() {
//        String sNumber = mBudget_etValue.getText().toString();//.replaceAll("\\D", "");
//        String sNumberKrw = mBudget_etValue_kor.getText().toString();
//        sNumber = sNumber.replaceAll("[^\\f]", "");
//        sNumberKrw = sNumberKrw.replaceAll("[^\\f]", "");
//        mBudget = Double.valueOf(sNumber);
//        mBudgetKRW = Double.valueOf(sNumberKrw);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String sBegin = String.format("%04d%02d%02d", mBeginDate_year, mBeginDate_month, mBeginDate_day);
            String sEnd = String.format("%04d%02d%02d", mEndDate_year, mEndDate_month, mEndDate_day);
            Date dateBegin = format.parse(sBegin);
            Date dateEnd = format.parse(sEnd);
            if(dateEnd.getTime()<dateBegin.getTime())
            {
                changeDate_Begin_End();
            }
        }catch (Exception e) {
        }

        mPurseDatalab.setBudget(mBudget);
        mPurseDatalab.setmBudgetKRW(mBudgetKRW);

        mPurseDatalab.setBeginDate(mBeginDate_year, mBeginDate_month, mBeginDate_day);
        mPurseDatalab.setEndDate(mEndDate_year, mEndDate_month, mEndDate_day);
        mPurseDatalab.setBudgetCurrency(mCurrency);
        mPurseDatalab.savePurseData_DB();
    }
    public void changeDate_Begin_End()
    {
        int nYear = mBeginDate_year;
        int nMonth = mBeginDate_month;
        int nDay = mBeginDate_day;

        mBeginDate_year = mEndDate_year;
        mBeginDate_month = mEndDate_month;
        mBeginDate_day = mEndDate_day;

        mEndDate_year = nYear;
        mEndDate_month = nMonth;
        mEndDate_day = nDay;
    }

    public void loadData() {
        mEndDate_year = mPurseDatalab.getEndDate_year();
        mEndDate_month = mPurseDatalab.getEndDate_month();
        mEndDate_day = mPurseDatalab.getEndDate_day();

        mBeginDate_year = mPurseDatalab.getBeginDate_year();
        mBeginDate_month = mPurseDatalab.getBeginDate_month();
        mBeginDate_day = mPurseDatalab.getBeginDate_day();

        mBudget = mPurseDatalab.getBudget();
        mBudgetKRW = mPurseDatalab.getBudgetKRW();

        mCurrency = mPurseDatalab.getBudgetCurrency();
    }

}
