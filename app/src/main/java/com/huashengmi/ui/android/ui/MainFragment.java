package com.huashengmi.ui.android.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.download.MultiDownloadActivity;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MainFragment extends Fragment {


    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startActivity(new Intent(getActivity(), MultiDownloadActivity.class));
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


}
