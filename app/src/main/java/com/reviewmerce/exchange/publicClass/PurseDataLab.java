package com.reviewmerce.exchange.publicClass;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.ExchangeData;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.db.ExchangeDatabase;
import com.reviewmerce.exchange.db.PurseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by onebuy on 2015-09-07.
 */
public class PurseDataLab {
    GlobalVar mGlobalVar=null;
    NationDataLab mNationLab = null;
    private static PurseDataLab sPurseDataLab;
    public static PurseDatabase mPurseDB = null;
    public static PurseDataLab get(Context c) {
        if (sPurseDataLab == null) {
            sPurseDataLab = new PurseDataLab(c.getApplicationContext());
        }
        return sPurseDataLab;
    }
    public PurseDataLab(Context appContext) {
        mDbisOpened = false;
        mGlobalVar = GlobalVar.get();
        mNationLab = NationDataLab.get(null);
        if(appContext != null)
            mAppContext = appContext;
        initData();
    }
    ////////////////////////////////////////////////////////////////////////////

    private Context mAppContext;
    private boolean mDbisOpened=false;
    private String mBeginDate="";
    private String mEndDate="";
    private ArrayList<PurseData> mPurseData;
    private double mBudget=.0f;
    private String mCurrencyChar="";
    ///////////////////////////////////////////////////////////////////////////////////////
    // 데이터 set/ get
    public void setBudget(double dVal)
    {
        mBudget = dVal;
    }
    public double getBudget()
    {
        return mBudget;
    }
    public void setCurrencyChar(String sVal)
    {
        mCurrencyChar = sVal;
    }
    public String getCurrencyChar()
    {
        return mCurrencyChar;
    }
    public void setBeginDate(int nYear, int nMonth, int nDay)
    {
        try {
            mBeginDate = String.format("%04d%02d%02d", nYear, nMonth, nDay);
        }catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
    }
    public void setEndDate(int nYear, int nMonth, int nDay)
    {
        try {
            mEndDate = String.format("%04d%02d%02d", nYear, nMonth, nDay);
        }catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
    }
    public int getBeginDate_year()
    {
        int nRtnVal = 0;
        try {
            int length = mBeginDate.length();
            if (length < 8)
                return 0;
            nRtnVal = Integer.valueOf(mBeginDate.substring(0, 4));
        }
        catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
        return nRtnVal;
    }
    public int getBeginDate_month()
    {
        int nRtnVal = 0;
        try {
            int length = mBeginDate.length();
            if (length < 8)
                return 0;
            nRtnVal = Integer.valueOf(mBeginDate.substring(4, 6));
        }
        catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
        return nRtnVal;
    }
    public int getBeginDate_day()
    {
        int nRtnVal = 0;
        try {
            int length = mBeginDate.length();
            if (length < 8)
                return 0;
            nRtnVal = Integer.valueOf(mBeginDate.substring(6, 8));
        }
        catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
        return nRtnVal;
    }
    public int getEndDate_year() {
        int nRtnVal = 0;
        try
        {
            int length = mBeginDate.length();
            if (length < 8)
                return 0;
            nRtnVal = Integer.valueOf(mEndDate.substring(0, 4));
        }catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
    }
        return nRtnVal;
    }
    public int getEndDate_month()
    {
        int nRtnVal = 0;
        try {
            int length = mBeginDate.length();
            if (length < 8)
                return 0;
            nRtnVal = Integer.valueOf(mEndDate.substring(4, 6));
        }catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
        return nRtnVal;
    }
    public int getEndDate_day()
    {
        int nRtnVal = 0 ;
        try {
            int length = mBeginDate.length();
            if (length < 8)
                return 0;
            nRtnVal = Integer.valueOf(mEndDate.substring(6, 8));
        }catch (Exception e) {

            Log.e(BasicInfo.TAG, "Data Error");
        }
        return nRtnVal;
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    public void initData() {

        initPurseData();
        loadPurseData_DB();
    }
    public void initPurseData() {
        mPurseData = new ArrayList<PurseData>();

    }
    public ArrayList<PurseData> getData()
    {
        return mPurseData;
    }
    public void openDatabase() {
        mPurseDB = PurseDatabase.getInstance(mAppContext);

        boolean isOpen = mPurseDB.open();
        if (isOpen) {
            mDbisOpened = true;
            Log.d(BasicInfo.TAG, "exchange database is open.");
        } else {
            Log.d(BasicInfo.TAG, "exchange database is not open.");
        }
        mPurseDB.createPurseTable("purse_table");//createPurseTable("purse_table");
    }
    public void loadPurseData_DB() {
        try {
            openDatabase();
            if(mPurseData==null)
                mPurseData = new ArrayList<PurseData>();
            else
                mPurseData.clear();

            excutePurseHeaderLoad();
            excutePurseLoad();
//            mLastCurrency = GlobalVar.mCurrency;
        } catch (Exception e) {

            Log.e(BasicInfo.TAG, "Error loading ExchangeData");
        }


    }
    public void savePurseData_DB()
    {
        excuteDataSave();
    }



    private void excutePurseLoad()
    {
        if (mDbisOpened == false) {
            openDatabase();
        }

        String SQL = "select * "
                + "from " + "purse_table" + " order by " + BasicInfo.TABLE_PURSE_INDEX + " asc";
        Cursor c1;

        try {

            c1 = mPurseDB.rawQuery(SQL);
            if(c1==null)
                return ;
        }
        catch(Exception e)
        {
            Log.e("Excute Bank LoadDB : ", e.toString());
            return ;
        }
        int recordCount = c1.getCount();
        String sIndex, sItem,sValue,sItemDate,sItemTime,sCurrency;
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
        Date date;


        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            sIndex = "";
            sIndex = c1.getString(0);
            if (sIndex.length()>0) {

                    sItem = c1.getString(1);
                    sItemDate = c1.getString(2);
                    try {
                        date = fm.parse(sItemDate);
                        sItemDate = fmNew.format(date);
                    } catch (Exception e) {
                        Log.e("Excute InitDB : ", e.toString());
                    }
                    sItemTime = c1.getString(3);
                    sValue = c1.getString(4);
                    sCurrency = c1.getString(5);
                    mPurseData.add(new PurseData(i,sItem,sItemDate,sItemTime,Double.valueOf(sValue),sCurrency));
                }
            }
        c1.close();
    }
    private void excutePurseHeaderLoad()
    {
        if (mDbisOpened == false) {
            openDatabase();
        }
        String SQL = "select * "
                + "from " + "purse_table_header";
        Cursor c1;
        try {
            c1 = mPurseDB.rawQuery(SQL);
            if(c1==null)
                return ;
        }
        catch(Exception e)
        {
            Log.e("Excute Bank LoadDB : ", e.toString());
            return ;
        }
        String sIndex,sStartDate,sEndDate,sBudget,sCurrency, sItem,sValue,sItemDate,sItemTime;
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
        Date date;
        c1.moveToNext();
        sStartDate = c1.getString(0);
        int nComp = sStartDate.compareTo("null");
        if (nComp < 0) {

            try {
                date = fm.parse(sStartDate);
                sStartDate = fmNew.format(date);
            } catch (Exception e) {
                Log.e("Excute InitDB : ", e.toString());
            }
            sEndDate = c1.getString(1);
            try {
                date = fm.parse(sEndDate);
                sEndDate = fmNew.format(date);
            } catch (Exception e) {
                Log.e("Excute InitDB : ", e.toString());
            }

            sBudget = c1.getString(2);
            sCurrency = c1.getString(3);

            mBeginDate = sStartDate;
            mEndDate = sEndDate;
            mBudget = Double.valueOf(sBudget);
            mCurrencyChar = sCurrency;
        }
    }
    private void excuteDataSave()
    {
        //insert into USD values ("2012-07-12",	1171, 	0.65);
        if (mDbisOpened == false) {
            openDatabase();
        }
        int nIndex = 0;

        try {
            mPurseDB.delete_createPurseTable("purse_table");
            String SQL = "insert or replace into purse_table_header" + " values (" +
                    "\"" + mBeginDate + "\"," +
                    "\"" + mEndDate + "\"," +
                    Double.toString(mBudget) + "," +
                    "\"" + mNationLab.getCurrencyCodeInEng()+ "\"" +//mCurrencyChar + "\"" +
                    ")";
            mPurseDB.execSQL(SQL);


            for (PurseData e : mPurseData) {
                String SQL2 = "insert or replace into purse_table" + " values (" +
                        Integer.toString(nIndex) + "," +
                        "\"" + e.getTitle() + "\"," +
                        "\"" + e.getDate() + "\"," +
                        "\"" + e.getTime() + "\"," +
                        Double.toString(e.getValue()) +"," +
                        "\"" + e.getCurrencyCode() + "\"" +
                        ")";
                mPurseDB.execSQL(SQL2);
                nIndex++;
            }

        } catch (Exception e) {
            Log.e("AddDB error : ", e.toString());
        }
    }


    public void addItem( String sTitle,  String sDate, String sTime, double dValue,String sCurrencycode)
    {
        int nIndex = mPurseData.size();
        PurseData item = new PurseData(nIndex,sTitle,sDate,sTime,dValue,sCurrencycode);
        mPurseData.add(item);
    }
    public void delItem(PurseData item)
    {
        int nIndex = mPurseData.size();
        //PurseData item = new PurseData(nIndex,sTitle,sDate,sTime,dValue);
        mPurseData.remove(item);
    }

}
