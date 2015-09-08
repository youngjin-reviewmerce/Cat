package com.reviewmerce.exchange.graph;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.custom.MyMarkerView;
import com.reviewmerce.exchange.innerProc.ExchangeData;

import java.util.ArrayList;

/**
 * Created by onebuy on 2015-08-11.
 */
public class GraphControl implements OnChartGestureListener, OnChartValueSelectedListener {
    public static final String TAG = "ChartControl";
    private LineChart mChart = null;
    Context mParentContext = null;
    private float mAvg = .0f;
    private float mMax = .0f;
    private float mMin = .0f;
    private int mBeginIndex = 0;
    private int mEndIndex = 0;
    private boolean m_bAddline1 = true;                 // 추세선
    private int mAddline1Count = 14;                     // 추세선에 사용될 갯수(5개 평균)
    private ArrayList<ExchangeData> mDataArray = null;
    ExchangeData mTodayData = null;
    public void setBeginIndex(int index)
    {
        mBeginIndex = index;
    }
    public void setEndIndex(int index)
    {
        mEndIndex = index;
    }
    public void setAvg(float avg)
    {
        mAvg = avg;
    }
    public void setMax(float max)
    {
        mMax = max;
    }
    public void setMin(float min)
    {
        mMin = min;
    }
    public LineChart getChart()
    {
        return mChart;
    }
    public GraphControl(LineChart chart,Context context)
    {
        mChart = chart;
        mParentContext = context;

        mAvg = .0f;
        mMax = .0f;
        mTodayData = new ExchangeData();
    }

    public void setDataArray(ArrayList<ExchangeData> dataArray)
    {
        mDataArray = dataArray;

    }
    public void setTodayData(ExchangeData data)
    {
        mTodayData.setData(data.getDate(),String.valueOf(data.getBasicRates()),data.getTime());
    }
    public void initChart() {
        long start_time = System.currentTimeMillis();

        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(mParentContext, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);
        long time = System.currentTimeMillis();
        long l1 = time - start_time;
        Log.i("initChartTime = ", Long.toString(l1));
    }

    public void setChartInitValue()
    {
        long start_time = System.currentTimeMillis();
        LimitLine ll1 = new LimitLine(mAvg, "평균 : " + String.format(("%.2f"), mAvg));
        ll1.setLineWidth(1f);
        //ll1.enableDashedLine(10f, 1f, 0f);
        ll1.setLineColor(Color.BLUE);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll1.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        float fRange = mMax - mMin;
        float fSpace = (float)(fRange * 0.1);

        leftAxis.setAxisMaxValue(mMax + fSpace);
        leftAxis.setAxisMinValue(mMin - fSpace);
        leftAxis.setStartAtZero(false);
        leftAxis.setYOffset(1f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        // add data
        long time1 = System.currentTimeMillis();
        setData_to_Graph();
        long time2 = System.currentTimeMillis();
     //   mChart.animateX(800, Easing.EasingOption.EaseInOutQuart);
    //    Legend l = mChart.getLegend();
   //     l.setForm(Legend.LegendForm.LINE);


        long time = System.currentTimeMillis();
        long l1 = time - start_time;
        Log.i(TAG + "InitVal = ", Long.toString(l1));
    }
    public void setData_to_Graph() {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        double fTemp = .0f;
        int nAddLine1Index = 0;
        int nMidIndex = mAddline1Count / 2 - 1;         // 그래프를 그릴 x 값
        if(nMidIndex < 0 )
            nMidIndex = 0;
        int nYval2Index = 0;
        // add data
        int index = 0;

        try {
            for (int i = mBeginIndex; i <= mEndIndex; i++) {
                xVals.add(mDataArray.get(i).getDate());
                yVals.add(new Entry(
                        (float)(mDataArray.get(i).getBasicRates()),
                        index));
                index++;
                if(m_bAddline1 == true)
                {
                    fTemp += mDataArray.get(i).getBasicRates();
                    nAddLine1Index++;

                    if((i==mBeginIndex) ||(i==mEndIndex))
                    {
                        yVals2.add(new Entry((float)(mDataArray.get(i).getBasicRates()),index));
                    }

                    if(nAddLine1Index >= mAddline1Count)
                    {
                        fTemp = fTemp / mAddline1Count;
                        yVals2.add(new Entry((float)fTemp,nYval2Index + nMidIndex));
                        nYval2Index = index;
                        fTemp = .0f;
                        nAddLine1Index = 0;
                    }
                }
            }
            xVals.add(mTodayData.getDate());
            yVals.add(new Entry(
                    (float)(mTodayData.getBasicRates()), index));
            index++;

        } catch (Exception e) {
            Log.e(TAG, "Graph set Data Error");
        }
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "단위:원");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f); // 라인, 공간, 각도
        set1.setColor(Color.GRAY);
        set1.setCircleColor(Color.GRAY);
        set1.setLineWidth(1f);
        set1.setCircleSize(1f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(6f);
        set1.setFillAlpha(35);
        set1.setFillColor(Color.GRAY);
        set1.setDrawValues(false);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        if(m_bAddline1 == true)
        {
            LineDataSet set2 = new LineDataSet(yVals2, "추세선");
            // set1.setFillAlpha(110);
            // set1.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            int color = Color.rgb(00, 102, 180);
            set2.enableDashedLine(10f, 0f, 0f); // 라인, 공간, 각도
            set2.setColor(color);
            set2.setCircleColor(color);
            set2.setLineWidth(1f);
            set2.setCircleSize(1f);
            set2.setDrawCircleHole(false);
            set2.setValueTextSize(6f);
            set2.setFillAlpha(35);
            set2.setFillColor(color);
            set2.setDrawValues(false);
            dataSets.add(set2);
        }
        //set1.setVisible(false);
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
        mChart.invalidate();
    }
    public void setChartDataVisible(boolean b)
    {
        ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                .getDataSets();
        LineDataSet set = sets.get(1);
        set.setVisible(b);
        mChart.invalidate();
    }
    public void setChartAddlineVisible(boolean b)
    {
        ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                .getDataSets();
        LineDataSet set = sets.get(0);
        set.setVisible(b);
        mChart.invalidate();
    }
    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }
    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("", "low: " + mChart.getLowestVisibleXIndex() + ", high: " + mChart.getHighestVisibleXIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}
