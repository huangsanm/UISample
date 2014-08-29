package com.huashengmi.ui.android.ui.net.volley;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.utils.Constant;

import org.json.JSONObject;


public class VolleyActivity extends BaseActivity implements View.OnClickListener {


    private NetworkImageView mImageView;
    private Button mJsonButton;
    private Button mSimpleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        mImageView = (NetworkImageView) findViewById(R.id.iv_image);
        mSimpleButton = (Button) findViewById(R.id.btn_simple);
        mSimpleButton.setOnClickListener(this);
        mJsonButton = (Button) findViewById(R.id.btn_json);
        mJsonButton.setOnClickListener(this);

        mImageView.setImageUrl("http://img.huxiu.com/portal/201408/27/155536jerftsztzyeqp9gt.png", mImageLoader);

    }

    @Override
    public void onClick(View v) {
        final long times = System.currentTimeMillis();
        switch (v.getId()){
            case R.id.btn_simple:
                executeRequest(new StringRequest(Request.Method.GET, Constant.simpleStr, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        mSimpleButton.setText((System.currentTimeMillis() - times) + "s");
                    }
                }, errorListener()));
                break;
            case R.id.btn_json:
                executeRequest(new JsonObjectRequest(Request.Method.GET, Constant.jsonStr, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    final String list = response.getString("List");
                                    Toast.makeText(mContext, list, Toast.LENGTH_SHORT).show();
                                    mJsonButton.setText((System.currentTimeMillis() - times) + "s");
                                } catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        }, errorListener()));
                break;
        }
    }


}
