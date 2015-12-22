package com.reviewmerce.exchange.testClass;

/**
 * Created by onebuy on 2015-09-07.
 */
public class BankSortedData implements Comparable{
    private double mDiffrence;
    private int mBank;
    private double mRates;
    private String mDate;
    private String mTime;

    public BankSortedData() {
    }

    public BankSortedData(String sDate, String sTime,String sIndex, String sBank, String sRates) {
        setData(sDate, sTime, sIndex,sBank,  sRates);
    }
    public BankSortedData(String sDate, String sTime, double dDiff, int nBank, Double dRates) {
        setData(sDate, sTime, dDiff, nBank, dRates);
    }
    public void setData(String sDate, String sTime, double dDiff, int nBank, Double dRates) {
        mDate = sDate;
        mTime = sTime;
        mDiffrence = dDiff;
        mBank = nBank;
        mRates = dRates;
    }

    public void setData(String sDate, String sTime, String sDiff, String sBank, String sRates) {

        mDate = sDate;
        mTime = sTime;
        mDiffrence = Double.valueOf(sDiff);
        try {
            mDiffrence = Double.valueOf(sDiff);
        }catch (Exception e) {
            mDiffrence = .0f;
        }
        try {
            mBank = Integer.valueOf(sBank);
        }catch (Exception e) {
            mBank = 0;
        }

        try {
            mRates = Double.valueOf(sRates);
        }catch (Exception e) {
            mRates = .0f;
        }
    }
    public int compareTo(Object obj)
    {
        BankSortedData other = (BankSortedData)obj;
        if(mRates < other.mRates)
            return -1;
        else if (mRates > other.mRates)
            return 1;
        else
            return 0;
    }

    public double getDiffrence() {
        return mDiffrence;
    }

    public int getBank()
    {
        return mBank;
    }

    public double getRates()
    {
        return mRates;
    }
    public String getTime(){return mTime;}
    public String getDate(){return mDate;}


    public void setDiffrence(double dDiff)
    {
        mDiffrence = dDiff;
    }
    public void setBank(int nBank)
    {
        mBank = nBank;
    }
    public void setRates(double dRates)
    {
        mRates = dRates;
    }
    public void setTime(String sTime)
    {
        mTime = sTime;
    }

    public void setDate(String sDate)
    {
        mDate = sDate;
    }
}
