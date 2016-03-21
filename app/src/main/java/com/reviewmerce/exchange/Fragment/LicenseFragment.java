package com.reviewmerce.exchange.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reviewmerce.exchange.BasicInfo;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.custom.MyMenuBtn;

/**
 * A placeholder fragment containing a simple view.
 */
public class LicenseFragment extends baseOnebuyFragment implements AdapterView.OnItemSelectedListener {

    private TextView mLicenseView;
    private ImageView mMain_ivHeader;
    public LicenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_license, container, false);
       // mLayout = (LinearLayout)v.findViewById(R.id.mainLayout_bank);
        mLicenseView = (TextView)v.findViewById(R.id.tvText_license);

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
                    } else if (nTouchPosX > m_nPreTouchPosX + BasicInfo.g_nMovePos) {
                    }

                    m_nPreTouchPosX = nTouchPosX;
                }
                return true;
            }
        });

        mMain_ivHeader = (ImageView)v.findViewById(R.id.ivHeader_license);

        mMain_ivHeader.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   mCallback.chgFragment(BasicInfo.FRAGMENT_LICENSE,1);


                                               }
                                           }
        );

        setLicenseInfo();
        return v;
    }
    public void setLicenseInfo()
    {
        String szInfo = "\r\n\r\n<< MPAndroidChart >>\r\n";
        String szInfo2 = "\r\nCopyright 2011-2015 Sergey Tarasevich\r\n";
        String szInfo3 = "\r\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\r\n";
        String szInfo4 = "\r\nhttp://www.apache.org/licenses/LICENSE-2.0\r\n";
        String szInfo5 = "\r\nUnless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.\r\n";
//        String szInfo6 = "\r\nSpecial thanks to danielgindi, mikegr, ph1lb4 and jitpack.io for their contributions to this project.\r\n";

        String szInfo7 = "\r\n\r\n<< UniversalImageLoader >>\r\n";
        String szInfo8 = "\r\nCopyright 2015 Philipp Jahoda\r\n";
        String szInfo9 = "\r\nLicensed under the Apache License, Version 2.0 (the \"License\"); \r\nyou may not use this file except in compliance with the License. \nYou may obtain a copy of the License at\r\n";
        String szInfo10 = "\r\nhttp://www.apache.org/licenses/LICENSE-2.0\r\n";
        String szInfo11 = "\r\nUnless required by applicable law or agreed to in writing, software\n distributed under the License is distributed on an \"AS IS\" BASIS,\n WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.\r\n";
//        String szInfo12 = "\r\nSpecial thanks to danielgindi, mikegr, ph1lb4 and jitpack.io for their contributions to this project.\r\n";

        mLicenseView.setText(szInfo + szInfo2 + szInfo3 + szInfo4 + szInfo5 + szInfo7 + szInfo8 + szInfo9 + szInfo10 + szInfo11);


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
//        Copyright 2011-2015 Sergey Tarasevich
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.
}
