
package com.reviewmerce.exchange.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.GlobalVar;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.publicClass.NationDataLab;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class TabBottomFragment extends Fragment {
    public baseOnebuyFragment.FragmentChangeListener mCallback;
    private TextView btnGraph,btnBank,btnPurse;//,btnShopping;
    private TextView tvGraphTop,tvBankTop,tvPurseTop;//,tvShoppingTop;
    private String mTitle;
    private int mFragmentType = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_bottom_tab, container, false);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString("title");
            mFragmentType = args.getInt("type");
        }
        btnGraph = (TextView)v.findViewById(R.id.tab_Graph);
        btnBank = (TextView)v.findViewById(R.id.tab_Bank);
        btnPurse = (TextView)v.findViewById(R.id.tab_Purse);

        tvGraphTop = (TextView)v.findViewById(R.id.tab_GraphTop);
        tvBankTop = (TextView)v.findViewById(R.id.tab_BankTop);
        tvPurseTop = (TextView)v.findViewById(R.id.tab_PurseTop);


        btnGraph.setOnClickListener(listener);
        btnBank.setOnClickListener(listener);
        btnPurse.setOnClickListener(listener);

        switch(mFragmentType)
        {
            case BasicInfo.FRAGMENT_EXCHANGE_GRAPH:
                tvGraphTop.setVisibility(View.VISIBLE);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_BANK:
                tvBankTop.setVisibility(View.VISIBLE);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE:
                tvPurseTop.setVisibility(View.VISIBLE);
                break;
        }
        return v;
    }
    View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(v.equals(btnGraph))
            {
             //   changeSelectPoint(BasicInfo.FRAGMENT_EXCHANGE_GRAPH);
                mCallback.chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_GRAPH);
            }
            else if(v.equals(btnBank))
            {
             //   changeSelectPoint(BasicInfo.FRAGMENT_EXCHANGE_BANK);
                mCallback.chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_BANK);
            }
            else if(v.equals(btnPurse))
            {
             //   changeSelectPoint(BasicInfo.FRAGMENT_EXCHANGE_PURSE);
                mCallback.chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_EXCHANGE_PURSE);
            }

        }
    };
    public void changeSelectPoint(int nIndex)
    {
        tvGraphTop.setVisibility(View.INVISIBLE);
        tvBankTop.setVisibility(View.INVISIBLE);
        tvPurseTop.setVisibility(View.INVISIBLE);
        switch(nIndex)
        {
            case BasicInfo.FRAGMENT_EXCHANGE_GRAPH:
                tvGraphTop.setVisibility(View.VISIBLE);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_BANK:
                tvBankTop.setVisibility(View.VISIBLE);
                break;
            case BasicInfo.FRAGMENT_EXCHANGE_PURSE:
                tvPurseTop.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mCallback = (baseOnebuyFragment.FragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}