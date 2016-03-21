package com.reviewmerce.exchange.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reviewmerce.exchange.Activity.MainActivity;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.Caculator;
import com.reviewmerce.exchange.commonData.NationTaxData;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.custom.WrapperEditText;
import com.reviewmerce.exchange.db.MainDatabase;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;
import com.reviewmerce.exchange.publicClass.PurseDataLab;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Queue;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class PurseInputDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView mInsert_ivHeader;
    TextView  mInsert_tvDateValue;
    WrapperEditText mInsert_tvItemValue,mInsert_tvInputValue;
    TextView  mInsert_tvItemTitle,mInsert_tvDateTitle,mInsert_tvInputTitle;
    TextView mInsert_tvNationTitle, mInsert_tvNationValue;
    private baseOnebuyFragment.FragmentChangeListener mCallback;
    private baseOnebuyFragment.PurseInterface mCallback_result;
    private GlobalVar mGlobalVar=null;
    private PurseDataLab mPurseDatalab=null;
    private LiveDataLab mLiveDatalab = null;
    private NationDataLab mNationLab = null;
    private boolean bIsKeyboard = false;
    private int argType;
    private PurseData argEditData;
    private String mCurrency;
    // The request code must be 0 or greater.
    public PurseInputDialogFragment() {
        // Required empty public constructor

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
        if(argType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT)
        {
            argEditData = args.getParcelable("editdata");
        }
        mGlobalVar = GlobalVar.get();
        View v = inflater.inflate(R.layout.fragment_purse_insertitem, container, false);
        initControlValue(v);
// remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mGlobalVar = GlobalVar.get();
        mPurseDatalab = PurseDataLab.get(null);
        mLiveDatalab = LiveDataLab.get(null);
        mNationLab = NationDataLab.get(null);
        mCurrency = mPurseDatalab.getBudgetCurrency();
        initInsertLayoutView(v);

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
//        mNationLab.chgCurrency(sCurrency);
        mCurrency = sCurrency;
        String sCurrencyChg = mNationLab.getCountryNameInKorean(mCurrency) + " (" + mCurrency + ")";
        mInsert_tvNationValue.setText(sCurrencyChg);
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
    private void initInsertLayoutView(View v)
    {
        mInsert_ivHeader = (ImageView)v.findViewById(R.id.ivHeader);


        mInsert_ivHeader.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String sItem = "";
                                                    if (mInsert_tvItemValue.getText().length() > 0)
                                                        sItem = mInsert_tvItemValue.getText().toString();
                                                    double dValue = 0;

                                                    if (mInsert_tvInputValue.getText().length() > 0) {
                                                        try {
                                                            dValue = Double.valueOf(mInsert_tvInputValue.getText().toString());
                                                        }
                                                        catch (Exception e)
                                                        {
                                                            closeKeyboard();
                                                            dismiss();
                                                            return;
                                                        }
                                                    }
                                                    else {
//                                                        changeView(0);
                                                        closeKeyboard();
                                                        dismiss();
                                                        return;
                                                    }
                                                    String sDate = mInsert_tvDateValue.getText().toString();
                                                    DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                                                    DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
                                                    try {
                                                        Date newDate = fm.parse(sDate);
                                                        sDate = fmNew.format(newDate);
                                                    } catch (Exception e) {
                                                    }

                                                    if (argType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT)
                                                        mPurseDatalab.addItem(sItem, sDate, "000000", dValue, mCurrency);
                                                    else if (argType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT)
                                                        mPurseDatalab.editItem(argEditData.getIndex(), sItem, sDate, "000000", dValue, mCurrency);
                                                    //         mPurseDatalab.addItem(sItem, sDate, "000000", dValue, mNationLab.getCurrencyCodeInEng());
                                                    if (mPurseDatalab != null)
                                                        mPurseDatalab.savePurseData_DB();
//                                                    changeView(0);
                                                    closeKeyboard();
//                                                    saveData();
//                                                    dataToView();
                                                    mCallback_result.applyData_to_view(argType);
                                                    dismiss();


                                                }
                                            }
        );

        mInsert_ivHeader.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    changeView(0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        mInsert_tvItemValue = (WrapperEditText)v.findViewById(R.id.tv_purse_item_input);
        mInsert_tvDateValue = (TextView)v.findViewById(R.id.tv_purse_date_input);
        mInsert_tvInputValue = (WrapperEditText)v.findViewById(R.id.tv_purse_value_input);

        mInsert_tvItemTitle = (TextView)v.findViewById(R.id.tv_purse_item_title);
        mInsert_tvDateTitle = (TextView)v.findViewById(R.id.tv_purse_date_title);
        mInsert_tvInputTitle = (TextView)v.findViewById(R.id.tv_purse_value_title);

        mInsert_tvNationTitle = (TextView)v.findViewById(R.id.tv_purse_title_nation);
        mInsert_tvNationValue = (TextView)v.findViewById(R.id.tv_purse_value_nation);
        mInsert_tvNationValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.makeNationDialog(BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT);
            }
        });
        String sCurrencyChg;

        if(argType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT)
        {
            sCurrencyChg = mNationLab.getCountryNameInKorean(argEditData.getCurrencyCode()) + " (" + argEditData.getCurrencyCode() + ")";
        }
        else
            sCurrencyChg = mNationLab.getCountryNameInKorean(mCurrency) + " (" + mCurrency + ")";

        mInsert_tvNationValue.setText(sCurrencyChg);


        mInsert_tvItemValue.setFocusable(false);
        mInsert_tvItemValue.setClickable(false);
        mInsert_tvItemValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                mInsert_tvItemValue.setText("");
                mInsert_tvItemValue.setInputType(InputType.TYPE_CLASS_TEXT);
                mInsert_tvItemValue.setFocusableInTouchMode(true);
                mInsert_tvItemValue.setFocusable(true);
                mInsert_tvItemValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mInsert_tvItemValue.setKeyImeChangeListener(new WrapperEditText.KeyImeChange() {
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
        mInsert_tvItemValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                        return false;               // 이거 해야 내 의도대로 닫을 수 있음
//                        break;
                }
                return false;
            }
        });

        GregorianCalendar today = new GregorianCalendar();
        final int todayYear = today.get(today.YEAR);
        final int todayMonth = today.get(today.MONTH) + 1;
        final int todayDay = today.get(today.DAY_OF_MONTH);
        String sDate = String.format("%04d/%02d/%02d",todayYear,todayMonth,todayDay);
        mInsert_tvDateValue.setText(sDate);
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

        mInsert_tvInputValue.setFocusable(false);
        mInsert_tvInputValue.setClickable(false);
        mInsert_tvInputValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                mInsert_tvInputValue.setText("");
                mInsert_tvInputValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                mInsert_tvInputValue.setFocusableInTouchMode(true);
                mInsert_tvInputValue.setFocusable(true);
                mInsert_tvInputValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                bIsKeyboard = true;
            }
        });
        mInsert_tvInputValue.setKeyImeChangeListener(new WrapperEditText.KeyImeChange() {
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
        mInsert_tvInputValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                        return false;
//                        break;
                }
                return false;
            }
        });
        if(argType == BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT)
        {
            String sTemp=String.format("%.2f", argEditData.getValue());
            mInsert_tvItemValue.setText(argEditData.getTitle());
            mInsert_tvDateValue.setText(argEditData.getDate());
            mInsert_tvInputValue.setText(sTemp);
        }
    }
    private void closeKeyboard()
    {
        try {
    //        mInsert_tvInputValue;
    //        mInsert_tvItemValue;
            mInsert_tvInputValue.clearFocus();
            mInsert_tvInputValue.setFocusableInTouchMode(false);
            mInsert_tvInputValue.setFocusable(false);

            mInsert_tvItemValue.clearFocus();
            mInsert_tvItemValue.setFocusableInTouchMode(false);
            mInsert_tvItemValue.setFocusable(false);
            if(getActivity().getCurrentFocus().getWindowToken() != null) {
                mInsert_tvInputValue.setInputType(InputType.TYPE_NULL);
                mInsert_tvItemValue.setInputType(InputType.TYPE_NULL);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.i(BasicInfo.TAG, e.toString());
        }

        bIsKeyboard = false;
    }
    public void setResultCallback(baseOnebuyFragment.PurseInterface callback)
    {
        mCallback_result = callback;
    }
    @Override
    public void onPause() {
        mCallback_result.applyData_to_view(argType);
        super.onPause();
    }
}
