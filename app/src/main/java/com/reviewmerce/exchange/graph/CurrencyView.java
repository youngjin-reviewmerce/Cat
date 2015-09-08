package com.reviewmerce.exchange.graph;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.R;

/**
 * Created by onebuy on 2015-08-18.
 */
public class CurrencyView extends LinearLayout {
    /**
     * Icon
     */
    private ImageView mIcon;

    /**
     * TextView 01
     */
    private TextView mText01;
    private TextView mText02;

    /**
     * TextView 02
     */
 //   private TextView mText02;

    /**
     * TextView 03
     */
  //  private TextView mText03;

    public CurrencyView(Context context, CurrencyItem aItem) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_list_currency, this, true);

        // Set Icon
        mIcon = (ImageView) findViewById(R.id.imgContury);
        mIcon.setImageDrawable(aItem.getIcon());
        mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Set Text 01
        mText01 = (TextView) findViewById(R.id.tvCurrencyCode);
        mText01.setText(aItem.getData(0));

        mText02 = (TextView) findViewById(R.id.tvNation);
        mText02.setText(aItem.getData(1));

    }

    /**
     * set Text
     *
     * @param index
     * @param data
     */
    public void setText(int index, String data) {
        if (index == 0) {
            mText01.setText(data);
        }
        else if (index == 1) {
            mText02.setText(data);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * set Icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }
}
