package com.huashengmi.ui.android.ui.listview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.huashengmi.ui.android.R;

/**
 * Created by huangsm on 2014/9/9 0009.
 * Email:huangsanm@foxmail.com
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mIsLoadingMore = false;
    private boolean mAutoLoadingMore = true;

    private RelativeLayout mFooterView;

    public LoadMoreListView(Context context) {
        super(context);

        initComponent(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initComponent(context);
    }

    public void initComponent(Context context) {
        mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.refresh_footer, this, false);

        addFooterView(mFooterView);
        setOnScrollListener(this);
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mIsLoadingMore = firstVisibleItem + visibleItemCount >= totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
            if(mIsLoadingMore && mAutoLoadingMore){
                onLoadMore();
            }
        }
    }

    public void onLoadMore() {
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void onLoadMoreComplete(boolean hasNextPage) {
        if(hasNextPage){
            mAutoLoadingMore = false;
            removeFooterView(mFooterView);
        }else{
            mAutoLoadingMore = true;
            if(getFooterViewsCount() == 0){
                addFooterView(mFooterView);
            }
        }
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
