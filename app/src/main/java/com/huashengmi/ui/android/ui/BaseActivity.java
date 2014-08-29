package com.huashengmi.ui.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.huashengmi.ui.android.ui.net.volley.VolleyManager;

public class BaseActivity extends FragmentActivity {

    public Context mContext;
    public ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mImageLoader = VolleyManager.getInstance(this).getImageLoader();
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

}