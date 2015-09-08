package com.reviewmerce.exchange.innerProc;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.db.ExchangeDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by onebuy on 2015-07-23.
 */
public class ExchangeDataLab {
    private static final String TAG = "ExchangeDataLab";
    private static final String FILENAME = "exchangeData.json";
    private ArrayList<ExchangeData> mExchangeData;

    private String mLastTime;
    private String mLastPrice;
    private String mLastDate;
    private static ExchangeDataLab sExchangeDataLab;
    private Context mAppContext;
    public static ExchangeDatabase mExchangeDB = null;
    private Date mBeginDate, mEndDate;
    private Date mRequestBeginDate, mRequestEndDate;

    private int mBeginIndex, mEndIndex;

    private double mMin, mMax;
    private double mAvg;
    Boolean mDbisOpened;

    String m_sDBname="USD";


    private ExchangeData mLastLoadedExchangeData;

    public ExchangeData getLastLoadedExchangeData()
    {
        return mLastLoadedExchangeData;
    }
    public int getBeginIndex() {
        return mBeginIndex;
    }

    public int getEndIndex() {
        return mEndIndex;
    }

    public Date getRequstBeginDate() {
        return mRequestBeginDate;
    }

    public Date getRequstEndDate() {
        return mRequestEndDate;
    }

    public String getLastTime() {
        return mLastTime;
    }

    public String getLastPrice() {
        return mLastPrice;
    }

    public String getLastDate() {
        return mLastDate;
    }

    public void setLastTime(String time) {
        mLastTime = time;
    }

    public void setLastPrice(String price) {
        mLastPrice = price;
    }

    public void setLastDate(String date) {
        mLastDate = date;
    }

    public ExchangeDataLab(Context appContext) {
        mDbisOpened = false;
        if(appContext != null)
            mAppContext = appContext;
        mMin = 0;
        mMax = 0;
        mAvg = 0;
        mBeginIndex = 0;
        mEndIndex = 0;
        mLastLoadedExchangeData = new ExchangeData();
        initBankData();
        LoadData();
    }
    public void initBankData()
    {

        // 시중은행 및 많이 사용하는 금융기관
        BasicInfo.mBankCode.put (1,"한국은행");
        BasicInfo.mBankCode.put (2,"산업은행");
        BasicInfo.mBankCode.put (3,"기업은행");
        BasicInfo.mBankCode.put (4,"국민은행");
        BasicInfo.mBankCode.put (5,"외한은행");
        BasicInfo.mBankCode.put (7,"수협중앙회");
        BasicInfo.mBankCode.put (8,"수출입은행");
        BasicInfo.mBankCode.put (11,"농협중앙회");
        BasicInfo.mBankCode.put (12,"단위 농,축협 농협 중앙회");
        BasicInfo.mBankCode.put (20,"우리은행");
        BasicInfo.mBankCode.put (23,"SC은행");
        BasicInfo.mBankCode.put (27,"한국씨티은행");
        BasicInfo.mBankCode.put (45,"새마을금고중앙회");
        BasicInfo.mBankCode.put (48,"우체국");
        BasicInfo.mBankCode.put (71,"하나은행");
        BasicInfo.mBankCode.put (81,"신한은행");

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
    public void setDBname(String szName)
    {
        m_sDBname = szName;
    }
    public String getDBName()
    {
        return m_sDBname;
    }
    public void LoadData()
    {
        try {
            mLastLoadedExchangeData.setData("","","");
            openDatabase();
            //   mExchangeData = mSerializer.loadData();
            procLoadedData();
        } catch (Exception e) {
            mExchangeData = new ArrayList<ExchangeData>();
            Log.e(TAG, "Error loading ExchangeData");
        }
    }
    private void setIndex() {               // 데이터를 돌며 마지막
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int i = 0;
        long lValue, lStart;
        lStart = mBeginDate.getTime();
        try {
            for (ExchangeData e : mExchangeData) {
                lValue = dateFormat.parse(e.getDate()).getTime();
                if (lValue >= lStart) {
                    mBeginIndex = i;
                    break;
                }
                i++;
            }
            mEndIndex = mExchangeData.size() - 1;
        } catch (Exception e) {
            Log.e(TAG, "Graph set Data Error");
        }
    }



    public int getRequsetDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            if((mLastLoadedExchangeData == null) ||(mLastLoadedExchangeData.getDate() == ""))
            {
                int diffDays = 365*4;
                return diffDays;
            }
            else {
                String szDate = mLastLoadedExchangeData.getDate();
                mRequestBeginDate = dateFormat.parse(szDate);
                Calendar cal = new GregorianCalendar(Locale.KOREA);

                mRequestEndDate = getToday();
                cal.setTime(mRequestEndDate);
                cal.add(Calendar.DAY_OF_YEAR, -1);

                long diff = cal.getTime().getTime() - mRequestBeginDate.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffDays < 0)
                    diffDays = 0;

                return (int) diffDays;
            }
        } catch (Exception e) {
            return 0;
        }

    }
    public void clearAllData()
    {
        mExchangeData.clear();

        mLastTime = "";
        mLastPrice="";
        mLastDate="";

        mBeginDate.setTime(0);
        mEndDate.setTime(0);
        mRequestBeginDate.setTime(0);
        mRequestEndDate.setTime(0);

        mBeginIndex=0;
        mEndIndex=0;

        mMin = 0;
        mMax =0;
        mAvg = 0;
    }
    public int updateData(String sUpdateDate, double fBasicRates, String sTime) {
        int i = 0;
        for (ExchangeData e : mExchangeData) {
            if (e.getDate().equals(sUpdateDate)) {
                e.setData(sUpdateDate, String.valueOf(fBasicRates), sTime);
                mExchangeData.set(i, e);
                return 0;
            }
            i++;
        }
        mExchangeData.add(new ExchangeData(sUpdateDate, String.valueOf(fBasicRates), sTime));
        setIndex();
        return 1;
    }

    public int updateData(ExchangeData e) {
        int nRtn = updateData(e.getDate(), e.getBasicRates(), e.getTime());
        return nRtn;
    }

    public Date getToday() {
        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(today.YEAR);
        int month = today.get(today.MONTH) + 1;
        int day = today.get(today.DAY_OF_MONTH);
        Date time = today.getTime();

        //String szRtn = String.format("%d / %02d / %02d", year ,month,day ); // ���� ��¥
        return time;
    }
    public void changeBeginDate(int nType) {
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(mEndDate);
        switch (nType) {
            case 0:
                cal.add(Calendar.YEAR, -3);
                break;
            case 1:
                cal.add(Calendar.YEAR, -1);
                break;
            case 2:
                cal.add(Calendar.MONTH, -6);
                break;
            case 3:
                cal.add(Calendar.MONTH, -1);
                break;
            case 4:
                cal.add(Calendar.DAY_OF_YEAR, -1);
                break;
        }
        mBeginDate = cal.getTime();
        setIndex();
    }

    public Date getBeginDate() {
        return mBeginDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String szInput) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            mEndDate = formatter.parse(szInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static ExchangeDataLab get(Context c) {
        if (sExchangeDataLab == null) {
            sExchangeDataLab = new ExchangeDataLab(c.getApplicationContext());
        }
        return sExchangeDataLab;
    }

    public ArrayList<ExchangeData> getArrays() {
        return mExchangeData;
    }

    public void addExchangeData(ExchangeData d) {
        mExchangeData.add(d);
        setIndex();
    }

    public boolean saveExchangeData() {
        try {
            //mSerializer.saveData(mExchangeData);
            Log.d(TAG, "ExchangeData saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving ExchangeData : ", e);
            return false;
        }
    }

    public void procLoadedData() {
        double fVal = 0;
        double fSum = 0;
        int nCount = 0;
        int nDivideNum = 0;
        mMax = 0;
        mMin = BasicInfo.MAX_PRCIE_KRW;
        try {
            for (int i = mBeginIndex; i <= mEndIndex; i++) {
                fVal = mExchangeData.get(i).getBasicRates();
                if (fVal > mMax)
                    mMax = fVal;
                if (fVal < mMin)
                    mMin = fVal;
                fSum = fSum + fVal;
                nCount++;
            }
        } catch (Exception e) {
            Log.e(TAG, "ProcLoadedData Error");
        }

        if (nCount > 0) {
            mAvg = fSum / nCount;
            mLastLoadedExchangeData = mExchangeData.get(mExchangeData.size() - 1);
        }
        else
        {
            if(mLastLoadedExchangeData != null)
                mLastLoadedExchangeData.setData("","","");
        }
    }

    public double getMax() {
        return mMax;
    }

    public double getMin() {
        return mMin;
    }

    public double getAvg() {
        return mAvg;
    }

    public double getLastExchange() {
        double fRtn = .0f;
        try {
            fRtn = mLastLoadedExchangeData.getBasicRates();
        } catch (Exception e) {
            Log.e(TAG, "ProcLoadedData Error");
        }
        return fRtn;
    }

    private boolean openDatabase() {

        mExchangeDB = ExchangeDatabase.getInstance(mAppContext);

        boolean isOpen = mExchangeDB.open();
        if (isOpen) {
            mDbisOpened = true;
            Log.d(TAG, "exchange database is open.");
        } else {
            Log.d(TAG, "exchange database is not open.");
        }

        mExchangeDB.createTable("USD");
        mExchangeDB.createTable("EUR");
        mExchangeDB.createTable("CNY");
        mExchangeDB.createTable("JPY");
        mExchangeDB.createTable("GBP");
        mExchangeDB.createTable("HKD");

        excuteSQL_getInitData();
        return true;
    }

    private void excuteSQL_getInitData() {
//        String SQL = "select DATE, PRICE, PRICE_DECIMALS, TIME, BANK "
        String SQL = "select * "
                + "from " + m_sDBname + " order by "+ BasicInfo.TABLE_MONI_DATE + " asc";

        Cursor c1;
        c1 = mExchangeDB.rawQuery(SQL);
        int recordCount = c1.getCount();
        String sDate, sBasicRates, sTime;
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat fmNew = new SimpleDateFormat("yyyyMMdd");
        Date date;
        int nPrice;
        float fPriceDecimals;
        mExchangeData = new ArrayList<ExchangeData>();
        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            sDate = c1.getString(0);
            if(sDate != null) {
                int nComp = sDate.compareTo("null");
                if (nComp < 0) {
                    try {
                        date = fm.parse(sDate);
                        sDate = fmNew.format(date);
                    } catch (Exception e) {
                        Log.e("Excute InitDB : ", e.toString());
                    }
                    sBasicRates = c1.getString(1);
                    sTime = c1.getString(2);

                    if (sDate.length() > 0)
                        mExchangeData.add(new ExchangeData(sDate, sBasicRates, sTime));
                }
            }
        }
        c1.close();
    }

    public void excuteSQL_addData(ArrayList<ExchangeData> DataList) {
        //insert into USD values ("2012-07-12",	1171, 	0.65);
        if (mDbisOpened == false) {
            openDatabase();
        }
        try {
            for (ExchangeData e : DataList) {
                String SQL = "insert or replace into " + m_sDBname + " values (\"" +
                        e.getDate() + "\"," +
                        Double.toString(e.getBasicRates()) + "," +
                        "\""+ e.getTime() + "\"" +
                        ")";
                mExchangeDB.execSQL(SQL);
            }
        } catch (Exception e) {
            Log.e("AddDB error : ", e.toString());
        }
    }
}
