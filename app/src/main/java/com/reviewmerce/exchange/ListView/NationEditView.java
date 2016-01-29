package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.NationItem;

public class NationEditView extends LinearLayout {
    private ImageView countryIcon;
    private CheckBox countryCheck;
    private TextView tvEngCountry;
    private TextView tvKorCountry;

    private LinearLayout itemLayout;

    public NationEditView(final Context context, final NationItem mItem) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_nation_list_item_edit, this, true);
        //set layout
        itemLayout = (LinearLayout)findViewById(R.id.item_layout_edit);
        //set Icon
        countryIcon = (ImageView)findViewById(R.id.image_view_edit);
        countryIcon.setImageBitmap(mItem.getIcon());
        countryIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //set checkBox
        countryCheck = (CheckBox)findViewById(R.id.check_box_edit);
        countryCheck.setChecked(mItem.isChecked());
        //set Text1
        tvEngCountry = (TextView)findViewById(R.id.textView_country_edit);
        tvEngCountry.setText(mItem.getEngName());
        //set Text2
        tvKorCountry = (TextView)findViewById(R.id.textView_value_edit);
        tvKorCountry.setText(mItem.getKorName());
    }
    public void setCountryCheck(boolean bChecked){
        countryCheck.setChecked(bChecked);
    }

    public void setClickedEvent(boolean bClicked){
        if(bClicked == true){
            itemLayout.setBackgroundColor(Color.BLACK);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setText(int index, String data){
        if(index == 0){
            tvEngCountry.setText(data);
        } else if(index == 1){
            tvKorCountry.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setIcon(Bitmap icon) {
        countryIcon.setImageBitmap(icon);
    }

    public void showCheckBox(boolean bool){
        if(bool == true){
            countryCheck.setVisibility(View.VISIBLE);
        } else {
            countryCheck.setVisibility(View.INVISIBLE);
        }
    }
}