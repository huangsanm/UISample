package com.huashengmi.ui.android.baidu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.Frontia;
import com.huashengmi.ui.android.R;

/**
 * baidu  push
 */
public class BaiduPushActivity extends Activity {


    private String app_key = "dcMVsRENj8NkuvpIafo1f0Zn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_push);

        Frontia.init(getApplicationContext(), app_key);

        final TextView tv = (TextView) findViewById(R.id.start_push);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, app_key);
                tv.setText("已开启");
            }
        });
    }
}
