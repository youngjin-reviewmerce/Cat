package com.reviewmerce.exchange.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;


public class OpeningActivity extends FragmentActivity{
      //  implements  baseOnebuyFragment.FragmentChangeListener{
    RelativeLayout rl;
    private Bitmap bmpBackground1 = null;
    private Bitmap bmpBackground2 = null;
    private ImageView ivBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_opening);
        rl = (RelativeLayout)findViewById(R.id.background);
        ivBitmap = (ImageView)findViewById(R.id.ivBitmap);
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ShoppingDataLab dataLab = ShoppingDataLab.get(null);
        imageLoader.displayImage("assets://image/"+"opening_background.png",ivBitmap,dataLab.getDisplayOptions());
//        imageLoader.displayImage("file://"+item.getLocalUrl(),holder.image,dataLab.getDisplayOptions());
  //      getBitmapFile1("opening_background.png");
    //    getBitmapFile2("opening_background2.png");
//        ivBitmap.setImageBitmap(bmpBackground1);

        //mAzureTotal.authenticate(false);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = getIntent();
                intent.putExtra("result", BasicInfo.SEQUENCE_LOGINOK);
                // setResult(BasicInfo.ACTIVITY_COMMUNICATION_OPENINGACTIVITY, intent);
                setResult(BasicInfo.SEQUENCE_LOGINOK, intent);
                finish();

                //next();       // 3 초후 이미지를 닫아버림
            }
        }, 1000);


        //   setContentView(R.layout.layout_common_kakao_login);

    }
    public void next()
    {
        //rl.setBackgroundResource(R.drawable.opening_background2);
        ivBitmap.setImageBitmap(bmpBackground2);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();       // 3 초후 이미지를 닫아버림
            }
        }, 1000);
        Intent intent = getIntent();
        intent.putExtra("result", BasicInfo.SEQUENCE_LOGINOK);
        // setResult(BasicInfo.ACTIVITY_COMMUNICATION_OPENINGACTIVITY, intent);
        setResult(BasicInfo.SEQUENCE_LOGINOK, intent);
    }
    /*
    @Override
    public void newWebpage(String url)
    {
    }

    @Override
    public void chgTopbar(int nTopbarType, String sTopbarName) {

    }

    @Override
    public void chgFragment(int nMyIndex, int nDirect) {


        if(nMyIndex == BasicInfo.FRAGMENT_LOGINFRAGMENT) // LoginFragment or topviewLoginButton
        {
            Intent intent = getIntent();
            if(nDirect == BasicInfo.SEQUENCE_LOGINOK)
            {
                intent.putExtra("result", BasicInfo.SEQUENCE_LOGINOK);
               // setResult(BasicInfo.ACTIVITY_COMMUNICATION_OPENINGACTIVITY, intent);
                setResult(BasicInfo.SEQUENCE_LOGINOK, intent);
            }
            else
            {
                intent.putExtra("result", BasicInfo.SEQUENCE_LOGINOK);
                // setResult(BasicInfo.ACTIVITY_COMMUNICATION_OPENINGACTIVITY, intent);
                setResult(BasicInfo.SEQUENCE_LOGINOK, intent);

                //replaceFragment(BasicInfo.FRAGMENT_LOGINFRAGMENT);
            }
        }
    }
   */

    private void getBitmapFile1(String image)
    {
        String InternalPath = "/data/data/com.reviewmerce.shipping/";
//        if(bitmapFlagFile==null)
        {
            String sFilename = InternalPath + "image/" + image;
            try {

                if (bmpBackground1 != null) {
                    bmpBackground1.recycle();
                    bmpBackground1 = null;
                }

                bmpBackground1 = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        //return bitmapFile;
    }
    private void getBitmapFile2(String image)
    {
        String InternalPath = "/data/data/com.reviewmerce.shipping/";
//        if(bitmapFlagFile==null)
        {
            String sFilename = InternalPath + "image/" + image;
            try {

                if (bmpBackground2 != null) {
                    bmpBackground2.recycle();
                    bmpBackground2 = null;
                }

                bmpBackground2 = BitmapFactory.decodeFile(sFilename);
            } catch (Exception e) {
                Log.i("", "makeBmpError");
            }
        }
        //return bitmapFile;
    }

    @Override
    protected void onStop() {
        if (bmpBackground1 != null) {
            bmpBackground1.recycle();
            bmpBackground1 = null;
        }
        if (bmpBackground2 != null) {
            bmpBackground2.recycle();
            bmpBackground2 = null;
        }
        super.onStop();
    }

}
