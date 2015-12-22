package com.reviewmerce.exchange.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;

/**
 * Created by onebuy on 2015-07-24.
 */
public class MainDatabase {
    public static final String TAG = "ExchangeDatabase";
    public static MainDatabase database;
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
    private MainDatabase(Context context) {
        this.context = context;
    }

    /**
     */
    public static MainDatabase getInstance(Context context) {
        if (database == null) {
            database = new MainDatabase(context);
        }

        return database;
    }

    /**
     */
    public boolean open() {
        println("opening database [" + BasicInfo.MAIN_DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    /**
     */
    public void close() {
        println("closing database [" + BasicInfo.MAIN_DATABASE_NAME + "].");
        db.close();

        database = null;
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

    public void createTable()
    {
        String sTable = "option";
        println("creating database [" + BasicInfo.MAIN_DATABASE_NAME + "].");

        // TABLE_MEMO
        println("creating table [" + sTable + "].");

        // create table
        String CREATE_SQL = "create table " + sTable + " ("
                + BasicInfo.TABLE_MAIN_SCREENPAGE  + " INTEGER, "
                + BasicInfo.TABLE_MAIN_NATIONCODE  + " STRING, "
                + BasicInfo.TABLE_MAIN_PERIOD  + " INTEGER, "
                + BasicInfo.TABLE_MAIN_EXCHANGETYPE  + " STRING, "
                + BasicInfo.TABLE_MAIN_NETWORKMODE  + " BOOLEAN"
                + " );";
        //   create table exchange_rate (DATE DATE NOT NULL PRIMARY KEY, VALUE INTEGER, DIGIT INTEGER);
        try {
            Log.e(TAG, "CREATE_SQL");
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
    }
    public void delete_createTable()
    {
        String sTable = "option";
        println("creating database [" + BasicInfo.MAIN_DATABASE_NAME + "].");

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
                + BasicInfo.TABLE_MAIN_SCREENPAGE  + " INTEGER, "
                + BasicInfo.TABLE_MAIN_NATIONCODE  + " STRING, "
                + BasicInfo.TABLE_MAIN_PERIOD  + " INTEGER, "
                + BasicInfo.TABLE_MAIN_EXCHANGETYPE  + " STRING, "
                + BasicInfo.TABLE_MAIN_NETWORKMODE  + " BOOLEAN"
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
     * Database Helper inner class
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final String PACKAGE_DIR = "/data/data/com.reviewmerce.exchange/databases";

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.MAIN_DATABASE_NAME, null, DATABASE_VERSION);
            initialize(context);
        }

        public void initialize(Context ctx) {

        }

        public void onCreate(SQLiteDatabase db) {
//            /*

//*/
        }

        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + BasicInfo.MAIN_DATABASE_NAME + "].");

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
