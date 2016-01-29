package com.reviewmerce.exchange.commonData;

import android.graphics.Bitmap;

public class NationItem {
    private Bitmap mIcon;
    private String mEngName;
    private String mKorName;
    private boolean mChecked = true;
    private boolean mSelectable;

    public NationItem(Bitmap icon, String sEng, String sKor){
        mChecked = true;
        mIcon = icon;
        mEngName = sEng;
        mKorName = sKor;
    }

    public NationItem(boolean checked, Bitmap icon, String sEng, String sKor){
        mChecked = checked;
        mIcon = icon;
        mEngName = sEng;
        mKorName = sKor;
        mSelectable = false;
    }

    public boolean isChecked(){
        return mChecked;
    }

    public void setChecked(boolean checked){
        mChecked = checked;
    }


    public boolean isSelectable(){
        return mSelectable;
    }
    public void setSelectable(boolean selectable){
        mSelectable = selectable;
    }

    public String getKorName(){
        return mKorName;
    }
    public String getEngName(){
        return mEngName;
    }

    public Bitmap getIcon(){
        return mIcon;
    }
}
