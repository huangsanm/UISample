package com.huashengmi.ui.android.ui.listview;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.ui.listview.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends BaseActivity implements RefreshListView.onRefreshListener {

    private RefreshListView mListView;
    private List<String> mList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        buildComponent();
    }

    private void buildComponent() {
        mListView = (RefreshListView) findViewById(R.id.refresh_list);
        mListView.setonRefreshListener(this);
        mList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            mList.add("Item" + i);
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mList);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onPullUpRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int len = mList.size();
                for (int i = len; i < len + 10; i++) {
                    mList.add("Item" + i);
                }
                mAdapter.notifyDataSetChanged();
                mListView.onRefreshComplete();
            }
        }, 3000);
    }

    @Override
    public void onPullDownRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                for (int i = 0; i < 20; i++) {
                    mList.add("Item" + i);
                }
                mAdapter.notifyDataSetChanged();
                mListView.onRefreshComplete();
            }
        }, 3000);
    }
}
