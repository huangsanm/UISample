package com.huashengmi.ui.android.ui.net.retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

public class RetrofitActivity extends BaseActivity implements View.OnClickListener{

    private final String HOST = "http://www.romzhijia.net";

    private Button mSimpleButton;
    private Button mJsonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

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
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(HOST)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setConverter(new SimpleXmlConverter())
                        .build();
                SimpleStr simpleStr = restAdapter.create(SimpleStr.class);
                simpleStr.getSimpleText(new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Toast.makeText(mContext, s + ":" + response, Toast.LENGTH_SHORT).show();
                        mSimpleButton.setText((System.currentTimeMillis() - times) + "s");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mSimpleButton.setText(error.toString() + ":" + (System.currentTimeMillis() - times) + "s");
                    }
                });


                break;
            case R.id.btn_json:

                break;
        }
    }

    public interface SimpleStr {

        @GET("/api/GetRsaKey?")
        void getSimpleText(Callback<String> callback);

    }

}
