package com.reviewmerce.exchange.Bank;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onebuy on 2015-09-07.
 */
public class BankAdapter extends BaseAdapter {

    private Context mContext;

    private List<BankItem> mItems = new ArrayList<BankItem>();

    public BankAdapter(Context context) {
        mContext = context;
    }

    public void addItem(BankItem it) {
        mItems.add(it);
    }

    public void setListItems(List<BankItem> lit) {
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
        BankView itemView;
        if (convertView == null) {
            itemView = new BankView(mContext, mItems.get(position));
//            itemView.setBackgroundResource(R.color.item_color_0);
        } else {
            itemView = (BankView) convertView;
//            itemView.setBackgroundResource(R.color.item_color_0);
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
        }
        return itemView;
    }
}
