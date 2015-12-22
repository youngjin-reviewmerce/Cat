
package com.reviewmerce.exchange.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.util.AttributeSet;
import android.view.View;
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

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyCircularView extends View {
    GlobalVar gv = GlobalVar.get();
    private Paint mPaintCircle = null;

    private int diameter = 80;

    private int nMaxProgress=4;
    private int nSelectProgress=nMaxProgress-1;

    public MyCircularView(Context ct) {
        super(ct);
    }
    public MyCircularView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    Paint mPaint;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawThum(canvas);
    }
    private void drawThum(Canvas canvas) {
        GlobalVar gv = GlobalVar.get();
        int nWidth = this.getWidth();
        int nHeight = this.getHeight() ;
        int drawX = 0;
        int drawY = 0;
        int nSliceX = 0;
        int nSliceY = 0;
        diameter = nWidth / 12;
        if(nMaxProgress == 1)
        {
            nSliceX = nWidth;
        }
        else if(nMaxProgress != 0) {
            nSliceX = nWidth / (nMaxProgress);
        }
        else
            return;
        nSliceY = diameter / 2;
        drawX = (nSelectProgress*nSliceX) + (nSliceX/2); // 전체 크기 절반으로 위치 이동
        drawY = nSliceY;

//        if(nSelectProgress==nMaxProgress-1)
//        {
//            drawX = nWidth + diameter;
//        }
        int arcRadius = (diameter - 15) / 4;

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);


        mPaintCircle.setColor(gv.getGraphColor());

        canvas.drawCircle(drawX, drawY, arcRadius, mPaintCircle);

        int colorCalc = Color.parseColor("#00ffffff");
        int color = gv.getGraphColor();
        color = color & colorCalc;
        colorCalc = Color.parseColor("#30000000");
        color = color | colorCalc;
        mPaintCircle.setColor(color);

        Paint pnt = new Paint();
        pnt.setStyle(Style.STROKE);
        pnt.setStrokeWidth(3);
        pnt.setColor(gv.getGraphColor());
        pnt.setAntiAlias(true);
        arcRadius = (diameter - 15) / 2;
        canvas.drawCircle(drawX, drawY, arcRadius, mPaintCircle);

        Paint testP = new Paint();
        testP.setAntiAlias(true);
        testP.setColor(Color.parseColor("#005500"));

        /*
        canvas.drawRect(0, 0, nSliceX, nSliceY, mPaintCircle);
        canvas.drawRect(nSliceX,0,nSliceX*2,nSliceY,testP);
        canvas.drawRect(nSliceX*2,0,nSliceX*3,nSliceY,mPaintCircle);
        canvas.drawRect(nSliceX*3,0,nSliceX*4,nSliceY,testP);
        */
    }
    private void drawText()
    {
        GlobalVar gv = GlobalVar.get();
        int nWidth = this.getWidth();
        int nHeight = this.getHeight() / 2;
        int drawX = 0;
        int drawY = 0;

        for(int x=0 ; x<4 ; x++)
        {

        }
    }

    public int getMaxProgress()
    {
        return this.nMaxProgress;
    }
    public void setMaxProgress(int nMax)
    {
        this.nMaxProgress = nMax;
    }
    public int getSelectProgress()
    {
        return this.nSelectProgress;
    }
    public void setSelectProgress(int nSelect)
    {
        this.nSelectProgress = nSelect;
    }
    public void immediateDraw()
    {
        invalidate();
    }
    public int calcPoint_to_Progress(float x, float y)
    {
        if (nMaxProgress <= 1 )
        {
            return -1;
        }
        float fSlice = this.getWidth() / (nMaxProgress);
        float minX=.0f;
        float maxX=.0f;
        int nRtnVal = -1;
        for (int i=0; i<nMaxProgress-1 ; i++)
        {
            maxX = (i+1) * fSlice;
            if(maxX >this.getWidth())
                maxX = this.getWidth();

           if((minX <= x) && (x<maxX)) {
               nRtnVal = i;
               break;
           }
            minX = maxX;
        }
        if(nRtnVal < 0)
            nRtnVal = nMaxProgress-1;
        nSelectProgress = nRtnVal;
        invalidate();
        return nSelectProgress;
    }



}