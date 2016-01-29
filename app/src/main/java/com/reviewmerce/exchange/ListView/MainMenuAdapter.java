package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.commonData.MenuData;
import com.reviewmerce.exchange.commonData.PurseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onebuy on 2015-09-07.
 */
public class MainMenuAdapter extends BaseAdapter {


    private Context mContext;

    private List<MenuData> mItems = new ArrayList<MenuData>();
    private String mCurrency="";
    public MainMenuAdapter(Context context) {
        mContext = context;
    }

    public void addItem(MenuData it) {
        mItems.add(it);

    }
    public void delItem(PurseData it) {
        mItems.remove(it);
    }
    public void setListItems(List<MenuData> lit) {
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
        MenuView itemView;
        GlobalVar gv = GlobalVar.get();
        if (convertView == null) {
            itemView = new MenuView(mContext, mItems.get(position));
            itemView.setTitle(mItems.get(position).getTitle());

            itemView.setCheck(gv.getNetworkMode());
//            itemView.setBackgroundResource(R.color.item_color_0);
        } else {
            itemView = (MenuView) convertView;
//            itemView.setBackgroundResource(R.color.item_color_0);
            itemView.setTitle(mItems.get(position).getTitle());
            itemView.setCheck(gv.getNetworkMode());
        }
        return itemView;
    }
    public void setCheck(int nPosition,boolean bCheck)
    {
        if(mItems.get(nPosition).getType()==0)
        mItems.get(nPosition).setIsChecked(bCheck);
        }
    }
