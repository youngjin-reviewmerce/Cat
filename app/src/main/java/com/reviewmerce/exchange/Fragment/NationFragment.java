package com.reviewmerce.exchange.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.DndListView;
import com.reviewmerce.exchange.ListView.NationAdapter;
import com.reviewmerce.exchange.ListView.NationAdapterExpand;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.commonData.NationItem;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.commonData.SoundSearcher;
import com.reviewmerce.exchange.publicClass.NationDataLab;

import java.util.List;

public class NationFragment extends DialogFragment {
    public baseOnebuyFragment.FragmentChangeListener mCallback;
    private DndListView mListView;
    private NationAdapter mNationAdapter=null;
 //   private NationAdapterExpand mAllNationAdapter=null;
    private ToggleButton toggleButton;
    private NationDataLab mNationLab = null;
    private int mCallFragmentId;
    protected ImageView mBackgroundView;
    private EditText etEditSearch;

    FrameLayout mFrameLayout;

    public static boolean isRunning() {
        return m_bRunning;
    }

    public static void setRunning(boolean bRunning) {
        m_bRunning = m_bRunning;
    }

    static boolean m_bRunning;
    LinearLayout mExpandLayout, mBasicLayout;

///*
    public NationFragment(){
        mCallFragmentId = 0;
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mCallback = (baseOnebuyFragment.FragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        int dialogWidth = (int)(width*0.8); // specify a value here
        int dialogHeight = (int)(height*0.8); // specify a value here

        if(dialogHeight<1000)
            dialogHeight=1000;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);


        //  setButton();
        // ... other stuff you want to do in your onStart() method
    }
    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }
    @Override
    public void onStop() {
        m_bRunning = false;
        super.onStop();

    }
    public void setCallFragmentId(int nId)
    {
        mCallFragmentId = nId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_nation, container, false);
///*
        long lStart = System.currentTimeMillis();
        // make dialog itself transparent
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // remove background dim
  //      getDialog().getWindow().setDimAmount(1);

        //[add more custom code here...]
        m_bRunning = true;
        mNationLab = NationDataLab.get(null);
        //countryAdapter = mNationLab.getCurrencyCalcAdapter(getActivity());
        mListView = (DndListView)rootView.findViewById(R.id.list_country);
        toggleButton = (ToggleButton)rootView.findViewById(R.id.toggle_button);
        mBackgroundView = (ImageView)rootView.findViewById(R.id.ivScreen);


        etEditSearch = (EditText) rootView.findViewById(R.id.tv_edit_search);
        etEditSearch.addTextChangedListener(inputSearchListener);
        long lMid1 = System.currentTimeMillis();
        //tv_title_searchl;
        if((mCallFragmentId == 30) ||(mCallFragmentId == 31))   // 계산기는
            mNationAdapter = mNationLab.getNationCalcAdapter();
        else
            mNationAdapter = mNationLab.getNationAdapter();

     //   mAllNationAdapter = mNationLab.getAllNationAdapter(getActivity());

        toggleButton.setOnClickListener(toggleClickListener);
        mListView.setDragAvalablePerc(0.9);
        mListView.setAdapter(mNationAdapter);
        mListView.setOnItemClickListener(ItemClickListener);
        long lMid2 = System.currentTimeMillis();

        String str = String.format("start : %d, mid1 : %d,mid2 : %d",lStart- BasicInfo.gStartTime, lMid1 - lStart, lMid2 - lMid1);
    //    Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();

   //     setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    //    getDialog().getWindow().setBackgroundDrawable(Color.TRANSPARENT);
       // mBackgroundView.setBackgroundColor(Color.TRANSPARENT);
        return rootView;
    }
//*/
    DndListView.DragListener dragListener = new DndListView.DragListener() {
        @Override
        public void drag(int from, int to) {
        }
    };

    DndListView.DropListener dropListener = new DndListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            /*
            NationItem fromItem;
            NationItem nextItem;
            NationItem postItem;
            List<NationItem> Items = mAllNationAdapter.getDataArray();

            if(from < to){
                for(int currentPos = from ; currentPos < to ; currentPos++){
                    fromItem = Items.get(currentPos);
                    nextItem = Items.get(currentPos+1);
                    Items.set(currentPos+1, fromItem);
                    Items.set(currentPos, nextItem);
            }
            } else if(from > to){
                for(int currentPos = from ; currentPos > to ; currentPos--){
                    fromItem = Items.get(currentPos);
                    postItem = Items.get(currentPos-1);
                    Items.set(currentPos-1, fromItem);
                    Items.set(currentPos, postItem);
                }
            } else {
               //
            }
//            mAllNationAdapter.notifyDataSetChanged();
*/
        }
    };


    View.OnClickListener toggleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
            if (toggleButton.isChecked()) {   //켜짐
                //mNationAdapter.setBoolShowCheckBox(true);
                mListView.setDragListener(dragListener);
                mListView.setDropListener(dropListener);
                mListView.setAdapter(mAllNationAdapter);
                mAllNationAdapter.notifyDataSetChanged();
            } else {                          //꺼짐
                //mNationAdapter.setBoolShowCheckBox(false);
                mListView.setDragListener(null);
                mListView.setDropListener(null);
                mListView.setAdapter(mNationAdapter);
                mNationAdapter.notifyDataSetChanged();
            }
            */
        }
    };
    TextWatcher inputSearchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
           int nIndex = mNationAdapter.searchNation(s.toString());
            mListView.setSelection(nIndex);
        }
    };
    View.OnClickListener editSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NationItem item = (NationItem)parent.getItemAtPosition(position);
            mCallback.returnDialog(mCallFragmentId,item.getEngName());
            dismiss();
        }
    };
            /*
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
        CurrencyItem item = (CurrencyItem) (adapter.getItem(which));
        chgCurrency(item.getData(0));

        int nCount = 0;
        nCount = mBankDataLab.loadData();
        if (nCount > 0) {
            setValue();
        }
        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
        if ((bWifi == true) || (mGlobalVar.getNetworkMode())) {
            refresh();
        }
        bDoingChgCurrency = false;

    }
});
*/
/*
    public Bitmap getBitmapFlagFile(String sFile)
    {
        Bitmap bitmapFlagFile = null;
        if(bitmapFlagFile==null)
        {
            String sFilename = InternalPath + "image/flag/" + sFile;
            try {
                if (bitmapFlagFile != null) {
                    bitmapFlagFile.recycle();
                    bitmapFlagFile = null;
                }
                bitmapFlagFile = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapFlagFile;
    }

    final String InternalPath = "/data/data/com.example.reviewmerce.myapplication/";
    */
}