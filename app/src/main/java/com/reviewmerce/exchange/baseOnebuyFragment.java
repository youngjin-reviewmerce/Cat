package com.reviewmerce.exchange;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by onebuy on 2015-09-07.
 */
public class baseOnebuyFragment extends Fragment {
    public FragmentChangeListener mCallback;
    public interface FragmentChangeListener {
        public void chgFragment(int nMyIndex, int nDirect);
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mCallback = (FragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }
}
