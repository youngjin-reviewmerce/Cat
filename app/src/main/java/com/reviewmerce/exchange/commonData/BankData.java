package com.reviewmerce.exchange.commonData;

import com.reviewmerce.exchange.BasicInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by onebuy on 2015-09-07.
 */
public class BankData {
    private int mIndex;
    private String mUpdateDate;
    private String mUpdateTime;
    private int mBank;

    private double mBasicRates;

    private double mCashBuying;
    private double mCashSelling;
    private double mStransferSending;
    private double mStransferReceiving;
    private double mCheckBuying;
    private double mCheckSelling;

    private double mDifference;
    private int mType;
    public BankData() {
    }

    public BankData(int nIndex, String sDate, String sTime, String sBank, String sBasicRates, String sCashBuying, String sCashSelling
            , String sStransferSending, String sStransferReceiving, String sCheckBuying, String sCheckSelling) {
        setData(nIndex, sDate, sTime, sBank, sBasicRates, sCashBuying, sCashSelling, sStransferSending, sStransferReceiving, sCheckBuying, sCheckSelling);
    }


    public BankData(JSONObject json) throws JSONException {
        mUpdateDate         = json.getString(BasicInfo.json_BANK_UPDATEDATE);
        mUpdateTime         = json.getString(BasicInfo.json_BANK_UPDATETIME);
        mBank                = json.getInt(BasicInfo.json_BANK_BANK);
        mBasicRates         = json.getDouble(BasicInfo.json_BANK_BASICRATES);

        mCashBuying         = json.getDouble(BasicInfo.json_BANK_CASHBUYING);
        mCashSelling        = json.getDouble(BasicInfo.json_BANK_CASHSELLING);
        mStransferSending  = json.getDouble(BasicInfo.json_BANK_STRANSFERSENDING);
        mStransferReceiving = json.getDouble(BasicInfo.json_BANK_STRANSFERRECEIVING);
        mCheckBuying        = json.getDouble(BasicInfo.json_BANK_CHECKBUYING);
        mCheckSelling       = json.getDouble(BasicInfo.json_BANK_CHECKSELLING);
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(BasicInfo.json_MONI_DATE, mUpdateDate);
        json.put(BasicInfo.json_MONI_TIME, mUpdateTime);
        json.put(BasicInfo.json_BANK_BANK, String.valueOf(mBank));
        json.put(BasicInfo.json_MONI_BASICRATES, String.valueOf(mBasicRates));

        json.put(BasicInfo.json_BANK_CASHBUYING, String.valueOf(mCashBuying));
        json.put(BasicInfo.json_BANK_CASHSELLING, String.valueOf(mCashSelling));
        json.put(BasicInfo.json_BANK_STRANSFERSENDING, String.valueOf(mStransferSending));
        json.put(BasicInfo.json_BANK_STRANSFERRECEIVING, String.valueOf(mStransferReceiving));
        json.put(BasicInfo.json_BANK_CHECKBUYING, String.valueOf(mCheckBuying));
        json.put(BasicInfo.json_BANK_CHECKSELLING, String.valueOf(mCheckSelling));
        return json;
    }

    public void setData(int nIndex, String sDate, String sTime, String sBank, String sBasicRates, String sCashBuying, String sCashSelling
                        , String sStransferSending, String sStransferReceiving, String sCheckBuying, String sCheckSelling) {

        mIndex = nIndex;
        mUpdateDate = sDate;
        mUpdateTime = sTime;

        try {
            mBank = Integer.valueOf(sBank);
        }catch (Exception e) {
            mBank = 0;
        }
        try {
            mBasicRates = Double.valueOf(sBasicRates);
        }catch (Exception e) {
            mBasicRates = .0f;
        }

        try {
            mCashBuying = Double.valueOf(sCashBuying);
        }catch (Exception e) {
            mCashBuying = .0f;
        }
        try {
            mCashSelling = Double.valueOf(sCashSelling);
        }catch (Exception e) {
            mCashSelling = .0f;
        }

        try {
            mStransferSending = Double.valueOf(sStransferSending);
        }catch (Exception e) {
            mStransferSending = .0f;
        }
        try {
            mStransferReceiving = Double.valueOf(sStransferReceiving);
        }catch (Exception e) {
            mStransferReceiving = .0f;
        }

        try {
            mCheckBuying = Double.valueOf(sCheckBuying);
        }catch (Exception e) {
            mCheckBuying = .0f;
        }
        try {
            mCheckSelling = Double.valueOf(sCheckSelling);
        }catch (Exception e) {
            mCheckSelling = .0f;
        }

        mDifference = .0f;
        mType = 0;
    }
    @Override
    public String toString() {
        String sRtn;
        sRtn = mUpdateDate + " " + mUpdateTime + " " + String.valueOf(mBank) + " " + String.valueOf(mBasicRates) + " " + String.valueOf(mCashBuying)
                + " " + String.valueOf(mCashSelling) + " " + String.valueOf(mStransferSending) + " " + String.valueOf(mStransferReceiving)
                + " " + String.valueOf(mCheckBuying) + " " + String.valueOf(mCheckSelling);
        return sRtn;
    }

    public String getDate() {
        return mUpdateDate;
    }

    public String getTime()
    {
        return mUpdateTime;
    }

    public int getBank()
    {
        return mBank;
    }

    public double getBasicRates() {
        return mBasicRates;
    }

    public double getCashBuying() {
        return mCashBuying;
    }
    public double getCashSelling() {
        return mCashSelling;
    }

    public double getStransferSending() {
        return mStransferSending;
    }
    public double getStransferReceiving() {
        return mStransferReceiving;
    }

    public double getCheckBuying() {
        return mCheckBuying;
    }
    public double getCheckSelling() {
        return mCheckSelling;
    }
    public void setIndex(int index)
    {
        mIndex = index;
    }
    public int getIndex()
    {
        return mIndex;
    }
    public double getDiffrence()
    {
        return mDifference;
    }
    public void setDifference(double dVal)
    {
        mDifference = dVal;
    }
    public void setType(int nType)
    {
        mType = nType;
    }
    public int getTyep()
    {
        return mType;
    }

    public double getValue()
    {
        double dRtn = .0f;
        switch(mType)
        {
            case 1:
                dRtn =  Double.valueOf(mBasicRates);
                break;
            case 2:
                dRtn =  Double.valueOf(mCashBuying);
                break;
            case 3:
                dRtn =  Double.valueOf(mCashSelling);
                break;
            case 4:
                dRtn =  Double.valueOf(mStransferSending);
                break;
            case 5:
                dRtn =  Double.valueOf(mStransferReceiving);
                break;
            case 6:
                dRtn =  Double.valueOf(mCheckBuying);
                break;
            case 7:
                dRtn =  Double.valueOf(mCheckSelling);
                break;
        }
        return dRtn;
    }
}
