package com.huashengmi.ui.android.ui.download;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.ui.download.common.DownloadUtils;
import com.huashengmi.ui.android.ui.download.db.DownloadManager;
import com.huashengmi.ui.android.utils.Globals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiDownloadActivity extends BaseActivity  {

    private DownloadManager mDownloadManager;

    private Context mContext;

    private List<Game> mGameList;
    private ListView mListView;
    private GameAdapter mGameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_download);
        mContext = this;

        buildFileDir();

        buildComponent();
    }

    private void buildFileDir(){
        File file = new File(DownloadUtils.getFileDir());
        if(!file.exists()){
            if(file.mkdir()){
                Globals.log(file.getAbsolutePath());
            }
        }
    }

    private void buildComponent(){
        mListView = (ListView) findViewById(R.id.download_list);

        //init...
        mGameList = new ArrayList<Game>();
        DataUtils.initData(mGameList);

        mGameAdapter = new GameAdapter(mContext, mGameList);
        mListView.setAdapter(mGameAdapter);

        mDownloadManager = new DownloadManager(getContentResolver());

        findViewById(R.id.download_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DownloadTaskActivity.class));
            }
        });
    }
}
