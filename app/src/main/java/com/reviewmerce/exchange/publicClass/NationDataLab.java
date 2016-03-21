package com.reviewmerce.exchange.publicClass;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.ListView.NationAdapter;
import com.reviewmerce.exchange.ListView.NationAdapterExpand;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.NationData;
import com.reviewmerce.exchange.commonData.NationItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onebuy on 2015-09-07.
 */
public class NationDataLab {
    static String mTag = "NationData";
    public static String TABLE_NATION_CURRENCYCODE_INENG = "currencycode_in_eng";
    public static String TABLE_NATION_COUNTRYNAME_INKOR = "countryname_in_kor";
    public static String TABLE_NATION_CURRENCY_CHAR = "curency_char";
    public static String TABLE_NATION_NUMERICEXPRESSION = "numeric_expression";

    public static String TABLE_NATION_GRAPH_TEXTCOLOR = "graph_text_color";
    public static String TABLE_NATION_GRAPH_COLOR = "graph_color";
    public static String TABLE_NATION_GRAPH_FILLCOLOR = "graph_fill_color";
    public static String TABLE_NATION_PURSE_TEXTCOLOR = "purse_text_color";
    public static String TABLE_NATION_PURSE_TEXTCOLOR2 = "purse_text_color2";

    public static String TABLE_NATION_BACKGROUND_FILE = "background_file";
    public static String TABLE_NATION_FLAG_FILE = "flag_file";

    public static String TABLE_GRAPH_BUBBLE_FILE = "graph_bubble_file";

    public static String TABLE_PURSE_BAR_FILE1 = "purse_bar_file1";
    public static String TABLE_PURSE_BAR_FILE2 = "purse_bar_file2";
    public static String TABLE_HALF_BAR_FILE = "half_bar_file";

    protected NationAdapter mNationCalcAdapter;
    protected NationAdapter mNationAdapter;
    private String[] origin44 = {
            "USD","EUR","CNY","JPY","VND","IDR","PHP","THB","AUD","SGD",
            "MYR","GBP","CAD","HKD","TWD","MXN","CHF","NZD","SEK","DKK",
            "NOK","SAR","KWD","BHD","AED","ZAR","RUB","HUF","PLN","TRY",
            "KZT","CZK","QAR","MNT","INR","PKR","BDT","EGP","BRL","BND",
            "ILS","JOD","OMR","CLP"};
    private String[] another122 = {
            "KES","IQD","XAU","COP","ARS","MAD","XOF","LKR","UAH","NGN",
            "TND","UGX","RON","PEN","GEL","XAF","FJD","VEF","BYR","HRK",
            "UZS","BGN","DZD","IRR","DOP","ISK","XAG","CRC","SYP","LYD",
            "JMD","MUR","GHS","AOA","UYU","AFN","LBP","XPF","TTD","TZS",
            "ALL","XCD","GTQ","NPR","BOB","BBD","LAK","BWP","HNL","PYG",
            "ETB","NAD","PGK","SDG","MOP","NIO","BMD","PAB","BAM","GYD",
            "YER","MGA","KYD","MZN","RSD","SCR","AMD","SBD","AZN","SLL",
            "TOP","BZD","MWK","GMD","BIF","SOS","HTG","GNF","MVR","CDF",
            "STD","TJS","MMK","LSL","LRD","KGS","GIP","XPT","MDL","CUP",
            "KHR","MKD"
    };
            //,"KES","XAU","COP","ARS","MAD"
            //"XOF","LKR","UAH","NGN","TND","UGX","RON","PEN","GEL","XAF",
            //"FJD","VEF","BYR","HRK","UZS","BGN","IRR","DOP","ISK",
            //"XAG","CRC"};

    private String[] kor122 = {
            "KES","IQD","XAU","COP","ARS","MAD","XOF","LKR","UAH","NGN",
            "TND","UGX","RON","PEN","GEL","XAF","FJD","VEF","BYR","HRK",
            "UZS","BGN","DZD","IRR","DOP","ISK","XAG","CRC","SYP","LYD",
            "JMD","MUR","GHS","AOA","UYU","AFN","LBP","XPF","TTD","TZS",
            "ALL","XCD","GTQ","NPR","BOB","BBD","LAK","BWP","HNL","PYG",
            "ETB","NAD","PGK","SDG","MOP","NIO","BMD","PAB","BAM","GYD",
            "YER","MGA","KYD","MZN","RSD","SCR","AMD","SBD","AZN","SLL",
            "TOP","BZD","MWK","GMD","BIF","SOS","HTG","GNF","MVR","CDF",
            "STD","TJS","MMK","LSL","LRD","KGS","GIP","XPT","MDL","CUP",
            "KHR","MKD"
            };
    /*
    private String mHalfBarFile;
    */
    //public static PurseDatabase database;

    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;


    private static NationDataLab mNationDataLab;
    ////////////////////////////////////////////////////////////////////////////
    private Context mAppContext;
    private boolean mNationDbisOpened=false;
    protected HashMap<String, NationData> mAllNationData;
    protected List<String> mUseNationData;
    private NationData mSelectCurrency=null;
    protected HashMap<String, String> mAnotherNationData;
    private NationData mCatNationData=null;

    public static NationDataLab get(Context c) {
        if (mNationDataLab == null) {
            mNationDataLab = new NationDataLab(c.getApplicationContext());
        }
        return mNationDataLab;
    }

    public NationDataLab(Context appContext) {
        mNationDbisOpened = false;
        if(appContext != null)
            mAppContext = appContext;
        initData();
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    public void initData() {
        mNationAdapter = new NationAdapter(mAppContext);
        mNationCalcAdapter = new NationAdapter(mAppContext);
        mSelectCurrency = new NationData();
        initNationData();
        initNationAdapter();

     //   loadData_DB();
    }
    public void changeCatData(NationData nd)
    {
        if(nd!=null) {
            nd.setGraphTextColor(Color.parseColor("#ff3e3a39"));
            nd.setGraphColor(Color.parseColor("#ff0092ff"));
            nd.setGraphFillColor(Color.parseColor("#800092ff"));
            nd.setPurseTextColor(Color.parseColor("#ff3fa9f5"));
            nd.setPurseTextColor2(Color.parseColor("#ff7ac6f4"));
            nd.setGraphBubbleFile("image/bubble/cat_bubble.png");
            nd.setPurseBarFile1("aud_bar.png");
            nd.setPurseBarFile2("aud_bar2.png");
            nd.setHalfBarFile("usd_barhalf.png");
        }
    }

    public void initNationAdapter()
    {
        mNationAdapter = initNationAdapter(mAppContext);
        mNationCalcAdapter = initNationCalcAdapter(mAppContext);
    }
    public void initNationData() {

        mAllNationData = new HashMap<String, NationData>();
        mUseNationData = new ArrayList<String>();
        int nNationCount = loadData_DB();

        if(nNationCount<=0)
        {
            setAllNationList();
            initAnotherData();
            saveNationData_DB();

        }
        setUseNationList(false);
    }
    public void setUseNationList(boolean bAll)
    {
        if(mUseNationData == null)
        {
            mUseNationData = new ArrayList<>();
        }
        else
            mUseNationData.clear();

        NationData nd = new NationData();
        for(String s : origin44)
        {
            nd = mAllNationData.get(s);
            nd.setUseData(true);
            mUseNationData.add(nd.getCurrencyCodeInEng());
        }
        if(bAll)
        {
            for(String s : another122)
            {
                nd = mAllNationData.get(s);
                nd.setUseData(true);
                mUseNationData.add(nd.getCurrencyCodeInEng());
            }
        }

    }
    public List<String> getUseNationData()
    {
        return mUseNationData;
    }
    public HashMap<String, NationData> getAllNationData()
    {
        return mAllNationData;
    }
    public List<String> getAllNationDataList()
    {
        ArrayList<String> rtnList = new ArrayList<>();
        rtnList.addAll(mAllNationData.keySet());

        return rtnList;
    }
    public void setAllNationData(HashMap<String, NationData> mapAll)
    {
        mAllNationData = mapAll;
    }

    public void setUseNationData(List<String> useList)
    {
        mUseNationData = useList;
    }
    public void openDatabase() {

        boolean isOpen = db_open();
        if (isOpen) {
            mNationDbisOpened = true;
            Log.d(mTag, "nation database is open.");
        } else {
            Log.d(mTag, "nation database is not open.");
        }

    }
    // 전체 국가 정보를 읽어오고(ReadOnly)
    // 사용 국가리스트를 불러와서 매칭하여 uselist생성
    public int loadData_DB() {
        try {
            openDatabase();

            if(mAllNationData==null)
                mAllNationData = new HashMap<String, NationData>();
            else
                mAllNationData.clear();
            excuteNationLoad("nation_all");

        } catch (Exception e) {

            Log.e(BasicInfo.TAG, "Error loading ExchangeData");
        }
        int nRtnVal = mAllNationData.size();
        return nRtnVal;
    }

    public void saveNationData_DB()
    {
        db_createNationTable("nation_all");
        excuteNationSave("nation_all");
    }
    private void excuteNationLoad(String sTable)
    {
//      /*
        if (mNationDbisOpened == false) {
            openDatabase();
        }

        String SQL = "select * "
                + "from " + sTable;
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


        String sEngName, sKorName, sCurrencyChar, NumericExpression,
                sBackgroundFile, sFlagFile, sGraphBubbleFile, sPurseBarFile1, sPurseBarFile2, sHalfBarFile;
        int nGraphTextColor = 0;
        int nGraphColor = 0;
        int nGraphFillColor = 0;
        int nPurseTextColor = 0;
        int nPurseTextColor2 = 0;
        NationData nd;

        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();

            sEngName = "";
            sEngName = c1.getString(0);
            if (sEngName.length()>0) {
                try {
                    nd = new NationData(sEngName);

                    nd.setCountryNameInKorean(c1.getString(1));
                    nd.setCurrencyChar(c1.getString(2));
                    nd.setNumericExpression(c1.getString(3));

                    nd.setGraphTextColor(c1.getInt(4));
                    nd.setGraphColor(c1.getInt(5));
                    nd.setGraphFillColor(c1.getInt(6));
                    nd.setPurseTextColor(c1.getInt(7));
                    nd.setPurseTextColor2(c1.getInt(8));

                    nd.setBackgroundFile(c1.getString(9));
                    nd.setFlagFile(c1.getString(10));
                    nd.setGraphBubbleFile(c1.getString(11));
                    nd.setPurseBarFile1(c1.getString(12));

                    nd.setPurseBarFile2(c1.getString(13));
                    nd.setHalfBarFile(c1.getString(14));

                    mAllNationData.put(nd.getCurrencyCodeInEng(),nd);
                }
                catch (Exception e)
                {

                }

                //////////////////////////////////////////////////
                /*
                sKorName = c1.getString(1);
                sCurrencyChar = c1.getString(2);
                NumericExpression = c1.getString(3);

                nGraphTextColor = c1.getInt(4);
                nGraphColor = c1.getInt(5);
                nGraphFillColor = c1.getInt(6);
                nPurseTextColor = c1.getInt(7);
                nPurseTextColor2 = c1.getInt(8);

                sBackgroundFile = c1.getString(9);
                sFlagFile = c1.getString(10);
                sGraphBubbleFile = c1.getString(11);
                sPurseBarFile1 = c1.getString(12);
                sPurseBarFile2 = c1.getString(13);
                sHalfBarFile = c1.getString(14);
                */


            }
        }
        c1.close();
     //   */
    }

    private void excuteNationSave(String sTable)
    {
        //insert into USD values ("2012-07-12",	1171, 	0.65);
        if (mNationDbisOpened == false) {
            openDatabase();
        }
        int nIndex = 0;

        try {
            this.db_delete_createNationTable(sTable);
            NationData e;
            for (String s : mAllNationData.keySet()) {
                e = mAllNationData.get(s);
                String SQL = "insert or replace into " + sTable +  " values (" +
                        "\"" + e.getCurrencyCodeInEng() + "\"," +
                        "\"" + e.getCountryNameInKorean() + "\"," +
                        "\"" + e.getCurrencyChar() + "\"," +
                        "\"" + e.getNumericExpression() + "\"," +

                        "\"" + e.getGraphTextColor() + "\"," +
                        "\"" + e.getGraphColor() + "\"," +
                        "\"" + e.getGraphFillColor() + "\"," +
                        "\"" + e.getPurseTextColor() + "\"," +
                        "\"" + e.getPurseTextColor2() + "\"," +

                        "\"" + e.getBackgroundFile() + "\"," +
                        "\"" + e.getFlagFile() + "\"," +
                        "\"" + e.getGraphBubbleFile() + "\"," +
                        "\"" + e.getPurseBarFile1() + "\"," +
                        "\"" + e.getPurseBarFile2() + "\"," +
                        "\"" + e.getHalfBarFile() +  "\"" +
                        ")";
                db_execSQL(SQL);
                nIndex++;
            }
        } catch (Exception e) {
            Log.e("AddDB error : ", e.toString());
        }
    }
    public NationData getNationCurrencyData(String sCode)
    {

        NationData Rtnval = new NationData();
        /*
        for(ExchangeData e : mLiveData)
        {
            if(e.getCurrencyCode().indexOf(sCode)>=0)
            {
                Rtnval = e;
            }
        }
        */
        return Rtnval;
    }

    private boolean db_open() {
        Log.i(mTag, "opening database [" + BasicInfo.NATION_DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(mAppContext);
        db = dbHelper.getWritableDatabase();

        return true;
    }
    public void close()
    {
        db_close();
    }
    /**
     */
    private void db_close() {
        Log.i(mTag, "closing database [" + BasicInfo.NATION_DATABASE_NAME + "].");
        db.close();
    }
    private void db_delete_createNationTable(String sTable)
    {

        Log.i(mTag, "creating database [" + BasicInfo.NATION_DATABASE_NAME + "].");

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
        db_createNationTable(sTable);

    }

    private void db_createNationTable(String sTable)
    {

        Log.i(mTag, "creating database [" + BasicInfo.NATION_DATABASE_NAME + "].");

        // TABLE_MEMO
        Log.i(mTag, "creating table [" + sTable + "].");
                // create table
        // create table
        String CREATE_SQL = "create table " + sTable + " ("
                + TABLE_NATION_CURRENCYCODE_INENG + " STRING NOT NULL PRIMARY KEY, "
                + TABLE_NATION_COUNTRYNAME_INKOR  + " STRING, "
                + TABLE_NATION_CURRENCY_CHAR  + " STRING, "
                + TABLE_NATION_NUMERICEXPRESSION  + " STRING, "
                + TABLE_NATION_GRAPH_TEXTCOLOR  + " INTEGER, "
                + TABLE_NATION_GRAPH_COLOR  + " INTEGER, "
                + TABLE_NATION_GRAPH_FILLCOLOR  + " INTEGER, "
                + TABLE_NATION_PURSE_TEXTCOLOR  + " INTEGER, "
                + TABLE_NATION_PURSE_TEXTCOLOR2  + " INTEGER, "

                + TABLE_NATION_BACKGROUND_FILE  + " STRING, "
                + TABLE_NATION_FLAG_FILE  + " STRING, "
                + TABLE_GRAPH_BUBBLE_FILE  + " STRING, "
                + TABLE_PURSE_BAR_FILE1  + " STRING, "
                + TABLE_PURSE_BAR_FILE2  + " STRING, "
                + TABLE_HALF_BAR_FILE  + " STRING"
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
            super(context, BasicInfo.NATION_DATABASE_NAME, null, DATABASE_VERSION);
            initialize(context);
        }

        public void initialize(Context ctx) {
        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onOpen(SQLiteDatabase db) {
            Log.i(mTag, "opened database [" + BasicInfo.NATION_DATABASE_NAME + "].");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.i(mTag, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
    public CurrencyAdapter getCurrencyAdapter(Context context) {
        if ((mUseNationData == null) | (mUseNationData.size() <= 0))
        {
            return null;
        }

        final CurrencyAdapter adapter = new CurrencyAdapter(context);

        NationData nd;
        for(String s  : mUseNationData)
        {
            nd = mAllNationData.get(s);
            adapter.addItem(new CurrencyItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        }
        return adapter;
    }
    public CurrencyAdapter getCurrencyCalcAdapter(Context context) {
        if ((mUseNationData == null) | (mUseNationData.size() <= 0))
        {
            return null;
        }
        final CurrencyAdapter adapter = new CurrencyAdapter(context);

        NationData nd;
        for (String s : mUseNationData)
        {
            nd = mAllNationData.get(s);
            adapter.addItem(new CurrencyItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        }
        nd = mAllNationData.get("KRW");
        adapter.addItem(new CurrencyItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        return adapter;
    }
    public NationAdapter getNationCalcAdapter() {
        return mNationCalcAdapter;
    }
    public NationAdapter initNationCalcAdapter(Context context) {
        if ((mAllNationData == null) | (mAllNationData.size() <= 0))
        {
            return null;
        }
        final NationAdapter adapter = new NationAdapter(context);

        NationData nd = new NationData();
        nd = mAllNationData.get("KRW");
        adapter.addItem(new NationItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        for(String s : origin44)
        {
            nd = mAllNationData.get(s);
            nd.setUseData(true);
            adapter.addItem(new NationItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        }
        for(String s : another122)
        {
            nd = mAllNationData.get(s);
            nd.setUseData(true);
            adapter.addItem(new NationItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        }


        return adapter;
    }
    public NationAdapter getNationAdapter() {
        return mNationAdapter;
    }
    public NationAdapter initNationAdapter(Context context) {
        if ((mUseNationData == null) | (mUseNationData.size() <= 0))
        {
            return null;
        }
        final NationAdapter adapter = new NationAdapter(context);

        NationData nd;
        for (String s : mUseNationData)
        {
            nd = mAllNationData.get(s);
            adapter.addItem(new NationItem(nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        }
        return adapter;
    }
    public NationAdapterExpand getAllNationAdapter(Context context) {
        if ((mUseNationData == null) | (mUseNationData.size() <= 0))
        {
            return null;
        }
        final NationAdapterExpand adapter = new NationAdapterExpand(context);

        NationData nd;
        for (String s :mAllNationData.keySet() )
        {
            nd = mAllNationData.get(s);

            adapter.addItem(new NationItem(nd.isUseData(),nd.getBitmapFlagFile(), nd.getCurrencyCodeInEng(), nd.getCountryNameInKorean()));
        }
        return adapter;
    }

    public void  setAllNationList() {
        if (mAllNationData == null) {
            mAllNationData = new HashMap<String, NationData>();
        } else
            mAllNationData.clear();

        // 210개국
        NationData nd = null;
        for (int i = 0; i < 44; i++) {
            nd = new NationData(origin44[i]);
            nd.setCountryNameInKorean("국가");
            nd.setCurrencyChar("$");
            nd.setNumericExpression("a*0.1");

            nd.setGraphTextColor(Color.parseColor("#ff0038a8"));
            nd.setGraphColor(Color.parseColor("#ff3fa9f5"));
            nd.setGraphFillColor(Color.parseColor("#803fa9f5"));
            nd.setPurseTextColor(Color.parseColor("#ff3fa9f5"));
            nd.setPurseTextColor2(Color.parseColor("#ff7ac6f4"));

            nd.setBackgroundFile("inter_flag.png");
            nd.setFlagFile("international.png");
            nd.setGraphBubbleFile("usd_bubble.png");
            nd.setPurseBarFile1("aud_bar.png");
            nd.setPurseBarFile2("aud_bar2.png");
            nd.setHalfBarFile("usd_barhalf.png");
            mAllNationData.put(nd.getCurrencyCodeInEng(), nd);
        }


        nd = new NationData("KRW");
        nd.setCountryNameInKorean("대한민국");
        nd.setCurrencyChar("\\");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ff0038a8"));
        nd.setGraphColor(Color.parseColor("#ff3fa9f5"));
        nd.setGraphFillColor(Color.parseColor("#803fa9f5"));
        nd.setPurseTextColor(Color.parseColor("#ff3fa9f5"));
        nd.setPurseTextColor2(Color.parseColor("#ff7ac6f4"));
        nd.setBackgroundFile("aus_flag.png");
        nd.setFlagFile("kor.png");
        nd.setGraphBubbleFile("usd_bubble.png");
        nd.setPurseBarFile1("aud_bar.png");
        nd.setPurseBarFile2("aud_bar2.png");
        nd.setHalfBarFile("usd_barhalf.png");
        mAllNationData.put("KRW", nd);

        setRedNation();
        setBlue_LightNation();
        setOrangeNation();
        setYellowNation();
        setGreenNation();
        setEtcNation();

    }
    public void setRedNation() {
        NationData ndVal = new NationData();
        ndVal.setCurrencyCodeInEng("USD");
        ndVal.setCountryNameInKorean("미국");
        ndVal.setCurrencyChar("$");
        ndVal.setNumericExpression("a*0.1");
        ndVal.setGraphTextColor(Color.parseColor("#ffc90006"));
        ndVal.setGraphColor(Color.parseColor("#ffc90006"));
        ndVal.setGraphFillColor(Color.parseColor("#80ff0000"));
        ndVal.setPurseTextColor(Color.parseColor("#ffff8ba2"));
        ndVal.setPurseTextColor2(Color.parseColor("#ffffaec1"));
        ndVal.setBackgroundFile("usd_flag.png");
        ndVal.setFlagFile("usd.png");
        ndVal.setGraphBubbleFile("usd_bubble.png");
        ndVal.setPurseBarFile1("red_bar.png");
        ndVal.setPurseBarFile2("red_bar2.png");
        ndVal.setHalfBarFile("red_barhalf.png");


        NationData nd = null;
        nd = mAllNationData.get("USD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("USD");
        nd.setCountryNameInKorean("미국");
        nd.setCurrencyChar("$");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("usd_flag.png");
        nd.setFlagFile("usd.png");
        nd.setGraphBubbleFile("usd_bubble.png");


        nd = mAllNationData.get("CHF");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("CHF");
        nd.setCountryNameInKorean("스위스");
        nd.setCurrencyChar("CHF");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("chf_flag.png");
        nd.setFlagFile("chf.png");

        nd = mAllNationData.get("CAD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("CAD");
        nd.setCountryNameInKorean("캐나다");
        nd.setCurrencyChar("C$");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("cad_flag.png");
        nd.setFlagFile("cad.png");

        nd = mAllNationData.get("DKK");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("DKK");
        nd.setCountryNameInKorean("덴마크");
        nd.setCurrencyChar("kr");
        nd.setBackgroundFile("dkk_flag.png");
        nd.setFlagFile("dkk.png");

        nd = mAllNationData.get("BHD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("BHD");
        nd.setCountryNameInKorean("바레인");
//        nd.setCurrencyChar("C$");
//        nd.setNumericExpression("a*0.1");
//        nd.setGraphBubbleFile("usd_bubble.png");
        nd.setBackgroundFile("bhd_flag.png");
        nd.setFlagFile("bhd.png");

        nd = mAllNationData.get("AED");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("AED");
        nd.setCountryNameInKorean("아랍에미리트");
//        nd.setCurrencyChar("C$");
//        nd.setNumericExpression("a*0.1");
//        nd.setGraphBubbleFile("usd_bubble.png");
        nd.setBackgroundFile("aed_flag.png");
        nd.setFlagFile("aed.png");

        nd = mAllNationData.get("THB");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("THB");
        nd.setCountryNameInKorean("태국");
        nd.setCurrencyChar("฿");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("thb_flag.png");
        nd.setFlagFile("thb.png");

        nd = mAllNationData.get("IDR");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("IDR");
        nd.setCountryNameInKorean("인도네시아");
        nd.setCurrencyChar("Rp");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("idr_flag.png");
        nd.setFlagFile("idr.png");

        nd = mAllNationData.get("PLN");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("PLN");
        nd.setCountryNameInKorean("폴란드");
        nd.setCurrencyChar("zł");
        nd.setBackgroundFile("pln_flag.png");
        nd.setFlagFile("pln.png");

        nd = mAllNationData.get("TRY");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("TRY");
        nd.setCountryNameInKorean("터키");
        nd.setCurrencyChar("TL");
        nd.setFlagFile("n_try.png");
        nd.setBackgroundFile("try_flag.png");

        nd = mAllNationData.get("CZK");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("CZK");
        nd.setCountryNameInKorean("체코");
        nd.setCurrencyChar("Kč");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("czk_flag.png");
        nd.setFlagFile("czk.png");

        nd = mAllNationData.get("EGP");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("EGP");
        nd.setCountryNameInKorean("이집트");
        nd.setCurrencyChar("£");
        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("egp_flag.png");
        nd.setFlagFile("egp.png");

        nd = mAllNationData.get("JOD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("JOD");
        nd.setCountryNameInKorean("요르단");
        //        nd.setCurrencyChar("₪");
//        nd.setNumericExpression("a*0.1");
        nd.setBackgroundFile("jod_flag.png");
        nd.setFlagFile("jod.png");

        nd = mAllNationData.get("OMR");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("OMR");
        nd.setCountryNameInKorean("오만");
        nd.setCurrencyChar("﷼");
        nd.setBackgroundFile("omr_flag.png");
        nd.setFlagFile("omr.png");

        nd = mAllNationData.get("CLP");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("CLP");
        nd.setCountryNameInKorean("칠레");
        nd.setCurrencyChar("$");
        nd.setBackgroundFile("clp_flag.png");
        nd.setFlagFile("clp.png");

        nd = mAllNationData.get("NOK");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("NOK");
        nd.setCountryNameInKorean("노르웨이");
        nd.setCurrencyChar("kr");
        nd.setBackgroundFile("nok_flag.png");
        nd.setFlagFile("nok.png");

        nd = mAllNationData.get("KWD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("KWD");
        nd.setCountryNameInKorean("쿠웨이트");
        nd.setFlagFile("kwd.png");
        nd.setBackgroundFile("kwd_flag.png");
    }
    public void setBlue_LightNation() {
        NationData ndVal = new NationData();

        ndVal.setCurrencyCodeInEng("PHP");
        ndVal.setCountryNameInKorean("필리핀");
        ndVal.setCurrencyChar("$");
        ndVal.setNumericExpression("a*0.1");
        ndVal.setGraphTextColor(Color.parseColor("#ff0038a8"));
        ndVal.setGraphColor(Color.parseColor("#ff3fa9f5"));
        ndVal.setGraphFillColor(Color.parseColor("#803fa9f5"));
        ndVal.setPurseTextColor(Color.parseColor("#ff3fa9f5"));
        ndVal.setPurseTextColor2(Color.parseColor("#ff7ac6f4"));
        ndVal.setBackgroundFile("usd_flag.png");
        ndVal.setFlagFile("usd.png");
        ndVal.setGraphBubbleFile("php_bubble.png");
        ndVal.setPurseBarFile1("blue_bar.png");
        ndVal.setPurseBarFile2("blue_bar2.png");
        ndVal.setHalfBarFile("blue_barhalf.png");

        NationData nd = null;
        nd = mAllNationData.get("PHP");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("PHP");
        nd.setCountryNameInKorean("필리핀");
        nd.setCurrencyChar("₱");
        nd.setBackgroundFile("php_flag.png");
        nd.setFlagFile("php.png");

        nd = mAllNationData.get("AUD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("AUD");
        nd.setCountryNameInKorean("호주");
        nd.setCurrencyChar("AUD$");
        nd.setBackgroundFile("aus_flag.png");
        nd.setFlagFile("aud.png");

        nd = mAllNationData.get("NZD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("NZD");
        nd.setCountryNameInKorean("뉴질랜드");
        nd.setCurrencyChar("$");
        nd.setFlagFile("nzd.png");
        nd.setBackgroundFile("nzd_flag.png");

        nd = mAllNationData.get("KZT");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("KZT");
        nd.setCountryNameInKorean("카자흐스탄");
        nd.setCurrencyChar("лв");
        nd.setBackgroundFile("kzt_flag.png");
        nd.setFlagFile("kzt.png");

        nd = mAllNationData.get("ILS");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("ILS");
        nd.setCountryNameInKorean("이스라엘");
        nd.setCurrencyChar("₪");
        nd.setBackgroundFile("ils_flag.png");
        nd.setFlagFile("ils.png");

    }
    public void setOrangeNation() {
        NationData ndVal = new NationData();

        ndVal.setCurrencyCodeInEng("SGD");
        ndVal.setCountryNameInKorean("싱가포르");
        ndVal.setCurrencyChar("$");
        ndVal.setNumericExpression("a*0.1");
        ndVal.setGraphTextColor(Color.parseColor("#ffef3230"));
        ndVal.setGraphColor(Color.parseColor("#ffff7d4d"));
        ndVal.setGraphFillColor(Color.parseColor("#80ff7d4d"));
        ndVal.setPurseTextColor(Color.parseColor("#ffff7d4d"));
        ndVal.setPurseTextColor2(Color.parseColor("#ffffba94"));
        ndVal.setBackgroundFile("usd_flag.png");
        ndVal.setFlagFile("usd.png");
        ndVal.setGraphBubbleFile("sgd_bubble.png");
        ndVal.setPurseBarFile1("orange_bar.png");
        ndVal.setPurseBarFile2("orange_bar2.png");
        ndVal.setHalfBarFile("orange_barhalf.png");

        NationData nd = null;
        nd = mAllNationData.get("SGD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("SGD");
        nd.setCountryNameInKorean("싱가포르");
        nd.setCurrencyChar("S$");
        nd.setBackgroundFile("sgd_flag.png");
        nd.setFlagFile("sgd.png");

        nd = mAllNationData.get("HKD");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("HKD");
        nd.setCountryNameInKorean("홍콩");
        nd.setCurrencyChar("HK$");
        nd.setBackgroundFile("hkd_flag.png");
        nd.setFlagFile("hkd.png");

        nd = mAllNationData.get("VND");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("VND");
        nd.setCountryNameInKorean("베트남");
        nd.setCurrencyChar("₫");
        nd.setBackgroundFile("vnd_flag.png");
        nd.setFlagFile("vnd.png");
    }
    public void setYellowNation() {
        NationData ndVal = new NationData();

        ndVal.setCurrencyCodeInEng("MYR");
        ndVal.setCountryNameInKorean("말레이시아");
        ndVal.setCurrencyChar("$");
        ndVal.setNumericExpression("a*0.1");
        ndVal.setGraphTextColor(Color.parseColor("#ff010066"));
        ndVal.setGraphColor(Color.parseColor("#fffbcc01"));
        ndVal.setGraphFillColor(Color.parseColor("#80fbcc01"));
        ndVal.setPurseTextColor(Color.parseColor("#ffffcc01"));
        ndVal.setPurseTextColor2(Color.parseColor("#ffffdf85"));
        ndVal.setBackgroundFile("myr_flag.png");
        ndVal.setFlagFile("myr.png");
        ndVal.setGraphBubbleFile("myr_bubble.png");
        ndVal.setPurseBarFile1("myr_bar.png");
        ndVal.setPurseBarFile2("myr_bar2.png");
        ndVal.setHalfBarFile("usd_barhalf.png");

        NationData nd = null;
        nd = mAllNationData.get("MYR");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("MYR");
        nd.setCountryNameInKorean("말레이시아");
        nd.setCurrencyChar("RM");
        nd.setBackgroundFile("myr_flag.png");
        nd.setFlagFile("myr.png");

        nd = mAllNationData.get("SEK");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("SEK");
        nd.setCountryNameInKorean("스웨덴");
        nd.setCurrencyChar("kr");
        nd.setFlagFile("sek.png");
        nd.setBackgroundFile("sek_flag.png");

        nd = mAllNationData.get("BND");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("BND");
        nd.setCountryNameInKorean("브루나이");
        nd.setCurrencyChar("$");
        nd.setBackgroundFile("bnd_flag.png");
        nd.setFlagFile("bnd.png");
    }
    public void setGreenNation() {
        NationData ndVal = new NationData();

        ndVal.setCurrencyCodeInEng("SAR");
        ndVal.setCountryNameInKorean("사우디");
        ndVal.setCurrencyChar("$");
        ndVal.setNumericExpression("a*0.1");
        ndVal.setGraphTextColor(Color.parseColor("#ff59d88d"));
        ndVal.setGraphColor(Color.parseColor("#ff59d88d"));
        ndVal.setGraphFillColor(Color.parseColor("#ff59d88d"));
        ndVal.setPurseTextColor(Color.parseColor("#ff59d88d"));
        ndVal.setPurseTextColor2(Color.parseColor("#ffaae8c3"));
        ndVal.setBackgroundFile("sar_flag.png");
        ndVal.setFlagFile("sar.png");
        ndVal.setGraphBubbleFile("green_bubble.png");
        ndVal.setPurseBarFile1("green_bar.png");
        ndVal.setPurseBarFile2("green_bar2.png");
        ndVal.setHalfBarFile("green_barhalf.png");

        NationData nd = null;
        nd = mAllNationData.get("SAR");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("SAR");
        nd.setCountryNameInKorean("사우디");
        nd.setCurrencyChar("﷼");
        nd.setBackgroundFile("sar_flag.png");
        nd.setFlagFile("sar.png");

        nd = mAllNationData.get("PKR");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("PKR");
        nd.setCountryNameInKorean("파키스탄");
        nd.setCurrencyChar("₨");
        nd.setBackgroundFile("pkr_flag.png");
        nd.setFlagFile("pkr.png");

        nd = mAllNationData.get("BDT");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("BDT");
        nd.setCountryNameInKorean("방글라데시");
        nd.setBackgroundFile("bdt_flag.png");
        nd.setFlagFile("bdt.png");

        nd = mAllNationData.get("MXN");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("MXN");
        nd.setCountryNameInKorean("맥시코");
        nd.setFlagFile("mxn.png");
        nd.setBackgroundFile("mxn_flag.png");

        nd = mAllNationData.get("BRL");
        nd.setData(ndVal);
        nd.setCurrencyCodeInEng("BRL");
        nd.setCountryNameInKorean("브라질");
        nd.setCurrencyChar("R$");
        nd.setBackgroundFile("brl_flag.png");
        nd.setFlagFile("blr.png");
    }

    public void setEtcNation() {
        NationData nd = null;

        nd = mAllNationData.get("EUR");
        nd.setCountryNameInKorean("유로");
        nd.setCurrencyChar("€");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ff29429c"));
        nd.setGraphColor(Color.parseColor("#ff299ec9"));
        nd.setGraphFillColor(Color.parseColor("#80299ec9"));
        nd.setPurseTextColor(Color.parseColor("#ff299ec9"));
        nd.setPurseTextColor2(Color.parseColor("#ff69b1c4"));
        nd.setBackgroundFile("eur_flag.png");
        nd.setFlagFile("eur.png");
        nd.setGraphBubbleFile("eur_bubble.png");
        nd.setPurseBarFile1("eur_bar.png");
        nd.setPurseBarFile2("eur_bar2.png");
        nd.setHalfBarFile("usd_barhalf.png");

        nd = mAllNationData.get("CNY");
        nd.setCountryNameInKorean("중국");
        nd.setCurrencyChar("元");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ffe61714"));
        nd.setGraphColor(Color.parseColor("#ffff7449"));
        nd.setGraphFillColor(Color.parseColor("#80ff0000"));
        nd.setPurseTextColor(Color.parseColor("#fffe9a4c"));
        nd.setPurseTextColor2(Color.parseColor("#fffcb86d"));
        nd.setBackgroundFile("cny_flag.png");
        nd.setFlagFile("cny.png");
        nd.setGraphBubbleFile("cny_bubble.png");
        nd.setPurseBarFile1("cny_bar.png");
        nd.setPurseBarFile2("cny_bar2.png");
        nd.setHalfBarFile("usd_barhalf.png");

        nd = mAllNationData.get("JPY");
        nd.setCountryNameInKorean("일본");
        nd.setCurrencyChar("￥");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ffc51a28"));
        nd.setGraphColor(Color.parseColor("#ffff686e"));
        nd.setGraphFillColor(Color.parseColor("#80ff686e"));
        nd.setPurseTextColor(Color.parseColor("#ffff686e"));
        nd.setPurseTextColor2(Color.parseColor("#fffdafa6"));
        nd.setBackgroundFile("jpy_flag.png");
        nd.setFlagFile("jpy.png");
        nd.setGraphBubbleFile("jpy_bubble.png");
        nd.setPurseBarFile1("jpy_bar.png");
        nd.setPurseBarFile2("jpy_bar2.png");
        nd.setHalfBarFile("usd_barhalf.png");

        NationData jpy = new NationData();
        jpy.setData(nd);
        nd = mAllNationData.get("QAR");
        nd.setData(jpy);
        nd.setCurrencyCodeInEng("QAR");
        nd.setCountryNameInKorean("카타르");
        nd.setCurrencyChar("﷼");
        nd.setBackgroundFile("qar_flag.png");
        nd.setFlagFile("qar.png");

        nd = mAllNationData.get("GBP");
        nd.setCountryNameInKorean("영국");
        nd.setCurrencyChar("£");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ff25258b"));
        nd.setGraphColor(Color.parseColor("#ff1f6ba9"));
        nd.setGraphFillColor(Color.parseColor("#801f6ba9"));
        nd.setPurseTextColor(Color.parseColor("#ff1f6ba9"));
        nd.setPurseTextColor2(Color.parseColor("#ff5694bf"));
        nd.setBackgroundFile("gbp_flag.png");
        nd.setFlagFile("gbp.png");
        nd.setGraphBubbleFile("gbp_bubble.png");
        nd.setPurseBarFile1("gbp_bar.png");
        nd.setPurseBarFile2("gbp_bar2.png");
        nd.setHalfBarFile("usd_barhalf.png");

        nd = mAllNationData.get("TWD");
        nd.setCountryNameInKorean("대만");
        nd.setCurrencyChar("NT$");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ff010066"));
        nd.setGraphColor(Color.parseColor("#fffbcc01"));
        nd.setGraphFillColor(Color.parseColor("#80fbcc01"));
        nd.setPurseTextColor(Color.parseColor("#ffffcc01"));
        nd.setPurseTextColor2(Color.parseColor("#ffffdf85"));
        nd.setBackgroundFile("twd_flag.png");
        nd.setFlagFile("twd.png");
        nd.setGraphBubbleFile("myr_bubble.png");
        nd.setPurseBarFile1("myr_bar.png");
        nd.setPurseBarFile2("myr_bar2.png");
        nd.setHalfBarFile("usd_barhalf.png");



        nd = mAllNationData.get("ZAR");
        nd.setCountryNameInKorean("남아공");
        nd.setFlagFile("zar.png");
        nd.setCurrencyChar("R");
        //        nd.setNumericExpression("a*0.1");
//        nd.setGraphTextColor(Color.parseColor("#ffff9933"));
//        nd.setGraphColor(Color.parseColor("#ffff9933"));
//        nd.setGraphFillColor(Color.parseColor("#ffff9933"));
//        nd.setPurseTextColor(Color.parseColor("#ffff9933"));
//        nd.setPurseTextColor2(Color.parseColor("#ffffc392"));
        nd.setBackgroundFile("zar_flag.png");
        nd.setGraphBubbleFile("php_bubble.png");
//        nd.setPurseBarFile1("inr_bar.png");
//        nd.setPurseBarFile2("inr_bar2.png");
//        nd.setHalfBarFile("usd_barhalf.png");


        nd = mAllNationData.get("RUB");
        nd.setCountryNameInKorean("러시아");
        nd.setCurrencyChar("руб");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ffff9933"));
        nd.setGraphColor(Color.parseColor("#ffff9933"));
        nd.setGraphFillColor(Color.parseColor("#ffff9933"));
        nd.setPurseTextColor(Color.parseColor("#ffff9933"));
        nd.setPurseTextColor2(Color.parseColor("#ffffc392"));
        nd.setBackgroundFile("rub_flag.png");
        nd.setFlagFile("rub.png");
        nd.setGraphBubbleFile("myr_bubble.png");
        nd.setPurseBarFile1("inr_bar.png");
        nd.setPurseBarFile2("inr_bar2.png");
        nd.setHalfBarFile("inr_barhalf.png");

        nd = mAllNationData.get("HUF");
        nd.setCountryNameInKorean("헝가리");
        nd.setCurrencyChar("Ft");
//        nd.setNumericExpression("a*0.1");
//        nd.setGraphTextColor(Color.parseColor("#ffff9933"));
//        nd.setGraphColor(Color.parseColor("#ffff9933"));
//        nd.setGraphFillColor(Color.parseColor("#ffff9933"));
//        nd.setPurseTextColor(Color.parseColor("#ffff9933"));
//        nd.setPurseTextColor2(Color.parseColor("#ffffc392"));
        nd.setBackgroundFile("huf_flag.png");
        nd.setFlagFile("huf.png");
        nd.setGraphBubbleFile("php_bubble.png");
//        nd.setPurseBarFile1("inr_bar.png");
//        nd.setPurseBarFile2("inr_bar2.png");
//        nd.setHalfBarFile("usd_barhalf.png");



        nd = mAllNationData.get("MNT");
        nd.setCountryNameInKorean("몽골");
        nd.setCurrencyChar("₮");
//        nd.setNumericExpression("a*0.1");
//        nd.setGraphTextColor(Color.parseColor("#ffff9933"));
//        nd.setGraphColor(Color.parseColor("#ffff9933"));
//        nd.setGraphFillColor(Color.parseColor("#ffff9933"));
//        nd.setPurseTextColor(Color.parseColor("#ffff9933"));
//        nd.setPurseTextColor2(Color.parseColor("#ffffc392"));
        nd.setBackgroundFile("mnt_flag.png");
        nd.setFlagFile("mnt.png");
        nd.setGraphBubbleFile("php_bubble.png");
//        nd.setPurseBarFile1("inr_bar.png");
//        nd.setPurseBarFile2("inr_bar2.png");
//        nd.setHalfBarFile("usd_barhalf.png");

        nd = mAllNationData.get("INR");
        nd.setCountryNameInKorean("인도");
        nd.setCurrencyChar("Rs.");
        nd.setNumericExpression("a*0.1");
        nd.setGraphTextColor(Color.parseColor("#ffff9933"));
        nd.setGraphColor(Color.parseColor("#ffff9933"));
        nd.setGraphFillColor(Color.parseColor("#ffff9933"));
        nd.setPurseTextColor(Color.parseColor("#ffff9933"));
        nd.setPurseTextColor2(Color.parseColor("#ffffc392"));
        nd.setBackgroundFile("inr_flag.png");
        nd.setFlagFile("inr.png");
        nd.setGraphBubbleFile("myr_bubble.png");
        nd.setPurseBarFile1("inr_bar.png");
        nd.setPurseBarFile2("inr_bar2.png");
        nd.setHalfBarFile("inr_barhalf.png");
//*/
//        Collections.sort(mNationData, new NationIndexSortedCompareSmall());
    }
    public void initAnotherData()
    {
        NationData nd;
        for (int i = 0; i< 92 ; i++) {
            nd = new NationData(kor122[i]);
            nd.setCountryNameInKorean(kor122[i]);
            nd.setCurrencyChar("$");
            nd.setNumericExpression("a*0.1");

            nd.setGraphTextColor(Color.parseColor("#ff0038a8"));
            nd.setGraphColor(Color.parseColor("#ff3fa9f5"));
            nd.setGraphFillColor(Color.parseColor("#803fa9f5"));
            nd.setPurseTextColor(Color.parseColor("#ff3fa9f5"));
            nd.setPurseTextColor2(Color.parseColor("#ff7ac6f4"));

            nd.setBackgroundFile("inter_flag.png");
            nd.setFlagFile("international.png");
            nd.setGraphBubbleFile("php_bubble.png");
            nd.setPurseBarFile1("aud_bar.png");
            nd.setPurseBarFile2("aud_bar2.png");
            nd.setHalfBarFile("usd_barhalf.png");
            mAllNationData.put(nd.getCurrencyCodeInEng(), nd);
        }
        nd = mAllNationData.get("KES");
        nd.setCountryNameInKorean("케냐");
        nd.setCurrencyChar("Ksh");
        nd.setFlagFile("kes.gif");

        nd = mAllNationData.get("IQD");
        nd.setCountryNameInKorean("이라크");
        nd.setCurrencyChar("د.ع");
        nd.setFlagFile("iqd.gif");

        // 없음
        nd = mAllNationData.get("XAU");
        nd.setCountryNameInKorean("금화(온스)");
        nd.setCurrencyChar("oz");

        nd = mAllNationData.get("COP");
        nd.setCountryNameInKorean("콜롬비아");
        nd.setCurrencyChar("$");
        nd.setFlagFile("cop.gif");

        nd = mAllNationData.get("ARS");
        nd.setCountryNameInKorean("아르헨티나");
        nd.setCurrencyChar("$");
        nd.setFlagFile("ars.gif");

        nd = mAllNationData.get("MAD");
        nd.setCountryNameInKorean("모로코");
        nd.setCurrencyChar("د.م.");
        nd.setFlagFile("mad.gif");

        nd = mAllNationData.get("XOF");
        nd.setCountryNameInKorean("코트디부아르");//토고,베냉,부르키나파소,기니비사우,,말리,니제르,세네갈");
        nd.setCurrencyChar("Fr");
        nd.setFlagFile("xof.gif");

        nd = mAllNationData.get("LKR");
        nd.setCountryNameInKorean("스리랑카");
        nd.setCurrencyChar("₨");
        nd.setFlagFile("lkr.gif");

        nd = mAllNationData.get("UAH");
        nd.setCountryNameInKorean("우크라이나");
        nd.setCurrencyChar("₴");
        nd.setFlagFile("uah.gif");

        nd = mAllNationData.get("NGN");
        nd.setCountryNameInKorean("나이지리아");
        nd.setCurrencyChar("₦");
        nd.setFlagFile("ngn.gif");

        /////////////////////////////////////////////////////////////////////
        nd = mAllNationData.get("TND");
        nd.setCountryNameInKorean("튀니지");
        nd.setCurrencyChar("DT, د.ت");

        nd = mAllNationData.get("UGX");
        nd.setCountryNameInKorean("우간다");
        nd.setCurrencyChar("Ush");
        nd.setFlagFile("ugx.gif");

        nd = mAllNationData.get("RON");
        nd.setCountryNameInKorean("루마니아");
        nd.setCurrencyChar("lei");
        nd.setFlagFile("ron.gif");

        nd = mAllNationData.get("PEN");
        nd.setCountryNameInKorean("페루");
        nd.setCurrencyChar("S/.");
        nd.setFlagFile("pen.gif");

        nd = mAllNationData.get("GEL");
        nd.setCountryNameInKorean("조지아");
        nd.setCurrencyChar("ლ");
        nd.setFlagFile("gel.gif");

        nd = mAllNationData.get("XAF");
        nd.setCountryNameInKorean("카메룬");//, 중앙아프리카 공하국, 차드, 콩고 민주공하국, 적도 기니, 가붕");
        nd.setCurrencyChar("Fr");
        nd.setFlagFile("xaf.gif");

        nd = mAllNationData.get("FJD");
        nd.setCountryNameInKorean("피지");
        nd.setCurrencyChar("$");
        nd.setFlagFile("fjd.gif");

        nd = mAllNationData.get("VEF");
        nd.setCountryNameInKorean("베네수엘라");
        nd.setCurrencyChar("Bs");

        nd = mAllNationData.get("BYR");
        nd.setCountryNameInKorean("벨라루스");
        nd.setCurrencyChar("p.");
        nd.setFlagFile("byr.gif");

        nd = mAllNationData.get("HRK");
        nd.setCountryNameInKorean("크로아티아");
        nd.setCurrencyChar("kn");
        nd.setFlagFile("hrk.gif");

        nd = mAllNationData.get("UZS");
        nd.setCountryNameInKorean("우즈베키스탄");
        nd.setCurrencyChar("лв");
        nd.setFlagFile("uzs.gif");

        nd = mAllNationData.get("BGN");
        nd.setCountryNameInKorean("불가리아");
        nd.setCurrencyChar("лв");
        nd.setFlagFile("bgn.gif");

        nd = mAllNationData.get("DZD");
        nd.setCountryNameInKorean("알제리");
        nd.setCurrencyChar("دج");
        nd.setFlagFile("dzd.gif");

        nd = mAllNationData.get("IRR");
        nd.setCountryNameInKorean("이란");
        nd.setCurrencyChar("﷼");

        nd = mAllNationData.get("DOP");
        nd.setCountryNameInKorean("도미니카공하국");
        nd.setCurrencyChar("RD$");
        nd.setFlagFile("dop.gif");

        nd = mAllNationData.get("ISK");
        nd.setCountryNameInKorean("아이슬란드");
        nd.setCurrencyChar("kr");


        nd = mAllNationData.get("XAG");
        nd.setCountryNameInKorean("은화(온스)");
        nd.setCurrencyChar("oz");


        nd = mAllNationData.get("CRC");
        nd.setCountryNameInKorean("코스타리카");
        nd.setCurrencyChar("₡");
        nd.setFlagFile("crc.gif");


        nd = mAllNationData.get("SYP");
        nd.setCountryNameInKorean("시리아");
        nd.setCurrencyChar("£");
        nd.setFlagFile("syp.gif");

        nd = mAllNationData.get("LYD");
        nd.setCountryNameInKorean("리비아");
        nd.setCurrencyChar("ل.د");
        nd.setFlagFile("lyd.gif");

        nd = mAllNationData.get("JMD");
        nd.setCountryNameInKorean("자메이카");
        nd.setCurrencyChar("J$");
        nd.setFlagFile("jmd.gif");


        nd = mAllNationData.get("MUR");
        nd.setCountryNameInKorean("모리셔스");
        nd.setCurrencyChar("₨");


        nd = mAllNationData.get("GHS");
        nd.setCountryNameInKorean("가나");
        nd.setCurrencyChar("¢");
        nd.setFlagFile("ghs.gif");

        nd = mAllNationData.get("AOA");
        nd.setCountryNameInKorean("앙골라");
        nd.setCurrencyChar("Kz");
        nd.setFlagFile("aoa.gif");

        nd = mAllNationData.get("UYU");
        nd.setCountryNameInKorean("우루과이");
        nd.setCurrencyChar("$U");
        nd.setFlagFile("uyu.gif");

        nd = mAllNationData.get("AFN");
        nd.setCountryNameInKorean("아프가니스탄");
        nd.setCurrencyChar("؋");
        nd.setFlagFile("afn.gif");

        nd = mAllNationData.get("LBP");
        nd.setCountryNameInKorean("레바논");
        nd.setCurrencyChar("£");
        nd.setFlagFile("lbp.gif");

        nd = mAllNationData.get("XPF");
        nd.setCountryNameInKorean("누벨칼레도니");
        nd.setCurrencyChar("F");


        nd = mAllNationData.get("TTD");
        nd.setCountryNameInKorean("트리니다드 토바고");
        nd.setCurrencyChar("TT$");
        nd.setFlagFile("ttd.gif");

        nd = mAllNationData.get("TZS");
        nd.setCountryNameInKorean("탄자니아");
        nd.setCurrencyChar("Sh");

        nd = mAllNationData.get("ALL");
        nd.setCountryNameInKorean("알바니아");
        nd.setCurrencyChar("Lek");
        nd.setFlagFile("all.gif");

        nd = mAllNationData.get("XCD");
        nd.setCountryNameInKorean("앤티카바부다");
        nd.setCurrencyChar("$");
        nd.setFlagFile("xcd.gif");

        nd = mAllNationData.get("GTQ");
        nd.setCountryNameInKorean("과테말라");
        nd.setCurrencyChar("Q");
        nd.setFlagFile("gtq.gif");

        nd = mAllNationData.get("NPR");
        nd.setCountryNameInKorean("네팔");
        nd.setCurrencyChar("₨\n");
        nd.setFlagFile("npr.gif");

        nd = mAllNationData.get("BOB");
        nd.setCountryNameInKorean("볼리비아");
        nd.setCurrencyChar("$b");
        nd.setFlagFile("bob.gif");

        nd = mAllNationData.get("BBD");
        nd.setCountryNameInKorean("바베이도스");
        nd.setCurrencyChar("$");
        nd.setFlagFile("bbd.gif");

        nd = mAllNationData.get("LAK");
        nd.setCountryNameInKorean("라오스");
        nd.setCurrencyChar("₭");

        nd = mAllNationData.get("BWP");
        nd.setCountryNameInKorean("보츠와나");
        nd.setCurrencyChar("P");
        nd.setFlagFile("bwp.gif");

        nd = mAllNationData.get("HNL");
        nd.setCountryNameInKorean("온두라스");
        nd.setCurrencyChar("L");
        nd.setFlagFile("hnl.gif");

        nd = mAllNationData.get("PYG");
        nd.setCountryNameInKorean("파라과이");
        nd.setCurrencyChar("Gs");
        nd.setFlagFile("pyg.gif");


        nd = mAllNationData.get("ETB");
        nd.setCountryNameInKorean("에티오피아");
        nd.setCurrencyChar("Br");
        nd.setFlagFile("etb.gif");

        nd = mAllNationData.get("NAD");
        nd.setCountryNameInKorean("나미비아");
        nd.setCurrencyChar("$");
        nd.setFlagFile("nad.gif");

        nd = mAllNationData.get("PGK");
        nd.setCountryNameInKorean("파푸아 뉴기니");
        nd.setCurrencyChar("K");
        nd.setFlagFile("pgk.gif");

        nd = mAllNationData.get("SDG");
        nd.setCountryNameInKorean("수단");
        nd.setCurrencyChar("£");
        nd.setFlagFile("sdg.gif");

        nd = mAllNationData.get("MOP");
        nd.setCountryNameInKorean("마카오");
        nd.setCurrencyChar("MOP$");

        nd = mAllNationData.get("NIO");
        nd.setCountryNameInKorean("니카라과");
        nd.setCurrencyChar("C$");
        nd.setFlagFile("nio.gif");

        nd = mAllNationData.get("BMD");
        nd.setCountryNameInKorean("버뮤다");
        nd.setCurrencyChar("$");

        nd = mAllNationData.get("PAB");
        nd.setCountryNameInKorean("파나마");
        nd.setCurrencyChar("B/.");
        nd.setFlagFile("pab.gif");

        nd = mAllNationData.get("BAM");
        nd.setCountryNameInKorean("보스니아 헤르체고비나");
        nd.setCurrencyChar("KM");
        nd.setFlagFile("bam.gif");

        nd = mAllNationData.get("GYD");
        nd.setCountryNameInKorean("가이아나");
        nd.setCurrencyChar("$");
        nd.setFlagFile("gyd.gif");


        nd = mAllNationData.get("YER");
        nd.setCountryNameInKorean("예멘");
        nd.setCurrencyChar("﷼");
        nd.setFlagFile("yer.gif");


        nd = mAllNationData.get("MGA");
        nd.setCountryNameInKorean("마다가스카르");
        nd.setCurrencyChar("Ar");
        nd.setFlagFile("mga.gif");


        nd = mAllNationData.get("KYD");
        nd.setCountryNameInKorean("케이맨 제도");
        nd.setCurrencyChar("$");


        nd = mAllNationData.get("MZN");
        nd.setCountryNameInKorean("모잠비크");
        nd.setCurrencyChar("MT");
        nd.setFlagFile("mzn.gif");

        nd = mAllNationData.get("RSD");
        nd.setCountryNameInKorean("세르비아");
        nd.setCurrencyChar("Дин.");
        nd.setFlagFile("rsd.gif");

        nd = mAllNationData.get("SCR");
        nd.setCountryNameInKorean("세이셸");
        nd.setCurrencyChar("₨");
        nd.setFlagFile("scr.gif");

        // 없음
        nd = mAllNationData.get("AMD");
        nd.setCountryNameInKorean("아르메니아");
        nd.setCurrencyChar("AMD");
        nd.setFlagFile("amd.gif");


        nd = mAllNationData.get("SBD");
        nd.setCountryNameInKorean("솔로몬 제도");
        nd.setCurrencyChar("$");
        nd.setFlagFile("sbd.gif");


        nd = mAllNationData.get("AZN");
        nd.setCountryNameInKorean("아제르바이잔");
        nd.setCurrencyChar("ман");


        nd = mAllNationData.get("SLL");
        nd.setCountryNameInKorean("시에라리온");
        nd.setCurrencyChar("Le");
        nd.setFlagFile("sll.gif");


        nd = mAllNationData.get("TOP");
        nd.setCountryNameInKorean("통가");
        nd.setCurrencyChar("T$");
        nd.setFlagFile("top.gif");

        nd = mAllNationData.get("BZD");
        nd.setCountryNameInKorean("벨리즈");
        nd.setCurrencyChar("BZ$");
        nd.setFlagFile("bzd.gif");

        nd = mAllNationData.get("MWK");
        nd.setCountryNameInKorean("말라위");
        nd.setCurrencyChar("MK");
        nd.setFlagFile("mwk.gif");

        nd = mAllNationData.get("GMD");
        nd.setCountryNameInKorean("감비아");
        nd.setCurrencyChar("D");
        nd.setFlagFile("gmd.gif");

        nd = mAllNationData.get("BIF");
        nd.setCountryNameInKorean("부룬디");
        nd.setCurrencyChar("Fr");
        nd.setFlagFile("bif.gif");

        nd = mAllNationData.get("SOS");
        nd.setCountryNameInKorean("소말리아");
        nd.setCurrencyChar("S");
        nd.setFlagFile("sos.gif");


        nd = mAllNationData.get("HTG");
        nd.setCountryNameInKorean("아이티");
        nd.setCurrencyChar("G");
        nd.setFlagFile("htg.gif");

        nd = mAllNationData.get("GNF");
        nd.setCountryNameInKorean("기니");
        nd.setCurrencyChar("Fr");
        nd.setFlagFile("gnf.gif");

        nd = mAllNationData.get("MVR");
        nd.setCountryNameInKorean("몰디브");
        nd.setCurrencyChar("Rf, MRf, MVR, .ރ , /-");
        nd.setFlagFile("mvr.gif");

        nd = mAllNationData.get("CDF");
        nd.setCountryNameInKorean("콩고 공화국");
        nd.setCurrencyChar("FC, Fr");
        nd.setFlagFile("cdf.gif");



        nd = mAllNationData.get("STD");
        nd.setCountryNameInKorean("상투메 프린시페");
        nd.setCurrencyChar("Db");
        nd.setFlagFile("std.gif");

        nd = mAllNationData.get("TJS");
        nd.setCountryNameInKorean("타지키스탄");
        nd.setCurrencyChar("SM");
        nd.setFlagFile("tjs.gif");


//        mAnotherNationData.put("KPW", "");
//        nd = mAllNationData.get("KPW");
//        nd.setCountryNameInKorean("조선민주주의인민공하국");
//        nd.setCurrencyChar("₦");


        nd = mAllNationData.get("MMK");
        nd.setCountryNameInKorean("미얀마");
        nd.setCurrencyChar("K");
        nd.setFlagFile("mmk.gif");

        nd = mAllNationData.get("LSL");
        nd.setCountryNameInKorean("레소토");
        nd.setCurrencyChar("L, M");
        nd.setFlagFile("lsl.gif");

        nd = mAllNationData.get("LRD");
        nd.setCountryNameInKorean("라이베리아");
        nd.setCurrencyChar("$");
        nd.setFlagFile("lrd.gif");

        nd = mAllNationData.get("KGS");
        nd.setCountryNameInKorean("키르기스스탄");
        nd.setCurrencyChar("лв");
        nd.setFlagFile("kgs.gif");

        // 없음
        nd = mAllNationData.get("GIP");
        nd.setCountryNameInKorean("지브롤터");
        nd.setCurrencyChar("£");

        // 없음
        nd = mAllNationData.get("XPT");
        nd.setCountryNameInKorean("백금(온스)");
        nd.setCurrencyChar("oz");

        nd = mAllNationData.get("MDL");
        nd.setCountryNameInKorean("몰도바");
        nd.setCurrencyChar("L");
        nd.setFlagFile("mdl.gif");


        nd = mAllNationData.get("CUP");
        nd.setCountryNameInKorean("쿠바");
        nd.setCurrencyChar("₱");
        nd.setFlagFile("cup.gif");

        nd = mAllNationData.get("KHR");
        nd.setCountryNameInKorean("캄보디아");
        nd.setCurrencyChar("៛");
        nd.setFlagFile("khr.gif");

        nd = mAllNationData.get("MKD");
        nd.setCountryNameInKorean("마케도니아");
        nd.setCurrencyChar("ден");


    }



    public NationData getNationData(String sNation)
    {
        if((sNation==null) || (sNation.length()<=0))
            return null;
        NationData rtn = new NationData();
        rtn = mAllNationData.get(sNation);
        return rtn;
    }

    public int getGraphTextColor()
    {
        int nRtn = mSelectCurrency.getGraphTextColor();
        return nRtn;
    }
    public int getGraphColor()
    {
        int nRtn = mSelectCurrency.getGraphColor();
        return nRtn;
    }
    public int getGraphFillColor()
    {
        int nRtn = mSelectCurrency.getGraphFillColor();
        return nRtn;
    }
    public String getGraphBubbleId()
    {
        String sRtn = mSelectCurrency.getGraphBubbleFile();
        return sRtn;
    }

    public int getPurseTextColor()
    {
        int nRtn = mSelectCurrency.getPurseTextColor();
        return nRtn;
    }
    public int getPurseTextColor2()
    {
        int nRtn = mSelectCurrency.getPurseTextColor2();
        return nRtn;
    }
    public String getPurseBarId1()
    {
        String sRtn = mSelectCurrency.getPurseBarFile1();
        return sRtn;
    }
    public String getPurseBarId2()
    {
        String sRtn = mSelectCurrency.getPurseBarFile2();

        return sRtn;
    }
    public String getHalfBarId()
    {
        String sRtn = mSelectCurrency.getHalfBarFile();
        return sRtn;
    }
    public String getCurrencyChar()
    {
        String sRtn = mSelectCurrency.getCurrencyChar();
        return sRtn;
    }
    public String getCurrencyChar(String sCurrency)
    {
        String sRtn="";
        sRtn = mAllNationData.get(sCurrency).getCurrencyChar();
        return sRtn;
    }
    public String getBackgroundFile()
    {
        String sRtn = mSelectCurrency.getBackgroundFile();
        return sRtn;
    }
    public void chgCurrency(String sCurrency) {
        NationData nd = mAllNationData.get(sCurrency);
        GlobalVar globalVar = GlobalVar.get();
        if (nd !=null)
        {
            if(mSelectCurrency == null)
                mSelectCurrency = new NationData();
            changeCatData(nd);
            mSelectCurrency.setData(nd); // 복사
            globalVar.setSelectCurrency(nd);
        }
        else
        {
            nd =new NationData("USD");
                    //mAllNationData.get("USD");
            changeCatData(nd);
            if(mSelectCurrency == null)
                mSelectCurrency = new NationData();
            mSelectCurrency.setData(nd);
        }
    }
    public String getCurrencyCodeInEng() {
        if(mSelectCurrency==null)
            return "";
        String sRtn = mSelectCurrency.getCurrencyCodeInEng();
        return sRtn;
    }
    public String getCountryNameInKorean() {
        String sRtn = mSelectCurrency.getCountryNameInKorean();
        return sRtn;
    }
    public String getCountryNameInKorean(String sCurrency)
    {
        String sRtn="";
        sRtn = mAllNationData.get(sCurrency).getCountryNameInKorean();
        return sRtn;
    }
    public Bitmap getPurseBar1Bitmap()
    {
        Bitmap bmp  = mSelectCurrency.getBitmapPurseBarFile1();
        return bmp;
    }
    public Bitmap getPurseBar2Bitmap()
    {
        Bitmap bmp  = mSelectCurrency.getBitmapPurseBarFile2();
        return bmp;
    }
    public Bitmap getHalfBarBitmap()
    {
        Bitmap bmp  = mSelectCurrency.getBitmapHalfBarFile();
        return bmp;
    }
    public Bitmap getgGraphBubbleBitmap()
    {
        Bitmap bmp  = mSelectCurrency.getBitmapGraphBubbleFile();
        return bmp;

    }
}
