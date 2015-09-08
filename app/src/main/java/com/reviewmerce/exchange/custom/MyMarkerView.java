
package com.reviewmerce.exchange.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.innerProc.ExchangeData;
import com.reviewmerce.exchange.innerProc.ExchangeDataLab;

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    ExchangeDataLab mExchangeLab = null;
    private ArrayList<ExchangeData> mDataArray=null;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mExchangeLab = ExchangeDataLab.get(null);

        tvContent = (TextView) findViewById(R.id.tvContent);
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
                if(index < mDataArray.size())
                    tvContent.setText(mDataArray.get(index).getDate() + "\r\n" + e.getVal());
                else
                    tvContent.setText("");

            }
            catch (Exception ex)
            {
                Log.e("MarkerView"," Array Error");
            }
        }
    }

    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }

    @Override
    public void draw(Canvas canvas, float posx, float posy) {

        posx += (float)this.getXOffset();
        posy += (float)this.getYOffset();
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int nMarkerHeight = this.getHeight();
        int nMarkerWidth = this.getWidth();

        if(posx > width-nMarkerWidth)
            posx = width-nMarkerWidth;
//        canvas.translate(width-210, height-100);
        canvas.translate(posx, posy);

        this.draw(canvas);
        canvas.translate(-posx, -posy);
    }
}
