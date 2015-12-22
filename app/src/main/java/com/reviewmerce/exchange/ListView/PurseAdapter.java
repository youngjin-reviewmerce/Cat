package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.BankData;
import com.reviewmerce.exchange.commonData.BankItem;
import com.reviewmerce.exchange.commonData.PurseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onebuy on 2015-09-07.
 */
public class PurseAdapter extends BaseAdapter {


    private Context mContext;

    private List<PurseData> mItems = new ArrayList<PurseData>();
    private String mCurrency="";
    public PurseAdapter(Context context) {
        mContext = context;
    }

    public void addItem(PurseData it) {
        mItems.add(it);

    }
    public void delItem(PurseData it) {
        mItems.remove(it);
    }
    public void setListItems(List<PurseData> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }
    public void setCurrencyStr(String sCurrency)
    {
        mCurrency = sCurrency;
    }
    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GlobalVar gv = GlobalVar.get();
        PurseView itemView;
        if (convertView == null) {
            itemView = new PurseView(mContext, mItems.get(position));
            itemView.setText(0, mItems.get(position).getTitle());
            itemView.setText(1, mItems.get(position).getDate());
            String sTemp=String.format("%.2f",mItems.get(position).getValue());
            String sCurrencycode = gv.getCurrencyChar(mItems.get(position).getCurrencyCode());
            itemView.setText(2,  sCurrencycode + " " + sTemp);
//            itemView.setBackgroundResource(R.color.item_color_0);
        } else {
            itemView = (PurseView) convertView;
//            itemView.setBackgroundResource(R.color.item_color_0);
            itemView.setText(0, mItems.get(position).getTitle());
            itemView.setText(1, mItems.get(position).getDate());
            String sTemp=String.format("%.2f",mItems.get(position).getValue());
            String sCurrencycode = gv.getCurrencyChar(mItems.get(position).getCurrencyCode());
            itemView.setText(2,  sCurrencycode + " " + sTemp);
        }
        return itemView;
    }
}
