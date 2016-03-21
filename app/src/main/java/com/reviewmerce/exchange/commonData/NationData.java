package com.reviewmerce.exchange.commonData;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by onebuy on 2015-09-07.
 */
public class NationData{

    //private int mIndex=0;           // 기간
    private String mCurrencyCodeInEng;
    private String mCountryNameInKorean;
    private String mCurrencyChar;
    private String mNumericExpression;

    // UI
    private int mGraphTextColor;
    private int mGraphColor;
    private int mGraphFillColor;
    private int mPurseTextColor;
    private int mPurseTextColor2;

    private String mBackgroundFile;
    private String mFlagFile;
    private String mGraphBubbleFile;
    private String mPurseBarFile1;
    private String mPurseBarFile2;
    private String mHalfBarFile;

    //private Bitmap bitmapBackgroundFile;
    private Bitmap bitmapFlagFile;
    private Bitmap bitmapGraphBubbleFile;
    private Bitmap bitmapPurseBarFile1;
    private Bitmap bitmapPurseBarFile2;
    private Bitmap bitmapHalfBarFile;



    private boolean mUseData;
    public NationData() {
    }
    public NationData(String sCurrencyCodeInEng)
    {
        initData();
        mCurrencyCodeInEng = sCurrencyCodeInEng;
    }
    public void initData()
    {
        mCurrencyCodeInEng="USD";
        mCountryNameInKorean ="미국";
        mCurrencyChar = "$";
        mNumericExpression = "";
        // UI
        mGraphTextColor = Color.parseColor("#ffc90006");
        mGraphColor= Color.parseColor("#ffc90006");
        mGraphFillColor= Color.parseColor("#ffff0000");
        mPurseTextColor= Color.parseColor("#ffff7449");
        mPurseTextColor2= Color.parseColor("#ffff0000");

        mBackgroundFile = "usd_flag.png";
        mFlagFile = "usd.png";
        mGraphBubbleFile =  "usd_bubble.png";
        mPurseBarFile1 = "usd_bar.png";
        mPurseBarFile2 = "usd_bar2.png";
        mHalfBarFile = "usd_barhalf.png";
        mUseData = false;

//        bitmapBackgroundFile=null;
        if (bitmapFlagFile != null) {
            bitmapFlagFile.recycle();
            bitmapFlagFile = null;
        }
        if (bitmapGraphBubbleFile != null) {
            bitmapGraphBubbleFile.recycle();
            bitmapGraphBubbleFile = null;
        }
        if (bitmapPurseBarFile1 != null) {
            bitmapPurseBarFile1.recycle();
            bitmapPurseBarFile1 = null;
        }
        if (bitmapPurseBarFile2 != null) {
            bitmapPurseBarFile2.recycle();
            bitmapPurseBarFile2 = null;
        }
        if (bitmapHalfBarFile != null) {
            bitmapHalfBarFile.recycle();
            bitmapHalfBarFile = null;
        }
    }

    public void setData(NationData nd) {
        mCurrencyCodeInEng= nd.getCurrencyCodeInEng();
        mCountryNameInKorean = nd.getCountryNameInKorean();
        mCurrencyChar = nd.getCurrencyChar();
        mNumericExpression = nd.getNumericExpression();
        // UI
        mGraphTextColor = nd.getGraphTextColor();
        mGraphColor= nd.getGraphColor();
        mGraphFillColor= nd.getGraphFillColor();
        mPurseTextColor= nd.getPurseTextColor();
        mPurseTextColor2= nd.getPurseTextColor2();

        mBackgroundFile = nd.getBackgroundFile();
        mFlagFile = nd.getFlagFile();
        mGraphBubbleFile =  nd.getGraphBubbleFile();
        mPurseBarFile1 = nd.getPurseBarFile1();
        mPurseBarFile2 = nd.getPurseBarFile2();
        mHalfBarFile = nd.getHalfBarFile();

        if (bitmapFlagFile != null) {
            bitmapFlagFile.recycle();
            bitmapFlagFile = null;
        }
        if (bitmapGraphBubbleFile != null) {
            bitmapGraphBubbleFile.recycle();
            bitmapGraphBubbleFile = null;
        }
        if (bitmapPurseBarFile1 != null) {
            bitmapPurseBarFile1.recycle();
            bitmapPurseBarFile1 = null;
        }
        if (bitmapPurseBarFile2 != null) {
            bitmapPurseBarFile2.recycle();
            bitmapPurseBarFile2 = null;
        }
        if (bitmapHalfBarFile != null) {
            bitmapHalfBarFile.recycle();
            bitmapHalfBarFile = null;
        }
    }

    public String getCurrencyCodeInEng() {
        return mCurrencyCodeInEng;
    }
    public String getCountryNameInKorean() {
        return mCountryNameInKorean;
    }

    public int getGraphTextColor()
    {
        return mGraphTextColor;
    }
    public int getGraphColor()
    {
        return mGraphColor;
    }
    public int getGraphFillColor()
    {
        return mGraphFillColor;
    }


    public int getPurseTextColor()
    {
        return mPurseTextColor;
    }
    public int getPurseTextColor2()
    {
        return mPurseTextColor2;
    }

    public String getCurrencyChar()
    {
        return mCurrencyChar;
    }
    public String getBackgroundFile()
    {
        return mBackgroundFile;
    }


    ////////////////////////////////////////////////////////////////////////


    public void setCurrencyCodeInEng(String sCurrencyCodeInEng) {
        mCurrencyCodeInEng = sCurrencyCodeInEng;
    }
    public void setCountryNameInKorean(String sCountryNameInKorean) {
        mCountryNameInKorean = sCountryNameInKorean;
    }
    public void setBackgroundFile(String sBackgroundFile)
    {
        mBackgroundFile = sBackgroundFile;
    }

    public void setGraphTextColor(int nGraphTextColor)
    {
        mGraphTextColor = nGraphTextColor;
    }
    public void setGraphColor(int nGraphColor)
    {
        mGraphColor = nGraphColor;
    }
    public void setGraphFillColor(int nGraphFillColor)
    {
        mGraphFillColor = nGraphFillColor;
    }

    public void setPurseTextColor(int nPurseTextColor)
    {
        mPurseTextColor = nPurseTextColor;
    }
    public void setPurseTextColor2(int nPurseTextColor2)
    {
        mPurseTextColor2 = nPurseTextColor2;
    }

    public void setCurrencyChar(String sCurrencyChar)
    {
        mCurrencyChar = sCurrencyChar;
    }
    ///////////////////////////////////////////////////////////////////////
    public void setNumericExpression(String sExpression)
    {
        mNumericExpression = sExpression;
    }
    public String getNumericExpression()
    {
        return mNumericExpression;
    }
    public void setFlagFile(String sVal)
    {
        mFlagFile = sVal;
    }
    public String getFlagFile()
    {
        return mFlagFile;
    }
    public Bitmap getBitmapFlagFile()
    {
//        if(bitmapFlagFile==null)
        {
            String sFilename = BasicInfo.InternalPath + "image/flag/" + mFlagFile;
            try {
                /*
                if (bitmapFlagFile != null) {
                    bitmapFlagFile.recycle();
                    bitmapFlagFile = null;
                }
                */
                bitmapFlagFile = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapFlagFile;
    }
    public void setGraphBubbleFile(String sVal)
    {
        mGraphBubbleFile = sVal;
    }
    public String getGraphBubbleFile()
    {
        return mGraphBubbleFile;
    }
    public Bitmap getBitmapGraphBubbleFile()
    {
//        if(bitmapGraphBubbleFile==null)
        {
            String sFilename = BasicInfo.InternalPath +  mGraphBubbleFile;
            try {
                if (bitmapGraphBubbleFile != null) {
                    bitmapGraphBubbleFile.recycle();
                    bitmapGraphBubbleFile = null;
                }
                bitmapGraphBubbleFile = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapGraphBubbleFile;
    }
    public void setPurseBarFile1(String sVal)
    {
        mPurseBarFile1 = sVal;
    }
    public String getPurseBarFile1()
    {
        return mPurseBarFile1;
    }
    public Bitmap getBitmapPurseBarFile1()
    {
 //       if(bitmapPurseBarFile1==null)
        {
            String sFilename = BasicInfo.InternalPath +  "image/bar/" + mPurseBarFile1;
            try {
                if (bitmapPurseBarFile1 != null) {
                    bitmapPurseBarFile1.recycle();
                    bitmapPurseBarFile1 = null;
                }
                bitmapPurseBarFile1 = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapPurseBarFile1;
    }
    public void setPurseBarFile2(String sVal)
    {
        mPurseBarFile2 = sVal;
    }
    public String getPurseBarFile2()
    {
        return mPurseBarFile2;
    }
    public Bitmap getBitmapPurseBarFile2()
    {
 //       if(bitmapPurseBarFile2==null)
        {
            String sFilename = BasicInfo.InternalPath + "image/bar/" + mPurseBarFile2;
            try {
                if (bitmapPurseBarFile2 != null) {
                    bitmapPurseBarFile2.recycle();
                    bitmapPurseBarFile2 = null;
                }
                bitmapPurseBarFile2 = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapPurseBarFile2;
    }
    public void setHalfBarFile(String sVal)
    {
        mHalfBarFile = sVal;
    }
    public String getHalfBarFile()
    {
        return mHalfBarFile;
    }
    public Bitmap getBitmapHalfBarFile()
    {
//        if(bitmapHalfBarFile==null)
        {
            String sFilename = BasicInfo.InternalPath + "image/bar/" + mHalfBarFile;
            try {
                if (bitmapHalfBarFile != null) {
                    bitmapHalfBarFile.recycle();
                    bitmapHalfBarFile = null;
                }
                bitmapHalfBarFile = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapHalfBarFile;
    }
    public boolean isUseData() {
        return mUseData;
    }

    public void setUseData(boolean useData) {
        this.mUseData = useData;
    }
}
