package com.huashengmi.ui.android.ui.net.retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.utils.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.QueryMap;

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
        RestAdapter restAdapter = null;
        switch (v.getId()){
            case R.id.btn_simple:
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(HOST)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setConverter(new StringConverter())
                        .build();
                GameTest simpleStr = restAdapter.create(GameTest.class);
                simpleStr.getSimpleText(new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                        mSimpleButton.setText((System.currentTimeMillis() - times) + "s");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mSimpleButton.setText(error.toString() + ":" + (System.currentTimeMillis() - times) + "s");
                    }
                });
                break;
            case R.id.btn_json:
                restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://data.shouyouzhijia.net")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setConverter(new GsonConverter(new Gson()))
                    .build();
                GameTest jsonGame = restAdapter.create(GameTest.class);
                Map<String, String> map = new HashMap<String, String>();
                map.put("action", "getgametop");
                map.put("type", "7");
                jsonGame.getGameList(map, new Callback<GameList>() {
                    @Override
                    public void success(GameList games, Response response) {
                        Toast.makeText(mContext, "size:" + games.getList().size(), Toast.LENGTH_SHORT).show();
                        mJsonButton.setText((System.currentTimeMillis() - times) + "s");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mJsonButton.setText(error.toString() + ":" + (System.currentTimeMillis() - times) + "s");
                    }
                });
                break;
        }
    }

    public interface GameTest {

        @GET("/api/GetRsaKey?")
        void getSimpleText(Callback<String> callback);

        //http://data.shouyouzhijia.net/youxi.ashx?action=getgametop&type=7
        @GET("/youxi.ashx")
        void getGameList(@QueryMap Map<String, String> params, Callback<GameList> callback);

    }

}
