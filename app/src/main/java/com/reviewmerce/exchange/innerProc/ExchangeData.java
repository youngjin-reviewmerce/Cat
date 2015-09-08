package com.reviewmerce.exchange.innerProc;


import android.text.format.Time;

import com.reviewmerce.exchange.BasicInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by onebuy on 2015-07-23.
 */
public class ExchangeData {
//    private static final String JSON_Date = "date";
//    private static final String JSON_Price = "price";
//    private static fin    al String JSON_PriceDecimals = "price_decimals";

    private String mUpdateDate;
    private double mBasicRates; // 1/100 ��
    private String mUpdateTime;

    public ExchangeData() {
    }
    public ExchangeData(String sDate, String sBasicRates, String sTime) {
        setData(sDate,sBasicRates,sTime);
    }
    public ExchangeData(JSONObject json) throws JSONException {
        //mDate = new Date(json.getLong(JSON_Date));
        mUpdateDate = json.getString(BasicInfo.json_MONI_DATE);
        mBasicRates = json.getDouble(BasicInfo.json_MONI_BASICRATES);
        mUpdateTime = json.getString(BasicInfo.json_MONI_TIME);
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(BasicInfo.json_MONI_DATE, mUpdateDate);
        json.put(BasicInfo.json_MONI_BASICRATES, String.valueOf(mBasicRates));
        json.put(BasicInfo.json_MONI_TIME, mUpdateTime);

        return json;
    }

    public void setData(String sDate, String sBasicRates, String sTime) {
        mUpdateDate = sDate;
        try {
            mBasicRates = Double.valueOf(sBasicRates);
        }catch (Exception e)
        {
            mBasicRates = .0f;
        }
        mUpdateTime = sTime;
    }

    @Override
    public String toString() {
        String sRtn;
        sRtn = mUpdateDate + " " + String.valueOf(mBasicRates) + " " + String.valueOf(mUpdateTime);
        return sRtn;
    }

    public String getDate() {
        return mUpdateDate;
    }

    public double getBasicRates() {
        return mBasicRates;
    }

    public String getTime()
    {
        return mUpdateTime;
    }

}
