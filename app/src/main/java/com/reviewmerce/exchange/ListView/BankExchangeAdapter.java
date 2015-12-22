package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.reviewmerce.exchange.commonData.BankExchangeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onebuy on 2015-09-07.
 */
public class BankExchangeAdapter extends BaseAdapter {

    private Context mContext;

    private List<BankExchangeItem> mItems = new ArrayList<BankExchangeItem>();

    public BankExchangeAdapter(Context context) {
        mContext = context;
    }

    public void addItem(BankExchangeItem it) {
        mItems.add(it);
    }

    public void setListItems(List<BankExchangeItem> lit) {
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
        BankExchangeView itemView;
        if (convertView == null) {
            itemView = new BankExchangeView(mContext, mItems.get(position));
//            itemView.setBackgroundResource(R.color.item_color_0);
        } else {
            itemView = (BankExchangeView) convertView;
//            itemView.setBackgroundResource(R.color.item_color_0);
            itemView.setText(0, mItems.get(position).getData(0));

        }
        return itemView;
    }
}
