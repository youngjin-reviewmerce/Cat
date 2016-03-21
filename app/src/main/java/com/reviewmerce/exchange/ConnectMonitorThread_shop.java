package com.reviewmerce.exchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.reviewmerce.exchange.commonData.apiData.ListedItem;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by onebuy on 2015-07-30.
 */
public class ConnectMonitorThread_shop extends AsyncTask {

    // http://api.reviewmerce.com/v1/ui/main
    // http://api.reviewmerce.com/v1/ui/top?current_ui_ver=
    // http://api.reviewmerce.com/v1/ui/mid?current_ui_ver=
    // http://api.reviewmerce.com/v1/ui/bot?current_ui_ver=
    // http://api.reviewmerce.com/v1/search/query?keywords=
    // http://api.reviewmerce.com/v1/search/suggest?keywords=


    String urlStr;
    Handler mParentHandler;
    Context mParentContext = null;
    //int m_nDays = 0;
    int m_nPort = 5000; // 5050 : 개발용 5000 실서버
    String mHostUrl = "https://5fhdvexrml.execute-api.ap-northeast-1.amazonaws.com"; // http://api.reviewmerce.com";
    String mVer = "v1";
    String mSearchKeyword = "";
    private ShoppingDataLab mUserDataLab;
    private Bitmap bmImg;
    private int mType=0;
    private int mProcType = BasicInfo.HTTP_TYPE_FACEBOOK_IDINFO;
    public ConnectMonitorThread_shop(String inStr, Context ctx, Handler handler, int nProcType) {
        urlStr = inStr;
        mParentContext = ctx;
        mParentHandler = handler;
        mProcType = nProcType;

        mUserDataLab = ShoppingDataLab.get(null);
        mType = nProcType;

    }
    public void setSearchKeyword(String sVal)
    {
        mSearchKeyword = sVal;
    }
    public void run() {
        long t = System.currentTimeMillis();
        switch (mProcType) {

            case BasicInfo.THREAD_TYPE_HTTP_SEARCH_KEYWORD:
                procAPI_SearchKeyword();
                break;
            case BasicInfo.THREAD_TYPE_HTTP_SUGGEST_KEYWORD:
                procAPI_SuggestKeyword();
                break;
            case BasicInfo.THREAD_TYPE_HTTP_MAIN:
                procAPI_ui_Main();
                break;
            case BasicInfo.THREAD_TYPE_HTTP_SEARCH_CATEGORY_ITEM1:  // 화장품
            case BasicInfo.THREAD_TYPE_HTTP_SEARCH_CATEGORY_ITEM2:  // 레고
            case BasicInfo.THREAD_TYPE_HTTP_SEARCH_CATEGORY_ITEM3:  // 타블릿
                procAPI_SearchKeyword();
            default:
                break;

        }
        long t2 = System.currentTimeMillis();
        String s = String.format("%d ms", t2 - t);
        Log.i("Network : ", s);
    }
    private boolean procAPI_ui_Main()
        {
            https://5fhdvexrml.execute-api.ap-northeast-1.amazonaws.com/v1/ui/main?current_ui_ver=t01m02
        try {
            String sRequest = mHostUrl + "/" + mVer + "/";
            String sType = "ui";
            String sOperator = "main";
            sRequest = sRequest + sType + "/" + sOperator; //+ "?current_ui_ver=" + mSearchKeyword;

            String sResponse = "";
            sResponse = doHttp_get(sRequest);

            if(sResponse.length()>0)
            {
//                sResponse = sResponse.replaceAll("\\\\", "");
//                sResponse = sResponse.substring(1,sResponse.length()-1);    // 문자열(json)
                mUserDataLab.change_MainImageLink_jsonString_to_data(sResponse);    // 데이터(dataLab)
                mUserDataLab.proc_localImg_ImageLink();          //
//                Thread.sleep(20000);
                mUserDataLab.saveMainData();
            }
            else    // 더미 데이터 생성
            {
                mUserDataLab.initDummyData_main();
                mUserDataLab.proc_localImg_ImageLink();          //
            }
        //    mUserDataLab.setLastSearchKeyword(mSearchKeyword);

            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.THREAD_TYPE_HTTP_MAIN,true));
            return true;
        }
        catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.THREAD_TYPE_HTTP_MAIN, false));
            ex.printStackTrace();
            return false;
        }
    }
    private boolean procAPI_SearchKeyword()
    {
        try {
            String sRequest = mHostUrl + "/" + mVer + "/";
            String sType = "search";
            String sOperator = "query";
            sRequest = sRequest + sType + "/" + sOperator + "?keywords=" + mSearchKeyword;

            String sResponse = "";
            sResponse = doHttp_get(sRequest);
            if(sResponse.length()>0)
            {
//                sResponse = sResponse.replaceAll("\\\\", "");
//                sResponse = sResponse.substring(1,sResponse.length()-1);
                mUserDataLab.change_SearchListedItem_jsonString_to_data(sResponse);
 //               getSearchItem_Url_in_DataSet();
            }
            else    // 더미 데이터 생성
            {
                mUserDataLab.initDummyData_searchKeyword();
            }
            mUserDataLab.setLastSearchKeyword(mSearchKeyword);

            mParentHandler.sendMessage(Message.obtain(mParentHandler, mType, "done"));
            return true;
        }
        catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, mType, "fail"));
            ex.printStackTrace();
            return false;
        }
    }
    private boolean procAPI_SuggestKeyword()
    {
        try {
            String sRequest = mHostUrl + "/" + mVer + "/";
            String sType = "search";
            String sOperator = "query";
            sRequest = sRequest + sType + "/" + sOperator + "?keywords=" + mSearchKeyword;

//            JSONObject json = new JSONObject();
//            json.put("operation", "get_live_currency");

            String sResponse = "";
            sResponse = doHttp_get(sRequest);
            if(sResponse.length()>0)
            {
                sResponse = sResponse.replaceAll("\\\\", "");
                sResponse =  sResponse.substring(1,sResponse.length()-1);
            }
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.THREAD_TYPE_HTTP_SUGGEST_KEYWORD, "refresh"));
            return true;
        }
        catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.THREAD_TYPE_HTTP_SUGGEST_KEYWORD, "refresh"));
            ex.printStackTrace();
            return false;
        }
    }
    private String doHttp_post(String urlStr, JSONObject json) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String response="";

        //HttpPost httpPost = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(4 * 1000);
            conn.setReadTimeout(6 * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            os = conn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
            }
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
        return response;
    }
    private String doHttp_get(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String response="";

        //HttpPost httpPost = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(1 * 1000);
            conn.setReadTimeout(1 * 1000);
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Cache-Control", "no-cache");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            os = conn.getOutputStream();
//            os.write(json.toString().getBytes());
//            os.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
            }
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
        return response;
    }

    public Bitmap getUrltoImageBitmap(String urls)
    {
        // TODO Auto-generated method stub
        try{
            URL myFileUrl = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();

            bmImg = BitmapFactory.decodeStream(is);


        }catch(IOException e){
            e.printStackTrace();
        }
        return bmImg;
    }

    public void getSearchItem_Url_in_DataSet()
    {
        ArrayList<ListedItem> searchList = null;
        searchList = mUserDataLab.getSearchedListData();
        for(ListedItem item : searchList)
        {
            item.makeBitmapFile_inURL();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        run();
        return null;
    }
}

