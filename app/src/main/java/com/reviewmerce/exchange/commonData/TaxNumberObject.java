package com.reviewmerce.exchange.commonData;

/**
 * Created by onebuy on 2015-09-07.
 */
public class TaxNumberObject {

    final int SENDTAX = 0x01;
    final int RECEIVETAX = 0x02;
    final int CARDRATE = 0x04;
    final int TIPRATE = 0x08;

    public double getResultData()
    {
        return mResultValue;
    }
    public double getOrginData()
    {
        return mOriginValue;
    }
    private int mAvailableMode;
    private double mSendTax;
    private double mReceiveTax;
    private double mCardRate;
    private double mTipRate;

    private double mOriginValue;
    private double mResultValue;

    public TaxNumberObject() {
    }

    public TaxNumberObject(int nAvailableMode, double dSendTax, double dReceiveTax, double dCardRate, double dTipRate) {
        setData(nAvailableMode, dSendTax, dReceiveTax, dCardRate, dTipRate);
    }
    public TaxNumberObject(NationTaxData p)
    {
        mAvailableMode = p.getAvailableMode();
        mSendTax = p.getSendTax();
        mReceiveTax = p.getReceiveTax();
        mCardRate = p.getCardRate();
        mTipRate = p.getTipRate();
    }

    public void setData(int nAvailableMode, double dSendTax, double dReceiveTax, double dCardRate, double dTipRate)
    {
        mAvailableMode = nAvailableMode;
        mSendTax = dSendTax;
        mReceiveTax = dReceiveTax;
        mCardRate = dCardRate;
        mTipRate = dTipRate;
    }

    public double procCalc()
    {
        double dSend = .0f;
        double dRecieve = .0f;
        double dCard = .0f;
        double dTip = .0f;
        if((mAvailableMode & SENDTAX) > 0)
        {
            dSend = procSendTax(mOriginValue,mSendTax);
        }
        if((mAvailableMode & RECEIVETAX) > 0)
        {
            dRecieve = procRecieveTax(mOriginValue,mReceiveTax);

        }
        if((mAvailableMode & CARDRATE) > 0)
        {
            dCard = procCardRate(mOriginValue, mCardRate);
        }
        if((mAvailableMode & TIPRATE) > 0)
        {
            dTip = procTipRate(mOriginValue,mTipRate);
        }
        mResultValue = mOriginValue + dSend - dRecieve + dCard + dTip;
        return mResultValue;
    }


    public double procSendTax(double dOrigin, double dRate)
    {
        double dRtn = dOrigin * dRate;
        return dRtn;
    }

    public double procRecieveTax(double dOrigin, double dRate)
    {
        double dRtn = dOrigin * dRate;
        return dRtn;
    }

    public double procCardRate(double dOrigin, double dRate)
    {
        double dRtn = dOrigin * dRate;
        return dRtn;
    }

    public double procTipRate(double dOrigin, double dRate)
    {
        double dRtn = dOrigin * dRate;
        return dRtn;
    }
    public void setSendTax(double dVal)
    {
        mSendTax = dVal;
    }
    public void setReceiveTax(double dVal)
    {
        mReceiveTax = dVal;
    }
    public void setTipRate(double dVal)
    {
        mTipRate = dVal;
    }
    public void setCardRate(double dVal)
    {
        mCardRate = dVal;
    }
    public double getSendTax( )
    {
        return mSendTax;
    }
    public double getReceiveTax()
    {
        return mReceiveTax;
    }
    public double getCardRate( )
    {
        return mCardRate;
    }
    public double getTipRate()
    {
        return mTipRate;
    }
    public int getAvailableMode()
    {
        return mAvailableMode;
    }
    public void setAvailableMode(int nVal)
    {
        mAvailableMode = nVal;
    }
    public int compareTo(BankItem other) {
        return 0;
    }
}

