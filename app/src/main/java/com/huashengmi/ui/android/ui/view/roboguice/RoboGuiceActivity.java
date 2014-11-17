package com.huashengmi.ui.android.ui.view.roboguice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huashengmi.ui.android.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class RoboGuiceActivity extends RoboActivity implements View.OnClickListener{

    @InjectView(R.id.button1)
    private Button mButton1;

    @InjectView(R.id.button2)
    private Button mButton2;

    @InjectView(R.id.editText)
    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_guice);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String textStr = mEditText.getEditableText().toString();
        switch (v.getId()){
            case R.id.button1:
                Toast.makeText(this, textStr, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Toast.makeText(this, textStr, Toast.LENGTH_SHORT).show();
                break;
        }
    }



}
