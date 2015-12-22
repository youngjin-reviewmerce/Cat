package com.reviewmerce.exchange.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.publicClass.PurseDataLab;

import org.w3c.dom.Text;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurseBudgetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PurseBudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurseBudgetFragment extends baseOnebuyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView m_tvDate1;
    TextView m_tvDate2;
    TextView m_tvBudget;
    Handler mHandlerHttp;
    PurseDataLab mDataLab = null;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurseBudgetFragment newInstance(String param1, String param2) {
        PurseBudgetFragment fragment = new PurseBudgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PurseBudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    //    Toast.makeText(getActivity(),"OnCreate", Toast.LENGTH_SHORT).show();
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_purse_budget, container, false);
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.purseMainLinear);
//        Toast.makeText(getActivity(),"OnCreateView", Toast.LENGTH_SHORT).show();
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
                        //                     recycleView(mLayout);
                        mCallback.chgFragment(3, 1);
                    } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                        //                       recycleView(mLayout);
                        mCallback.chgFragment(3, -1);
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return false;
            }
        });
        mHandlerHttp = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BasicInfo.TYPE_PURSE:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정

                        break;
                }
            }
        };
        mDataLab = PurseDataLab.get(getActivity());
        initControlValue(v);
        testFunc();

        return v;
    }
    public void initControlValue(View v) {
        m_tvBudget =  (TextView) v.findViewById(R.id.tv_purse_budget_budget);
        m_tvDate1 =  (TextView) v.findViewById(R.id.tv_purse_budget_date1);
        m_tvDate2 =  (TextView) v.findViewById(R.id.tv_purse_budget_date2);

        m_tvDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nYear = mDataLab.getBeginDate_year();
                int nMonth = mDataLab.getBeginDate_month();
                int nDay = mDataLab.getBeginDate_day();
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mDataLab.setBeginDate(year,monthOfYear,dayOfMonth);
                                dataToView();
                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener,nYear,nMonth,nDay);
                alert.show();
            }});
        m_tvDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nYear = mDataLab.getEndDate_year();
                int nMonth = mDataLab.getEndDate_month();
                int nDay = mDataLab.getEndDate_day();
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mDataLab.setEndDate(year, monthOfYear, dayOfMonth);
                                dataToView();
                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, nYear, nMonth, nDay);
                alert.show();
            }
        });
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public void testFunc()
    {


        /*
        dataLab.setBeginDate(2015,10,14);
        dataLab.setEndDate(2016, 11, 15);
        dataLab.setCurrencyChar("$");
        dataLab.setBudget(1000.50);
        dataLab.addItem("아침", "20151015", "090000",100.10);
        dataLab.save excuteSaveData();
        */
        mDataLab.loadPurseData_DB();
    }


    public void saveData()
    {
        if(mDataLab != null)
            mDataLab.savePurseData_DB();
    }
    public void loadData()
    {
        if(mDataLab != null) {
            mDataLab.loadPurseData_DB();
            dataToView();
        }
    }
    public void dataToView()
    {

        String strVal = String.valueOf(mDataLab.getBeginDate_month())+ " /"+String.valueOf(mDataLab.getBeginDate_day());
        m_tvDate1.setText(strVal);

        strVal = String.valueOf(mDataLab.getEndDate_month())+ " /"+String.valueOf(mDataLab.getEndDate_day());
        m_tvDate2.setText(strVal);

        strVal = String.valueOf(mDataLab.getBudget());
        m_tvBudget.setText(strVal);
    }

}
