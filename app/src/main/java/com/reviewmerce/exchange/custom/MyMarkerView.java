
package com.reviewmerce.exchange.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.ExchangeData;
import com.reviewmerce.exchange.publicClass.ExchangeDataLab;
import com.reviewmerce.exchange.publicClass.LiveDataLab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {
    GlobalVar mGlobalVar=null;
    private TextView tvContent;
    private RelativeLayout layout;
    ExchangeDataLab mExchangeLab = null;
    LiveDataLab mLiveDataLab = null;
    private ArrayList<ExchangeData> mDataArray=null;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mExchangeLab = ExchangeDataLab.get(null);
        mLiveDataLab = LiveDataLab.get(null);
        tvContent = (TextView) findViewById(R.id.tvContent);
        layout = (RelativeLayout) findViewById(R.id.markerLayout);
        mGlobalVar = GlobalVar.get();
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight dataSetIndex) {
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            //tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
            try {
                mDataArray = mExchangeLab.getArrays();
                int index = mExchangeLab.getBeginIndex() + dataSetIndex.getXIndex();
                String sString="";
                if(index < mDataArray.size())
                    sString = mDataArray.get(index).getDate();
                else if(index == mDataArray.size())
                {
                    ExchangeData ex = mLiveDataLab.getLiveCurrencyData(mGlobalVar.getCurrencyCodeInEng());
                    sString = ex.getDate();
                }
                else
                {
                    tvContent.setText("");
                    return;
                }

                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date dateTemp = new Date();
                try {
                    dateTemp = formatter.parse(sString);
                } catch (ParseException ee) {
                    ee.printStackTrace();
                }
                sString = fm.format(dateTemp);

                sString += "\r\n " + e.getVal();

                tvContent.setText(sString);
            }
            catch (Exception ex)
            {
                Log.e("MarkerView"," Array Error");
            }
        }
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }

    @Override
    public void draw(Canvas canvas, float posx, float posy) {

        layout.setBackgroundResource(mGlobalVar.getGraphBubbleId());
        posx += (float)this.getXOffset(posx);
        posy += (float)this.getYOffset(posy);
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int nMarkerHeight = this.getHeight();
        int nMarkerWidth = this.getWidth();
        int nXOffset = 10;
        if(posx > width-nMarkerWidth-nXOffset)
            posx = width-nMarkerWidth-nXOffset;
//        canvas.translate(width-210, height-100);
        canvas.translate(posx, posy);

        this.draw(canvas);
        canvas.translate(-posx, -posy);
    }
}
