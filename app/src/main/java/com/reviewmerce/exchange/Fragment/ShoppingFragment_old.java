package com.reviewmerce.exchange.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread_shop;
import com.reviewmerce.exchange.ListView.ImageItemAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.apiData.ImageAndLink;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;

import java.util.ArrayList;

public class ShoppingFragment_old extends Fragment
//        implements LoaderManager.LoaderCallbacks<Cursor>
{
    public baseOnebuyFragment.FragmentChangeListener mCallback;
    private String mTitle;
    private int mFragmentType = 0;
    public RecyclerView rvTop = null;
    //    public RecyclerView rvMid=null;
    public RecyclerView rvBot = null;
    private ShoppingDataLab mUserDataLab = null;
    private Handler mHandlerHttp;
    private LinearLayout mMainLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (baseOnebuyFragment.FragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onResume() {
        switch (mFragmentType) {
            case BasicInfo.FRAGMENT_MAINPAGE:
             //   mCallback.chgTopbar(BasicInfo.TOPBAR_NONE, mTitle);
             //   paddingNone();
                break;
            case BasicInfo.FRAGMENT_SEARCHPAGE:
              //  mCallback.chgTopbar(BasicInfo.TOPBAR_SEARCH, mTitle);
                break;
            case BasicInfo.FRAGMENT_CATEGORY_TOP:
              //  mCallback.chgTopbar(BasicInfo.TOPBAR_NORMAL, mTitle);
                break;
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString("title");
            mFragmentType = args.getInt("type");
        }
        mUserDataLab = ShoppingDataLab.get(null);
        rvTop = (RecyclerView) rootView.findViewById(R.id.top_recyclerview);
//        rvMid = (RecyclerView)rootView.findViewById(R.id.mid_recyclerview);
        rvBot = (RecyclerView) rootView.findViewById(R.id.bot_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setHasFixedSize(true);
        rvTop.setLayoutManager(layoutManager);

        GridLayoutManager layoutMidManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        layoutMidManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBot.setHasFixedSize(true);
        rvBot.setLayoutManager(layoutMidManager);

        mMainLayout = (LinearLayout)rootView.findViewById(R.id.fragmentLayout);
//        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
//        Boolean b3g = NetworkInfo.Is3GAvailable(getActivity());
//        if (bWifi | b3g == false) {
//            mUserDataLab.loadMainData();
//        }
        mUserDataLab.loadMainData();
        doData_to_Adapter(rvTop, rvBot);

        initHttpHandler();
        ConnectMonitorThread_shop thread = new ConnectMonitorThread_shop("", getContext(), mHandlerHttp, BasicInfo.THREAD_TYPE_HTTP_MAIN);
        //thread.setSearchKeyword(sKeyword);
        thread.execute();


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initHttpHandler() {
        mHandlerHttp = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BasicInfo.THREAD_TYPE_HTTP_MAIN:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정
                        int nRtn = msg.arg1;
                        if (nRtn == 0) {
 //                           addTestSeolAdapter(rvTop);
//                            addTestAdapterVertical(rvBot);
                            doData_to_Adapter(rvTop,rvBot);
                        } else {
                            doData_to_Adapter(rvTop,rvBot);
                        }
                        //                       mCallback.chgFragment(BasicInfo.FRAGMENT_SEARCHPAGE,BasicInfo.FRAGMENT_SEARCHPAGE);
                        break;

                }
            }
        };
    }
    public void doData_to_Adapter(RecyclerView topView, RecyclerView midView)
    {
        ArrayList<ImageAndLink> listMain,listTop,listMid;
        listMain = mUserDataLab.getMainImageLinkList();
        listTop = new ArrayList<ImageAndLink>();
        listMid = new ArrayList<ImageAndLink>();


        for(ImageAndLink item : listMain)
        {
            ImageAndLink addItem = new ImageAndLink(item);
            if(addItem.getIALType().indexOf("T")>=0)
            {
                listTop.add(addItem);
            }
            else
            {
                listMid.add(addItem);
            }
        }
        ImageItemAdapter topAdapter = (ImageItemAdapter)topView.getAdapter();
        if(topAdapter==null)
            topAdapter = new ImageItemAdapter(getContext(), listTop, R.layout.fragment_shopping);
        else {
            topAdapter.removeAllItem();
            topAdapter.setItemList(listTop);
            topAdapter.notifyDataSetChanged();
        }

        topAdapter.setCallback(mCallback);
        topView.setAdapter(topAdapter);
   //     topAdapter.notifyDataSetChanged();

        ImageItemAdapter midAdapter = (ImageItemAdapter)midView.getAdapter();
        if(midAdapter==null)
            midAdapter = new ImageItemAdapter(getContext(), listMid, R.layout.fragment_shopping);
        else
        {
            midAdapter.removeAllItem();
            midAdapter.setItemList(listMid);
            midAdapter.notifyDataSetChanged();
        }
        midAdapter.setCallback(mCallback);
        midView.setAdapter(midAdapter);
    //    midAdapter.notifyDataSetChanged();
        //////////////////////////////////////////////////////////////////////////////////////////////////////

//        topAdapter.setCallback(mCallback);
//        topView.setAdapter(topAdapter);
//        ImageItemAdapter midAdapter = new ImageItemAdapter(getContext(), listMid, R.layout.fragment_contentview);
//        midAdapter.setCallback(mCallback);
//        midView.setAdapter(midAdapter);



    }
    public void paddingNone() {
        // 아래와 같이 해줘야 기기에 맞는 DP가 나온다.
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(20 * dm.density);
        if(mMainLayout != null)
            mMainLayout.setPadding(0, 0, 0, 0);
//        Button btn = new Button(mContext);
        //패딩은 그냥 이렇게 쓰면 된다.
        //       btn.setPadding(0,size,0,0);
 /*
        //마진을 쓸려면  아래와 같은 방법을 쓰면 된다.
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        param.topMargin = size;
        btn.setLayoutParams(param);
        */
    }
}