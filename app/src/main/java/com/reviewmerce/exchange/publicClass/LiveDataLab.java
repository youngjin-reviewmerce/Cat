package com.reviewmerce.exchange.publicClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.ExchangeData;
import com.reviewmerce.exchange.db.PurseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by onebuy on 2015-09-07.
 */
public class LiveDataLab {
    static String mTag = "LiveDataDB";
    //public static PurseDatabase database;

    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;


    GlobalVar mGlobalVar=null;
    private static LiveDataLab mLiveDataLab;
    ////////////////////////////////////////////////////////////////////////////
    private Context mAppContext;
    private boolean mLiveDbisOpened=false;
    protected ArrayList<ExchangeData> mLiveData;
    protected String mCurrencyChar="";
    private final Object syncObj1= new Object();;
    public static LiveDataLab get(Context c) {

            if (mLiveDataLab == null) {
                mLiveDataLab = new LiveDataLab(c.getApplicationContext());
                       // 동기화 객체

        }
        return mLiveDataLab;
    }
    public LiveDataLab(Context appContext) {
        mLiveDbisOpened = false;
        mGlobalVar = GlobalVar.get();
        if(appContext != null)
            mAppContext = appContext;
        initData();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // 데이터 set/ get
    public void setCurrencyChar(String sVal)
    {
        mCurrencyChar = sVal;
    }
    public String getCurrencyChar()
    {
        return mCurrencyChar;
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    public void initData() {

        initLiveData();
        loadLiveData_DB();
    }
    public void initLiveData() {

        synchronized (syncObj1) {
            mLiveData = new ArrayList<ExchangeData>();
        }
    }
    public ArrayList<ExchangeData> getCurrencyData()
    {
        synchronized (syncObj1) {
            return mLiveData;
        }
    }
    public void openDatabase() {

        boolean isOpen = db_open();
        if (isOpen) {
            mLiveDbisOpened = true;
            Log.d(BasicInfo.TAG, "exchange database is open.");
        } else {
            Log.d(BasicInfo.TAG, "exchange database is not open.");
        }
        db_createLiveTable("live_table");//createPurseTable("purse_table");
    }
    public void loadLiveData_DB() {
        try {
            openDatabase();
            excuteLiveLoad();
        } catch (Exception e) {

            Log.e(BasicInfo.TAG, "Error loading ExchangeData");
        }
    }

    public void saveLiveData_DB()
    {
        excuteLiveSave();
    }
    private void excuteLiveLoad()
    {
        if (mLiveDbisOpened == false) {
            openDatabase();
        }

        String SQL = "select * "
                + "from " + "live_table";
        Cursor c1;

        try {

            c1 = db_rawQuery(SQL);
            if(c1==null)
                return ;
        }
        catch(Exception e)
        {
            Log.e(mTag, e.toString());
            return ;
        }

        int recordCount = c1.getCount();
        synchronized (syncObj1) {
            if (recordCount > 0) {
                if (mLiveData == null)
                    mLiveData = new ArrayList<ExchangeData>();
                else
                    mLiveData.clear();
            }
            String sCurrencyCode, sValue, sItemDate, sItemTime;
            DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
            Date date;

            for (int i = 0; i < recordCount; i++) {
                c1.moveToNext();
                sCurrencyCode = "";
                sCurrencyCode = c1.getString(0);
                if (sCurrencyCode.length() > 0) {


                    sItemDate = c1.getString(1);
                    try {
                        date = fm.parse(sItemDate);
                        sItemDate = fmNew.format(date);
                    } catch (Exception e) {
                        Log.e("Excute InitDB : ", e.toString());
                    }

                    sValue = c1.getString(2);
                    sItemTime = c1.getString(3);

                    mLiveData.add(new ExchangeData("0", sCurrencyCode, sItemDate, sValue, sItemTime));
                    //                mPurseData.add(new PurseData(i,sItem,sItemDate,sItemTime,));
                }
            }
        }
        c1.close();
    }

    private void excuteLiveSave()
    {
        synchronized (syncObj1) {
            //insert into USD values ("2012-07-12",	1171, 	0.65);
            if (mLiveDbisOpened == false) {
                openDatabase();
            }
            int nIndex = 0;

            try {
                db_delete_createCurrencyTable("live_table");

                for (ExchangeData e : mLiveData) {
                    String SQL = "insert or replace into live_table" + " values (" +
                            "\"" + e.getCurrencyCode() + "\"," +
                            "\"" + e.getDate() + "\"," +
                            "\"" + e.getBasicRates() + "\"," +
                            "\"" + e.getTime() + "\"" +
                            ")";
                    db_execSQL(SQL);
                    nIndex++;
                }
            } catch (Exception e) {
                Log.e("AddDB error : ", e.toString());
            }
        }
    }
    public ExchangeData getLiveCurrencyData(String sCode)
    {
        synchronized (syncObj1) {
            ExchangeData Rtnval = new ExchangeData();
            for (ExchangeData e : mLiveData) {
                if (e.getCurrencyCode().indexOf(sCode) >= 0) {
                    Rtnval = e;
                }
            }
            return Rtnval;
        }
    }
    public double getCurrencyValue(String sCode)
    {
        synchronized (syncObj1) {
            double dRtnval = .0f;
            double dUsd = .0f;
            if (sCode.indexOf("KRW") >= 0)
                return 1.f;
            for (ExchangeData e : mLiveData) {
                if (e.getCurrencyCode().indexOf(sCode) >= 0) {
                    dRtnval = Double.valueOf(e.getBasicRates());
                    if ((e.getCurrencyCode().indexOf("JPY") >= 0) || (e.getCurrencyCode().indexOf("IDR") >= 0) || (e.getCurrencyCode().indexOf("VND") >= 0) || (e.getCurrencyCode().indexOf("KHR") >= 0))
                        dRtnval = dRtnval / 100;
                }
                if (e.getCurrencyCode().indexOf("USD") >= 0) {
                    dUsd = Double.valueOf(e.getBasicRates());
                }
            }
            if (dRtnval <= .0f)
                return dUsd;
            return dRtnval;
        }
    }
    public String getLiveDate_Time(String sCode)
    {
        synchronized (syncObj1) {
            String sTime = "";
            String sDate = "";
            for (ExchangeData e : mLiveData) {
                if (e.getCurrencyCode().indexOf(sCode) >= 0) {
                    sTime = e.getTime();
                    sDate = e.getDate();
                    DateFormat fm = new SimpleDateFormat("yyyyMMdd");
                    DateFormat fmNew = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    try {
                        date = fm.parse(sDate);
                        sDate = fmNew.format(date);
                    } catch (Exception ee) {
                        Log.e(mTag, ee.toString());
                    }
                }
            }
            return sDate + " " + sTime;
        }
    }

    public void addCurrencyItem(String sCurrency,  String sDate,  String sValue, String sTime)
    {
        synchronized (syncObj1) {
            ExchangeData item = new ExchangeData("0", sCurrency, sDate, sValue, sTime);
            mLiveData.add(item);
        }
    }
    public void removeAllLiveItem()
    {
        synchronized (syncObj1) {
            mLiveData.clear();
        }

    }
    public void net_procPursetoDatalab(String sResult)
    {
        JSONObject json = null;
        ArrayList<BankData> AddBankDataArray = new ArrayList<BankData>();
        String sDate, sTime, sBank, sBasicRates, sCashBuying, sCashSelling,
                sStransferSending, sStransferReceiving, sCheckBuying, sCheckSelling;
        String sCurrency;
        try {
            json = new JSONObject(sResult);
            JSONArray jsonArray = json.getJSONArray("exchange_rates");
            JSONObject j;
            for (int i = 0; i < jsonArray.length(); i++) {
                j = jsonArray.getJSONObject(i);
                try {
                    sDate = j.getString(BasicInfo.json_BANK_UPDATEDATE);      //key
                }catch (Exception e) {
                    continue;
                }
                try {
                    sTime = j.getString(BasicInfo.json_BANK_UPDATETIME);
                }catch (Exception e) {
                    sTime = "000000";
                }
//                try {
//                    sBank = j.getString(BasicInfo.json_BANK_BANK);
//                }catch (Exception e) {
//                    continue;
//                }

                try {
                    sBasicRates = j.getString(BasicInfo.json_BANK_BASICRATES);
                }catch (Exception e) {
                    sBasicRates = "0";
                }
                try {
                    sCurrency = j.getString(BasicInfo.json_BANK_CURRENCY);
                }catch (Exception e) {
                    sCurrency = "";
                }
                //ExchangeData ed = new ExchangeData(sCurrency,sDate,sBasicRates,sTime);
                addCurrencyItem(sCurrency, sDate, sBasicRates, sTime);
            } // for
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }
    }
    private boolean db_open() {
        Log.i(mTag, "opening database [" + BasicInfo.LIVE_DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(mAppContext);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    /**
     */
    private void db_close() {
        Log.i(mTag, "closing database [" + BasicInfo.LIVE_DATABASE_NAME + "].");
        db.close();
    }
    private void db_delete_createCurrencyTable(String sTable)
    {
        Log.i(mTag, "creating database [" + BasicInfo.LIVE_DATABASE_NAME + "].");

        // TABLE_MEMO
        Log.i(mTag, "creating table [" + sTable + "].");

        // drop existing table
        String DROP_SQL = "drop table if exists " + sTable;
        //String DROP_SQL = "drop table if exists USD";
        try {
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            Log.e(mTag, "Exception in DROP_SQL", ex);
        }
        db_createLiveTable(sTable);

    }

    private void db_createLiveTable(String sTable)
    {
        Log.i(mTag,"creating database [" + BasicInfo.LIVE_DATABASE_NAME + "].");

        // TABLE_MEMO
        Log.i(mTag,"creating table [" + sTable + "].");
        String CREATE_SQL = "create table " + sTable + " ("
                + BasicInfo.TABLE_MONI_CURENCYCODE + " STRING NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_MONI_DATE + " STRING, "
                + BasicInfo.TABLE_MONI_BASICRATES + " REAL, "
                + BasicInfo.TABLE_MONI_TIME  + " STRING"
                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(mTag, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.e(mTag, "Exception in CREATE_SQL", ex);
        }

    }
    private Cursor db_rawQuery(String SQL) {
        Log.i(mTag, "\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            Log.i(mTag, "cursor count : " + c1.getCount());
        } catch (Exception ex) {
            Log.e(mTag, "Exception in executeQuery", ex);
        }

        return c1;
    }

    private boolean db_execSQL(String SQL) {
        Log.i(mTag, "\nexecute called.\n");

        try {
            Log.d(mTag, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch (Exception ex) {
            Log.e(mTag, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }
    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final String PACKAGE_DIR = "/data/data/com.reviewmerce.exchange/databases";

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.LIVE_DATABASE_NAME, null, DATABASE_VERSION);
            initialize(context);
        }

        public void initialize(Context ctx) {

        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onOpen(SQLiteDatabase db) {
            Log.i(mTag,"opened database [" + BasicInfo.LIVE_DATABASE_NAME + "].");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.i(mTag,"Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
}
