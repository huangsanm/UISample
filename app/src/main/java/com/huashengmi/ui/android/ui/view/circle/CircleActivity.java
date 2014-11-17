package com.huashengmi.ui.android.ui.view.circle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.huashengmi.ui.android.R;

public class CircleActivity extends Activity {

    private CircleProgress mCircleProgress;
    private TextView mPercentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        mCircleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        mCircleProgress.setTotalProgress(100, 60);
        mCircleProgress.startDraw();

        mPercentTextView = (TextView) findViewById(R.id.circle_percent);
        mPercentTextView.setText("60%");
    }


}
