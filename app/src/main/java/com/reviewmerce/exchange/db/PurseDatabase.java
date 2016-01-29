package com.reviewmerce.exchange.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by onebuy on 2015-07-24.
 */
public class PurseDatabase {
    public static final String TAG = "ExchangeDatabase";
    public static PurseDatabase database;

    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    /**
     */
    private PurseDatabase(Context context) {
        this.context = context;
    }

    /**
     */
    public static PurseDatabase getInstance(Context context) {
        if (database == null) {
            database = new PurseDatabase(context);
        }

        return database;
    }

    /**
     */
    public boolean open() {
        println("opening database [" + BasicInfo.PURSE_DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    /**
     */
    public void close() {
        println("closing database [" + BasicInfo.PURSE_DATABASE_NAME + "].");
        db.close();

        database = null;
    }
    public void delete_createCurrencyTable(String sTable)
    {
        println("creating database [" + BasicInfo.DATABASE_NAME + "].");

        // TABLE_MEMO
        println("creating table [" + sTable + "].");

        // drop existing table
        String DROP_SQL = "drop table if exists " + sTable;
        //String DROP_SQL = "drop table if exists USD";
        try {
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }
//*/
        // create table
        String CREATE_SQL = "create table " + sTable + " ("
                + BasicInfo.TABLE_MONI_CURENCYCODE + " STRING NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_MONI_DATE + " STRING, "
                + BasicInfo.TABLE_MONI_BASICRATES + " REAL, "
                + BasicInfo.TABLE_MONI_TIME  + " STRING"
                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(TAG, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
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


    public void createPurseTable(String sTable)
    {
        println("creating database [" + BasicInfo.PURSE_DATABASE_NAME + "].");

        // TABLE_MEMO
        println("creating table [" + sTable + "].");

        // create table
        String CREATE_SQL = "create table " + sTable+"_header" + " ("
                //+ BasicInfo.TABLE_PURSE_INDEX + " INTEGER NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_PURSE_STARTDATE + " STRING, "
                + BasicInfo.TABLE_PURSE_ENDDATE + " STRING, "
                + BasicInfo.TABLE_PURSE_BUDGET  + " REAL, "
                + BasicInfo.TABLE_PURSE_CURRENCY + " STRING"
                + " );";
        // create table
        String CREATE_SQL2 = "create table " + sTable + " ("
                + BasicInfo.TABLE_PURSE_INDEX + " INTEGER NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_PURSE_ITEM  + " STRING, "
                + BasicInfo.TABLE_PURSE_ITEMDATE  + " STRING, "
                + BasicInfo.TABLE_PURSE_ITEMTIME  + " STRING, "
                + BasicInfo.TABLE_PURSE_VALUE  + " REAL, "
                + BasicInfo.TABLE_PURSE_ITEMCURRENCY  + " STRING"
                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(TAG, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
            db.execSQL(CREATE_SQL2);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
    }
    public void delete_createPurseTable(String sTable)
    {
        println("creating database [" + BasicInfo.PURSE_DATABASE_NAME + "].");

        // TABLE_MEMO
        println("creating table [" + sTable + "].");

        // drop existing table
        String DROP_SQL = "drop table if exists " + sTable+"_header";
        String DROP_SQL2 = "drop table if exists " + sTable;
        //String DROP_SQL = "drop table if exists USD";
        try {
            db.execSQL(DROP_SQL);
            db.execSQL(DROP_SQL2);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }
//*/
        String CREATE_SQL = "create table " + sTable+"_header" + " ("
                //+ BasicInfo.TABLE_PURSE_INDEX + " INTEGER NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_PURSE_STARTDATE + " STRING, "
                + BasicInfo.TABLE_PURSE_ENDDATE + " STRING, "
                + BasicInfo.TABLE_PURSE_BUDGET  + " REAL, "
                + BasicInfo.TABLE_PURSE_CURRENCY + " STRING"
                + " );";
        // create table
        String CREATE_SQL2 = "create table " + sTable + " ("
                + BasicInfo.TABLE_PURSE_INDEX + " INTEGER NOT NULL PRIMARY KEY, "
                + BasicInfo.TABLE_PURSE_ITEM  + " STRING, "
                + BasicInfo.TABLE_PURSE_ITEMDATE  + " STRING, "
                + BasicInfo.TABLE_PURSE_ITEMTIME  + " STRING, "
                + BasicInfo.TABLE_PURSE_VALUE  + " REAL, "
                + BasicInfo.TABLE_PURSE_ITEMCURRENCY  + " STRING"
                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(TAG, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
            db.execSQL(CREATE_SQL2);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
    }
    /**
     * Database Helper inner class
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final String PACKAGE_DIR = "/data/data/com.reviewmerce.exchange/databases";

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.PURSE_DATABASE_NAME, null, DATABASE_VERSION);
            initialize(context);
        }

        public void initialize(Context ctx) {

        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + BasicInfo.PURSE_DATABASE_NAME + "].");

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
