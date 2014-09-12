package com.huashengmi.ui.android.ui.listview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SwipeRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<String> mList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);

        buildComponent();
    }

    private void buildComponent(){
        mList = new ArrayList<String>();
        mListView = (ListView) findViewById(R.id.swipe_refresh_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(
                android.R.color.white,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        for (int i = 0; i < 10; i ++){
            mList.add("Item" + i);
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                    final int size = mAdapter.getCount();
                    for (int i = size; i < size + 10; i ++){
                        mList.add("Item" + i);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, 3000);
    }
}
