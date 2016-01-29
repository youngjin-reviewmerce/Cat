package com.reviewmerce.exchange.Fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.exchange.AnalyticsApplication;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.publicClass.NationDataLab;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class CurrencyFragment extends ListFragment {
    GlobalVar mGlobalVar=null;
    NationDataLab mNationLab = null;
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
        mNationLab = NationDataLab.get(null);
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        Log.i(BasicInfo.TAG, "Setting screen name: " + "BankFragment");
        mTracker.setScreenName("Screen" + "BankFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        adapter = mNationLab.getCurrencyAdapter(getActivity());
        setListAdapter(adapter);

//        TouchListView tlv=(TouchListView)getListView();

 //       tlv.setDropListener(onDrop);
  //      tlv.setRemoveListener(onRemove);
        return v;
    }


}
