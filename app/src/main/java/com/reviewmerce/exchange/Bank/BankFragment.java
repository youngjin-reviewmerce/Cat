package com.reviewmerce.exchange.Bank;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.baseOnebuyFragment;
import com.reviewmerce.exchange.graph.GraphFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class BankFragment extends baseOnebuyFragment {
    BankAdapter mAdapter;
    ListView mListView;
    private int m_nPreTouchPosX = 0;
    public BankFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bank, container, false);

        mListView = (ListView) v.findViewById(R.id.listBank);
        mAdapter = new BankAdapter(getActivity());
        mAdapter.addItem(new BankItem("외환은행","2000.11"));
        mAdapter.addItem(new BankItem("국민은행", "2000.11"));

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankItem curItem = (BankItem) mAdapter.getItem(position);
                String[] curData = curItem.getData();

                //   Toast.makeText(getApplicationContext(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();

            }

        });
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
                    //    Intent intent = new Intent(getActivity(), BankActivity.class);
                    //    startActivity(intent);
                        mCallback.chgFragment(1,1);
                    }
                    else if (nTouchPosX > m_nPreTouchPosX)
                    {
                        mCallback.chgFragment(1,-1);

                        //MovewPreviousView();
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return true;
            }
        });
        return v;
    }
}
