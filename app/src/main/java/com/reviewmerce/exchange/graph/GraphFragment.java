package com.reviewmerce.exchange.graph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.reviewmerce.exchange.Bank.BankFragment;
import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.ConnectThread;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.baseOnebuyFragment;
import com.reviewmerce.exchange.innerProc.ExchangeData;
import com.reviewmerce.exchange.innerProc.ExchangeDataLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A placeholder fragment containing a simple view.
 */
public class GraphFragment extends baseOnebuyFragment implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener {

    public GraphFragment() {
    }
    // Fragment를 담고 있는 Activity는 이 interface를 구현해야 한다.

    final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ExchangeDataLab mExchangeLab;
    private ImageButton mButtonRefresh;
    private Button mButtonCurrency;
    private Spinner mSpinCurrency;
    private GraphControl mGraph;
    private SeekBar mDaysBar;
    private TextView tvConverted_value, tv3Year, tvYear, tvHalfyear, tvMonth, tvDate,tvCurrency;
    Handler mHandlerHttp;
    public static final String TAG = "ExchangeActivity";
    public boolean mFirstDataGet;
    public final int SEEKCOUNT = 3;
    private boolean m_bConnectThreadRunning = false;
    private int mLastDaySeekbarIndex = SEEKCOUNT;
    private boolean m_bVisibleData = true;
    private boolean m_bVisibleAddLine = true;

    private int m_nPreTouchPosX = 0;
    //////////////////////////////////////////////////////////////////////////////////////////
    // 앱의 상태에 따른 함수
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    m_nPreTouchPosX = (int)event.getX();
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    int nTouchPosX = (int)event.getX();

                    if (nTouchPosX < m_nPreTouchPosX)   // 오른쪽
                    {
                        mCallback.chgFragment(0,1);
                    }
                    else if (nTouchPosX > m_nPreTouchPosX)
                    {
                        mCallback.chgFragment(0,-1);
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return false;
            }
        });
        setHasOptionsMenu(true);
        mFirstDataGet = false;
        LineChart Chart = (LineChart) v.findViewById(R.id.exchangechart);
        mGraph = new GraphControl(Chart,getActivity());
        initData();
        initControlValue(v);
        mGraph.initChart();
      //  loadDbData();
        refresh_ThreadAndDialog();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        setDaysSeekbarText();
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    // 컨트롤 관련 함수

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
  //      inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_graph_datavisible) {
            if(mGraph == null)
                return true;
            if(m_bVisibleData == true)
            {
                m_bVisibleData = false;

            }
            else
            {
                m_bVisibleData = true;
            }
            mGraph.setChartDataVisible(m_bVisibleData);

            return true;
        }
        else if(id == R.id.menu_graph_addlinevisible) {
            if(mGraph == null)
                return true;
            if(m_bVisibleAddLine == true) {
                m_bVisibleAddLine = false;
            }
            else
            {
                m_bVisibleAddLine = true;
            }
            mGraph.setChartAddlineVisible(m_bVisibleAddLine);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tv3Year.setTextSize(12);
        tvYear.setTextSize(12);
        tvHalfyear.setTextSize(12);
        tvMonth.setTextSize(12);
        setDaysSeekbarText();
        int nSeek = getSeekIndex(progress);

        switch (nSeek) {
            case 0:
                tv3Year.setTextSize(16);
                break;
            case 1:
                tvYear.setTextSize(16);
                break;
            case 2:
                tvHalfyear.setTextSize(16);
                break;
            case 3:
                tvMonth.setTextSize(16);
                break;
            case 5:
                break;
        }

        if(mLastDaySeekbarIndex != nSeek)
        {

            /*
            mExchangeLab.changeBeginDate(nSeek);
            mExchangeLab.procLoadedData();
            drawChart();
            tvConverted_value.setText(mExchangeLab.getLastPrice() + "원");
            tvDate.setText(mExchangeLab.getLastDate() + " " + mExchangeLab.getLastTime());
            mLastDaySeekbarIndex = nSeek;
            */
            mExchangeLab.changeBeginDate(nSeek);
            drawChart();
            mLastDaySeekbarIndex = nSeek;

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
    public void onButtonCurrency(){

    }
    //////////////////////////////////////////////////////////////////////////////////////////
    // 제작 함수 (시퀀스 관련)

    public void initData()      // 시스템 가동시 시작 할 작업 모음
    {
        mExchangeLab = ExchangeDataLab.get(getActivity());
        mExchangeLab.setEndDate(chgDateString(getToday()));
    }
    public void refresh() {
        if(m_bConnectThreadRunning == false) {
            m_bConnectThreadRunning = true;
            ConnectThread thread = new ConnectThread(BasicInfo.ApiURL, getActivity(), mHandlerHttp);
            thread.setCurrency(mExchangeLab.getDBName());
            thread.start();
        }
    }
    public void loadDbData() {
        mExchangeLab.LoadData();
    }
    public void drawChart()
    {
        m_bVisibleAddLine = true;
        m_bVisibleData = true;

        mExchangeLab.procLoadedData();
        mGraph.setAvg((float) (mExchangeLab.getAvg()));
        mGraph.setBeginIndex(mExchangeLab.getBeginIndex());
        mGraph.setEndIndex(mExchangeLab.getEndIndex());
        mGraph.setDataArray(mExchangeLab.getArrays());
        mGraph.setTodayData(new ExchangeData(mExchangeLab.getLastDate(),mExchangeLab.getLastPrice(),mExchangeLab.getLastTime()));
        mGraph.setMax((float)(mExchangeLab.getMax()));
        mGraph.setMin((float)(mExchangeLab.getMin()));
        mGraph.setChartInitValue();
        mGraph.setData_to_Graph();

        tvConverted_value.setText(mExchangeLab.getLastPrice() + "원");
        tvDate.setText(mExchangeLab.getLastDate() + " " + mExchangeLab.getLastTime());
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    // 제작 함수 (기타)
    public Date getToday() {
        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(today.YEAR);
        int month = today.get(today.MONTH) + 1;
        int day = today.get(today.DAY_OF_MONTH);
        Date time = today.getTime();

        //String szRtn = String.format("%d / %02d / %02d", year ,month,day ); // 오늘 날짜
        return time;
    }
    public String chgDateString(Date date) {
        SimpleDateFormat fm;

        fm = new SimpleDateFormat("yyyyMMdd");
        String szRtn = fm.format(date);
        return szRtn;

    }
    private void println(String msg) {
        Log.d(TAG, msg);
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    // 제작 함수 (컨트롤 관련)
    private void setDaysSeekbarText() {
        int progress = 0;
        int max = 0;
        int offset = 0;
        float percent = 0;
        int width = 0;
        int answer = 0;
        int nControlWidth = mDaysBar.getWidth();
        max = SEEKCOUNT;
        offset = mDaysBar.getThumbOffset();
        progress = 1;
        percent = ((float) progress) / (float) max;
        width = nControlWidth - 2 * offset;
        answer = ((int) (width * percent + offset - tvYear.getWidth() / 2));
        tvYear.setX(answer);
        progress = 2;
        percent = ((float) progress) / (float) max;
        width = nControlWidth - 2 * offset;
        answer = ((int) (width * percent + offset - tvHalfyear.getWidth() / 2));
        tvHalfyear.setX(answer);
    }
    public int getSeekIndex(int nVal)
    {
        int nSpace = mDaysBar.getMax() / SEEKCOUNT;
        int nLine = nSpace / 2;
        int nSeekIndex = (nVal / nLine +1)/2;
        return nSeekIndex;
    }
    public int getSeekVal(int nIndex)
    {
        int nSpace = mDaysBar.getMax() / SEEKCOUNT;
        int nSeekVal = nSpace * nIndex;
        return nSeekVal;
    }
    public void initControlValue(View v) {

        tvConverted_value = (TextView) v.findViewById(R.id.tvConverted_value);
        tv3Year = (TextView) v.findViewById(R.id.tv3Year);
        tvYear = (TextView) v.findViewById(R.id.tvYear);
        tvHalfyear = (TextView) v.findViewById(R.id.tvHalfyear);
        tvMonth = (TextView) v.findViewById(R.id.tvMonth);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        Date now = getToday();
        tvDate.setText(chgDateString(now));
        tv3Year.setTextSize(12);
        tvYear.setTextSize(12);
        tvHalfyear.setTextSize(12);
        tvMonth.setTextSize(16);
        mExchangeLab.changeBeginDate(3);         //1달 전 데이터
        mExchangeLab.procLoadedData();
        mDaysBar = (SeekBar) v.findViewById(R.id.seekBarDays);
        mDaysBar.setMax(1000);
        mDaysBar.setProgress(1000);
        mDaysBar.setOnSeekBarChangeListener(this);
        mButtonRefresh = (ImageButton) v.findViewById(R.id.btnRefresh);
        mButtonRefresh.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  refresh_ThreadAndDialog();
                                              }
                                          }
        );
//        /*
        mButtonCurrency = (Button)v.findViewById(R.id.btnCurrency);
        mButtonCurrency.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   final CurrencyAdapter adapter = new CurrencyAdapter(getActivity());
                                                   Resources res = getResources();
                                                   adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.usd), "USD","미국"));
                                                   adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.eur), "EUR","유로"));
                                                   adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.cny), "CNY","중국"));
                                                   adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.jpy), "JPY","일본"));
                                                   adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.gbp), "GBP","영국"));
                                                   adapter.addItem(new CurrencyItem(res.getDrawable(R.drawable.hkd), "HKD","홍콩"));
//                                                   CurrencyDialog dlg = new CurrencyDialog(getActivity(),adapter);
//                                                   dlg.show();
//                                                   LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
//                                                   final View dialogView = inflater.inflate(R.layout.currency_dialog,null);
 //                                                  /*



                                               //new AlertDialog.Builder(getActivity()).setView(viewCurrency).setTitle("").setItems(items, new DialogInterface.OnClickListener() {


                                                   AlertDialog.Builder alert =
                                                       new AlertDialog.Builder(getActivity()).//setView(dialogView).
                                                               setAdapter(adapter, new DialogInterface.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialog, int which) {
                                                                       CurrencyItem item = (CurrencyItem) (adapter.getItem(which));
                                                                       if((item.getData(0)=="JPY")
                                                                           || (item.getData(0)=="IDR")
                                                                           || (item.getData(0)=="VND") )
                                                                           mButtonCurrency.setText(item.getData(1) + " " + item.getData(0) + " 100");
                                                                       else
                                                                           mButtonCurrency.setText(item.getData(1) + " " + item.getData(0));
                                                                 //      mExchangeLab.clearAllData();
                                                                       mExchangeLab.setDBname(item.getData(0));
                                                                       mExchangeLab.LoadData();
                                                                       refresh_ThreadAndDialog();
                                                                   }
                                                               });
                                                           ;


                                                   alert.show();
//*/
                                               }
                                           }
        );
  //      */
        mHandlerHttp = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:     // 메시지로 넘겨받은 파라미터, 이 값으로 어떤 처리를 할지 결정
                        mFirstDataGet = true;
                        drawChart();
                        m_bConnectThreadRunning = false;
                        handler.sendEmptyMessage(0);
                        break;
                }
            }
        };


    }
    private void showAlertList(Context context, String[] strings, CustomOnItemClickListener onItemClickListener, String headerTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
/*
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout, null);

        ListView listView = (ListView)view.findViewById(R.id.currency_dialog);
        TextView headerView = (TextView)view.findViewById(R.id.alert_header);

        listView.setAdapter(new CurrencyAdapter()Adapter(strings));
        listView.setOnItemClickListener(onItemClickListener);

        if(headerTitle != null && !headerTitle.equals("")) {
            headerView.setText(headerTitle);
        } else {
            headerView.setVisibility(View.GONE);
        }

        builder.setView(view);
        final AlertDialog dig = builder.create();
        onItemClickListener.setAlertDialog(dig);
        dig.show();
        */
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            m_nPreTouchPosX = (int)event.getX();
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            int nTouchPosX = (int)event.getX();

            if (nTouchPosX < m_nPreTouchPosX)   // 오른쪽
            {
            }
            else if (nTouchPosX > m_nPreTouchPosX)
            {
                //MovewPreviousView();
            }

            m_nPreTouchPosX = nTouchPosX;
        }
        return true;
    }

    private abstract class CustomOnItemClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog dig;
        @Override
        public abstract void onItemClick(AdapterView<?> adapterView, View view, int i, long l);
        protected void dismissDig() {
            if(dig != null) {
                dig.dismiss();
            }
        }
        public void setAlertDialog(AlertDialog dig) {
            this.dig = dig;
        }
    }
    private ProgressDialog loagindDialog;	// Loading Dialog
    void refresh_ThreadAndDialog() {

        loagindDialog = ProgressDialog.show(getActivity(), "로딩중",
                "Loading. Please wait...", true, false);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                refresh();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex)
                {
                    handler.sendEmptyMessage(0);
                }

            }
        });
        thread.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            loagindDialog.dismiss();    // 다이얼로그 삭제
            // View갱신
        }
    };
    private void copyInitdb()
    {

    }
}
