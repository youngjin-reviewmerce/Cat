package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.BankItem;
import com.reviewmerce.exchange.commonData.PurseData;

/**
 * Created by onebuy on 2015-08-18.
 */
public class PurseView extends LinearLayout {
    /**
     * Icon
     */
 //   private ImageView mIcon;

    /**
     * TextView 01
     */
    private TextView mText01;
    private TextView mText02;
    private TextView mText03;

    /**
     * TextView 02
     */
 //   private TextView mText02;

    /**
     * TextView 03
     */
  //  private TextView mText03;

    public PurseView(Context context, PurseData aItem) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.purse_listrow, this, true);

        // Set Icon
//        mIcon = (ImageView) findViewById(R.id.imgContury);
//        mIcon.setImageDrawable(aItem.getIcon());
//        mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Set Text 01
        mText01 = (TextView) findViewById(R.id.tvPurseRow1);
        mText01.setText(aItem.getTitle());

        mText02 = (TextView) findViewById(R.id.tvPurseRow2);
        mText02.setText(aItem.getDate());

        mText03 = (TextView) findViewById(R.id.tvPurseRow3);
        mText03.setText(String.valueOf(aItem.getValue()));

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
        else if (index == 2) {
            mText03.setText(data);
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
        //mIcon.setImageDrawable(icon);
    }
}
