package com.reviewmerce.exchange;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.reviewmerce.exchange.Bank.BankFragment;
import com.reviewmerce.exchange.graph.GraphFragment;


public class MainActivity extends ActionBarActivity implements OnTouchListener, baseOnebuyFragment.FragmentChangeListener{

//    private ViewFlipper m_viewFlipper;
    private int m_nPreTouchPosX = 0;
    private int m_nFragmentIndex = 0;
    private int m_nMaxFragementCount = 2;
    private static GraphFragment mFragmentGraph;
    private static BankFragment mFragmentBank;
    public void chgFragment(int nMyIndex, int nDirect)
    {
        if(nDirect >= 1) {
            if (nMyIndex + 1 >= m_nMaxFragementCount) // index + 1 = count
            {
                m_nFragmentIndex = 0;
            } else {
                m_nFragmentIndex = nMyIndex + 1;
            }
        }
        else {
            if (nMyIndex - 1 < 0) // index + 1 = count
            {
                m_nFragmentIndex = m_nMaxFragementCount-1;
            } else {
                m_nFragmentIndex = nMyIndex - 1;

            }
        }
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(m_nFragmentIndex)
        {
            case 0:             // Graph->
                transaction.replace(R.id.container, mFragmentGraph);
 //               getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentGraph).commit();
                break;
            case 1:
                //getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentBank).commit();

                transaction.replace(R.id.container, mFragmentBank);

                break;
        }
        transaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentGraph = new GraphFragment();
        mFragmentBank = new BankFragment();
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container,  mFragmentGraph).commit();
        }
 //       setContentView(R.layout.activity_graph);
/*
        m_viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

        m_viewFlipper.setOnTouchListener(MyTouchListener);
        View v1 = View.inflate(this,R.layout.fragment_graph,null);
        View v2 = View.inflate(this,R.layout.fragment_banks,null);
// ViewFlipper에 서브 레이아웃 추가
//        LinearLayout sub1 = (LinearLayout) View.inflate(this,
//                R.layout.sub_view1, null);

        m_viewFlipper.addView(v1);
        m_viewFlipper.addView(v2);


//        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // 메뉴를 사용할 경우 아래줄 주석 없앨것.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
///*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_graph_addlinevisible) {
            return false;
        }
        else if(id == R.id.menu_graph_addlinevisible) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }


    // */
    /*
  private void MoveNextView()
    {
        m_viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_right));
        m_viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_left));
        m_viewFlipper.showNext();
    }

    private void MovewPreviousView()
    {
        m_viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_left));
        m_viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_right));
        m_viewFlipper.showPrevious();
    }

    View.OnTouchListener MyTouchListener = new View.OnTouchListener()
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                m_nPreTouchPosX = (int)event.getX();
            }

            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                int nTouchPosX = (int)event.getX();

                if (nTouchPosX < m_nPreTouchPosX)
                {
                    MoveNextView();
                }
                else if (nTouchPosX > m_nPreTouchPosX)
                {
                    MovewPreviousView();
                }

                m_nPreTouchPosX = nTouchPosX;
            }

            return true;
        }
    };*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {     // 여기 안들어옴
   //     /*
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            m_nPreTouchPosX = (int)event.getX();
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            int nTouchPosX = (int)event.getX();

            if (nTouchPosX < m_nPreTouchPosX)   // 오른쪽
            {
//                Intent intent = new Intent(this, BankActivity.class);
//                startActivity(intent);
            }
            else if (nTouchPosX > m_nPreTouchPosX)
            {
                //MovewPreviousView();
            }

            m_nPreTouchPosX = nTouchPosX;
        }
//*/
        return false;
    }
}

