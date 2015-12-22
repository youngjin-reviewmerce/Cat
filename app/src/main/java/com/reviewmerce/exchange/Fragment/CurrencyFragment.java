package com.reviewmerce.exchange.Fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.exchange.AnalyticsApplication;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.BankAdapter;
import com.reviewmerce.exchange.ListView.BankExchangeAdapter;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.BankExchangeItem;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.custom.MyMenuBtn;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.publicClass.NetworkInfo;
import com.reviewmerce.exchange.testClass.BankSortedData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class CurrencyFragment extends ListFragment {
    GlobalVar mGlobalVar=null;

    private static String[] items={"1", "2", "3", "4", "5",	"6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15",	"16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25"};
    private CurrencyAdapter adapter=null;
    private ArrayList<String> array=new ArrayList<String>(Arrays.asList(items));

    private Tracker mTracker;
    public CurrencyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bank, container, false);
        mGlobalVar = GlobalVar.get();
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        Log.i(BasicInfo.TAG, "Setting screen name: " + "BankFragment");
        mTracker.setScreenName("Screen" + "BankFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        adapter = mGlobalVar.getCurrencyAdapter(getActivity());
        setListAdapter(adapter);

//        TouchListView tlv=(TouchListView)getListView();

 //       tlv.setDropListener(onDrop);
  //      tlv.setRemoveListener(onRemove);
        return v;
    }


}
