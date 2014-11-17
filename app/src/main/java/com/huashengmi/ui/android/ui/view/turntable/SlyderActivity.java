package com.huashengmi.ui.android.ui.view.turntable;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huashengmi.ui.android.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SlyderActivity extends RoboActivity implements View.OnClickListener {

    @InjectView(R.id.main_1)
    private CircleImageView mOneCircleImageView;

    @InjectView(R.id.main_2)
    private CircleImageView mTwoCircleImageView;

    @InjectView(R.id.main_3)
    private CircleImageView mThreeCircleImageView;

    @InjectView(R.id.main_4)
    private CircleImageView mFourCircleImageView;

    @InjectView(R.id.main_5)
    private CircleImageView mFiveCircleImageView;

    @InjectView(R.id.main_6)
    private CircleImageView mSixCircleImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slyder);

        mOneCircleImageView.setOnClickListener(this);
        mTwoCircleImageView.setOnClickListener(this);
        mThreeCircleImageView.setOnClickListener(this);
        mFourCircleImageView.setOnClickListener(this);
        mFiveCircleImageView.setOnClickListener(this);
        mSixCircleImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "" + v.getId(), Toast.LENGTH_SHORT).show();
    }
}