package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.MenuData;
import com.reviewmerce.exchange.commonData.PurseData;

/**
 * Created by onebuy on 2015-08-18.
 */
public class MenuView extends LinearLayout {

    private CheckBox mChkBox01;
    private TextView mText01;


    public MenuView(Context context, MenuData Item) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_listrow, this, true);

        // Set Icon
//        mIcon = (ImageView) findViewById(R.id.imgContury);
//        mIcon.setImageDrawable(aItem.getIcon());
//        mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Set Text 01
        mText01 = (TextView) findViewById(R.id.menu_listrow_tvRow2);
        mText01.setText(Item.getTitle());
        mChkBox01 = (CheckBox) findViewById(R.id.menu_listrow_cbRow1);
        mChkBox01.setClickable(false);
        mChkBox01.setFocusable(false);

        if(Item.getType()==1) {
            mChkBox01.setVisibility(View.VISIBLE);

            mChkBox01.setChecked(Item.getIsChecked());
        }
        else
        {
            mChkBox01.setVisibility(View.GONE);
        }
    }

    public void setTitle(String data) {

        mText01.setText(data);
    }
    public void setCheck(boolean b)
    {
        mChkBox01.setChecked(b);
    }
}
