package com.huashengmi.ui.android.ui.listview;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.BaseActivity;
import com.huashengmi.ui.android.ui.listview.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

public class LoadMoreActivity extends BaseActivity {

    private LoadMoreListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        buildComponent();
    }

    private void buildComponent(){
        mListView = (LoadMoreListView) findViewById(R.id.load_more_list);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int size = mAdapter.getCount();
                        for (int i = size; i < size + 10; i ++){
                            mList.add("Item" + i);
                        }
                        mAdapter.notifyDataSetChanged();
                        mListView.onLoadMoreComplete(false);
                    }
                }, 3000);
            }
        });

        mList = new ArrayList<String>();
        for (int i = 0; i < 15; i++){
            mList.add("Item" + i);
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mList);
        mListView.setAdapter(mAdapter);
        mListView.onLoadMoreComplete(false);
    }
}
