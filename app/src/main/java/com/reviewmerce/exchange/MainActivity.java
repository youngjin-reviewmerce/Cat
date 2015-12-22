package com.reviewmerce.exchange;

import android.annotation.SuppressLint;
//import android.app.FragmentTransaction;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.reviewmerce.exchange.Fragment.BankFragment;
import com.reviewmerce.exchange.Fragment.CalcFragment;
import com.reviewmerce.exchange.Fragment.GraphFragment;
import com.reviewmerce.exchange.Fragment.LicenseFragment;
import com.reviewmerce.exchange.Fragment.MenuFragment;
import com.reviewmerce.exchange.Fragment.PurseBudgetFragment;
import com.reviewmerce.exchange.Fragment.PurseFragment;
import com.reviewmerce.exchange.Fragment.TutorialFragment;
import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.ListView.MainMenuAdapter;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.MenuData;
import com.reviewmerce.exchange.commonData.NumberDigitObject;
import com.reviewmerce.exchange.db.MainDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends FragmentActivity implements OnTouchListener, baseOnebuyFragment.FragmentChangeListener{
    GlobalVar mGlobalVar=null;
    private Tracker mTracker;
//    private ViewFlipper m_viewFlipper;
    private int m_nPreTouchPosX = 0;
//    private int m_nFragmentIndex = 0;
    private int m_nMaxFragementCount = 3;
    private static GraphFragment mFragmentGraph;
    private static BankFragment mFragmentBank;
    private static CalcFragment mFragmentCalc;
    private static PurseFragment mFragmentPurse;
    private static PurseBudgetFragment mFragmengBudgetPurse;
    private static TutorialFragment mFragmentTutorial;
    private static LicenseFragment mFragmentLicense;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int mDialogType;
    private BackPressCloseHandler backPressCloseHandler;

    public Boolean mDBisOpened = false;
    public MainDatabase mMainDB = null;
    MainMenuAdapter menuAdapter = null;
    public void saveOptionData()
    {
        saveData();
    }
    public void chgFragment(int nMyIndex, int nDirect)
    {
        if(nMyIndex == 5)
        {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
        else if (nMyIndex ==6)
        {
            final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            mDialogType = 0;
            switch (mGlobalVar.getScreenPage()) {
                case 0:             // Graph->
                    transaction.replace(R.id.container, mFragmentGraph);
                    break;
                case 1:
                    transaction.replace(R.id.container, mFragmentBank);
                    break;
                case 2:
                    transaction.replace(R.id.container, mFragmentPurse);
                    break;
                case 3:
                    transaction.replace(R.id.container, mFragmengBudgetPurse);
                    break;

            }
            transaction.commit();
        }
        else {
            if (nDirect >= 1) {
                if (nMyIndex + 1 >= m_nMaxFragementCount) // index + 1 = count
                {
                    mGlobalVar.setScreenPage(0);
                } else {
                    mGlobalVar.setScreenPage(nMyIndex + 1);
                }
            } else {
                if (nMyIndex - 1 < 0) // index + 1 = count
                {
                    mGlobalVar.setScreenPage(m_nMaxFragementCount - 1);
                } else {
                    mGlobalVar.setScreenPage(nMyIndex - 1);

                }
            }
            saveData();
            final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            switch (mGlobalVar.getScreenPage()) {
                case 0:             // Graph->
                    transaction.replace(R.id.container, mFragmentGraph);
                    break;
                case 1:
                    transaction.replace(R.id.container, mFragmentBank);
                    break;
                case 2:
                    transaction.replace(R.id.container, mFragmentPurse);
                    break;
                case 3:
                    transaction.replace(R.id.container, mFragmengBudgetPurse);
                    break;

            }
            transaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlobalVar = GlobalVar.get();
        mDialogType = 0;
//        copyAssetDb(this);

        if(checkInitApp(this)==false)
        {
            copyAssetDb(this,BasicInfo.DATABASE_NAME);
            copyAssetDb(this,BasicInfo.LIVE_DATABASE_NAME);
            CopyAssets("image/flag");
            CopyAssets("image/bar");
            CopyAssets("image/tutorial");
            saveAppVersion();
        }

        // onebuy
        testFunc();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_main);


        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        String device_version;
        try {
            device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

        }
        catch (Exception e)
        {
            device_version = "";
            Log.e(BasicInfo.TAG,"App Version Err.");
        }
        Log.i(BasicInfo.TAG, "Setting screen name: " + "GraphFragment");
        mTracker.setScreenName("ver" + device_version);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
        mGlobalVar.setSdk(Build.VERSION.SDK.toString());
        loadData();
        mFragmentGraph = new GraphFragment();
        mFragmentBank = new BankFragment();
        mFragmentCalc = new CalcFragment();
        mFragmentPurse = new PurseFragment();
        mFragmengBudgetPurse = new PurseBudgetFragment();
        mFragmentTutorial = new TutorialFragment();
        mFragmentLicense = new LicenseFragment();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // onebuy
        mDrawerList.setItemsCanFocus(false);
        mDrawerList.setChoiceMode(ListView.CHOICE_MODE_NONE);// .CHOICE_MODE_SINGLE);

        menuAdapter = getMenuAdapter(this);

        mDrawerList.setAdapter(menuAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        backPressCloseHandler = new BackPressCloseHandler(this);

        if(savedInstanceState == null)
        {
            switch(mGlobalVar.getScreenPage())
            {
                case 0:
                    getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentGraph).commit();
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentBank).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentPurse).commit();
                    break;
            }

            //getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentPurse).commit();
        }
        //testNoti();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // 메뉴를 사용할 경우 아래줄 주석 없앨것.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
///*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    /*
        if(dtToggle.onOptionsItemSelected(item)){
            return true;
        }
*/
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_graph_addlinevisible) {
            return false;
        }
        else if(id == R.id.menu_graph_addlinevisible) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {     // 여기 안들어옴
   //     /*
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            m_nPreTouchPosX = (int)event.getX();
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            int nTouchPosX = (int)event.getX();

            if (nTouchPosX < m_nPreTouchPosX)   // 오른쪽
            {
//                Intent intent = new Intent(this, BankActivity.class);
//                startActivity(intent);
            }
            else if (nTouchPosX > m_nPreTouchPosX)
            {
                //MovewPreviousView();
            }

            m_nPreTouchPosX = nTouchPosX;
        }
//*/
        return false;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(!hasFocus) {
           // Toast.makeText(this, "Unfocused", Toast.LENGTH_SHORT).show();
            if(mGlobalVar.getScreenPage()==2)
            {
                mFragmentPurse.saveData();
            }
            else if (mGlobalVar.getScreenPage()==21)
            {
              //  mFragmengBudgetPurse.saveData();
            }
        }
        else
        ;
         //   Toast.makeText(this, "Focused", Toast.LENGTH_SHORT).show();


    }
    private static final String PACKAGE_DIR = "/data/data/com.reviewmerce.exchange/databases";
    public boolean checkInitApp(Context ctx)
    {
        try {
            FileInputStream file = new FileInputStream(BasicInfo.InternalPath + "conf/initOk.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String version = reader.readLine();
            try {
                String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                if(device_version.indexOf(version)>=0) {
                    file.close();
                    return true;
                }
                file.close();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public boolean saveAppVersion()
    {
        try {
            //FileOutputStream outStream = new FileOutputStream(BasicInfo.ExternalPath + "conf/initOk.txt");
            String mkdir = BasicInfo.InternalPath + "conf";
            File mpath = new File(mkdir);
            if(! mpath.isDirectory()) {
                mpath.mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(BasicInfo.InternalPath + "conf/initOk.txt");

            try {
                String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                outStream.write(device_version.getBytes());
                outStream.flush();
                outStream.close();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void copyAssetDb(Context ctx,String sFileName)
    {
        File folder = new File(PACKAGE_DIR);
        folder.mkdirs();

        File outfile = new File(PACKAGE_DIR + "/" + sFileName);
        //if (outfile.length() <= 0) {
        AssetManager assetManager = ctx.getResources().getAssets();
        try {
            InputStream is = assetManager.open(sFileName, AssetManager.ACCESS_BUFFER);
            long filesize = is.available();

            byte[] tempdata = new byte[(int) filesize];
            is.read(tempdata);
            is.close();
            outfile.createNewFile();
            FileOutputStream fo = new FileOutputStream(outfile);
            fo.write(tempdata);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void CopyAssets(String sFolder) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        String mkdir = null ;
        try {
            files = assetManager.list(sFolder);
            //이미지만 가져올때 files = assetManager.list("image");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for(int i=0; i<files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(sFolder + "/"+files[i]);

                //폴더생성
                /*
                String str = Environment.getExternalStorageState();
                if ( str.equals(Environment.MEDIA_MOUNTED)) {
                    mkdir = BasicInfo.ExternalPath + sFolder;
                } else {
                    Environment.getExternalStorageDirectory();
                    mkdir = BasicInfo.ExternalPath + sFolder;
                }*/
                mkdir = BasicInfo.InternalPath + sFolder;
                File mpath = new File(mkdir);
                if(! mpath.isDirectory()) {
                    mpath.mkdirs();
                }
                //

                out = new FileOutputStream(BasicInfo.InternalPath + sFolder + "/" + files[i]);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    private void openDatabase(){
        mMainDB = MainDatabase.getInstance(this);

        boolean isOpen = mMainDB.open();
        if (isOpen) {
            mDBisOpened = true;
            Log.d(BasicInfo.TAG, "exchange database is open.");
        } else {
            Log.d(BasicInfo.TAG, "exchange database is not open.");
        }
        mMainDB.createTable();
    }
    public void saveData()
    {
        try {
            if (mDBisOpened == false) {
                openDatabase();
            }
            int nIndex = 0;

            try {
                mMainDB.delete_createTable();
/*
                String SQLOld = "insert or replace into option" + " values (" +
                        Integer.toString(GlobalVar.mScreenPage) + "," +
                        "\"" + GlobalVar.mCurrency + "\"," +
                        Integer.toString(GlobalVar.mGraphPeriod) + "," +
                        "\"" + GlobalVar.mExchangeType + "\"," +
                        "\"" + Boolean.toString(GlobalVar.mNetworkMode) + "\"" +
                        ")";
                */
                String SQL = "insert or replace into option" + " values (" +
                        Integer.toString(mGlobalVar.getScreenPage()) + "," +
                        "\"" + mGlobalVar.getCurrencyCodeInEng() + "\"," +
                        Integer.toString(mGlobalVar.getGraphPeriod()) + "," +
                        "\"" + mGlobalVar.getExchangeType() + "\"," +
                        "\"" + Boolean.toString(mGlobalVar.getNetworkMode()) + "\"" +
                        ")";
                mMainDB.execSQL(SQL);


            } catch (Exception e) {
                Log.e("AddDB error : ", e.toString());
            }
        }
        catch (Exception e)
        {
            Log.e(BasicInfo.TAG,"MainActivity loadData Error");
        }
    }


    private void loadData() {
        try {
            if (mDBisOpened == false) {
                openDatabase();
            }
            String SQL = "select * "
                    + "from option";
            Cursor c1;
            try {

                c1 = mMainDB.rawQuery(SQL);
                if (c1 == null)
                    return;
            } catch (Exception e) {
                Log.e("Excute Main LoadDB : ", e.toString());
                return;
            }
            c1.moveToNext();
            if (c1.getCount() <= 0)
                return;
            mGlobalVar.setScreenPage(c1.getInt(0));
            mGlobalVar.chgCurrency(c1.getString(1));
            mGlobalVar.setGraphPeriod(c1.getInt(2));
            mGlobalVar.setExchangeType(c1.getString(3));
            mGlobalVar.setNetworkMode(Boolean.valueOf(c1.getString(4)));
            c1.close();
        }
        catch (Exception e)
        {
            Log.e(BasicInfo.TAG,"MainActivity loadData Error");
        }
    }
    //Toast.makeText(getActivity(),"OnCreateView", Toast.LENGTH_SHORT).show();
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            MenuData item = (MenuData)parent.getItemAtPosition(position);
            //MenuItem item = (MenuItem) (menuAdapter.getItem((int)id));

            switch((int)id)
            {
                case 0:
                    if(mGlobalVar.getNetworkMode())
                        mGlobalVar.setNetworkMode(false);
                    else
                        mGlobalVar.setNetworkMode(true);
                    saveData();
                    menuAdapter.notifyDataSetInvalidated();
                    break;
                case 1:
                    selectItem(1);
                    break;
                case 2:
                    selectItem(2);
                    break;
                case 3:
                    selectItem(3);
                    break;
            }
           // selectItem(position);
        }
    }
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        // Insert the fragment by replacing any existing fragment
        final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:             // Graph->
                break;
            case 1:
                transaction.replace(R.id.container, mFragmentTutorial);
                mDialogType = 1;
                break;
            case 2:
                transaction.replace(R.id.container, mFragmentLicense);
                mDialogType = 2;
                break;
            case 3:
                switch(mGlobalVar.getScreenPage())
                {
                    case 0:
                        mFragmentGraph.makeDialog();
                        break;
                    case 1:
                        mFragmentBank.makeDialog();
                        break;
                    case 2:
                        mFragmentPurse.makeDialog();
                        break;
                }
                break;
        }

        transaction.commit();
        // Highlight the selected item, update the title, and close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    public void testNoti()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, this.getClass());

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(this.getClass());
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }


    public static MainMenuAdapter getMenuAdapter(Context context)
    {
        final MainMenuAdapter adapter = new MainMenuAdapter(context);
        GlobalVar globalVar = GlobalVar.get();
        adapter.addItem(new MenuData(1,"모바일 데이터 사용",globalVar.getNetworkMode()));
        adapter.addItem(new MenuData(0, "튜토리얼", true));
        adapter.addItem(new MenuData(0,"라이센스 정보",true));

        adapter.addItem(new MenuData(0, "계산기", true));

        return adapter;
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        switch(mDialogType)
        {
            case 0:         // 메인화면
                backPressCloseHandler.onBackPressed();
                break;
            case 1:         // 튜토리얼
                chgFragment(6,1);
                break;
            case 2:         // 라이센스
                chgFragment(6,1);
                break;
        }

    }
    public void testFunc()
    {
        /*
        NumberDigitObject obj1,obj2,obj3;
        obj1 = new NumberDigitObject(330);
        obj2 = new NumberDigitObject(330.0);
        obj3 = new NumberDigitObject(33021.01);

        obj1.addNumber(0,0);
        Log.i(BasicInfo.TAG, obj1.toString());
        obj1.delNumber(0);
        obj1.delNumber(0);
        Log.i(BasicInfo.TAG, obj1.toString());

        obj2.addNumber(0,1);
        Log.i(BasicInfo.TAG, obj2.toString());
        obj2.delNumber(2);
        obj2.delNumber(1);
        Log.i(BasicInfo.TAG, obj2.toString());
*/

    }
    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                //activity.finish(); // 이건 Activity만 종료되고 App은 Background로 살아 있다.
                System.exit(0);
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity,
                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

