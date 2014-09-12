package com.huashengmi.ui.android.ui.download;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.app.UiSampleApp;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.ui.download.common.DownloadItem;
import com.huashengmi.ui.android.ui.download.db.DownLoadContentProvider;
import com.huashengmi.ui.android.ui.download.db.DownloadManager;

import java.util.ArrayList;
import java.util.List;

public class DownloadTaskActivity extends BaseActivity {

    private ListView mListView;
    private List<DownloadItem> mDownloadItems;
    private DownloadTaskAdapter mDownloadTaskAdapter;
    private Context mContext;

    private DownloadContentObserver mDownloadContentObserver = new DownloadContentObserver();
    private DownloadManager mDownloadManager;

    @Override
    protected void onStart() {
        getContentResolver().registerContentObserver(DownLoadContentProvider.CONTENT_URI, true, mDownloadContentObserver);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_task);
        mContext = this;
        mDownloadManager = new DownloadManager(getContentResolver());
        buildComponent();
    }

    private void buildComponent(){
        mListView = (ListView) findViewById(R.id.download_list);
        mDownloadItems = new ArrayList<DownloadItem>();
        mDownloadTaskAdapter = new DownloadTaskAdapter(this, mDownloadItems);
        mListView.setAdapter(mDownloadTaskAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadItems.clear();
        UiSampleApp.mDownloadItem.clear();
        mDownloadContentObserver.handlerChangeData();
    }

    @Override
    protected void onDestroy() {
        getContentResolver().unregisterContentObserver(mDownloadContentObserver);
        super.onDestroy();
    }

    private class DownloadContentObserver extends ContentObserver {

        public DownloadContentObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            handlerChangeData();
        }

        private void handlerChangeData(){
            Cursor cursor = mDownloadManager.queryTask();
            try {
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    do{
                        DownloadItem item = mDownloadManager.convertTaskItem(cursor);

                        if(!UiSampleApp.mDownloadItem.containsKey(item.getId())){
                            mDownloadItems.add(item);
                            UiSampleApp.mDownloadItem.put(item.getId(), item);
                        }else{
                            UiSampleApp.mDownloadItem.put(item.getId(), item);
                        }
                        mDownloadTaskAdapter.notifyDataSetChanged();
                    } while(cursor.moveToNext());
                }
            }finally {
                cursor.close();
            }
        }

    }

}
