package com.reviewmerce.exchange.publicClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.UserBasicData;
import com.reviewmerce.exchange.commonData.apiData.ImageAndLink;
import com.reviewmerce.exchange.commonData.apiData.ListedItem;
import com.reviewmerce.exchange.commonData.apiData.SuggestKeyword;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by onebuy on 2015-09-07.
 */
public class ShoppingDataLab {

    private static ShoppingDataLab sUserDataLab;
    final int MAX_SEARCHED_COUNT = 3;
    private DisplayImageOptions mDisplayOptions;
    public static ShoppingDataLab get(Context c) {
            if (sUserDataLab == null) {
                sUserDataLab = new ShoppingDataLab(c.getApplicationContext());
            }
            return sUserDataLab;
    }
    ////////////////////////////////////////////////////////////////////////////

    private Context mAppContext;

    private ArrayList<UserBasicData> mUserData;
    private ArrayList<SuggestKeyword> mSearchKeywordData;
    private ArrayList<ListedItem> mSearchedListedData;
    private ArrayList<ImageAndLink> mImageLinkItemList;
    private String mLastSearchKeyword;
    public DisplayImageOptions getDisplayOptions()
    {
        return mDisplayOptions;
    }
    public ShoppingDataLab(Context appContext) {
        if(appContext != null) {
            mAppContext = appContext;
            initData();
        }
    }

    public int setBasicData(UserBasicData udb) {
        mUserData.clear();
        mUserData.add(udb);
        return mUserData.size();
    }
    ///////////////////////////////////////////////////////////////////////////////////////



    public void clearBasicData()
    {
        mUserData.clear();
    }



    public void initData()
    {
        if(mUserData == null)
        {
            mUserData = new ArrayList<UserBasicData>();
        }
        else
        {
            mUserData.clear();
        }
        if(mSearchKeywordData == null)
        {
            mSearchKeywordData = new ArrayList<SuggestKeyword>();
        }
        else
        {
            mSearchKeywordData.clear();
        }
        if(mSearchedListedData == null)
        {
            mSearchedListedData = new ArrayList<ListedItem>();
        }
        else
        {
            mSearchedListedData.clear();
        }
        if(mImageLinkItemList == null)
        {
            mImageLinkItemList = new ArrayList<ImageAndLink>();
        }
        else
        {
            mImageLinkItemList.clear();
        }

        mDisplayOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.icon_login) // resource or drawable
 //               .showImageForEmptyUri(R.drawable.icon_search) // resource or drawable
  //              .showImageOnFail(R.drawable.icon_calc) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
//                .preProcessor(...)
//        .postProcessor(...)
//        .extraForDownloader(...)
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                        //        .decodingOptions(...)
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
    }

    public void addSearchedData(SuggestKeyword item)
    {
        if(mSearchKeywordData.size()>=MAX_SEARCHED_COUNT)
        {
            mSearchKeywordData.remove(0);
        }
        mSearchKeywordData.add(item);
        try {
            saveSearchedData();
        }catch(Exception e)
        {
            Log.e("Shipping Exception",e.toString());
        }
    }
    public int  getSearchedDataCount()
    {
        return mSearchKeywordData.size();
    }
    public void saveSearchedData() throws JSONException, IOException
    {
        JSONArray array = new JSONArray();
        for(SuggestKeyword item : mSearchKeywordData)
        {
//            JSONObject json = new JSONObject();
//            json.put("keyword",item.getKeyword());
//            json.put("description",item.getDescription());
            JSONObject json = item.toJson();
            array.put(json);
        }
        Writer writer = null;
        try{
            OutputStream out = mAppContext.openFileOutput("searched.txt",Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if(writer != null)
                writer.close();
        }
    }
    public SuggestKeyword getSearchedData(int nIndex)
    {
        if(nIndex >= mSearchKeywordData.size())
            return null;
        SuggestKeyword item = mSearchKeywordData.get(nIndex);
        return item;
    }
    public void loadSearchedData() throws JSONException, IOException
    {

        BufferedReader reader = null;
        try
        {
            InputStream in = mAppContext.openFileInput("searched.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine())!=null)
            {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            if(array.length()>0)
                mSearchKeywordData.clear();                // 이거 데이터 있을때 할것.
            for(int i=0; i< array.length();i++)
            {
                JSONObject json = array.getJSONObject(i);
                //SuggestKeyword item = new SuggestKeyword(json.getString("keyword"),json.getString("description"));
                SuggestKeyword item = new SuggestKeyword(json);
                item.setType(1);
                mSearchKeywordData.add(item);
            }
        }catch (FileNotFoundException e)
        {

        }
        finally {
            if(reader != null)
            {
                reader.close();
            }
        }
    }
    public int getSearchedData()
    {
        return mSearchedListedData.size();
    }
    public void change_SearchListedItem_jsonString_to_data(String sResult)
    {
        JSONObject json = null;
        JSONObject jsonImangelink;
        JSONArray jArray;
        try {
            jArray = new JSONArray(sResult);
            if(jArray.length()>0)
                mSearchedListedData.clear();
            for (int i = 0; i < jArray.length(); i++) {
                jsonImangelink = jArray.getJSONObject(i);
                json = new JSONObject(jsonImangelink.getString("ListedItem"));
                ListedItem item = new ListedItem(json);
                mSearchedListedData.add(item);
            }
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }
    }
    public void change_MainImageLink_jsonString_to_data(String sResult)
    {
        JSONObject json = null;
        JSONObject jsonImangelink;
        JSONArray jArray;
        try {
            jArray = new JSONArray(sResult);
            if(jArray.length()>0)
                mImageLinkItemList.clear();
            for (int i = 0; i < jArray.length(); i++) {
                jsonImangelink = jArray.getJSONObject(i);
                json = new JSONObject(jsonImangelink.getString("ImageAndLink"));
                ImageAndLink item = new ImageAndLink(json);
                mImageLinkItemList.add(item);
            }
        } catch (Exception e) {
            Log.e("HTTP", e.toString());
        }
    }
    public ArrayList<ListedItem> getSearchedListData()
    {
        return mSearchedListedData;
    }
    public String getLastSearchKeyword()
    {
        return mLastSearchKeyword;
    }
    public void setLastSearchKeyword(String sKeyword)
    {
        mLastSearchKeyword = sKeyword;
    }
    public void proc_localImg_ImageLink()
    {
        //String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );
        //String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
        String filename,url,localFile;
        for(ImageAndLink item : mImageLinkItemList)
        {
            url = item.getImgUrl();
            filename = url.substring(url.lastIndexOf('/') + 1, url.length());
            localFile = BasicInfo.ImageTempPath+filename;
            if(isExistFile(localFile)==false)
                downloadFile(url,localFile);
            item.setLocalUrl(localFile);
        }
    }
    boolean isExistFile(String filename)
    {
        File files = new File(filename);
        if(files.exists()==true)
            return true;
        else
            return false;
    }
    // 서버에서 다운로드 한 데이터를 파일로 저장
    boolean downloadFile(String strUrl, String fileName) {
        try {
            URL url = new URL(strUrl);
            // 서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 입력 스트림을 구한다
            InputStream is = conn.getInputStream();
            // 파일 저장 스트림을 생성
            FileOutputStream fos = new FileOutputStream(fileName);

            // 입력 스트림을 파일로 저장
            byte[] buf = new byte[1024];
            int count;
            while( (count = is.read(buf)) > 0 ) {
                fos.write(buf, 0, count);
            }
            // 접속 해제
            conn.disconnect();
            // 파일을 닫는다
            fos.close();
        } catch (Exception e) {
            Log.d("tag", "Image download error.");
            return false;
        }
        return true;
    }
    public void initDummyData_main()
    {
        if(mImageLinkItemList.size()>0)
            mImageLinkItemList.clear();
        ImageAndLink item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID001_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID002_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID003_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID004_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID005_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID006_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID007_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(0,"mid",1,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/item/RID008_s.jpg","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(1,"top",2,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/ui/EID001_w.png","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(1,"top",2,"https://s3.ap-northeast-2.amazonaws.com/shipping.reviewmerce/ui/EID002_w.png","http://dong-dong.azurewebsites.net/","","","");
        mImageLinkItemList.add(item);
        item = new ImageAndLink(item);
    }
    public void initDummyData_searchKeyword()
    {
        ListedItemDatabase db = ListedItemDatabase.getInstance(null);
        db.excuteDBLoad_ListedItem(mSearchedListedData, "listeditem");
    }
    public void loadMainData()
    {
        ListedItemDatabase db = ListedItemDatabase.getInstance(null);
        if(mImageLinkItemList.size()>0)
            mImageLinkItemList.clear();
        db.excuteDBLoad_ImageLinkItem(mImageLinkItemList,"mainpage");

    }
    public void saveMainData()
    {
        ListedItemDatabase db = ListedItemDatabase.getInstance(null);
        db.delete_createTable_ImageLinkItem("mainpage");
        db.excuteDBSave_ImageLinkItem(mImageLinkItemList, "mainpage");
    }
    public ArrayList<ImageAndLink> getMainImageLinkList()
    {
        return mImageLinkItemList;
    }


}
