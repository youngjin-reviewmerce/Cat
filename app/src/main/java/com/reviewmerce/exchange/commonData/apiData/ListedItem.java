package com.reviewmerce.exchange.commonData.apiData;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListedItem  {

    private String RID = null;
    private String ASIN = null;
    private String imgUrl = null;
    private String itemName = null;
    private String tag = null;
    private String price = null;
    private String description = null;
    private String localUrl = null;
    private String linkUrl = null;
    private Bitmap bitmapFile;

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }


    /**
     * a 6-character alphanumeric unique identifier assigned by reviewmerce.com (Reviewmerce ID)
     **/
    public String getRID() {
        return RID;
    }
    public void setRID(String RID) {
        this.RID = RID;
    }


    /**
     * a 10-character alphanumeric unique identifier assigned by Amazon.com (Amazon Standard Identification Number)
     **/
    public String getASIN() {
        return ASIN;
    }
    public void setASIN(String ASIN) {
        this.ASIN = ASIN;
    }


    /**
     **/
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    /**
     **/
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    /**
     **/
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }


    /**
     **/
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }


    /**
     **/
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     **/
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }



    @Override

    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListedItem {\n");
        sb.append("  RID: ").append(RID).append("\n");
        sb.append("  ASIN: ").append(ASIN).append("\n");
        sb.append("  imgUrl: ").append(imgUrl).append("\n");
        sb.append("  itemName: ").append(itemName).append("\n");
        sb.append("  tag: ").append(tag).append("\n");
        sb.append("  price: ").append(price).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append(" localurl: ").append(localUrl).append("\n");
        sb.append("  linkUrl: ").append(linkUrl).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public Bitmap getImage(){

        Bitmap bmp = getBitmapFile();
        return  bmp;
    }
    public Bitmap getBitmapFile()
    {
        String InternalPath = "/data/data/com.reviewmerce.shipping/";
//      if(bitmapFlagFile==null)
        {
            String sFilename = InternalPath + "image/" + localUrl;
            try {
                if (bitmapFile != null) {
                    bitmapFile.recycle();
                    bitmapFile = null;
                }
                bitmapFile = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        return bitmapFile;
    }
    public void makeBitmapFile_inURL()
    {
        try{
            if (bitmapFile != null) {
                bitmapFile.recycle();
                bitmapFile = null;
            }
            URL myFileUrl = new URL(this.getImgUrl());
            HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();

            bitmapFile = BitmapFactory.decodeStream(is);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public Bitmap getLoadedImageBitmap()
    {
        return bitmapFile;
    }
    public ListedItem(ListedItem item)
    {
        setListedItemData(item);
    }
    public ListedItem(JSONObject json)throws JSONException
    {
        toData(json);
    }
    public ListedItem()
    {

    }
    public ListedItem(String rid, String asin, String imgurl, String itemname, String tag, String price, String description, String linkurl)
    {
        this.RID = rid;
        this.ASIN = asin;
        this.imgUrl = imgurl;
        this.itemName = itemname;
        this.tag = tag;
        this.price = price;
        this.description = description;
        this.linkUrl = linkurl;
        this.localUrl = "";
    }

    public void setListedItemData(ListedItem item)
    {
        this.RID = item.getRID();
        this.ASIN = item.getASIN();
        this.imgUrl = item.getImgUrl();
        this.itemName = item.getItemName();
        this.tag = item.getTag();
        this.price = item.getPrice();
        this.description = item.getDescription();
        this.linkUrl = item.getLinkUrl();
        this.localUrl = item.getLocalUrl();
    }
    public JSONObject toJson() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("RID",this.getRID());
        json.put("ASIN",this.getASIN());

        json.put("img_url",this.getImgUrl());
        json.put("item_name",this.getItemName());
        json.put("tag",this.getTag());
        json.put("price", this.getPrice());
        json.put("description", this.getDescription());
        json.put("link_url",this.getLinkUrl());
//        json.put("localurl",this.getLocalUrl());
        return json;
    }
    public void toData(JSONObject json) throws JSONException
    {
        this.RID = json.getString("RID");
        this.ASIN = json.getString("ASIN");
        this.imgUrl = json.getString("img_url");
        this.itemName = json.getString("item_name");
        this.tag = json.getString("tag");
        this.price = json.getString("price");
        this.description = json.getString("description");
        this.linkUrl = json.getString("link_url");
        //this.localUrl = json.getString("localurl");
    }

}
