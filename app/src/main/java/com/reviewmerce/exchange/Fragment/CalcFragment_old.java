package com.reviewmerce.exchange.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.NationTaxData;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;

import java.io.InputStream;
import java.text.NumberFormat;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class CalcFragment_old extends DialogFragment implements View.OnClickListener {
    final int MAX_FRACTION = 10;
    protected int m_nPreTouchPosX = 0;
    protected boolean bDoingChgCurrency = false;
    protected TextView btExchange1,btExchange2;
    protected TextView tvExchange1, tvExchange2, tvInfo;
    protected Button bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9;
    protected Button btDot,btClear, btDel, btChg;
    protected Button btSendTax;
    protected double mOutputKorValue;
    protected double mOutputForeignValue;
    protected double mInputValue;
    protected double mSendTaxValue;
    protected int mType;
    protected GlobalVar mGlobalVar = null;
    protected LiveDataLab mLiveDatalab = null;
    protected NationDataLab mNationLab = null;
    protected boolean bDotMode=false;
    protected int mFractionDigit=0;
    protected String mCurrency="";
    protected NationTaxData mTaxData=null;
    protected FrameLayout mFrameLayout;
    protected LinearLayout mParentLinearLayout;
    protected LinearLayout mTaxLayout,mLinearButtonLayout;
    protected Bitmap mBackgroundBitmap=null;
    protected ImageView mBackgroundView;
    protected Boolean bSendTaxMode = false;
    // The request code must be 0 or greater.
    public CalcFragment_old() {
        // Required empty public constructor
        mOutputKorValue = 0;
        mOutputForeignValue = 0;
        mInputValue = 0;
        mSendTaxValue = 0;
        mType = 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        View v = inflater.inflate(R.layout.fragment_calc, container, false);
        initControlValue(v);
// remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mGlobalVar = GlobalVar.get();
        mLiveDatalab = LiveDataLab.get(null);
        mNationLab = NationDataLab.get(null);
        mCurrency = mNationLab.getCurrencyCodeInEng();
        mTaxData = new NationTaxData();
        mTaxData.setCardRate(0.1f);
        mTaxData.setReceiveTax(.0f);
        mTaxData.setSendTax(0.1f);
        mTaxData.setTipRate(0.1f);

        //  initLinearView(v);
        chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + "calc_background.png", 2);
        mBackgroundView.setImageBitmap(mBackgroundBitmap);
        initCurrency();
        changeCurrency();
        // remove dialog background
        //       getDialog().getWindow().setBackgroundDrawable(
        //               new ColorDrawable(android.graphics.Color.TRANSPARENT));
        v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                initView(v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
            }
        });

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

        int dialogWidth = (int)(width*0.8); // specify a value here
        int dialogHeight = (int)(height*0.7); // specify a value here

        if(dialogHeight<840)
            dialogHeight=840;
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 40;
        params.rightMargin = 40;
        bt0.setLayoutParams(params);

        params.topMargin = 40;
        params.rightMargin = 80;
        bt1.setLayoutParams(params);

    }

    private void initLinearView(View v)
    {
        LayoutInflater infalInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mFrameLayout = new FrameLayout(getActivity());

        mParentLinearLayout = (LinearLayout)v.findViewById(R.id.calcMainLinear);
        mTaxLayout = (LinearLayout) infalInflater.inflate(R.layout.calc_tax_listrow, null);

        mFrameLayout.addView(mTaxLayout);
        mTaxLayout.setVisibility(View.VISIBLE);
        mParentLinearLayout.addView(mFrameLayout);
    }


    public void changeCurrency()
    {
        if(mType == 0) {
            btExchange1.setText(mCurrency);
        }
        else
        {
            btExchange2.setText(mCurrency);
        }

        calculation();
        dataToControl();

        String sVal;
        sVal = String.format("%.02f", mLiveDatalab.getCurrencyValue(mCurrency));
        if(sVal!=null)
            tvInfo.setText(sVal);
    }
    public void initControlValue(View v)
    {
        btExchange1 = (TextView) v.findViewById(R.id.btnExchange1);
        btExchange2 = (TextView) v.findViewById(R.id.btnExchange2);
        tvExchange1 = (TextView)v.findViewById(R.id.textExchange1);
        tvExchange2 = (TextView)v.findViewById(R.id.textExchange2);
        //tvExchange1.setBackgroundColor(Color.parseColor("#ff0066b4"));
        tvExchange1.setBackgroundColor(Color.parseColor("#00444444"));
        tvExchange2.setBackgroundColor(Color.parseColor("#00444444"));
        tvInfo = (TextView)v.findViewById(R.id.tvDate);


        mBackgroundView = (ImageView)v.findViewById(R.id.ivScreen);

        bt0 = (Button) v.findViewById(R.id.button0);
        bt1 = (Button) v.findViewById(R.id.button1);
        bt2 = (Button) v.findViewById(R.id.button2);
        bt3 = (Button) v.findViewById(R.id.button3);
        bt4 = (Button) v.findViewById(R.id.button4);
        bt5 = (Button) v.findViewById(R.id.button5);
        bt6 = (Button) v.findViewById(R.id.button6);
        bt7 = (Button) v.findViewById(R.id.button7);
        bt8 = (Button) v.findViewById(R.id.button8);
        bt9 = (Button) v.findViewById(R.id.button9);

        btDel = (Button) v.findViewById(R.id.button_del);
        btClear = (Button) v.findViewById(R.id.button_clr);
        btChg = (Button) v.findViewById(R.id.button_chg);
        btDot = (Button)v.findViewById(R.id.button_dot);
        btSendTax = (Button)v.findViewById(R.id.button_blank);

        bt0.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt9.setOnClickListener(this);

        btDel.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btChg.setOnClickListener(this);
        btDot.setOnClickListener(this);

        btSendTax.setOnClickListener(this);
        btSendTax.setText("00");


    }

    @Override
    public void onClick(View v) {
        String sVal;
        int nVal=-1;
        if (v.equals(bt0)) {
            nVal = 0;
            procNumberInput(nVal);
        } else if (v.equals(bt1)) {
            nVal = 1;
            procNumberInput(nVal);
        } else if (v.equals(bt2)) {
            nVal = 2;
            procNumberInput(nVal);
        } else if (v.equals(bt3)) {
            nVal = 3;
            procNumberInput(nVal);
        } else if (v.equals(bt4)) {
            nVal = 4;
            procNumberInput(nVal);
        } else if (v.equals(bt5)) {
            nVal = 5;
            procNumberInput(nVal);
        } else if (v.equals(bt6)) {
            nVal = 6;
            procNumberInput(nVal);
        } else if (v.equals(bt7)) {
            nVal = 7;
            procNumberInput(nVal);
        } else if (v.equals(bt8)) {
            nVal = 8;
            procNumberInput(nVal);
        } else if (v.equals(bt9)) {
            nVal = 9;
            procNumberInput(nVal);
        } else if (v.equals(btClear)) {
            mOutputKorValue = 0;
            mOutputForeignValue = 0;
            mInputValue = 0;
            mSendTaxValue = 0;
            mFractionDigit = 0;
            bDotMode = false;
        }
        else if (v.equals(btDel)) {
            mFractionDigit--;
            if(mFractionDigit < 0)
                mFractionDigit = 0;
            mInputValue = delNumber(mInputValue,mFractionDigit);
        } else if (v.equals(btChg)) {
            if(mType == 0) {
                mType = 1;
                btExchange2.setText(mCurrency);
                btExchange1.setText("KRW");
                //tvExchange1.setBackgroundColor(Color.parseColor("#ff0066b4"));
                //tvExchange2.setBackgroundColor(Color.parseColor("#00444444"));
            }
            else {
                mType = 0;
                btExchange2.setText("KRW");
                btExchange1.setText(mCurrency);
                //tvExchange1.setBackgroundColor(Color.parseColor("#00444444"));
                //tvExchange2.setBackgroundColor(Color.parseColor("#ff0066b4"));
            }
        }
        else if(v.equals(btDot))
        {
            bDotMode = true;
        }
        else if(v.equals(btSendTax))
        {
            /*
            if(bSendTaxMode==true) {
                bSendTaxMode = false;
                btSendTax.setText("TaxOn");
            }
            else {
                bSendTaxMode = true;
                btSendTax.setText("TaxOff");
            }
            */
            nVal = 0;
            procNumberInput(nVal);
            nVal = 0;
            procNumberInput(nVal);
        }
        calculation();
        dataToControl();
    }

    public void procNumberInput(int nVal)
    {
        if(bDotMode)
            mFractionDigit ++;
        if(mFractionDigit>MAX_FRACTION) {
            mFractionDigit = MAX_FRACTION;
            return;
        }
        if(nVal>=0) {
            mInputValue = addNumber(mInputValue, nVal,mFractionDigit);
        }
    }
    // OnInitialize와 동일한 기능
    public void initView(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
    {

    }
    public void dataToControl()
    {
        String sVal;
        if(mType==0) {
            sVal = makeDecimal(mOutputForeignValue,mFractionDigit,mFractionDigit);
            tvExchange1.setText(sVal);
            sVal = makeDecimal(mOutputKorValue,0,2);
            tvExchange2.setText(sVal);
        }
        else
        {
            sVal = makeDecimal(mOutputKorValue,mFractionDigit,mFractionDigit);
            tvExchange1.setText(sVal);
            sVal = makeDecimal(mOutputForeignValue,0,2);
            tvExchange2.setText(sVal);
        }
    }
    public double addNumber(double dOldVal,int nAddNum, int nFractionDigit)
    {
        Long lVal = (long)dOldVal;
        double dFract = dOldVal - lVal;
        double dAddVal = 1;
//        if(nFractionDigit>MAX_FRACTION)
//            return dOldVal;
        for(int i=0;i<nFractionDigit;i++) {
            dAddVal = dAddVal / 10;
        }
        dAddVal = nAddNum * dAddVal;
        if(dOldVal > 99999999999999.f)
            dOldVal = dOldVal;

        else {
            if(nFractionDigit==0) {
                lVal = lVal * 10 + nAddNum;
                dOldVal = lVal + dFract;
            }
            else
            {
                dOldVal = lVal + dFract + dAddVal;
            }
        }
        return dOldVal;
    }
    public double delNumber(double dOldVal, int nFractionDigit)
    {
        Long lVal = 0L;
        double dRtnVal = .0f;
        double dMulVal = 1f;
        if(nFractionDigit>0) {
            for (int i = 0; i < nFractionDigit; i++) {
                dMulVal = dMulVal * 10;
            }
            lVal = (long)(dOldVal*dMulVal);
            dRtnVal = lVal / dMulVal;
        }
        else {
            if(bDotMode)
            {
                lVal = (long)(dOldVal);
                dRtnVal = lVal;
                bDotMode = false;
            }
            else
            {
                lVal = (long)(dOldVal);
                if(lVal<10)
                    lVal = 0L;
                else
                    dRtnVal = lVal / 10;
            }
        }
        return dRtnVal;
    }
    public String makeDecimal(double lVal,int nMinFraction,int nFractionDigit)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumIntegerDigits(15); //최대수 지정
        nf.setMinimumFractionDigits(nMinFraction);
        nf.setMaximumFractionDigits(nFractionDigit);
        String sPrice = nf.format(lVal);

        return sPrice;
    }

    public void calculation()
    {
        double dCalcVal = mInputValue;
        mSendTaxValue = calc_SendTax(mInputValue,mTaxData.getSendTax());
        if(bSendTaxMode==true)
        {
            dCalcVal = dCalcVal + mSendTaxValue;
        }

        if(mType==0)
        {
            mOutputKorValue = calc_InputNumber(dCalcVal,mType);
            mOutputForeignValue = dCalcVal;
        }
        else
        {
            mOutputForeignValue = calc_InputNumber(dCalcVal,mType);
            mOutputKorValue = dCalcVal;
        }
    }

    public double calc_InputNumber(double dInput,int nMode)
    {
        double dChangeVal = mLiveDatalab.getCurrencyValue(mCurrency);
        if((mCurrency.indexOf("JPY")>=0) || (mCurrency.indexOf("IDR")>=0))
            dChangeVal = dChangeVal / 100;
        double dRtn = .0f;
        if(nMode == 0) // For
        {
            dRtn = dInput * dChangeVal;

        }
        else
        {
            dRtn = dInput / dChangeVal;
        }
        return dRtn;
    }
    public double calc_SendTax(double dInput, double dVal)
    {
        double dRtn = dInput * dVal;
        return dRtn;
    }
    void initCurrency()
    {
        btExchange1.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //if(bDoingChgCurrency==false)
                                               {
                                                   if (mType == 0) {
                                                       bDoingChgCurrency = true;
                                                       Resources res = getActivity().getResources();
                                                       final CurrencyAdapter adapter = mNationLab.getCurrencyAdapter(getActivity());
//                                               adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.kor), "KOR", "대한민국"));
                                                       AlertDialog.Builder alert =
                                                               new AlertDialog.Builder(getActivity()).//setView(dialogView).
                                                                       setAdapter(adapter, new DialogInterface.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialog, int which) {
                                                                       CurrencyItem item = (CurrencyItem) (adapter.getItem(which));
                                                                       mCurrency = item.getData(0);
                                                                       changeCurrency();
                                                                       bDoingChgCurrency = false;
                                                                   }
                                                               });
                                                       ;
                                                       alert.show();

                                                   }
                                               }
//*/
                                           }
                                       }
        );
        btExchange2.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (mType == 1) {
                                                   bDoingChgCurrency = true;
                                                   Resources res = getActivity().getResources();
                                                   final CurrencyAdapter adapter = mNationLab.getCurrencyAdapter(getActivity());
//                                               adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.kor), "KOR", "대한민국"));
                                                   AlertDialog.Builder alert =
                                                           new AlertDialog.Builder(getActivity()).//setView(dialogView).
                                                                   setAdapter(adapter, new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialog, int which) {
                                                                   CurrencyItem item = (CurrencyItem) (adapter.getItem(which));
                                                                   mCurrency = item.getData(0);
                                                                   changeCurrency();
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
}
