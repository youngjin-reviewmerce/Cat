package com.reviewmerce.exchange;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.HttpGet;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.ExchangeData;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.publicClass.ExchangeDataLab;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by onebuy on 2015-07-30.
 */
public class ConnectMonitorThread_v2 extends Thread {

    GlobalVar mGlobalVar=null;
    NationDataLab mNationLab = null;
    String urlStr;
    Handler mParentHandler;
    Context mParentContext = null;
    //int m_nDays = 0;
    int m_nPort = 5000; // 5050 : 개발용 5000 실서버
    String mCurrency = "USD";
    String mBank = "ALL";
    String mMilestone = "M11";//"M8";
    private int mProcType = BasicInfo.TYPE_MONITOR;
    public ConnectMonitorThread_v2(String inStr, Context ctx, Handler handler, int nProcType) {
        urlStr = inStr;
        mParentContext = ctx;
        mParentHandler = handler;
        mProcType = nProcType;
        mGlobalVar = GlobalVar.get();
        mNationLab = NationDataLab.get(null);
    }

    public void run() {
        long t = System.currentTimeMillis();
        switch (mProcType) {

            case BasicInfo.TYPE_MONITOR:
                getDayProc();
                break;
            case BasicInfo.TYPE_BANK:
                getBankProc();
                break;
            case BasicInfo.TYPE_CREATEDB:
                createDB();
                break;
            case BasicInfo.TYPE_PURSE:
                getLiveProc();
                break;
            case BasicInfo.TYPE_MONITOR_LIVE:
                getLiveProc();
                break;
            default:
                break;

        }
        long t2 = System.currentTimeMillis();
        String s = String.format("%d ms",t2-t);
        Log.i("Network : ",s);
    }
    public void setProc(int nType)
    {
        mProcType = nType;
    }
    private void getDayProc()
    {

        try {
            ExchangeDataLab DataLab = ExchangeDataLab.get(null);
            String sRequest ="https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com" + "/" +mMilestone+ "/currency";
            String sIdx = "";
            String sResponse = "";
            sIdx = DataLab.getRequsetDate();
            if(sIdx.length()<=0)
                sIdx = DataLab.getLastIndex();

            JSONObject json = new JSONObject();
            json.put("operation", "get_recent_currency");
            json.put("currency", mCurrency);
            json.put("idx", sIdx);

            sResponse = request_post_get_response(sRequest, json);
            if(sResponse.length()>0)
            {

                sResponse = sResponse.replaceAll("\\\\", "");
                sResponse =  sResponse.substring(1,sResponse.length()-1);
                DataLab.getDaytoDataLab(sResponse, mCurrency);
            }
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR, "refresh"));
        }
        catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR, "refresh"));
            ex.printStackTrace();
        }
    }
    private void getLiveProc()
    {
        try {
        String sRequest ="https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com" + "/" +mMilestone+ "/currency";
        String sResponse = "";
        JSONObject json = new JSONObject();
        json.put("operation", "get_live_currency");
        json.put("currency", "ALL");
        json.put("bank", "KEB");
        sResponse = request_post_get_response(sRequest, json);
        if(sResponse.length()>0)
        {
            sResponse = sResponse.replaceAll("\\\\", "");
            sResponse =  sResponse.substring(1,sResponse.length()-1);
            LiveDataLab liveLab = LiveDataLab.get(null);
            liveLab.net_procPursetoDatalab(sResponse);
        }
        mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR_LIVE, "refresh"));
    }
    catch (Exception ex) {
        mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR_LIVE, "refresh"));
        ex.printStackTrace();
    }
}
    private void getBankProc()
    {
        BankDataLab DataLab = BankDataLab.get(null);
        try {
            String sRequest ="https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com" + "/" +mMilestone+ "/currency";
            String sOperator = "get_live_currency";
            String sCurrency = mCurrency;
            String sBank = mBank;
//            HttpPost post = null;

            JSONObject json = new JSONObject();
            json.put("operation", "get_live_currency");
            json.put("currency", mCurrency);
            json.put("bank", mBank);
            String sResponse = "";
            sResponse = request_post_get_response(sRequest, json);
            if(sResponse.length()>0)
            {
                sResponse = sResponse.replaceAll("\\\\", "");
                sResponse =  sResponse.substring(1,sResponse.length()-1);
                DataLab.jsonToData(sResponse);
            }

            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_BANK, "refresh"));
        }
        catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_BANK, "refresh"));
            ex.printStackTrace();
        }
    }

    private void sendCreateDb(String sCurrency)
    {
        try {
            ExchangeDataLab DataLab = ExchangeDataLab.get(null);
            String sRequest ="https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com" + "/" +mMilestone+ "/currency";
            String sIdx = "";
            String sResponse = "";
            sIdx = DataLab.getRequsetDate();
            if(sIdx.length()<=0)
                sIdx = DataLab.getLastIndex();

            JSONObject json = new JSONObject();
            json.put("operation", "get_recent_currency");
            json.put("currency", sCurrency);
            json.put("idx", "0");

            sResponse = request_post_get_response(sRequest, json);
            if(sResponse.length()>0)
            {
                sResponse = sResponse.replaceAll("\\\\", "");
                sResponse =  sResponse.substring(1,sResponse.length()-1);
                createDBData(sResponse, sCurrency);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createDB()
    {
        String szCurreny;
        List<String> nation_list = mNationLab.getAllNationDataList();
        try
        {
            for(String s : nation_list)
            {
                sendCreateDb(s);
            }
        }catch (Exception ex) {
        mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR, "refresh"));
        ex.printStackTrace();
    }

    }

    public void setCurrency(String currency)
    {
        mCurrency = currency;
    }



    private String request_post_get_response(String urlStr, JSONObject json) throws Exception {
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



    public void createDBData(String szResponse, String szCurrency) {
        JSONObject json = null;
        ExchangeDataLab ExchangeLab = ExchangeDataLab.get(null);
        ArrayList<ExchangeData> AddExchangeDataArray = new ArrayList<ExchangeData>();
        try {
            json = new JSONObject(szResponse);
            JSONArray jsonArray = json.getJSONArray("exchange_rates");
            JSONObject j;
            String sIndex, sDate, sTime, sBasicRates;

            for (int i = 0; i < jsonArray.length(); i++) {
                j = jsonArray.getJSONObject(i);
                try {
                    int nIndex = j.getInt(BasicInfo.json_MONI_INDEX);
                    sIndex = String.valueOf(nIndex);
                }catch (Exception e) {
                    continue;
                }
                try {
                    int nDate = j.getInt(BasicInfo.json_MONI_DATE);
                    sDate = String.valueOf(nDate);
                } catch (Exception e) {
                    //  sDate = "19800805";
                    continue;
                }
                try {
                    sTime = j.getString(BasicInfo.json_MONI_TIME);
                } catch (Exception e) {
                    sTime = "000000";
                }

                try {
                    sBasicRates = j.getString(BasicInfo.json_MONI_BASICRATES);
                } catch (Exception e) {
                    sBasicRates = "0";
            //        continue;
                }
                try {
                    //if(Double.valueOf(sBasicRates)>=.0f) {
                    ExchangeData ex = new ExchangeData(sIndex, mNationLab.getCurrencyCodeInEng(), sDate, sBasicRates, sTime);
                    AddExchangeDataArray.add(ex);
                    //}
                } catch (Exception e) {
                    Log.e("http_Get",e.toString());
                    //sBasicRates = "0";
                    continue;
                }
/*


                try {
                    sTime = j.getString(BasicInfo.json_MONI_TIME);
                }catch (Exception e) {
                    sTime = "000000";
                }
                try {
                    sBasicRates = j.getString(BasicInfo.json_MONI_BASICRATES);
                }catch (Exception e) {
//                    sBasicRates = "0000";
                    continue;
                }
                if(Double.valueOf(sBasicRates)>.0f) {
                    ExchangeData ex = new ExchangeData(sIndex, sCurrency, sDate, sBasicRates, sTime);
//                if (ExchangeLab.updateData(ex) > 0)
                    AddExchangeDataArray.add(ex);
                }
 */

            }
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }

        if (AddExchangeDataArray.size() > 0) {
            ExchangeLab.excuteSQL_addData_Currency(AddExchangeDataArray, szCurrency);
        }
    }

}

