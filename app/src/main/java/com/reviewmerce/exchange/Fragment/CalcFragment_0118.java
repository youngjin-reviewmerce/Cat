package com.reviewmerce.exchange.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.MainActivity;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.Caculator;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.NationTaxData;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Queue;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class CalcFragment_0118 extends DialogFragment implements View.OnClickListener {
    protected TextView btExchange1,btExchange2;
    protected TextView tvExchange1, tvExchange2, tvInfo;

    protected Button bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9;
    protected Button btDot,btClear, btDel, btChg;
    protected Button btSendTax;
    protected Button btMul, btDiv, btPlus, btMinus, btEqual;

    protected boolean bDoingChgCurrency = false;
    protected int mType;
    protected GlobalVar mGlobalVar = null;
    protected NationDataLab mNationLab = null;
    protected LiveDataLab mLiveDatalab = null;
    protected String mCurrency="";
    protected NationTaxData mTaxData=null;

    protected Bitmap mBackgroundBitmap=null;
    protected ImageView mBackgroundView;
    protected int mOperatorIndex=0;

    Caculator mCalc=null;
    protected String mInputStr;
    // The request code must be 0 or greater.
    public CalcFragment_0118() {
        // Required empty public constructor
        mType = 0;
        mOperatorIndex=0;
        mInputStr = "";
        mCalc = new Caculator();
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
        chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + "calc_back1.png", 2);
        if(mBackgroundBitmap==null)
        {
            ((MainActivity)getActivity()).CopyAssets("image/background");
            chgBackground(BasicInfo.InternalPath + BasicInfo.BackgroudPath + "calc_back1.png", 2);
        }

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

    public void changeCurrency()
    {
        if(mType == 0) {
            btExchange1.setText(mCurrency);
        }
        else
        {
            btExchange2.setText(mCurrency);
        }

        showAnotherVal(mInputStr);

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
        tvExchange1.setTextColor(Color.parseColor("#ffffffff"));
        tvExchange2.setBackgroundColor(Color.parseColor("#00444444"));
        tvExchange2.setTextColor(Color.parseColor("#ffffffff"));
        btExchange1.setTypeface(null, Typeface.BOLD);
        btExchange2.setTypeface(null, Typeface.NORMAL);




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

        btMul = (Button)v.findViewById(R.id.button_mul);
        btDiv = (Button)v.findViewById(R.id.button_div);
        btPlus = (Button)v.findViewById(R.id.button_plus);
        btMinus = (Button)v.findViewById(R.id.button_minus);
        btEqual = (Button)v.findViewById(R.id.button_equal);

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

        btMul.setOnClickListener(this);
        btDiv.setOnClickListener(this);
        btPlus.setOnClickListener(this);
        btMinus.setOnClickListener(this);
        btEqual.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String sVal;
        try {
            int nVal = -1;
            if (v.equals(bt0)) {
                procNumberInput(0);
            } else if (v.equals(bt1)) {
                procNumberInput(1);
            } else if (v.equals(bt2)) {
                procNumberInput(2);
            } else if (v.equals(bt3)) {
                procNumberInput(3);
            } else if (v.equals(bt4)) {
                procNumberInput(4);
            } else if (v.equals(bt5)) {
                procNumberInput(5);
            } else if (v.equals(bt6)) {
                procNumberInput(6);
            } else if (v.equals(bt7)) {
                procNumberInput(7);
            } else if (v.equals(bt8)) {
                procNumberInput(8);
            } else if (v.equals(bt9)) {
                procNumberInput(9);
            } else if (v.equals(btClear)) {
                mInputStr = "";
                tvExchange1.setText(mInputStr);
                mOperatorIndex = 0;
                showAnotherVal(mInputStr);
                tvExchange2.setText("");
            } else if (v.equals(btDel)) {
                if (mInputStr.length() > 0) {
                    mInputStr = mInputStr.substring(0, mInputStr.length() - 1);
                    if(mOperatorIndex > mInputStr.length())
                        mOperatorIndex = 0;
                }
                String sTemp = changeCommaInDoubleString(mInputStr, mOperatorIndex, false);
                //////////////////////////////////////////////////////////////
                if ((sTemp != null) && (sTemp.length() > 0))
                    mInputStr = sTemp;
                calculation();
                tvExchange1.setText(mInputStr);
                showAnotherVal(mInputStr);
                //tvExchange1.setText(mInputStr);
            } else if (v.equals(btChg)) {
                if (mType == 0) {
                    mType = 1;   // 한국이 origin으로
                    btExchange2.setText(mCurrency);
                    btExchange1.setText("KRW");
                    btExchange2.setTypeface(null, Typeface.BOLD);
                    btExchange1.setTypeface(null,Typeface.NORMAL);
                } else {
                    mType = 0;
                    btExchange2.setText("KRW");
                    btExchange1.setText(mCurrency);
                    btExchange1.setTypeface(null, Typeface.BOLD);
                    btExchange2.setTypeface(null, Typeface.NORMAL);
                }
                if((mOperatorIndex>0) && (mOperatorIndex < mInputStr.length()))
                {
                    calculation();
                }
                else
                {
                    showAnotherVal(mInputStr);
                }
            } else if (v.equals(btDot)) {
                procDotInput();
            } else if (v.equals(btPlus)) {
                procOperatorInput('+');

            } else if (v.equals(btMinus)) {
                procOperatorInput('-');
            } else if (v.equals(btMul)) {
                procOperatorInput('*');
            } else if (v.equals(btDiv)) {
                procOperatorInput('/');
            } else if (v.equals(btEqual)) {
                if (mOperatorIndex < mInputStr.length()) {
                    double dValue = calculation();
                    mInputStr = makeDecimal(dValue,0,2);
                    mOperatorIndex = 0;
                }
                tvExchange1.setText(mInputStr);
            } else if (v.equals(btSendTax)) {

                procNumberInput(0);
                procNumberInput(0);
            }
        }
        catch (Exception e)
        {
            Log.e("calc", "inputProcess Error");
        }
    }

    public void procNumberInput(int nNum)
    {
        if(checkLength(mInputStr)==false)
            return;
        mInputStr = mInputStr + String.valueOf(nNum);
        //////////////////////////////////////////////////////////////
        String sTemp = changeCommaInDoubleString(mInputStr, mOperatorIndex, false);
        //////////////////////////////////////////////////////////////
        if((sTemp != null) && (sTemp.length()>0))
            mInputStr = sTemp;
        tvExchange1.setText(mInputStr);
        if((mOperatorIndex>0) && (mOperatorIndex < mInputStr.length()))
        {
            calculation();
        }
        else
        {
            showAnotherVal(mInputStr);
        }

    }
    public String changeCommaInDoubleString(String sValue,int nOperatorIndex,boolean bDigitLimit)
    {
        String sLeft="";
        String sRight="";
        String sOperator="";
        if((nOperatorIndex > 0) && (sValue.length() > nOperatorIndex))
        {
            sRight = sValue.substring(nOperatorIndex , sValue.length());
            sRight = addComma(sRight);
        }
        if(nOperatorIndex > 0)
        {
            sOperator = sValue.substring(nOperatorIndex - 1, nOperatorIndex);
        }
        if(sValue.length()>0)
        {
            if(nOperatorIndex > 0)
                sLeft = sValue.substring(0,nOperatorIndex-1);
            else
                sLeft = sValue;
            sLeft = addComma(sLeft);
        }

        String sRtn = sLeft + sOperator + sRight;
        return sRtn;
    }
    public String addComma(String sValue)
    {
        if(sValue == null)
            return null;
        int nDotIndex = sValue.indexOf(".");
        String sDecimal ="";
        String sPoint = "";
        if(nDotIndex==0)
        {
            sValue = '0' + sValue ;
            nDotIndex = sValue.indexOf(".");
        }
        if(nDotIndex >= 0)
        {
            sDecimal = sValue.substring(0, nDotIndex);
            sPoint = sValue.substring(nDotIndex, sValue.length());

            sDecimal = delCommaInDouble(sDecimal);
            sDecimal = makeDecimal(Integer.valueOf(sDecimal),0,0);
        }
        else
        {
            sDecimal = sValue;
            sDecimal = delCommaInDouble(sDecimal);
            sDecimal = makeDecimal(Double.valueOf(sDecimal), 0, 0);
        }
        return sDecimal+sPoint;
    }
    public void procOperatorInput(char s) {

        if(checkLength(mInputStr)==false)
            return;
        if(mInputStr.length()>0) {
            if (mCalc.isOperator(s) == true) {
                if ((mOperatorIndex > 0) && (mOperatorIndex < mInputStr.length())) {
                    {
                        double dValue = calculation();
                        mInputStr = makeDecimal(dValue,0,2);
                    }
                } else if (mOperatorIndex == mInputStr.length()) {
                    mInputStr = mInputStr.substring(0, mInputStr.length() - 1);
                }
                mInputStr += s;
                mOperatorIndex = mInputStr.length();
            }
            tvExchange1.setText(mInputStr);
        }
    }
    public boolean checkLength(String sVal)
    {
        int nMaxLength = 18;
        if(sVal.length()>nMaxLength) {
            Toast.makeText(getActivity(),
                    "입력 가능한 길이를 초과하였습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void procDotInput() {
        if(checkLength(mInputStr)==false)
            return;
        int nDotIndex = mInputStr.lastIndexOf(".");
        if(nDotIndex < 0) {
            mInputStr+=".";
        }
        else if((mOperatorIndex>0)&&(nDotIndex < mOperatorIndex))  // 111.2434 + 2
        {
            mInputStr+=".";
        }
        tvExchange1.setText(mInputStr);
    }

    // OnInitialize와 동일한 기능
    public void initView(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
    {

    }

    public String makeDecimal(double lVal,int nMinFraction,int nFractionDigit)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumIntegerDigits(15); //최대수 지정

        if(nMinFraction > 0)
            nf.setMinimumFractionDigits(nMinFraction);
        if(nFractionDigit > 0)
            nf.setMaximumFractionDigits(nFractionDigit);
        String sPrice = nf.format(lVal);

        return sPrice;
    }

    public double calculation()
    {
        if(mInputStr.length() <= 0)
            return .0f;
        String sTemp = delCommaInDouble(mInputStr);
        Queue<String> que = mCalc.transformPostfix(sTemp);
        double dResult = mCalc.caculatePostfix(que);
//        mInputStr = makeDecimal(dResult,0,2);
        sTemp = String.valueOf(dResult);

        showAnotherVal(sTemp);
        return dResult;
    }

    public void showAnotherVal(String sValue)
    {
        if(mInputStr.length()<=0) {
            tvExchange2.setText("");
            return;
        }
        try {
            String sTemp = delCommaInDouble(sValue);
            double dVal = Double.valueOf(sTemp);
            double dAnotherVal = calc_InputNumber(dVal, mType);
            tvExchange2.setText(makeDecimal(dAnotherVal, 0, 2));
        }
        catch(Exception e)
        {

        }
    }
    public String delCommaInDouble(String sVal)
    {
        StringBuffer sb = new StringBuffer(sVal.length());
        for (int i = 0; i < sVal.length(); i++) {
            char c = sVal.charAt(i);
            if (c==',') {
                continue;
            }
            sb.append(c);
        }
        String sRtn = String.valueOf(sb);
        return sRtn;
    }

    public double calc_InputNumber(double dInput,int nMode)
    {
        double dChangeVal = mLiveDatalab.getCurrencyValue(mCurrency);
        //   if((mCurrency.indexOf("JPY")>=0) || (mCurrency.indexOf("IDR")>=0))
        //       dChangeVal = dChangeVal / 100;
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
                                               if(bDoingChgCurrency==false)
                                               {
                                                   if (mType == 0) {
                                                       bDoingChgCurrency = true;
                                                       Resources res = getActivity().getResources();
                                                       final CurrencyAdapter adapter = mNationLab.getCurrencyCalcAdapter(getActivity());
                                                       // adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.kor), "KRW", "대한민국"));
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
