package com.reviewmerce.exchange;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.HttpGet;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.PurseData;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.commonData.ExchangeData;
import com.reviewmerce.exchange.publicClass.ExchangeDataLab;
import com.reviewmerce.exchange.publicClass.LiveDataLab;
import com.reviewmerce.exchange.publicClass.NationDataLab;
import com.reviewmerce.exchange.publicClass.PurseDataLab;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by onebuy on 2015-07-30.
 */
public class ConnectMonitorThread_v1 extends Thread {

    GlobalVar mGlobalVar=null;
    NationDataLab mNationLab = null;
    String urlStr;
    Handler mParentHandler;
    Context mParentContext = null;
    //int m_nDays = 0;
    int m_nPort = 5000; // 5050 : 개발용 5000 실서버
    String mCurrency = "USD";
    String mBank = "ALL";
    String mMilestone = "M8";//"M8";
    private int mProcType = BasicInfo.TYPE_MONITOR;
    public ConnectMonitorThread_v1(String inStr, Context ctx, Handler handler, int nProcType) {
        urlStr = inStr;
        mParentContext = ctx;
        mParentHandler = handler;
        mProcType = nProcType;
        mGlobalVar = GlobalVar.get();
        mNationLab = NationDataLab.get(null);
    }

    public void run() {
        switch (mProcType) {
            case BasicInfo.TYPE_MONITOR:
                procNetGet();
                break;
            case BasicInfo.TYPE_BANK:
                getBankProc();
                break;
            case BasicInfo.TYPE_CREATEDB:
                createDB();
                break;
            case BasicInfo.TYPE_PURSE:
                getPurseProc();
                break;
            case BasicInfo.TYPE_MONITOR_LIVE:
                procLiveGet();
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
        ExchangeDataLab DataLab = ExchangeDataLab.get(null);
        try {
            String sBlankIndex = DataLab.getRequsetDate();
            String szRequest = "";
            if(sBlankIndex.length() > 0) {
                szRequest = "http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" + mMilestone + "/" + mCurrency + "/" + mBank + "/GET_IDXS/" + sBlankIndex;
                Log.i(BasicInfo.TAG,szRequest);
                String output = requestDay(szRequest);
            }
            sBlankIndex = DataLab.getLastIndex();
            szRequest = "http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" + mMilestone + "/" + mCurrency + "/" + mBank + "/RECENT_IDX/" + sBlankIndex;
            Log.i(BasicInfo.TAG,szRequest);
            String output = requestDay(szRequest);
            //procLiveGet();
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR, "refresh"));
        } catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR, "refresh"));
            ex.printStackTrace();
        }
    }
    private void procLiveGet()
    {
        try {
            String szRequest = "";
            szRequest ="http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +mMilestone+ "/ALL/KEB/LIVE";
//            szRequest ="http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +mMilestone+ "/" + mCurrency + "/" + mBank +  "/LIVE";
            Log.i(BasicInfo.TAG, szRequest);
            String output;
//            if(mMilestone.indexOf("M11")>=0)
                output = requestTime_Post(szRequest);
//            else
//                output = requestTime(szRequest);

            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR_LIVE, "refresh"));
        }
        catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR_LIVE, "refresh"));
            ex.printStackTrace();
        }
    }
    private void getPurseProc()
    {
        try {
            String szRequest;
            szRequest ="http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +mMilestone+ "/ALL/KEB/LIVE";
            Log.i(BasicInfo.TAG,szRequest);
            String output = requestPurse(szRequest);
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_PURSE, "refresh"));
        } catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_PURSE, "refresh"));
            ex.printStackTrace();
        }
    }
    private void sendCreateDb(String sCurrency)
    {
        String szRequest = "http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" + mMilestone + "/" + sCurrency + "/" + mBank + "/RECENT_IDX/" + "0";
        createDBData(szRequest,sCurrency);
    }

    private void createDB()
    {
        String szCurreny;
        try {
            szCurreny = "USD";
            sendCreateDb(szCurreny);
            szCurreny = "EUR";
            sendCreateDb(szCurreny);
            szCurreny = "CNY";
            sendCreateDb(szCurreny);
            szCurreny = "JPY";
            sendCreateDb(szCurreny);
            szCurreny = "GBP";
            sendCreateDb(szCurreny);
            szCurreny = "HKD";
            sendCreateDb(szCurreny);
            szCurreny = "THB";
            sendCreateDb(szCurreny);
            szCurreny = "IDR";
            sendCreateDb(szCurreny);
            szCurreny = "PHP";
            sendCreateDb(szCurreny);
            szCurreny = "SGD";
            sendCreateDb(szCurreny);
            szCurreny = "MYR";
            sendCreateDb(szCurreny);
            szCurreny = "AUD";
            sendCreateDb(szCurreny);
            szCurreny = "VND";
            sendCreateDb(szCurreny);
            szCurreny = "TWD";
            sendCreateDb(szCurreny);

            sendCreateDb("KRW");
            sendCreateDb("CHF");
            sendCreateDb("CAD");
            sendCreateDb("NZD");
            sendCreateDb("SEK");
            sendCreateDb("DKK");
            sendCreateDb("NOK");
            sendCreateDb("SAR");
            sendCreateDb("KWD");
            sendCreateDb("BHD");
            sendCreateDb("AED");
            sendCreateDb("ZAR");
            sendCreateDb("RUB");
            sendCreateDb("HUF");
            sendCreateDb("PLN");
            sendCreateDb("TRY");
            sendCreateDb("KZT");
            sendCreateDb("CZK");
            sendCreateDb("QAR");
            sendCreateDb("MNT");
            sendCreateDb("INR");
            sendCreateDb("PKR");
            sendCreateDb("BDT");
            sendCreateDb("EGP");
            sendCreateDb("BRL");
            sendCreateDb("BND");
            sendCreateDb("ILS");
            sendCreateDb("JOD");
            sendCreateDb("OMR");
            sendCreateDb("CLP");

        }catch (Exception ex) {
        mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_MONITOR, "refresh"));
        ex.printStackTrace();
    }

    }
    private void getBankProc()
    {
        try {
            String szRequest ="http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +mMilestone+ "/" + mCurrency + "/" + mBank +  "/LIVE";
            Log.i(BasicInfo.TAG,szRequest);
            //String output = request("http://api.reviewmerce.com:" + Integer.toString(m_nPort) + "/" +"M2"+ "/" + mCurrency + "/" + mBank + "/RECENT_DAYS/"+ Integer.toString(m_nDays));
            String output = requestBank(szRequest);
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_BANK, "refresh"));
        } catch (Exception ex) {
            mParentHandler.sendMessage(Message.obtain(mParentHandler, BasicInfo.TYPE_BANK, "refresh"));
            ex.printStackTrace();
        }
    }
    public void setCurrency(String currency)
    {
        mCurrency = currency;
    }

    private String requestTime(String urlStr) throws Exception {
        StringBuilder output = new StringBuilder();
        LiveDataLab LiveLab = LiveDataLab.get(null);
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
            LiveLab.net_procPursetoDatalab(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }

        return output.toString();
    }
    private String requestTime_Post(String urlStr) throws Exception {
        StringBuilder output = new StringBuilder();
        LiveDataLab LiveLab = LiveDataLab.get(null);
        JSONObject json = new JSONObject();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(
                    "https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com/M11/currency");
            json.put("operation", "get_live_currency");
            json.put("currency", "MYR");
            json.put("bank", "ALL");
            StringEntity se = new StringEntity("JSON: "+ json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //Encoding POST data
            try {
                String msg = json.toString();
                httpPost.setEntity(new StringEntity(msg, "UTF8"));
                httpPost.setHeader("Content-type", "application/json");

            } catch (Exception e)
            {
                e.printStackTrace();
            }


//            /*
            client.getParams().setParameter("http.protocol.expect-continue", false);
            client.getParams().setParameter("http.connection.timeout", 2000);
            client.getParams().setParameter("http.socket.timeout", 2000);
//*/
            Log.d("SampleHTTPClient", "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpPost);
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
            szResponse = szResponse.replaceAll("\\\\", "");
            szResponse =  szResponse.substring(1,szResponse.length()-1);
            //szResponse = szResponse.substring(0,szResponse.length()-1);
            LiveLab.net_procPursetoDatalab(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }

        return output.toString();
    }

    private String requestDay(String urlStr) throws Exception {
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

            Log.d(BasicInfo.TAG, "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpGet);
            Log.d(BasicInfo.TAG, "\nResponse from HttpClient ...");

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
            getDaytoDataLab(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
        return output.toString();
    }
    private String requestBank(String urlStr) throws Exception {
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

            Log.d(BasicInfo.TAG, "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpGet);
            Log.d(BasicInfo.TAG, "\nResponse from HttpClient ...");

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
            // getDaytoJson(szResponse);
            procBanktoDatalab(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);


        }

        return output.toString();
    }
    private String requestPurse(String urlStr) throws Exception {
        StringBuilder output = new StringBuilder();
        LiveDataLab LiveLab = LiveDataLab.get(null);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlStr);
            client.getParams().setParameter("http.protocol.expect-continue", false);
            client.getParams().setParameter("http.connection.timeout", 15000);
            client.getParams().setParameter("http.socket.timeout", 15000);
            //       List<NameValuePair> fields = new ArrayList<NameValuePair>(1);
            //       fields.add(new BasicNameValuePair("data", "test"));
            //       httppost.setEntity(new UrlEncodedFormEntity(fields));

            Log.d(BasicInfo.TAG, "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpGet);
            Log.d(BasicInfo.TAG, "\nResponse from HttpClient ...");

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

            LiveLab.net_procPursetoDatalab(szResponse);
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
        return output.toString();
    }

    public void procBanktoDatalab(String sResult)
    {
        JSONObject json = null;
        BankDataLab BankLab = BankDataLab.get(null);
        ArrayList<BankData> AddBankDataArray = new ArrayList<BankData>();
        String sDate, sTime, sBank, sBasicRates, sCashBuying, sCashSelling,
                sStransferSending, sStransferReceiving, sCheckBuying, sCheckSelling;

        try {
            json = new JSONObject(sResult);
            JSONArray jsonArray = json.getJSONArray("exchange_rates");
            JSONObject j;

            for (int i = 0; i < jsonArray.length(); i++) {
                j = jsonArray.getJSONObject(i);
                try {
                    sDate = j.getString(BasicInfo.json_BANK_UPDATEDATE);      //key
                }catch (Exception e) {
                    continue;
                }
                try {
                    sTime = j.getString(BasicInfo.json_BANK_UPDATETIME);
                }catch (Exception e) {
                    sTime = "000000";
                }
                try {
                    sBank = j.getString(BasicInfo.json_BANK_BANK);        // 필수
                }catch (Exception e) {
                    continue;
                }

                try {
                    sBasicRates = j.getString(BasicInfo.json_BANK_BASICRATES);
                }catch (Exception e) {
                    sBasicRates = "0";
                }

                try {
                    sCashBuying = j.getString(BasicInfo.json_BANK_CASHBUYING);
                }catch (Exception e) {
                    sCashBuying = "0";
                }

                try {
                    sCashSelling = j.getString(BasicInfo.json_BANK_CASHSELLING);
                }catch (Exception e) {
                    sCashSelling = "0";
                }

                try {
                    sStransferSending= j.getString(BasicInfo.json_BANK_STRANSFERSENDING);
                }catch (Exception e) {
                    sStransferSending = "0";
                }

                try {
                    sStransferReceiving = j.getString(BasicInfo.json_BANK_STRANSFERRECEIVING);
                }catch (Exception e) {
                    sStransferReceiving = "0";
                }


                try {
                    sCheckBuying= j.getString(BasicInfo.json_BANK_CHECKBUYING);
                }catch (Exception e) {
                    sCheckBuying = "0";
                }

                try {
                    sCheckSelling = j.getString(BasicInfo.json_BANK_CHECKSELLING);
                }catch (Exception e) {
                    sCheckSelling = "0";
                }
                BankData bd = new BankData(i,sDate,sTime,sBank,sBasicRates,
                        sCashBuying,sCashSelling,sStransferSending,sStransferReceiving,sCheckBuying,sCheckSelling);

                BankLab.addData(bd);
                AddBankDataArray.add(bd);

            } // for
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }
    }
    public JSONObject getDaytoDataLab(String sResult) {
        JSONObject json = null;
        ExchangeDataLab ExchangeLab = ExchangeDataLab.get(null);
        ArrayList<ExchangeData> AddExchangeDataArray = new ArrayList<ExchangeData>();
        try {
            json = new JSONObject(sResult);
            JSONArray jsonArray = json.getJSONArray("exchange_rates");
            JSONObject j;
            String sIndex, sDate,sTime,sBasicRates;

            for (int i = 0; i < jsonArray.length(); i++) {
                j = jsonArray.getJSONObject(i);
                try {
                    sIndex = j.getString(BasicInfo.json_MONI_INDEX);
                }catch (Exception e) {
                    continue;
                }
                try {
                    sDate = j.getString(BasicInfo.json_MONI_DATE);
                }catch (Exception e) {
                    continue;
                }
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
                    ExchangeData ex = new ExchangeData(sIndex, mCurrency, sDate, sBasicRates, sTime);
//                if (ExchangeLab.updateData(ex) > 0)
                    AddExchangeDataArray.add(ex);
                }
            }
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }

        if (AddExchangeDataArray.size() > 0) {

            ExchangeLab.setAddExchangeData(AddExchangeDataArray,mCurrency);
        }
        return json;
    }
    public void createDBData(String urlStr, String szCurrency) {
        StringBuilder output = new StringBuilder();
        JSONObject json = null;
        ExchangeDataLab ExchangeLab = ExchangeDataLab.get(null);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlStr);
            client.getParams().setParameter("http.protocol.expect-continue", false);
            client.getParams().setParameter("http.connection.timeout", 15000);
            client.getParams().setParameter("http.socket.timeout", 15000);
            //       List<NameValuePair> fields = new ArrayList<NameValuePair>(1);
            //       fields.add(new BasicNameValuePair("data", "test"));setLastDate

            Log.d(BasicInfo.TAG, "\nRequest using HttpClient ...");
            HttpResponse response = client.execute(httpGet);
            Log.d(BasicInfo.TAG, "\nResponse from HttpClient ...");

            Log.i(BasicInfo.TAG,urlStr);
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
            ArrayList<ExchangeData> AddExchangeDataArray = new ArrayList<ExchangeData>();
            try {
                json = new JSONObject(szResponse);
                JSONArray jsonArray = json.getJSONArray("exchange_rates");
                JSONObject j;
                String sIndex, sDate, sTime, sBasicRates;

                for (int i = 0; i < jsonArray.length(); i++) {
                    j = jsonArray.getJSONObject(i);
                    try {
                        sIndex = j.getString(BasicInfo.json_MONI_INDEX);
                    }catch (Exception e) {
                        continue;
                    }
                    try {
                        sDate = j.getString(BasicInfo.json_MONI_DATE);
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
                        double dTemp = Double.valueOf(sBasicRates);
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


                }
            } catch (Exception e) {
                Log.e("HTTP", e.toString());
            }

            if (AddExchangeDataArray.size() > 0) {

                ExchangeLab.excuteSQL_addData_Currency(AddExchangeDataArray, szCurrency);
            }
            reader.close();
            instream.close();
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
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


}

