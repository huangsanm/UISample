package com.huashengmi.ui.android.ui.net.asynchttpclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.utils.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class AsyncHttpClientActivity extends BaseActivity implements View.OnClickListener {

    private AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();

    private Button mSimpleButton;
    private Button mJsonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_http_client);

        mSimpleButton = (Button) findViewById(R.id.btn_simple);
        mSimpleButton.setOnClickListener(this);
        mJsonButton = (Button) findViewById(R.id.btn_json);
        mJsonButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final long times = System.currentTimeMillis();
        switch (v.getId()){
            case R.id.btn_simple:
                mAsyncHttpClient.get(Constant.simpleStr, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                        mSimpleButton.setText((System.currentTimeMillis() - times) + "s");
                    }
                });
                break;
            case R.id.btn_json:
                mAsyncHttpClient.get(Constant.jsonStr, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                        mJsonButton.setText((System.currentTimeMillis() - times) + "s");
                    }

                    public void onFailure(int statusCode, Throwable e,
                                          JSONArray errorResponse) {
                        String errorMsg = statusCode + ":" + e.getMessage();
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
