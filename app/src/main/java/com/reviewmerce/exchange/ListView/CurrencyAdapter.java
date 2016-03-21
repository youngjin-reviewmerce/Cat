package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.reviewmerce.exchange.ListView.CurrencyView;
import com.reviewmerce.exchange.commonData.CurrencyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onebuy on 2015-08-18.
 */
public class CurrencyAdapter  extends BaseAdapter {

private Context mContext;

private List<CurrencyItem> mItems = new ArrayList<CurrencyItem>();

    public CurrencyAdapter(Context context) {
        mContext = context;
    }

    public void addItem(CurrencyItem it) {
        mItems.add(it);
    }

    public void setListItems(List<CurrencyItem> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
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
        CurrencyView itemView;
        if (convertView == null) {
            itemView = new CurrencyView(mContext, mItems.get(position));
//            itemView.setBackgroundResource(R.color.item_color_0);
        } else {
            itemView = (CurrencyView) convertView;
//            itemView.setBackgroundResource(R.color.item_color_0);
            itemView.setIcon(mItems.get(position).getIcon());
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));

        }
        return itemView;
    }
}
