package com.reviewmerce.exchange.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectMonitorThread;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.ListView.BankAdapter;
import com.reviewmerce.exchange.ListView.BankExchangeAdapter;
import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.BankExchangeItem;
import com.reviewmerce.exchange.commonData.CurrencyItem;
import com.reviewmerce.exchange.custom.MyMenuBtn;
import com.reviewmerce.exchange.publicClass.BankDataLab;
import com.reviewmerce.exchange.testClass.BankSortedData;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TutorialFragment extends baseOnebuyFragment implements AdapterView.OnItemSelectedListener {


    private ImageView mBackgroundView;
    private Bitmap mBackgroundBitmap=null;
    private int mTutorialIndex = 0;
    public TutorialFragment() {
        mTutorialIndex = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);
        mTutorialIndex = 0;
       // mLayout = (LinearLayout)v.findViewById(R.id.mainLayout_bank);
        mBackgroundView = (ImageView)v.findViewById(R.id.ivScreen_tutorial);

//        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.bankMainLinear);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    m_nPreTouchPosX = (int) event.getX();
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int nTouchPosX = (int) event.getX();

                    if (nTouchPosX < m_nPreTouchPosX - BasicInfo.g_nMovePos)   // 오른쪽
                    {
                        //    Intent intent = new Intent(getActivity(), BankActivity.class);
                        //    startActivity(intent);
                        chgBackground(BasicInfo.InternalPath + "image/tutorial/",1);
                    } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                        chgBackground(BasicInfo.InternalPath + "image/tutorial/",-1);
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return true;
            }
        });

        chgBackground(BasicInfo.InternalPath + "image/tutorial/",0);
        return v;
    }


    public void chgBackground(String sFoldername,int nValue)
    {
        mTutorialIndex += nValue;
        if(mTutorialIndex < 0 )
            mTutorialIndex = 0;
        if(mTutorialIndex >= BasicInfo.g_nTutirialCount)
            mCallback.chgFragment(6, 1);

        String sFilename = String.format("%stutorial_%02d.jpg",sFoldername, mTutorialIndex+1);
        try {
            if (mBackgroundBitmap != null)
                mBackgroundBitmap.recycle();
                mBackgroundBitmap = BitmapFactory.decodeFile(sFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBackgroundView.setImageBitmap(mBackgroundBitmap);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
