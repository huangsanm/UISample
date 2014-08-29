package com.huashengmi.ui.android.ui.net.retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

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
                /*RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(HOST)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
                SimpleStr simpleStr = restAdapter.create(SimpleStr.class);
                String text = simpleStr.getSimpleText();
                System.out.println(text);
                mSimpleButton.setText((System.currentTimeMillis() - times) + "s");*/

                // Create a very simple REST adapter which points the GitHub API endpoint.
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API_URL)
                        .build();

                // Create an instance of our GitHub API interface.
                GitHub github = restAdapter.create(GitHub.class);

                // Fetch and print a list of the contributors to this library.
                List<Contributor> contributors = github.contributors("square", "retrofit");
                for (Contributor contributor : contributors) {
                    System.out.println(contributor.login + " (" + contributor.contributions + ")");
                }

                break;
            case R.id.btn_json:

                break;
        }
    }

    public interface SimpleStr {

        @GET("/api/GetRsaKey?")
        String getSimpleText();

    }

    private static final String API_URL = "https://api.github.com";

    static class Contributor {
        String login;
        int contributions;
    }

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }
}
