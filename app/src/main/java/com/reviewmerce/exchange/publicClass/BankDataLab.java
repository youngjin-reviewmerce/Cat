package com.reviewmerce.exchange.publicClass;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.ExchangeData;
import com.reviewmerce.exchange.db.BankDatabase;
import com.reviewmerce.exchange.db.PurseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by onebuy on 2015-09-07.
 */
public class BankDataLab {

    private static BankDataLab sBankDataLab;
    public static BankDatabase mBankDB = null;
    private boolean mDBisOpened=false;
    NationDataLab mNationLab=null;
    GlobalVar mGlobalVar = null;
    public static BankDataLab get(Context c) {
            if (sBankDataLab == null) {
                sBankDataLab = new BankDataLab(c.getApplicationContext());
            }
            return sBankDataLab;
    }
    ////////////////////////////////////////////////////////////////////////////

    private Context mAppContext;

    private ArrayList<BankData> mBankData;
    private ArrayList<BankData> mBankDataSortList;
//    private String mCurrencyName="USD";
//    private String mConturyName="미국";
//    private String mExchangeType="기본 환율";

    public String mRepresentBank, mRepresentDate, mRepresentTime;
    public BankDataLab(Context appContext) {
        if(appContext != null) {
            mNationLab = NationDataLab.get(null);
            mGlobalVar = GlobalVar.get();
            mAppContext = appContext;
            initData();
        }
    }


    private void openDatabase(){
        mBankDB = BankDatabase.getInstance(mAppContext);

        boolean isOpen = mBankDB.open();
        if (isOpen) {
            mDBisOpened = true;
            Log.d(BasicInfo.TAG, "exchange database is open.");
        } else {
            Log.d(BasicInfo.TAG, "exchange database is not open.");
        }
        mBankDB.createTable(mNationLab.getCurrencyCodeInEng());
    }
    private void excuteSQL_getInitData() {

    }
    public void excuteSQL_addData(ArrayList<ExchangeData> DataList)
    {

    }
    public int addData(BankData bd) {
        mBankData.add(bd);
        return mBankData.size();
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    String getBankName(int nBankcode)
    {
        String sRtn = BasicInfo.mBankCode.get(nBankcode);
        return sRtn;
    }
    public String getExchangeType_name(int nExchangeType)
    {
        String sRtn = BasicInfo.mExchangeType.get(nExchangeType);
        return sRtn;
    }
    public int getExchangeType_index(String szType)
    {
        int nRtn = 0;
       Set<HashMap.Entry<Integer, String>> ets = BasicInfo.mExchangeType.entrySet();
        for(HashMap.Entry<Integer, String> item : ets)
        {
            if(item.getValue().compareTo(szType)==0)
            {

                return item.getKey();
            }
            nRtn++;
        }
        return -1;
    }
    public void jsonToData(String sResult)
    {
        JSONObject json = null;
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

                addData(bd);
                AddBankDataArray.add(bd);

            } // for
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }
    }
    public void sortToSortedData()
    {
        mBankDataSortList.clear();
        // "기본환율", "현금살때", "현금팔때", "송금살때", "송금살때", "TC살때", "외화수표"};

        int nExchangeType = 0;
        nExchangeType = getExchangeType_index(mGlobalVar.getExchangeType());
        double dValue;
        int i=0;
        for (BankData b : mBankData) {
            b.setType(nExchangeType);       // 소팅 하고자 하는 종류 설정
            dValue = b.getValue();
            if(dValue > 0f) {
                mBankDataSortList.add(b);
            }
            i++;
        }
        if((nExchangeType==3) ||(nExchangeType==5) ||(nExchangeType==7) )
            Collections.sort(mBankDataSortList,new BankSortedCompareBig());
        else
            Collections.sort(mBankDataSortList,new BankSortedCompareSmall());
        double dMinVal = .0f;
        i=0;

        mRepresentBank = "지원하는 은행 없음";

        for (BankData sb : mBankDataSortList)
        {
            if(i==0)
            {
                //if(nBank == 5)  // 외환은행
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date dateTemp = new Date();
                try {
                    dateTemp = formatter.parse(sb.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mRepresentDate = fm.format(dateTemp);
                mRepresentTime = sb.getTime();
                mRepresentBank = getBankName(sb.getBank());
            }
            if(dMinVal == .0f)
                dMinVal = sb.getValue();
            sb.setDifference(sb.getValue() - dMinVal);
            i++;
        }
    }

    public void clearData()
    {
        mBankDataSortList.clear();
        mBankData.clear();
    }
    public ArrayList getSortedData()
    {
        return mBankDataSortList;
    }

    static class BankSortedCompareBig implements Comparator<BankData> {
        @Override
        public int compare(BankData arg0, BankData arg1) {
            // TODO Auto-generated method stub
            return arg0.getValue() > arg1.getValue() ? -1 : arg0.getValue() < arg1.getValue() ? 1:0;
        }
    }
    static class BankSortedCompareSmall implements Comparator<BankData> {
        @Override
        public int compare(BankData arg0, BankData arg1) {
            // TODO Auto-generated method stub
            return arg0.getValue() > arg1.getValue() ? 1 : arg0.getValue() < arg1.getValue() ? -1:0;
        }
    }

    public String getRepresentDate()
    {
        return mRepresentDate;
    }
    public void setRepresentDate(String sRepresentDate)
    {
        mRepresentDate = sRepresentDate;
    }

    public String getRepresentTime()
    {
        return mRepresentTime;
    }
    public void setRepresentTime(String sRepresentTime)
    {
        mRepresentTime = sRepresentTime;
    }
    public String getRepresentBank()
    {
        return mRepresentBank;
    }
    public void setRepresentBank(String sRepresentBank)
    {
        mRepresentBank = sRepresentBank;
    }
    public void initData()
    {
        mBankData = new ArrayList<BankData>();
        mBankDataSortList = new ArrayList<BankData>();
        mRepresentDate = "1900-01-01";
        mRepresentTime = "00:00:00";

        initBankData();
        initExchangeType();
    }
    public void initExchangeType()
    {
        if(BasicInfo.mExchangeType.size()>0)
            BasicInfo.mExchangeType.clear();
        BasicInfo.mExchangeType.put(1,"기준 환율");
        BasicInfo.mExchangeType.put(2,"현금 살때");
        BasicInfo.mExchangeType.put(3,"현금 팔때");
        BasicInfo.mExchangeType.put(4,"송금 보낼때");
        BasicInfo.mExchangeType.put(5,"송금 받을때");
        BasicInfo.mExchangeType.put(6, "TC 살때");
        BasicInfo.mExchangeType.put(7, "TC 팔때");
    }

    public void initBankData()
    {
        if(BasicInfo.mBankCode.size() >0)
            BasicInfo.mBankCode.clear();

        // 시중은행 및 많이 사용하는 금융기관
        BasicInfo.mBankCode.put (0,"유럽중앙은행");
        BasicInfo.mBankCode.put (1,"한국은행");
        BasicInfo.mBankCode.put (2,"산업은행");
        BasicInfo.mBankCode.put (3,"기업은행");
        BasicInfo.mBankCode.put (4,"국민은행");
        BasicInfo.mBankCode.put (5,"외환은행");
        BasicInfo.mBankCode.put (7,"수협중앙회");
        BasicInfo.mBankCode.put (8,"수출입은행");
        BasicInfo.mBankCode.put (11,"농협중앙회");
        BasicInfo.mBankCode.put (12,"단위 농,축협 농협 중앙회");
        BasicInfo.mBankCode.put (20,"우리은행");
        BasicInfo.mBankCode.put (23,"SC은행");
        BasicInfo.mBankCode.put (27,"한국씨티은행");
        BasicInfo.mBankCode.put (45,"새마을금고중앙회");
        BasicInfo.mBankCode.put (48,"신협중앙회");
        BasicInfo.mBankCode.put (71,"우체국");
        BasicInfo.mBankCode.put (81,"하나은행");
        BasicInfo.mBankCode.put (88,"신한은행");

        // 지방은행 및 기타 은행
        BasicInfo.mBankCode.put (31,"대구은행");
        BasicInfo.mBankCode.put (32,"부산은행");
        BasicInfo.mBankCode.put (34,"광주은행");
        BasicInfo.mBankCode.put (35,"제주은행");
        BasicInfo.mBankCode.put (37,"전북은행");
        BasicInfo.mBankCode.put (39,"경남은행");
        BasicInfo.mBankCode.put (50,"상호저축은행");
        BasicInfo.mBankCode.put (64,"산림조합중앙회");

        // 외국계 은행
        BasicInfo.mBankCode.put (52,"모거스탠리은행");
        BasicInfo.mBankCode.put (54,"HSBC은행");
        BasicInfo.mBankCode.put (55,"도이치은행");
        BasicInfo.mBankCode.put (56,"알비에스피엘씨은행");
        BasicInfo.mBankCode.put (57,"제이피모간체이스은행");
        BasicInfo.mBankCode.put (58,"미즈호은행");
        BasicInfo.mBankCode.put (59,"미쓰비시도쿄UFJ은행");
        BasicInfo.mBankCode.put (60,"BOA은행");
        BasicInfo.mBankCode.put (61,"비엔피파리바은행");
        BasicInfo.mBankCode.put (62,"중국공상은행");
        BasicInfo.mBankCode.put (63,"중국은행");
        BasicInfo.mBankCode.put (65,"대화은행");

        // 특수 금융기관
        BasicInfo.mBankCode.put (76,"신용보증기금");
        BasicInfo.mBankCode.put (77,"기술보증기금");
        BasicInfo.mBankCode.put (92,"한국정책금융공사");
        BasicInfo.mBankCode.put (93,"한국주택금융공사");
        BasicInfo.mBankCode.put (94,"서울보증보험");
        BasicInfo.mBankCode.put (95,"경찰청");
        BasicInfo.mBankCode.put (96,"한국전자금융(주)");
        BasicInfo.mBankCode.put (99,"금융결재원");

        // 증권회사
        BasicInfo.mBankCode.put (209,"유안타증권");
        BasicInfo.mBankCode.put (218,"현대증권");
        BasicInfo.mBankCode.put (230,"미래에셋증권");
        BasicInfo.mBankCode.put (238,"대우증권");
        BasicInfo.mBankCode.put (240,"삼성증권");
        BasicInfo.mBankCode.put (243,"한국투자증권");
        BasicInfo.mBankCode.put (247,"우리투자증권");
        BasicInfo.mBankCode.put (261,"교보증권");
        BasicInfo.mBankCode.put (262,"하이투자증권");
        BasicInfo.mBankCode.put (263,"HMC투자증권");
        BasicInfo.mBankCode.put (264,"키움증권");
        BasicInfo.mBankCode.put (265,"이트레이드증권");
        BasicInfo.mBankCode.put (266,"SK증권");
        BasicInfo.mBankCode.put (267,"대신증권");
        BasicInfo.mBankCode.put (268,"아이엠투자증권");
        BasicInfo.mBankCode.put (269,"한화투자증권");
        BasicInfo.mBankCode.put (270,"하나대투증권");
        BasicInfo.mBankCode.put (278,"신한금융투자");
        BasicInfo.mBankCode.put (279,"동부증권");
        BasicInfo.mBankCode.put (280,"유진투자증권");
        BasicInfo.mBankCode.put (287,"메리츠종합금융증권");
        BasicInfo.mBankCode.put (289,"NH농협증권");
        BasicInfo.mBankCode.put (290,"부국증권");
        BasicInfo.mBankCode.put (291,"신영증권");
        BasicInfo.mBankCode.put (292,"LIG투자증권");
    }

    public void excuteDataSave()
    {
        if(mDBisOpened == false)
        {
            openDatabase();
        }
        int nIndex = 0;

        try {
            mBankDB.delete_createTable(mNationLab.getCurrencyCodeInEng());

            for (BankData e : mBankData) {
                String SQL = "insert or replace into "+ mNationLab.getCurrencyCodeInEng() + " values (" +
                        Integer.toString(e.getBank()) + "," +
                        "\"" + e.getDate() + "\"," +
                        "\"" + e.getTime() + "\"," +
                        "\"" + Double.toString(e.getBasicRates()) +"\"," +
                        "\"" + Double.toString(e.getCashBuying()) +"\"," +
                        "\"" + Double.toString(e.getCashSelling()) +"\"," +
                        "\"" + Double.toString(e.getStransferSending()) +"\"," +
                        "\"" + Double.toString(e.getStransferReceiving()) +"\"," +
                        "\"" + Double.toString(e.getCashBuying()) +"\"," +
                        "\"" + Double.toString(e.getCashSelling()) +"\"" +
                        ")";
                mBankDB.execSQL(SQL);
                nIndex++;
            }

        } catch (Exception e) {
            Log.e("AddDB error : ", e.toString());
        }
    }
    public int loadData()
    {
        int nRtnVal = 0;
        nRtnVal = excuteDataLoad();
        return nRtnVal;
    }
    public void saveData()
    {
        excuteDataSave();
    }

    private int excuteDataLoad() {
        if (mDBisOpened == false) {
            openDatabase();
        }
        String SQL = "select * "
                + "from " + mNationLab.getCurrencyCodeInEng();
        Cursor c1;
        try {

            c1 = mBankDB.rawQuery(SQL);
            if(c1==null)
                return 0;
        }
        catch(Exception e)
        {
            Log.e("Excute Bank LoadDB : ", e.toString());
            return 0;
        }
        String sDate, sTime, sBank, sBasicRates, sCashBuying, sCashSelling,
                sStransferSending, sStransferReceiving, sCheckBuying, sCheckSelling;

        int recordCount = c1.getCount();
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
        Date date;

        if(recordCount > 0)
            mBankData.clear();
        for (int i = 0; i < recordCount; i++) {
            try {
                c1.moveToNext();
                sBank = c1.getString(0);
                sDate = c1.getString(1);
                try {
                    date = fm.parse(sDate);
                    sDate = fmNew.format(date);
                } catch (Exception e) {
                    Log.e("Excute Bank LoadDB : ", e.toString());
                }
                sTime = c1.getString(2);
                sBasicRates = c1.getString(3);
                sCashBuying = c1.getString(4);
                sCashSelling = c1.getString(5);
                sStransferSending = c1.getString(6);
                sStransferReceiving = c1.getString(7);
                sCheckBuying = c1.getString(8);
                sCheckSelling = c1.getString(9);

                BankData bd = new BankData(i, sDate, sTime, sBank, sBasicRates,
                        sCashBuying, sCashSelling, sStransferSending, sStransferReceiving, sCheckBuying, sCheckSelling);
                mBankData.add(bd);
            }catch (Exception e)
            {
                Log.e("Excute Bank LoadDB : ", e.toString());
                c1.close();
                return 0;
            }
        }
        c1.close();
        return recordCount;
    }

}
