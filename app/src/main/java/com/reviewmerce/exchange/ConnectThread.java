package com.reviewmerce.exchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.HttpGet;
import com.reviewmerce.exchange.innerProc.ExchangeData;
import com.reviewmerce.exchange.innerProc.ExchangeDataLab;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by onebuy on 2015-07-30.
 */
public class ConnectThread extends Thread {
    String urlStr;
    Handler mParentHandler;
    Context mParentContext = null;
    int m_nDays = 0;
    int m_nPort = 5050;
    String mCurrency = "USD";
    String mBank = "ALL";
    private int mProcType=0;
    public ConnectThread(String inStr, Context ctx, Handler handler) {
        urlStr = inStr;
        mParentContext = ctx;
        mParentHandler = handler;
        ExchangeDataLab DataLab = ExchangeDataLab.get(ctx);
        m_nDays = DataLab.getRequsetDate();
    }

    public void run() {


        switch (mProcType) {
            case 0:
                procNetGet();
                break;
            default:
                break;
        }
    }
    public void setProc(int nType)
    {
        mProcType = nType;
    }
    private void procNetGet()
    {
        try {
            String output = request("http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +"M3"+ "/" + mCurrency + "/" + mBank + "/RECENT_DAYS/"+ Integer.toString(m_nDays));
            output = requestTime("http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +"M3"+ "/" + mCurrency + "/" + mBank +  "/LIVE");
    //            String output = request("http://naver.com");
    //            output = requestTime("http://naver.com");
            mParentHandler.sendMessage(Message.obtain(mParentHandler, 1, "refresh"));
        } catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, 1, "refresh"));
            ex.printStackTrace();
        }
    }
    public void setCurrency(String currency)
    {
        mCurrency = currency;
    }

    private String requestTime(String urlStr) throws Exception {
        StringBuilder output = new StringBuilder();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlStr);
            client.getParams().setParameter("http.protocol.expect-continue", false);
            client.getParams().setParameter("http.connection.timeout", 2000);
            client.getParams().setParameter("http.socket.timeout", 2000);

            Log.d("SampleHTTPClient", "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpGet);
            Log.d("SampleHTTPClient", "\nResponse from HttpClient ...");

            InputStream instream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            String line = null;
            String szResponse = "";
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                } else
                    szResponse = szResponse + line;
                output.append(line + "\n");
            }
            //     getJson(szResponse);
            getTimetoJson(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            setLastTime_atException();
            Log.e("SampleHTTPClient", "Exception in processing response.", ex);
        }

        return output.toString();
    }

    private String request(String urlStr) throws Exception {
        StringBuilder output = new StringBuilder();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlStr);
            client.getParams().setParameter("http.protocol.expect-continue", false);
            client.getParams().setParameter("http.connection.timeout", 15000);
            client.getParams().setParameter("http.socket.timeout", 15000);
            //       List<NameValuePair> fields = new ArrayList<NameValuePair>(1);
            //       fields.add(new BasicNameValuePair("data", "test"));
            //       httppost.setEntity(new UrlEncodedFormEntity(fields));

            Log.d("SampleHTTPClient", "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpGet);
            Log.d("SampleHTTPClient", "\nResponse from HttpClient ...");

            InputStream instream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            String line = null;
            String szResponse = "";
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                } else
                    szResponse = szResponse + line;
                output.append(line + "\n");
            }
            getDaytoJson(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e("SampleHTTPClient", "Exception in processing response.", ex);


        }

        return output.toString();
    }

    public JSONObject getDaytoJson(String sResult) {
        JSONObject json = null;
        ExchangeDataLab ExchangeLab = ExchangeDataLab.get(mParentContext);
        ArrayList<ExchangeData> AddExchangeDataArray = new ArrayList<ExchangeData>();
        try {
            json = new JSONObject(sResult);
            JSONArray jsonArray = json.getJSONArray("exchange_rates");
            JSONObject j;

            for (int i = 0; i < jsonArray.length(); i++) {
                if (ExchangeLab.updateData(new ExchangeData(jsonArray.getJSONObject(i))) > 0)
                    AddExchangeDataArray.add(new ExchangeData(jsonArray.getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }

        if (AddExchangeDataArray.size() > 0) {

            ExchangeLab.excuteSQL_addData(AddExchangeDataArray);
        }
        return json;
    }

    public JSONObject getTimetoJson(String sResult) {
        JSONObject json = null;
        ExchangeDataLab ExchangeLab = ExchangeDataLab.get(mParentContext);
        String sTime, sDate, sBasicRates;
        ArrayList<ExchangeData> AddExchangeDataArray = new ArrayList<ExchangeData>();
        try {
            json = new JSONObject(sResult);
            JSONArray jsonArray = json.getJSONArray("exchange_rates");
            JSONObject j;
            sDate = jsonArray.getJSONObject(jsonArray.length() - 1).getString(BasicInfo.json_MONI_DATE);
            sTime = jsonArray.getJSONObject(jsonArray.length() - 1).getString(BasicInfo.json_MONI_TIME);
            sBasicRates = jsonArray.getJSONObject(jsonArray.length() - 1).getString(BasicInfo.json_MONI_BASICRATES);
            double fBasicRates = Double.valueOf(sBasicRates);
            SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date dateTemp = new Date();
            try {
                dateTemp = formatter.parse(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExchangeLab.setLastDate(fm.format(dateTemp));
            ExchangeLab.setLastPrice(Double.toString(fBasicRates));
            ExchangeLab.setLastTime(sTime);
        //    ExchangeLab.updateData(sDate,fBasicRates,sTime );

        } catch (Exception e) {
            setLastTime_atException();
            Log.e("HTTP", e.toString());
        }

        return json;
    }

    public String getUrlBytes(String sUrl) throws IOException {
        URL url = new URL(sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
        }
        byte[] rtnBytes;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            rtnBytes = out.toByteArray();

        } finally {
            connection.disconnect();
        }
        return new String(rtnBytes);
    }

    public void setLastTime_atException() {
        ExchangeDataLab exchangeLab = ExchangeDataLab.get(mParentContext);
        exchangeLab.setLastDate(exchangeLab.getLastLoadedExchangeData().getDate());
        exchangeLab.setLastPrice(Double.toString(exchangeLab.getLastLoadedExchangeData().getBasicRates()));
        exchangeLab.setLastTime("00:00:00");
    }
}

