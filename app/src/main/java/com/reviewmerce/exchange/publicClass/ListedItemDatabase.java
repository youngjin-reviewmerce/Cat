package com.reviewmerce.exchange.publicClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.commonData.apiData.ImageAndLink;
import com.reviewmerce.exchange.commonData.apiData.ListedItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by onebuy on 2015-07-24.
 */

// key설정, ReplaceInsert 확인

public class ListedItemDatabase {
    public static final String TAG = "ExchangeDatabase";
    public static ListedItemDatabase mInst;
    private final Object syncObj1= new Object();
    private final Object syncObj2= new Object();
    private boolean mDbisOpened=false;
 //   static String TableName = "listeditem";
    private ArrayList<ListedItem> mList_ViewListedItem=null;
    /*
    public static String TABLE_DATE = "DATE";
    public static String TABLE_PRICE = "PRICE";
    public static String TABLE_DECIMALS = "PRICE_DECIMALS";
    public static String TABLE_TIME = "TIME";
    public static String TABLE_BANK = "BANK";
    public static String TABLE_CURRENCY = "CURRENCY_CODE";
    */
    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    /**
     */
    private ListedItemDatabase(Context context) {
        this.context = context;
        mDbisOpened = false;
        initData();
    }

    /**
     */
    public static ListedItemDatabase getInstance(Context context) {
        if (mInst == null) {
            mInst = new ListedItemDatabase(context);

        }

        return mInst;
    }
    public void initData()
    {
        if(mList_ViewListedItem == null)
            mList_ViewListedItem = new ArrayList<ListedItem>();
        else
            mList_ViewListedItem.clear();
    }
    /**
     */
    public boolean open() {
        println("opening database [" + BasicInfo.LISTEDITEM_DBNAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    /**
     */
    public void close() {
        println("closing database [" + BasicInfo.LISTEDITEM_DBNAME + "].");
        db.close();

        mInst = null;
    }

    /**
     * execute raw query using the input SQL
     * close the cursor after fetching any result
     *
     * @param SQL
     * @return
     */
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch (Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }
    public void openDatabase() {

        boolean isOpen = open();
        if (isOpen) {
            mDbisOpened = true;
            Log.d(BasicInfo.TAG, "exchange database is open.");
        } else {
            Log.d(BasicInfo.TAG, "exchange database is not open.");
        }
        createTable_ListedItem("listeditem");//createPurseTable("purse_table");
        createTable_ImageLinkItem("mainpage");
    }
    public void clearDB()
    {
        if (mDbisOpened == false) {
            openDatabase();
        }
        delete_createTable_ListedItem("listeditem");
        delete_createTable_ImageLinkItem("mainpage");

    }
    public void createTable_ListedItem(String sTable)
    {

        println("creating database [" + BasicInfo.LISTEDITEM_DBNAME + "].");
        // TABLE_MEMO
        println("creating table [" + sTable + "].");

        // create table

        String CREATE_SQL = "create table " + sTable + " ("
                + BasicInfo.TABLE_ITEMLISTED_RID  + " STRING NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_ITEMLISTED_ASIN  + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_IMGURL  + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_ITEMNAME  + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_TAG   + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_PRICE  + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_DESCRIPTION  + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_LOCALURL  + " STRING, "
                + BasicInfo.TABLE_ITEMLISTED_LINKURL  + " STRING"
                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(TAG, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
    }
    public void delete_createTable_ListedItem(String sTable)
    {

        // drop existing table
        String DROP_SQL = "drop table if exists " + sTable;
        //String DROP_SQL = "drop table if exists USD";
        try {
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }
        createTable_ListedItem(sTable);
//*/
    }

    public void excuteDBSave_ListedItem(ArrayList<ListedItem>itemList,String sTableName)
    {
        synchronized (syncObj1) {
            //insert into USD values ("2012-07-12",	1171, 	0.65);
            if (mDbisOpened == false) {
                openDatabase();
            }
            int nIndex = 0;

            try {
         //       db_delete_createCurrencyTable("live_table");
                for (ListedItem e : itemList) {
                    ContentValues cv = new ContentValues();
                    cv.put(BasicInfo.TABLE_ITEMLISTED_RID,e.getRID());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_ASIN,e.getASIN());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_IMGURL,e.getImgUrl());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_ITEMNAME,e.getItemName());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_TAG,e.getTag());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_PRICE,e.getPrice());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_DESCRIPTION,e.getDescription());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_LOCALURL,e.getLocalUrl());
                    cv.put(BasicInfo.TABLE_ITEMLISTED_LINKURL,e.getLinkUrl());

                    db.insertWithOnConflict(sTableName,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                    /*
                    db.insertWithOnConflict(TableName,"",)
                    db.insert(BasicInfo.TABLE_ITEMLISTED_ASIN, e.)
                    String SQL = "insert or replace into live_table" + " values (" +
                            "\"" + e.getCurrencyCode() + "\"," +
                            "\"" + e.getDate() + "\"," +
                            "\"" + e.getBasicRates() + "\"," +
                            "\"" + e.getTime() + "\"" +
                            ")";
                    db_execSQL(SQL);
                    */
                    nIndex++;
                }
            } catch (Exception e) {
                Log.e("AddDB error : ", e.toString());
            }
        }
    }

    public void excuteDBLoad_ListedItem(ArrayList<ListedItem>itemList,String sTableName)
    {
        if (mDbisOpened == false) {
            openDatabase();
        }

        String SQLBasic = "select * "
                + "from " + sTableName;

        String SQLAppend = " where ";
        ListedItem item;
            //       db_delete_createCurrencyTable("live_table");
        for (int i=0 ; i<itemList.size() ; i++) {
            item = itemList.get(i);
            SQLAppend = SQLAppend + BasicInfo.TABLE_ITEMLISTED_RID +" = '" + item.getRID() +"'";
            if(i != itemList.size()-1)
            {
                SQLAppend += " or ";
            }
        }
        Cursor c1;
        SQLBasic = SQLBasic + SQLAppend;
        try {

            c1 = rawQuery(SQLBasic);
            if(c1==null)
                return ;
        }
        catch(Exception e)
        {
            Log.e("ListedItem", e.toString());
            return ;
        }

        int recordCount = c1.getCount();
        synchronized (syncObj1) {
            String sRid,sLocalUrl;
            DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
            Date date;

            for (int i = 0; i < recordCount; i++) {
                c1.moveToNext();
                sRid = "";
                sRid = c1.getString(0);
                if (sRid.length() > 0) {
                    sLocalUrl = c1.getString(7);
                    ListedItem changeItem = getItem_by_id(itemList,sRid);
                    if(changeItem != null)
                        changeItem.setLocalUrl(sLocalUrl);
                }
            }
        }
        c1.close();

        ListedItem changeItem = getItem_by_id(itemList,"10000000");
        changeItem.setLocalUrl("reviewmerce0.jpg");
        changeItem = getItem_by_id(itemList,"10000001");
        changeItem.setLocalUrl("reviewmerce1.jpg");
        changeItem = getItem_by_id(itemList,"10000002");
        changeItem.setLocalUrl("reviewmerce2.jpg");
        changeItem = getItem_by_id(itemList,"10000003");
        changeItem.setLocalUrl("reviewmerce3.jpg");
        changeItem = getItem_by_id(itemList,"10000004");
        changeItem.setLocalUrl("reviewmerce4.jpg");
        changeItem = getItem_by_id(itemList,"10000005");
        changeItem.setLocalUrl("reviewmerce5.jpg");
        changeItem = getItem_by_id(itemList,"10000006");
        changeItem.setLocalUrl("reviewmerce6.jpg");
        changeItem = getItem_by_id(itemList,"10000007");
        changeItem.setLocalUrl("reviewmerce7.jpg");
        changeItem = getItem_by_id(itemList,"10000008");
        changeItem.setLocalUrl("seol3.jpg");
        changeItem = getItem_by_id(itemList,"10000009");
        changeItem.setLocalUrl("seol4.jpg");
    }
    public void createTable_ImageLinkItem(String sTable)
    {

        println("creating database [" + BasicInfo.LISTEDITEM_DBNAME + "].");
        // TABLE_MEMO
        println("creating table [" + sTable + "].");

        // create table
        String CREATE_SQL = "create table " + sTable + " ("
                + BasicInfo.TABLE_IMAGELINK_IALID  + " STRING NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_IMAGELINK_IALTYPE  + " STRING, "
                + BasicInfo.TABLE_IMAGELINK_PRIORITY  + " STRING, "
                + BasicInfo.TABLE_IMAGELINK_IMGURL  + " STRING, "
                + BasicInfo.TABLE_IMAGELINK_LINKURL  + " STRING, "
                + BasicInfo.TABLE_IMAGELINK_LINKTYPE  + " STRING, "
                + BasicInfo.TABLE_IMAGELINK_LINKNAME  + " STRING, "
                + BasicInfo.TABLE_IMAGELINK_LOCALURL  + " STRING"

                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(TAG, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
    }
    public void delete_createTable_ImageLinkItem(String sTable)
    {

        // drop existing table
        String DROP_SQL = "drop table if exists " + sTable;
        //String DROP_SQL = "drop table if exists USD";
        try {
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }
        createTable_ImageLinkItem(sTable);
//*/
    }

    public void excuteDBSave_ImageLinkItem(ArrayList<ImageAndLink>itemList,String sTableName)
    {
        synchronized (syncObj2) {
            //insert into USD values ("2012-07-12",	1171, 	0.65);
            if (mDbisOpened == false) {
                openDatabase();
            }
            int nIndex = 0;

            try {
                //       db_delete_createCurrencyTable("live_table");
                for (ImageAndLink item : itemList) {
                    ContentValues cv = new ContentValues();
                    cv.put(BasicInfo.TABLE_IMAGELINK_IALID,item.getIALId());
                    cv.put(BasicInfo.TABLE_IMAGELINK_IALTYPE,item.getIALType());
                    cv.put(BasicInfo.TABLE_IMAGELINK_PRIORITY,item.getPriority());
                    cv.put(BasicInfo.TABLE_IMAGELINK_IMGURL,item.getImgUrl());
                    cv.put(BasicInfo.TABLE_IMAGELINK_LINKURL,item.getLinkUrl());
                    cv.put(BasicInfo.TABLE_IMAGELINK_LINKTYPE,item.getLinkType());
                    cv.put(BasicInfo.TABLE_IMAGELINK_LINKNAME,item.getLinkName());
                    cv.put(BasicInfo.TABLE_IMAGELINK_LOCALURL,item.getLocalUrl());
                    db.insertWithOnConflict(sTableName,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                    nIndex++;
                }
            } catch (Exception e) {
                Log.e("AddDB error : ", e.toString());
            }
        }
    }

    public void excuteDBLoad_ImageLinkItem(ArrayList<ImageAndLink>itemList,String sTableName)
    {
        if (mDbisOpened == false) {
            openDatabase();
        }

        String SQLBasic = "select * "
                + "from " + sTableName;

        ListedItem item;
        //       db_delete_createCurrencyTable("live_table");

        Cursor c1;
        try {

            c1 = rawQuery(SQLBasic);
            if(c1==null)
                return ;
        }
        catch(Exception e)
        {
            Log.e("ImageLink", e.toString());
            return ;
        }


        int recordCount = c1.getCount();
        synchronized (syncObj2) {
            String sId,sType,sPriority,sImgUrl,sLinkUrl,sLocalUrl,sLinkType, sLinkName;

            for (int i = 0; i < recordCount; i++) {
                c1.moveToNext();

                sId = c1.getString(0);
                if (sId.length() > 0) {
                    sType = c1.getString(1);
                    sPriority = c1.getString(2);
                    sImgUrl = c1.getString(3);
                    sLinkUrl = c1.getString(4);
                    sLinkType = c1.getString(5);
                    sLinkName = c1.getString(6);
                    sLocalUrl = c1.getString(7);
                    ImageAndLink newItem = new ImageAndLink(Integer.valueOf(sId),sType, Integer.valueOf(sPriority),sImgUrl, sLinkUrl, sLinkType, sLinkName, sLocalUrl);
                    itemList.add(newItem);
                }
            }
        }
        c1.close();
    }
    public ListedItem getItem_by_id(ArrayList<ListedItem>itemList, String sId)
    {

        for(ListedItem item : itemList)
        {
            if(item.getRID().indexOf(sId)>=0)
            {
                return item;
            }
        }
        return null;
    }
    public void addViewListedData(ListedItem addItem)
    {
        mList_ViewListedItem.add(addItem);
    }
    public ArrayList<ListedItem> getViewListedData()
    {
        return mList_ViewListedItem;
    }
    /**
     * Database Helper inner class
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        private  final String PACKAGE_DIR = BasicInfo.InternalPath + "databases";

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.LISTEDITEM_DBNAME, null, DATABASE_VERSION);
            initialize(context);
        }

        public void initialize(Context ctx) {

        }

        public void onCreate(SQLiteDatabase db) {
//            /*

//*/
        }

        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + BasicInfo.LISTEDITEM_DBNAME + "].");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }
}
