package com.huashengmi.ui.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.mobstat.StatService;
import com.huashengmi.ui.android.ui.net.volley.VolleyManager;

public class BaseActivity extends FragmentActivity {

    public final String HOST = "http://www.romzhijia.net";

    public Context mContext;
    public ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mImageLoader = VolleyManager.getInstance(this).getImageLoader();

        startAnimated();
    }

    //启动动画
    private void startAnimated(){
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected void executeRequest(Request<?> request) {
        VolleyManager.getInstance(this).addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        VolleyManager.getInstance(this).cancelAll(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}