package com.huashengmi.ui.android.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.download.MultiDownloadActivity;
import com.huashengmi.ui.android.ui.view.ProgressActivity;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MainFragment extends Fragment {


    private String mClas;
    private MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClas = getArguments().getString("clas");
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = new Intent();
        intent.setClassName(mActivity, mClas);
        mActivity.startActivity(intent);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


}
