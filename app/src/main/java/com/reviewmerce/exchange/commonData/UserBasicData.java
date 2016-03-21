package com.reviewmerce.exchange.commonData;

/**
 * Created by onebuy on 2015-09-07.
 */
public class UserBasicData {

    //private int mIndex=0;           // 기간
    public String getId() {
        return mId;
    }

    public void setId(String sId) {
        this.mId = sId;
    }


    public String getLoginType() {
        return mLoginType;
    }

    public void setLoginType(String sLoginType) {
        this.mLoginType = sLoginType;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String sToken) {
        this.mToken = sToken;
    }
    public void setLastFragment(int nScreen)
    {
        mLastFragment = nScreen;
    }
    public int getLastFragment()
    {
        return mLastFragment;
    }
    private String mId;
    private String mLoginType;
    private String mToken;
    private int mLastFragment;

    public UserBasicData() {
        mLastFragment = 0;
    }
    public UserBasicData(String sId, String sLoginType, String sToken)
    {
        setData(sId, sLoginType, sToken);
    }
    public void setData(UserBasicData bd)
    {
        mId = bd.mId;
        mLoginType = bd.mLoginType;
        mToken = bd.mToken;
    }
    public void initData()
    {
        mId = "";
        mLoginType = "";
        mToken = "";
    }
    public void setData(String sId, String sLoginType, String sToken) {
        mId = sId;
        mLoginType = sLoginType;
        mToken = sToken;
    }
}
