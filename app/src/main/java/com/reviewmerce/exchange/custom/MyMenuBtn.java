
package com.reviewmerce.exchange.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.reviewmerce.exchange.GlobalVar;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMenuBtn extends View {

    int m_nWidth = 80;
    int m_nHeight = 100;

    public MyMenuBtn(Context ct) {
        super(ct);
    }
    public MyMenuBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    Paint mPaint;
    Paint mPaintEraser;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawIcon(canvas);
    }
    private void drawIcon(Canvas canvas) {
        GlobalVar gv = GlobalVar.get();
        m_nWidth = this.getWidth();
        m_nHeight = this.getHeight();
        mPaint = new Paint();
        mPaint.setColor(gv.getGraphColor());
        mPaintEraser = new Paint();
        mPaintEraser.setColor(Color.parseColor("#00000000"));
        int nHeight = m_nHeight / 10;
        int nWidth = m_nWidth / 3;
        //nHeight = 5;
        canvas.drawRect(nWidth/2,0,m_nWidth-nWidth/2,nHeight,mPaint);
        canvas.drawRect(nWidth/2,nHeight*2,m_nWidth-nWidth/2,nHeight*3,mPaint);
        canvas.drawRect(nWidth/2,nHeight*4,m_nWidth-nWidth/2,nHeight*5,mPaint);

    }



}