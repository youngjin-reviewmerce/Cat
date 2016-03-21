package com.reviewmerce.exchange.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread_shop;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.ImageItemAdapter;
import com.reviewmerce.exchange.ListView.PurseAdapter_2;
import com.reviewmerce.exchange.ListView.SearchItemAdapter;
import com.reviewmerce.exchange.ListView.SearchResultAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.apiData.ImageAndLink;
import com.reviewmerce.exchange.commonData.apiData.ListedItem;
import com.reviewmerce.exchange.commonData.apiData.SuggestKeyword;
import com.reviewmerce.exchange.custom.WrapperEditText;
import com.reviewmerce.exchange.publicClass.NetworkInfo;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment
    implements baseOnebuyFragment.SearchFragmentListener
//        implements LoaderManager.LoaderCallbacks<Cursor>
{
    public baseOnebuyFragment.FragmentChangeListener mCallback;
    private MediaPlayer mediaPlayer;
    private String mTitle;
    private int mFragmentType = 0;
    public RecyclerView rvTop = null;
    //    public RecyclerView rvMid=null;
    public RecyclerView rvBot = null;
    public RecyclerView rvBot_searched = null;
    private ShoppingDataLab mUserDataLab = null;
    private Handler mHandlerHttp;
    private LinearLayout mMainLayout;
    private TextView tvMidTitle = null;
    private ImageView ivTitle = null;
    private ImageButton ivTop = null;
    private ScrollView svScroll=null;

    public RecyclerView search_rvSearched = null;
    private WrapperEditText search_etSearchWord=null;
    private boolean search_bIsKeyboard = false;
    List<SuggestKeyword> search_MySearchWords=null;
    List<SuggestKeyword> search_SuggestWords = null;
    List<ListedItem> search_ItemList=null;
    private SearchResultAdapter search_SearchAdaptter = null;
    private SearchItemAdapter search_ItemAdapter;
    ImageButton search_ibSearch;
    final private int BOTTOMTYPE_MAIN=0;
    final private int BOTTOMTYPE_SEARCH=1;
    private String search_LastKeyword="";
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
        mCallback.chgBottombar(BasicInfo.FRAGMENT_SHOPPING,"Shopping");
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(search_bIsKeyboard)
        {
            closeKeyboard();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_shopping_bycat, container, false);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString("title");
            mFragmentType = args.getInt("type");
        }
        mUserDataLab = ShoppingDataLab.get(null);

        initMainView(rootView);
        initSearchView(rootView);

        mUserDataLab.loadMainData();
        doData_to_Adapter(rvTop, rvBot);

        GlobalVar globalVar = GlobalVar.get();
        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
        if((globalVar.getNetworkMode() == false) && (bWifi == false)){

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            String sMsg = String.format("서비스에 필요한 데이터를 가져올 수 없습니다.\n" +
                    "사용을 위해 모바일데이터를 활성화 해주세요.\n(메뉴 > 모바일데이터 선택)");
            alt_bld.setMessage(sMsg).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            alt_bld.show();
        }
        else if ((globalVar.getNetworkMode() == true) || (bWifi==true))
        {
            initHttpHandler();
            ConnectMonitorThread_shop thread = new ConnectMonitorThread_shop("", getContext(), mHandlerHttp, BasicInfo.THREAD_TYPE_HTTP_MAIN);
            thread.execute();
        }



        return rootView;
    }
    public void initMainView(View v)
    {
        rvTop = (RecyclerView) v.findViewById(R.id.top_recyclerview);
//        rvMid = (RecyclerView)rootView.findViewById(R.id.mid_recyclerview);
        rvBot = (RecyclerView) v.findViewById(R.id.bot_recyclerview);
        rvBot_searched = (RecyclerView) v.findViewById(R.id.bot_recyclerview_searched);

        svScroll = (ScrollView)v.findViewById(R.id.shipping_scrollView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setHasFixedSize(true);
        rvTop.setLayoutManager(layoutManager);
        tvMidTitle = (TextView)v.findViewById(R.id.shipping_midTitle);
        ivTitle = (ImageView)v.findViewById(R.id.shipping_titleImage);
        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBottomView(BOTTOMTYPE_MAIN);
            }
        });
        ivTop = (ImageButton)v.findViewById(R.id.shipping_btnTop);
        ivTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                svScroll.fullScroll(View.FOCUS_FORWARD);
            }
        });


        GridLayoutManager layoutMidManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        layoutMidManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBot.setHasFixedSize(true);
        rvBot.setLayoutManager(layoutMidManager);


        mMainLayout = (LinearLayout)v.findViewById(R.id.fragmentLayout);
//        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
//        Boolean b3g = NetworkInfo.Is3GAvailable(getActivity());
//        if (bWifi | b3g == false) {
//            mUserDataLab.loadMainData();
//        }
    }
    public void initSearchView(View v)
    {
        search_rvSearched = (RecyclerView)v.findViewById(R.id.search_recyclerview);
        search_rvSearched.setVisibility(View.GONE);

        search_etSearchWord = (WrapperEditText)v.findViewById(R.id.ev_edit_search);
        search_ibSearch = (ImageButton)v.findViewById(R.id.searchBar_ibSearch);
        search_ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_LastKeyword = search_etSearchWord.getText().toString();//.replaceAll("\\D", "");
                searchKeyword(search_LastKeyword);
            }
        });
        Bundle args = getArguments();
        String sTitle = args.getString("title");
        if(sTitle.indexOf("initsearch")<0)
            search_etSearchWord.setText(sTitle);
        setupEditText_search(search_etSearchWord);
        //addTestAdapter(rvResult);
        search_MySearchWords = new ArrayList<>();
        search_SuggestWords = new ArrayList<>();
        addMySearchedData();
        addSuggestData();
        initSearchList(search_rvSearched);
        initSearchItemList(rvBot_searched);
        search_SearchAdaptter.setSearchedItems(search_MySearchWords);
        search_SearchAdaptter.setSuggestItems(search_SuggestWords);
        initHttpHandler();
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
                    case BasicInfo.THREAD_TYPE_HTTP_SEARCH_KEYWORD:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정
                        changeBottomView(BOTTOMTYPE_SEARCH);
                        break;
                    case BasicInfo.THREAD_TYPE_HTTP_SUGGEST_KEYWORD:
                        break;
                }
            }
        };
    }
    public void changeBottomView(int nType)
    {
        switch(nType)
        {
            case BOTTOMTYPE_MAIN:
                rvBot_searched.setVisibility(View.GONE);
                rvBot.setVisibility(View.VISIBLE);
                tvMidTitle.setText("추천 상품");
                doData_to_Adapter(rvTop, rvBot);
                break;
            case BOTTOMTYPE_SEARCH:
                rvBot.setVisibility(View.GONE);
                rvBot_searched.setVisibility(View.VISIBLE);
                tvMidTitle.setText("검색 : "+search_LastKeyword);
                doSearchData_to_Adapter();
                break;
        }
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
    public void doSearchData_to_Adapter() {
        search_ItemList = mUserDataLab.getSearchedListData();
        SearchItemAdapter search_ItemAdapter = new SearchItemAdapter(getContext(), search_ItemList, R.layout.list_search_item);
        search_ItemAdapter.setCallback(mCallback);

        rvBot_searched.setAdapter(search_ItemAdapter);
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
    public void closeKeyboard()
    {
        //   /*
        try {
            search_etSearchWord.clearFocus();
            search_etSearchWord.setFocusableInTouchMode(false);
            search_etSearchWord.setFocusable(false);
            if(getActivity().getCurrentFocus().getWindowToken() != null) {
                search_etSearchWord.setInputType(InputType.TYPE_NULL);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                search_rvSearched.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.i("shipping", e.toString());
        }
//*/
        search_bIsKeyboard = false;
    }

    private void searchKeyword(final String sKeyword)
    {
        closeKeyboard();
        GlobalVar globalVar = GlobalVar.get();
        Boolean bWifi = NetworkInfo.IsWifiAvailable(getActivity());
        if((globalVar.getNetworkMode() == false) && (bWifi == false)){
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            String sMsg = String.format("서비스에 필요한 데이터를 가져올 수 없습니다.\n" +
                    "사용을 위해 모바일데이터를 활성화 해주세요.\n(메뉴 > 모바일데이터 선택)");
            alt_bld.setMessage(sMsg).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alt_bld.show();
        }
        else if ((globalVar.getNetworkMode() == true) || (bWifi==true))
        {
            doSearchItem(sKeyword);
        }

    }
    private void doSearchItem(String sValue)
    {
        ConnectMonitorThread_shop thread = new ConnectMonitorThread_shop("",getContext(),mHandlerHttp, BasicInfo.THREAD_TYPE_HTTP_SEARCH_KEYWORD);
        thread.setSearchKeyword(sValue);
        thread.execute();
        SuggestKeyword item = new SuggestKeyword(sValue,"");
        item.setType(1);
        mUserDataLab.addSearchedData(item);
        search_rvSearched.setVisibility(View.GONE);
        addMySearchedData();
        search_SearchAdaptter.notifyDataSetChanged();
    }
    public void addMySearchedData()
    {
        int nCount = mUserDataLab.getSearchedDataCount();
        if(nCount > 0)
            search_MySearchWords.clear();
        for(int i=nCount-1; i>=0;i--)
        {
            SuggestKeyword item = new SuggestKeyword(mUserDataLab.getSearchedData(i));
            search_MySearchWords.add(item);
        }
    }
    public void addSuggestData()
    {
        search_SuggestWords.clear();
        SuggestKeyword[] item=new SuggestKeyword[3];
        item[0]=new SuggestKeyword("IT","it");
        item[0].setType(0);
        item[1]=new SuggestKeyword("lego","lego");
        item[1].setType(0);
        item[2]=new SuggestKeyword("baby","baby");
        item[2].setType(0);


        for (int i = 0; i<3; i++)
            search_SuggestWords.add(item[i]);
    }
    public void initSearchList(RecyclerView v)
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        v.setHasFixedSize(true);
        v.setLayoutManager(layoutManager);
        search_SearchAdaptter = new SearchResultAdapter(getContext());
        search_SearchAdaptter.setCallback(this);
        v.setAdapter(search_SearchAdaptter);
    }
    public void initSearchItemList(RecyclerView v)
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        v.setHasFixedSize(true);
        v.setLayoutManager(layoutManager);
        search_ItemAdapter = new SearchItemAdapter(getContext(),search_ItemList,R.layout.list_search_item);
        search_ItemAdapter.setCallback(mCallback);
        v.setAdapter(search_ItemAdapter);
    }
    @Override
    public void selectKeyword(SuggestKeyword item) {
        //      Toast.makeText(getContext(), item.getKeyword(), Toast.LENGTH_SHORT).show();
        search_LastKeyword = item.getKeyword();
        search_etSearchWord.setText(search_LastKeyword);
        searchKeyword(search_LastKeyword);
        //closeKeyboard();
    }
    public void setupEditText_search(WrapperEditText etWidget)
    {

        etWidget.setFocusable(false);           // 클릭시 자동으로 키보드가 올라오는 기능 막기
        //etSearchWord.setFocusableInTouchMode(true);
        //etSearchWord.setFocusable(true);

        etWidget.setClickable(false);           // 클릭시 자동으로 키보드가 올라오는 기능 막기
        //rvResult.setVisibility(View.VISIBLE);
        //etWidget.setInputType(0);

        //etWidget.requestFocus();
        //  /*
        etWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //           rvResult.setVisibility(View.VISIBLE);
                search_etSearchWord.requestFocus();
                search_etSearchWord.setFocusableInTouchMode(true);
                search_etSearchWord.setFocusable(true);
                search_rvSearched.setVisibility(View.VISIBLE);
                search_etSearchWord.setText("");
                search_etSearchWord.setInputType(InputType.TYPE_CLASS_TEXT);

                //       mBudget_etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                search_etSearchWord.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                search_bIsKeyboard = true;

            }
        });
        //     /*
        etWidget.setKeyImeChangeListener(new WrapperEditText.KeyImeChange() {
            @Override
            public boolean onKeyIme(int keyCode, KeyEvent event) {
                if (search_bIsKeyboard == true) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                                closeKeyboard();//bIsKeyboard = false;// 상위로 메시지 넘김
                        }

                    }
                    return true;
                }
                return false;
            }
        });
        //     */
        etWidget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etWidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:        // 완료
                    case EditorInfo.IME_ACTION_GO:          // 이동
                    case EditorInfo.IME_ACTION_NEXT:      // 다음
                    case EditorInfo.IME_ACTION_NONE:     // 엔터
                    case EditorInfo.IME_ACTION_SEARCH:  // 돋보기
                    case EditorInfo.IME_ACTION_SEND:    // 전송
                    case EditorInfo.IME_ACTION_UNSPECIFIED:     // 확인
                        search_LastKeyword = search_etSearchWord.getText().toString();//.replaceAll("\\D", "");
                        searchKeyword(search_LastKeyword);
                        break;
                }
                return false;
            }
        });
    }
    View.OnTouchListener touchMainListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(search_bIsKeyboard)
                {
                    closeKeyboard();
                }
            }
            return false;
        }

    };

}