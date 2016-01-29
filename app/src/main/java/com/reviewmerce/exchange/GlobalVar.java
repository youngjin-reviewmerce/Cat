package com.reviewmerce.exchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;

import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.NationData;
import com.reviewmerce.exchange.publicClass.NationDataLab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by onebuy on 2015-07-24.
 */
public class GlobalVar {

    private static GlobalVar mInst;
    private NationDataLab mNationLab = null;
    private Context mAppContext;
    public static int EXTANDPAGE_TYPE_CALC = 0x01;
    public static GlobalVar get() {

        if (mInst == null) {
            mInst = new GlobalVar();
        }
        return mInst;
    }
    /**
     */
    // 전역 변수

    private int mGraphPeriod=3;           // 기간
    //private String mSelectCurrency="USD";
    private NationData mSelectCurrency=null;
    private Boolean mNetworkMode = true;
    private int mScreenPage = 1;
    private String mSdk="";
    private int mExtandPage = 0;
    // UI
    private String mExchangeType="기준 환율";

    public NationData getSelectCurrency() {
        return mSelectCurrency;
    }

    public void setSelectCurrency(NationData nd) {
        if(mSelectCurrency == null)
        {
            mSelectCurrency = new NationData();
        }
        mSelectCurrency.setData(nd);
    }

    public GlobalVar()
    {
        mNationLab = NationDataLab.get(null);
        mSelectCurrency = new NationData();
    }

    public String getExchangeType()
    {
        return mExchangeType;
    }
    public void setExchangeType(String sExchangeType)
    {
        mExchangeType = sExchangeType;
    }
    public void setGraphPeriod(int nGraphPeriod)
    {
        mGraphPeriod = nGraphPeriod;
    }
    public int getGraphPeriod()
    {
        return mGraphPeriod;
    }
    public void setScreenPage(int nScreenPage)
    {
        mScreenPage = nScreenPage;
    }
    public int getScreenPage()
    {
        return mScreenPage;
    }
    public void setExtandPage(int nPage)
    {   mExtandPage = nPage;}
    public int getExtandPage()
    {
        return mExtandPage;
    }
    public void addExtandPage(int nPage)
    {
        mExtandPage = mExtandPage | nPage;
    }
    public void removeExtandPage(int nPage)
    {
        nPage = ~nPage;
        mExtandPage = mExtandPage & nPage;
    }
    public void setNetworkMode(Boolean bNetworkMode)
    {
        mNetworkMode = bNetworkMode;
    }
    public Boolean getNetworkMode()
    {
        return mNetworkMode;
    }
    public void setSdk(String sSdk)
    {
        mSdk = sSdk;
    }
    public String getSdk()
    {
        return mSdk;
    }


}
