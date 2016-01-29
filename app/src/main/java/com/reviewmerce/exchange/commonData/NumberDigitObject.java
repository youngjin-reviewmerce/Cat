package com.reviewmerce.exchange.commonData;

import java.text.NumberFormat;

/**
 * Created by onebuy on 2015-09-07.
 */
public class NumberDigitObject {

    private double mData;
    private int nDigit;
    final int MAX_FRACTION=10;
    final int MAX_LENGTH=16;
    public double getData()
    {
        return mData;
    }
    public void setData(double dData)
    {
        mData = dData;
    }



    public NumberDigitObject() {
        nDigit = 0;
    }

    public NumberDigitObject(double dData)
    {
        setData(dData);
    }
    public NumberDigitObject(NationTaxData p)
    {

    }
    public double addNumber(int nAddNum, boolean bFraction)
    {
        long lValue = 0L;
        long lMulValue = 1L;
        if(bFraction) {
            nDigit++;
            if(nDigit > MAX_FRACTION)
            {
                nDigit = MAX_FRACTION;
            }
            for(int i=0;i<nDigit;i++) {
                lMulValue = lMulValue * 10;
            }
            lValue = (long)(mData * lMulValue);
        }
        else
        {
            lValue = (long)(mData*10);
        }
        lValue += nAddNum;
        mData = lValue / lMulValue ;
        return mData;
    }
    public double delNumber() {

        long lValue = 0L;
        long lMulValue = 1L;
        if (nDigit > 0) {

            for (int i = 0; i < nDigit; i++) {
                lMulValue = lMulValue * 10;
            }
            lValue = (long) (mData * lMulValue);
            nDigit--;
        } else {
            lValue = (long) (mData / 10);
        }
        mData = lValue / lMulValue;
        return mData;
    }
    public String toString()
    {
        String str;
        str = makeDecimal(mData,0,nDigit);
        return str;
    }
    public String makeDecimal(double dVal,int nMinFraction,int nFractionDigit)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumIntegerDigits(15); //최대수 지정
        nf.setMinimumFractionDigits(nMinFraction);
        nf.setMaximumFractionDigits(nFractionDigit);
        String sPrice = nf.format(dVal);

        return sPrice;
    }
}

