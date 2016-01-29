package com.reviewmerce.exchange.commonData;

import java.util.Date;

/**
 * Created by onebuy on 2015-09-07.
 */
public class GraphPeriodData {
    private int mType;
    private int mBeginArrayIndex;
    private int mEndArrayIndex;
    private Date mBeginDate;
    private double mMin;
    private double mMax;
    private double mAvg;
    private int mCount;
    public GraphPeriodData() {
    }

    public GraphPeriodData(int nType, Date BeginDate) {
        mType = nType;
        mBeginDate = BeginDate;
        mMin = .0f;
        mMax = .0f;
        mAvg = .0f;
        mCount = 0;
    }
    public GraphPeriodData(GraphPeriodData p)
    {
        mType = p.getType();
        mBeginArrayIndex = p.getBeginArrayIndex();
        mEndArrayIndex = p.getEndArrayIndex();
        mBeginDate = p.getBeginDate();
        mMin = p.getMin();
        mMax = p.getMax();
        mAvg = p.getAvg();
        mCount = p.mCount;
    }

    public void setData(int nType, int nBeginArrayIndex, int nEndArrayIndex,Date BeginDate,double dMin, double dMax, double dAvg, int nCount)
    {
        mType = nType;
        mBeginArrayIndex = nBeginArrayIndex;
        mEndArrayIndex = nEndArrayIndex;
        mBeginDate = BeginDate;
        mMin = dMin;
        mMax = dMax;
        mAvg = dAvg;
        mCount = nCount;
    }
    public void calcValue(double dVal)
    {
        if(mMin == .0f)
            mMin = dVal;

        if(dVal<mMin)
            mMin = dVal;

        if(dVal>mMax)
            mMax = dVal;

        if(mAvg==.0f)
            mAvg = dVal;
        else
        {
            mAvg = mAvg*mCount + dVal;
            mCount++;
            mAvg = mAvg / mCount;
        }
    }
    public void setType(int nType)
    {
        mType = nType;
    }
    public int getType()
    {
        return mType;
    }

    public void setBeginArrayIndex(int nBeginIndex)
    {
        mBeginArrayIndex = nBeginIndex;
    }
    public int getBeginArrayIndex()
    {
        return mBeginArrayIndex;
    }

    public void setEndArrayIndex(int nEndIndex)
    {
        mEndArrayIndex = nEndIndex;
    }
    public int getEndArrayIndex()
    {
        return mEndArrayIndex;
    }

    public void setBeginDate(Date BeginDate)
    {
        mBeginDate = BeginDate;
    }
    public Date getBeginDate()
    {
        return mBeginDate;
    }

    public void setMin(double dMin)
    {
        mMin = dMin;
    }
    public double getMin()
    {
        return mMin;
    }

    public void setMax(double dMax)
    {
        mMax = dMax;
    }
    public double getMax()
    {
        return mMax;
    }
    public void setAvg(double dAvg)
    {
        mAvg = dAvg;
    }
    public double getAvg()
    {
        return mAvg;
    }
    public int compareTo(BankItem other) {

        return 0;
    }
    public void setCount(int nCount)
    {
        mCount = nCount;
    }
    public int getCount()
    {
        return mCount;
    }
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }
    public boolean isSelectable() {
        return mSelectable;
    }
    private boolean mSelectable = true;
}

