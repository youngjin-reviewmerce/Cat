package com.reviewmerce.exchange.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.reviewmerce.exchange.AnalyticsApplication;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.Fragment.BankFragment;
import com.reviewmerce.exchange.Fragment.CalcFragment;
import com.reviewmerce.exchange.Fragment.GraphFragment;
import com.reviewmerce.exchange.Fragment.LicenseFragment;
import com.reviewmerce.exchange.Fragment.NationFragment;
import com.reviewmerce.exchange.Fragment.PurseFragment;
import com.reviewmerce.exchange.Fragment.PurseInputDialogFragment;
import com.reviewmerce.exchange.Fragment.ShoppingFragment;
import com.reviewmerce.exchange.Fragment.ShoppingFragmentWeb;
import com.reviewmerce.exchange.Fragment.TutorialFragment;
import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.MainMenuAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.Fragment.TabBottomFragment;
import com.reviewmerce.exchange.db.MainDatabase;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.publicClass.ExchangeDataLab;
import com.reviewmerce.exchange.publicClass.ListedItemDatabase;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;
import com.reviewmerce.exchange.publicClass.NetworkInfo;
import com.reviewmerce.exchange.publicClass.PurseDataLab;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, baseOnebuyFragment.FragmentChangeListener{
    GlobalVar mGlobalVar=null;
    NationDataLab mNationDataLab=null;
//    private ViewFlipper m_viewFlipper;
    private int m_nPreTouchPosX = 0;
//    private int m_nFragmentIndex = 0;
    private int m_nMaxFragementCount = 3;
    private static GraphFragment mFragmentGraph;
    private static BankFragment mFragmentBank;
    private static CalcFragment mFragmentCalc;
    private static PurseFragment mFragmentPurse;

    private static TutorialFragment mFragmentTutorial;
    private static LicenseFragment mFragmentLicense;
    private static TabBottomFragment mFragmentBottomTab;
    private static ShoppingFragment mFragmentShopping;
    private static ShoppingFragmentWeb mFragmentShoppingWeb;
    private static PurseInputDialogFragment mDialogPurseInput;

    private static NationFragment mFragmentNation;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int mDialogType;
    private BackPressCloseHandler backPressCloseHandler;
    private Tracker mTracker;               // Google Anaylytics
    public Boolean mDBisOpened = false;
    public MainDatabase mMainDB = null;
    MainMenuAdapter menuAdapter = null;
    public void saveOptionData()
    {
        saveData();
    }
    boolean bFirstRun = false;
    ShoppingDataLab mShoppingDataLab = null;
    private ImageButton ibCalc,ibShopping, ibTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkVersion(this)==false)
        {
            copyAsset(BasicInfo.COPYASSET_DB|BasicInfo.COPYASSET_IMAGE);
            saveAppVersion();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);       // status bar 없애기
        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        mTracker = application.getDefaultTracker();
        initDataLab();
        initImageLoader();
        addTab();
        initActivity(savedInstanceState);
    }
    public void addTab()
    {
        Bundle args = new Bundle();
        args.putInt("type", mGlobalVar.getScreenPage());
        args.putString("title", "");
        AddBottomFragment(BasicInfo.FRAGMENT_BOTTOM_TAB,args);
//        TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
//
//        TabHost.TabSpec tabSpecTab1 = tabHost.newTabSpec("TAB1").setIndicator("강아지");
//        tabSpecTab1.setContent(R.id.tab1);
//        tabHost.addTab(tabSpecTab1);
//
//        TabHost.TabSpec tabSpecTab2 = tabHost.newTabSpec("TAB2").setIndicator("고양이");
//        tabSpecTab2.setContent(R.id.tab2);
//        tabHost.addTab(tabSpecTab2);
//
//        TabHost.TabSpec tabSpecTab3 = tabHost.newTabSpec("TAB3").setIndicator("토끼");
//        tabSpecTab3.setContent(R.id.tab3);
//        tabHost.addTab(tabSpecTab3);
//
//        TabHost.TabSpec tabSpecTab4 = tabHost.newTabSpec("TAB4").setIndicator("말");
//        tabSpecTab4.setContent(R.id.tab1);
//        tabHost.addTab(tabSpecTab4);
//
//        tabHost.setCurrentTab(0);
    }

    public void initDataLab()
    {
        mNationDataLab = NationDataLab.get(this);
        BankDataLab bankDataLab = BankDataLab.get(this);
        ExchangeDataLab exchangeDataLab = ExchangeDataLab.get(this);
        LiveDataLab liveDataLab  = LiveDataLab.get(this);
        ShoppingDataLab userDataLab = ShoppingDataLab.get(this);
        PurseDataLab purseDataLab = PurseDataLab.get(this);
        mShoppingDataLab = ShoppingDataLab.get(this);
        mGlobalVar = GlobalVar.get();
        mGlobalVar.setSdk(Build.VERSION.SDK.toString());

        backPressCloseHandler = new BackPressCloseHandler(this);
        ListedItemDatabase db = ListedItemDatabase.getInstance(this);
        loadData();
        purseDataLab.loadPurseData_DB();
        /*
        try {
            String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if(device_version.charAt(0)=='1')
            {
                purseDataLab.loadPurseData_DB_ver1();
            }
            else {
                purseDataLab.loadPurseData_DB();
            }
        }
        catch (Exception e)
        {

        }
*/

        try {
            mShoppingDataLab.loadSearchedData();
        }catch (Exception e)
        {
            Log.e("Shipping Exception",e.toString());
        }
    }
    public void copyAsset(int nType)
    {
        if((nType & BasicInfo.COPYASSET_DB)>0) {
            copyAssetDb(this, BasicInfo.DATABASE_NAME);
            copyAssetDb(this, BasicInfo.LIVE_DATABASE_NAME);
            copyAssetDb(this, BasicInfo.NATION_DATABASE_NAME);
//            copyAssetDb(this, BasicInfo.PURSE_DATABASE_NAME);  // test용
        }
        if((nType & BasicInfo.COPYASSET_IMAGE)>0) {
            CopyAssets(BasicInfo.InternalPath, "image");
            CopyAssets(BasicInfo.InternalPath,"image/flag");
            //CopyAssets(BasicInfo.InternalPath,"image/bar");
            CopyAssets(BasicInfo.InternalPath,"image/background");
            CopyAssets(BasicInfo.InternalPath,"image/tutorial");
            //CopyAssets(BasicInfo.InternalPath,"image/bubble");
        }
        makeFolder(BasicInfo.InternalPath,"temp");
        makeFolder(BasicInfo.InternalPath, "temp/image");
    }
    public boolean checkVersion(Context ctx)
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
            //처음 실행시
            bFirstRun = true;
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public void showVersion()
    {
        try {
            String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("버전 정보");
            builder.setMessage(device_version);
            builder.setIcon(R.drawable.icon_cat);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }catch (Exception e)
        {

        }
    }


    public void initActivity(Bundle savedInstanceState)
    {

        Intent openingIntent = new Intent(this,OpeningActivity.class);
        startActivityForResult(openingIntent, BasicInfo.ACTIVITY_COMMUNICATION_OPENINGACTIVITY);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setOnTouchListener(check_closeKeyboard);
        setSupportActionBar(toolbar);
///*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                closeKeyboard();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
//        toggle.onDrawerOpened();
        drawer.setDrawerListener(toggle);

        //drawer.setOnTouchListener(check_closeKeyboard);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setOnTouchListener(check_closeKeyboard);

        Menu menu = navigationView.getMenu();
        SubMenu itemSub = menu.getItem(0).getSubMenu();;

        MenuItem item = itemSub.getItem(0);

        if(mGlobalVar.getNetworkMode() == true)
            item.setIcon(R.drawable.icon_menu_mobile_chk);
        else
            item.setIcon(R.drawable.icon_menu_mobile);
        getSupportActionBar().setTitle(null);


        //*
        if(savedInstanceState == null)      // App 실행시 수행할 내용
        {
            if(bFirstRun)
            {
                chgFragment(BasicInfo.FRAGMENT_NONE,mGlobalVar.getScreenPage());
                chgFragment(BasicInfo.FRAGMENT_NONE, BasicInfo.FRAGMENT_TUTORIAL);
                //getSupportFragmentManager().beginTransaction().add(R.id.container, mFragmentTutorial).commit();
               // mDialogType = 1;
            }
            else {
                chgFragment(BasicInfo.FRAGMENT_NONE,mGlobalVar.getScreenPage());

//                if((mGlobalVar.getExtandPage()&0x01)>0)
//                    runExtandFragment(3);
            }
            //getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentPurse).commit();
        }
        ibShopping = (ImageButton)findViewById(R.id.main_toolbar_shopping);
        ibShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_SHOPPING);
            }
        });
        ibCalc = (ImageButton)findViewById(R.id.main_toolbar_calc);
        ibCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                Bundle args = new Bundle();
                args.putString("title", "initsearch");
                args.putInt("type",BasicInfo.FRAGMENT_SEARCHPAGE);
                //           replaceTopBarFragment(BasicInfo.TOPBAR_SEARCH, args);
                //replaceFragment(BasicInfo.FRAGMENT_SEARCHPAGE);
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                mFragmentCalc = new CalcFragment();
                String sz = "hi";
                mFragmentCalc.show(fm, sz);
            }
        });
        ibTitle = (ImageButton)findViewById(R.id.main_toolbar_ibtitle);
        ibTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                chgFragment(BasicInfo.FRAGMENT_NONE,mGlobalVar.getScreenPage());
            }
        });
        //*/
    }

///*
    private View.OnTouchListener check_closeKeyboard = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            closeKeyboard();
            return false;
        }
    };


    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(!hasFocus) {
           // Toast.makeText(this, "Unfocused", Toast.LENGTH_SHORT).show();
            if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_PURSE)
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
    public void returnDialog(int nType,String sInput)
    {
        switch (nType) {
            case BasicInfo.FRAGMENT_EXCHANGE_GRAPH:             // Graph->
                mFragmentGraph.OnDialogOkListener(sInput);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_BANK:
                mFragmentBank.OnDialogOkListener(sInput);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE:
                mFragmentPurse.OnDialogOkListener(nType, sInput);
                break;
            case BasicInfo.FRAGMENT_CALC_ORIGIN:
                mFragmentCalc.OnDialogOkListener(0,sInput);
                break;
            case BasicInfo.FRAGMENT_CALC_CONVERTED:
                mFragmentCalc.OnDialogOkListener(1,sInput);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_BUDGET:
                mFragmentPurse.OnDialogOkListener(nType, sInput);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_EDIT:
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT:
                mFragmentPurse.OnDialogOkListener(nType, sInput);
                break;

        }
    }
    public void makeNationDialog(int nType)
    {
        try {
                //               /*
            BasicInfo.gStartTime = System.currentTimeMillis();
                if(mFragmentNation.isRunning() == false) {
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    mFragmentNation = new NationFragment();
                    mFragmentNation.setCallFragmentId(nType);
                    String sz = "hi";
                    mFragmentNation.show(fm, sz);
                }
        }
        catch(Exception e)
        {
            Log.e(BasicInfo.TAG, "makeDialog Err");
        }
    }



    public void chgFragment(int nMyIndex, int nDirect)
    {
        try{
            switch(nMyIndex) {
                case BasicInfo.FRAGMENT_NONE:   // 새창 다시 띄움
                {
                    Bundle args = new Bundle();
                    args.putInt("type", nDirect);
                    args.putString("title", "");

                    if(nDirect == BasicInfo.FRAGMENT_EXCHANGE_GRAPH)
                    {
                        mGlobalVar.setScreenPage(nDirect);
                        args.putString("title", "환율추이 그래프");
                        stackAddFragment(BasicInfo.FRAGMENT_EXCHANGE_GRAPH, args);
                    }
                    if (nDirect == BasicInfo.FRAGMENT_EXCHANGE_BANK) {
                        mGlobalVar.setScreenPage(nDirect);
                        args.putString("title", "은행비교");
                        stackAddFragment(BasicInfo.FRAGMENT_EXCHANGE_BANK, args);
                    }
                    if (nDirect == BasicInfo.FRAGMENT_EXCHANGE_PURSE) {
                        mGlobalVar.setScreenPage(nDirect);
                        args.putString("title", "환율지갑");
                        stackAddFragment(BasicInfo.FRAGMENT_EXCHANGE_PURSE, args);

                    }
                    if (nDirect == BasicInfo.FRAGMENT_SHOPPING) {
                    args.putString("title", "직구");
                    stackAddFragment(BasicInfo.FRAGMENT_SHOPPING, args);
                }
                    if (nDirect == BasicInfo.FRAGMENT_LICENSE) {
                        args.putString("title", "라이센스정보");
                        stackAddFragment(BasicInfo.FRAGMENT_LICENSE, args);
                    }
                    if (nDirect == BasicInfo.FRAGMENT_TUTORIAL) {
                        args.putString("title", "튜토리얼");
                        stackAddFragment(BasicInfo.FRAGMENT_TUTORIAL, args);
                    }

                    break;
                }
                case BasicInfo.FRAGMENT_EXCHANGE_PURSE:         // 자겁내의 Dialog 관리
                {
                    if(nDirect == BasicInfo.FRAGMENT_EXCHANGE_PURSE_INPUT)
                    {
                        Bundle args = new Bundle();
                        args.putString("title", "initsearch");
                        args.putInt("type",BasicInfo.FRAGMENT_SEARCHPAGE);
                        //           replaceTopBarFragment(BasicInfo.TOPBAR_SEARCH, args);
                        //replaceFragment(BasicInfo.FRAGMENT_SEARCHPAGE);
                        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                        mDialogPurseInput = new PurseInputDialogFragment();
                        mDialogPurseInput.setResultCallback((baseOnebuyFragment.PurseInterface) mFragmentPurse);
                        String sz = "hi";
                        mDialogPurseInput.show(fm, sz);

                    }
                    break;
                }
                case BasicInfo.FRAGMENT_LICENSE:
                {
                    getSupportFragmentManager().popBackStack();

//                    final FragmentTransaction transaction = getSupportFragmentManager()
//                            .beginTransaction();
//                    transaction.remove(mFragmentLicense);
//                    transaction.commit();
                    break;
                }
                case BasicInfo.FRAGMENT_TUTORIAL:
                {
                    getSupportFragmentManager().popBackStack();
//                    final FragmentTransaction transaction = getSupportFragmentManager()
//                            .beginTransaction();
//                    transaction.remove(mFragmentTutorial);
//                    transaction.commit();
                    break;
                }
                case BasicInfo.FRAGMENT_MOVE:       // Fragment이동
                {
                    if(nDirect==1)
                    {
                        if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_GRAPH)
                        {
                            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_BANK);
                        }
                        else if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_BANK)
                        {
                            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_PURSE);
                        }
                        else if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_PURSE)
                        {
                            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_GRAPH);
                        }

                    }
                    else if(nDirect==-1)
                    {
                        if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_GRAPH)
                        {
                            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_PURSE);
                        }
                        else if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_BANK)
                        {
                            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_GRAPH);
                        }
                        else if(mGlobalVar.getScreenPage()==BasicInfo.FRAGMENT_EXCHANGE_PURSE)
                        {
                            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_BANK);
                        }

                    }
                    break;
                }
            }
        }catch (Exception ee) {
            Log.d("CAT", ee.toString());}
    }

    private static final String PACKAGE_DIR = "/data/data/com.reviewmerce.exchange/databases";

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

    public void CopyAssets(String sFolder) {
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
    public void CopyAssets(String sPath, String sFolder) {
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
                mkdir = sPath + sFolder;
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
            Log.d(BasicInfo.TAG, "main database is open.");
        } else {
            Log.d(BasicInfo.TAG, "main database is not open.");
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

                String SQL = "insert or replace into option" + " values (" +
                        Integer.toString(mGlobalVar.getScreenPage()) + "," +
                        "\"" + mGlobalVar.getSelectCurrency().getCurrencyCodeInEng() + "\"," +
                        Integer.toString(mGlobalVar.getGraphPeriod()) + "," +
                        "\"" + mGlobalVar.getExchangeType() + "\"," +
                        "\"" + Boolean.toString(mGlobalVar.getNetworkMode()) + "\"" + "," +
                        Integer.toString(mGlobalVar.getExtandPage()) +
                        ")";
                mMainDB.execSQL(SQL);


            } catch (Exception e) {
                Log.e("AddDB error : ", e.toString());
            }
        }
        catch (Exception e)
        {
            Log.e(BasicInfo.TAG, "MainActivity saveData Error");
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
            mNationDataLab.chgCurrency(c1.getString(1));
            mGlobalVar.setGraphPeriod(c1.getInt(2));
            mGlobalVar.setExchangeType(c1.getString(3));
            mGlobalVar.setNetworkMode(Boolean.valueOf(c1.getString(4)));
            mGlobalVar.setExtandPage(c1.getInt(5));
            c1.close();
        }
        catch (Exception e)
        {
            Log.e(BasicInfo.TAG,"MainActivity loadData Error");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args = new Bundle();
        args.putString("title", "initsearch");
        args.putInt("type", BasicInfo.FRAGMENT_SEARCHPAGE);

        final FragmentTransaction transaction;
        Fragment fragment = null;
        if (id == R.id.nav_license) {
            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_LICENSE);
//            transaction = this.getSupportFragmentManager().beginTransaction();
//            fragment = getFragment(BasicInfo.FRAGMENT_LICENSE);
//            transaction.replace(R.id.fragmentLayout, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
            // Handle the camera action
        } else if (id == R.id.nav_tutorial ) {
            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_TUTORIAL);
//            transaction = this.getSupportFragmentManager().beginTransaction();
//            //stackAddFragment(BasicInfo.FRAGMENT_LICENSE,args);
//            fragment = getFragment(BasicInfo.FRAGMENT_TUTORIAL);
//            transaction.replace(R.id.fragmentLayout, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
        }
        else if(id == R.id.nav_mobile)
        {
            if(mGlobalVar.getNetworkMode()) {
                mGlobalVar.setNetworkMode(false);
                item.setIcon(R.drawable.icon_menu_mobile);
            }
            else {
                mGlobalVar.setNetworkMode(true);
                item.setIcon(R.drawable.icon_menu_mobile_chk);
            }
            saveData();

        }
        else if (id == R.id.nav_shipping)
        {
            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_SHOPPING);
        }
        else if (id == R.id.nav_calc)
        {
            args = new Bundle();
            args.putString("title", "initsearch");
            args.putInt("type",BasicInfo.FRAGMENT_SEARCHPAGE);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            mFragmentCalc = new CalcFragment();
            String sz = "hi";
            mFragmentCalc.show(fm, sz);
        }
        else if( id == R.id.nav_version)
        {
            showVersion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            int nCount = getSupportFragmentManager().getBackStackEntryCount();


            if(nCount<=1)
                backPressCloseHandler.onBackPressed();
            else {
                int nPreCount = getSupportFragmentManager().getBackStackEntryCount();
                getSupportFragmentManager().popBackStack();




            }
            //super.onBackPressed();
        }
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
    public void initImageLoader()
    {
        /*
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY-1)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
                */
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .memoryCacheExtraOptions(1440, 2560) // default = device screen dimensions
                        //.diskCacheExtraOptions(480, 800, null)
                .diskCacheExtraOptions(1440, 2560, null)
                        //.taskExecutor(...)
                        //.taskExecutorForCachedImages(...)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
//                .imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

    }
    public void stackAddFragment(int reqNewFragmentIndex,Bundle bundle) {

        Fragment newFragment = null;

        Log.d("mainActivity", "fragmentReplace " + reqNewFragmentIndex);

        newFragment = getFragment(reqNewFragmentIndex);
        newFragment.setArguments(bundle);
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);


        // Commit the transaction
        transaction.commit();
    }
    public void AddBottomFragment(int reqNewFragmentIndex,Bundle bundle) {

        Fragment newFragment = null;

        Log.d("mainActivity", "fragmentReplace " + reqNewFragmentIndex);

        newFragment = getFragment(reqNewFragmentIndex);
        newFragment.setArguments(bundle);
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();


        transaction.replace(R.id.bottomLayout, newFragment);
        // transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case BasicInfo.FRAGMENT_EXCHANGE_GRAPH:
     //           if(mFragmentGraph==null)
                    mFragmentGraph = new GraphFragment();
                newFragment = mFragmentGraph;
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_BANK:
     //           if(mFragmentBank==null)
                    mFragmentBank = new BankFragment();
                newFragment = mFragmentBank;
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE:
     //           if(mFragmentPurse==null)
                    mFragmentPurse = new PurseFragment();
                newFragment = mFragmentPurse;
                break;
            case BasicInfo.FRAGMENT_SHOPPING:
                //           if(mFragmentPurse==null)
                mFragmentShopping = new ShoppingFragment();
                newFragment = mFragmentShopping;
                break;
            case BasicInfo.FRAGMENT_BOTTOM_TAB:
     //           if(mFragmentPurse==null)
                    mFragmentBottomTab = new TabBottomFragment();
                newFragment = mFragmentBottomTab;
                break;
            case BasicInfo.FRAGMENT_ITEMWEBPAGE:
                mFragmentShoppingWeb = new ShoppingFragmentWeb();
                newFragment = mFragmentShoppingWeb;
                break;
            case BasicInfo.FRAGMENT_TUTORIAL:
                mFragmentTutorial = new TutorialFragment();
                newFragment = mFragmentTutorial;
                break;
            case BasicInfo.FRAGMENT_LICENSE:
                mFragmentLicense = new LicenseFragment();
                newFragment = mFragmentLicense;
                break;

            default:
                Log.d("mainActivity", "Unhandle case");
                break;
        }
        return newFragment;
    }
    public void makeFolder(String sPath, String sFolder)
    {
        try {
            String mkdir = sPath + sFolder;
            File mpath = new File(mkdir);
            if (!mpath.isDirectory()) {
                mpath.mkdirs();
            }
        }catch(Exception e) {
            Log.e("makeFolder", e.getMessage());
        }
    }
    public void newWebpage(final String url)
    {

        Boolean bWifi = NetworkInfo.IsWifiAvailable(this);
        if((mGlobalVar.getNetworkMode() == false) && (bWifi == false)){
            android.app.AlertDialog.Builder alt_bld = new android.app.AlertDialog.Builder(this);
            String sMsg = String.format("서비스에 필요한 데이터를 가져올 수 없습니다.\n" +
                    "사용을 위해 모바일데이터를 활성화 해주세요.\n(메뉴 > 모바일데이터 선택)");
            alt_bld.setMessage(sMsg).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            alt_bld.show();
        }
        else
        {
            openWebPage(url);
        }

    }
    public void openWebPage(String url)
    {
        ShoppingFragmentWeb fragmentWeb = (ShoppingFragmentWeb)getFragment(BasicInfo.FRAGMENT_ITEMWEBPAGE);
        if(url.length()<=0)
        url = "http://dong-dong.cloudapp.net/SlidePage.html";
        fragmentWeb.setUrl(url);
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragmentLayout, fragmentWeb);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

    }

    public void chgBottombar(int nFragmentType, String sTopbarName)
    {
        if(mFragmentBottomTab==null)
            mFragmentBottomTab = (TabBottomFragment)getFragment(BasicInfo.FRAGMENT_BOTTOM_TAB);
        if((nFragmentType==BasicInfo.FRAGMENT_EXCHANGE_GRAPH) ||
                (nFragmentType==BasicInfo.FRAGMENT_EXCHANGE_BANK) ||
                (nFragmentType==BasicInfo.FRAGMENT_EXCHANGE_PURSE)) {
            mGlobalVar.setScreenPage(nFragmentType);
            saveData();
        }
        mFragmentBottomTab.changeSelectPoint(nFragmentType);
    }
    private void closeKeyboard()
    {
        //   /*
        try {
            if(mFragmentShopping!=null)
                mFragmentShopping.closeKeyboard();
            if(getCurrentFocus().getWindowToken() != null) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.i("keyboard", e.toString());
        }
//*/
    }
//    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener(){
//
//        @Override
//        public void onDrawerClosed(View drawerView) {
//        }
//
//        @Override
//        public void onDrawerOpened(View drawerView) {
//        }
//
//        @Override
//        public void onDrawerSlide(View drawerView, float slideOffset) {
//        }
//
//        @Override
//        public void onDrawerStateChanged(int newState) {
//            String state;
//            switch(newState){
//                case DrawerLayout.STATE_IDLE:
//                    state = "STATE_IDLE";
//                    break;
//                case DrawerLayout.STATE_DRAGGING:
//                    state = "STATE_DRAGGING";
//                    break;
//                case DrawerLayout.STATE_SETTLING:
//                    state = "STATE_SETTLING";
//                    break;
//                default:
//                    state = "unknown!";
//            }
//
//            textPrompt2.setText(state);
//        }};
//
//}
}

