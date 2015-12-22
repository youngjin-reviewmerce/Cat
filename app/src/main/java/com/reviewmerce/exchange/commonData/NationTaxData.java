package com.reviewmerce.exchange.commonData;

/**
 * Created by onebuy on 2015-09-07.
 */
public class NationTaxData {
    private int mAvailableMode;
    private double mSendTax;
    private double mReceiveTax;
    private double mCardRate;
    private double mTipRate;

    public NationTaxData() {
    }

    public NationTaxData(int nAvailableMode, double dSendTax, double dReceiveTax, double dCardRate, double dTipRate) {
        setData(nAvailableMode, dSendTax, dReceiveTax, dCardRate, dTipRate);
    }
    public NationTaxData(NationTaxData p)
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

