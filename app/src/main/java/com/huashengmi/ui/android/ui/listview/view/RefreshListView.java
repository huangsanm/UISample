package com.huashengmi.ui.android.ui.listview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huashengmi.ui.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huangsm on 2014/9/9 0009.
 * Email:huangsanm@foxmail.com
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    // 刷新的几个动作
    // 释放
    private final static int RELEASE_TO_REFRESH = 0;
    // 拉
    private final static int PULL_TO_REFRESH = RELEASE_TO_REFRESH + 1;
    // 加载
    private final static int LOADING = PULL_TO_REFRESH + 1;
    // 刷新
    private final static int REFRESHING = LOADING + 1;
    // Nothing
    private final static int DONE = REFRESHING + 1;
    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 2;

    private Context mContext;
    // 头部
    private LinearLayout mHeaderViewLinearLayout;
    private ImageView mHeaderImageView;
    private ProgressBar mHeaderProgressBar;
    private TextView mHeaderTipsTextView;
    private TextView mHeaderUpdateTextView;

    // 尾部
    private LinearLayout mFooterViewLinearLayout;
    private ImageView mFooterImageView;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterTipsTextView;
    private TextView mFooterUpdateTextView;

    // 旋转动画
    private RotateAnimation mUpAnimation;
    private RotateAnimation mDownAnimation;

    // 头部高度
    private int mHeaderHeight;
    // 尾部高度
    private int mFooterHeight;

    // 先缓存第一个item
    private int mFirstItemIndex;
    // 是否可以上拉,下拉刷新
    private boolean isLastItem = false;
    private boolean isFirstItem = false;

    private int mStartY;

    // 当前listview状态
    private int mRefreshState = DONE;

    // Touch事件完成之后刷新数据
    private boolean isTouch;

    // 拉动动作
    private boolean isPullUp = false;
    private boolean isPullDown = false;

    // 控制动画
    private boolean isRotate;

    private onRefreshListener mOnRefreshListener;

    // 第几个开始允许刷新
    private static final int START_REFRESH_POSITION = 1;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attr) {
        super(context, attr);

        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mHeaderViewLinearLayout = (LinearLayout) inflater.inflate(
                R.layout.view_listview_header, null);
        mFooterViewLinearLayout = (LinearLayout) inflater.inflate(
                R.layout.view_listview_footer, null);

        initHeader();
        initFooter();
        initAnimation();

        setOnScrollListener(this);
    }

    /**
     * 初始化header,测量高度
     */
    private void initHeader() {
        mHeaderImageView = (ImageView) mHeaderViewLinearLayout
                .findViewById(R.id.head_refresh);
        mHeaderProgressBar = (ProgressBar) mHeaderViewLinearLayout
                .findViewById(R.id.head_progress);
        mHeaderTipsTextView = (TextView) mHeaderViewLinearLayout
                .findViewById(R.id.head_tips);
        mHeaderUpdateTextView = (TextView) mHeaderViewLinearLayout
                .findViewById(R.id.head_last_update);
        mHeaderImageView.setMinimumWidth(70);
        mHeaderImageView.setMinimumHeight(50);
        // 测量高度
        measureView(mHeaderViewLinearLayout);
        // 获取测量之后的高度
        mHeaderHeight = mHeaderViewLinearLayout.getMeasuredHeight();
        mHeaderViewLinearLayout.setPadding(0, -1 * mHeaderHeight, 0, 0);
        // 通知重新绘制
        mHeaderViewLinearLayout.invalidate();
        addHeaderView(mHeaderViewLinearLayout, null, false);
    }

    /**
     * 初始化footer,测量高度
     */
    private void initFooter() {
        mFooterImageView = (ImageView) mFooterViewLinearLayout
                .findViewById(R.id.foot_refresh);
        mFooterProgressBar = (ProgressBar) mFooterViewLinearLayout
                .findViewById(R.id.foot_progress);
        mFooterTipsTextView = (TextView) mFooterViewLinearLayout
                .findViewById(R.id.foot_tips);
        mFooterUpdateTextView = (TextView) mFooterViewLinearLayout
                .findViewById(R.id.foot_last_update);
        mFooterImageView.setMinimumWidth(70);
        mFooterImageView.setMinimumHeight(50);

        measureView(mFooterViewLinearLayout);
        mFooterHeight = mFooterViewLinearLayout.getMeasuredHeight();
        mFooterViewLinearLayout.setPadding(0, 0, 0, -1 * mFooterHeight);

        mFooterViewLinearLayout.invalidate();
        addFooterView(mFooterViewLinearLayout, null, false);
    }

    /**
     * 旋转箭头动画
     */
    private void initAnimation() {
        mUpAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mUpAnimation.setInterpolator(new LinearInterpolator());
        mUpAnimation.setDuration(250);
        mUpAnimation.setFillAfter(true);

        mDownAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mDownAnimation.setInterpolator(new LinearInterpolator());
        mDownAnimation.setDuration(250);
        mDownAnimation.setFillAfter(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 减"1"为footerview
        if (view.getLastVisiblePosition() == view.getCount() - 1) {
            isLastItem = true;
        }
        if (view.getFirstVisiblePosition() <= START_REFRESH_POSITION) {
            isFirstItem = true;
        }else{
            isFirstItem = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mFirstItemIndex = firstVisibleItem;
        // adapter为空
        if (mFirstItemIndex == 0) {
            isLastItem = false;
        }
        if (mFirstItemIndex == 0&&visibleItemCount <= 2) {
            isFirstItem = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mOnRefreshListener == null)
            return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isTouch) {
                    isTouch = true;
                    mStartY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) ev.getY();
                // 往上拉 ,加载更多数据
                if (tempY - mStartY <= 0) {
                    isPullUp = true;
                    isPullDown = false;
                    if (mRefreshState != REFRESHING && isTouch
                            && mRefreshState != LOADING) {
                        if (mRefreshState == RELEASE_TO_REFRESH) {
                            // 正在拖动listview,需改变footerview状态
                            if ((mStartY - tempY) / RATIO < mFooterHeight
                                    && (mStartY - tempY) > 0) {
                                mRefreshState = PULL_TO_REFRESH;
                                changeFooterViewRefresh();
                            } else if (mStartY - tempY <= 0) {
                                mRefreshState = DONE;
                                changeFooterViewRefresh();
                            }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_TO_REFRESH状态
                        if (mRefreshState == PULL_TO_REFRESH && isLastItem) {
                            if ((mStartY - tempY) / RATIO >= mFooterHeight) {
                                mRefreshState = RELEASE_TO_REFRESH;
                                isRotate = true;
                                changeFooterViewRefresh();
                            } else if (mStartY - tempY <= 0) {
                                mRefreshState = DONE;
                                changeFooterViewRefresh();
                            }
                        }
                        // done状态下
                        if (mRefreshState == DONE) {
                            if (mStartY - tempY > 0) {
                                mRefreshState = PULL_TO_REFRESH;
                                changeFooterViewRefresh();
                            }
                        }
                        if (isLastItem) {
                            // 更新footView的size
                            if (mRefreshState == PULL_TO_REFRESH) {
                                mFooterViewLinearLayout
                                        .setPadding(0, 0, 0, -1 * mFooterHeight
                                                + (mStartY - tempY) / RATIO);
                            }
                            // 更新footView的paddingTop
                            if (mRefreshState == RELEASE_TO_REFRESH) {
                                mFooterViewLinearLayout.setPadding(0, 0, 0,
                                        (mStartY - tempY) / RATIO - mFooterHeight);
                            }
                        }
                    }
                } else if ((tempY - mStartY) >= 0) {
                    // 标识下拉刷新
                    isPullDown = true;
                    isPullUp = false;
                    if (mRefreshState != REFRESHING && isTouch
                            && mRefreshState != LOADING) {
                        // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                        // 可以松手去刷新了
                        if (mRefreshState == RELEASE_TO_REFRESH) {
                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((tempY - mStartY) / RATIO < mHeaderHeight)
                                    && (tempY - mStartY) > 0) {
                                mRefreshState = PULL_TO_REFRESH;
                                changeHeaderViewRefresh();
                            }
                            // 一下子推到顶了
                            else if (tempY - mStartY <= 0) {
                                mRefreshState = DONE;
                                changeHeaderViewRefresh();
                            }
                            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                            else {
                                // 不用进行特别的操作，只用更新paddingTop的值就行了
                            }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (mRefreshState == PULL_TO_REFRESH && isFirstItem) {
                            // setSelection(0);
                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - mStartY) / RATIO >= mHeaderHeight) {
                                mRefreshState = RELEASE_TO_REFRESH;
                                isRotate = true;
                                changeHeaderViewRefresh();
                            }
                            // 上推到顶了
                            else if (tempY - mStartY <= 0) {
                                mRefreshState = DONE;
                                changeHeaderViewRefresh();
                            }
                        }
                        // done状态下
                        if (mRefreshState == DONE) {
                            if (tempY - mStartY > 0) {
                                mRefreshState = PULL_TO_REFRESH;
                                changeHeaderViewRefresh();
                            }
                        }
                        if (isFirstItem) {
                            // 更新headView的size
                            if (mRefreshState == PULL_TO_REFRESH) {
                                mHeaderViewLinearLayout.setPadding(0,
                                        -1 * mHeaderHeight + (tempY - mStartY)
                                                / RATIO, 0, 0);
                            }

                            // 更新headView的paddingTop
                            if (mRefreshState == RELEASE_TO_REFRESH) {
                                mHeaderViewLinearLayout.setPadding(0,
                                        (tempY - mStartY) / RATIO - mHeaderHeight,
                                        0, 0);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL://失去焦点&取消动作
            case MotionEvent.ACTION_UP:
                if (mRefreshState != REFRESHING && mRefreshState != LOADING) {
                    if (mRefreshState == PULL_TO_REFRESH) {
                        mRefreshState = DONE;
                        if (isPullUp) {
                            changeFooterViewRefresh();
                        }

                        if (isPullDown) {
                            changeHeaderViewRefresh();
                        }
                    }
                    if (mRefreshState == RELEASE_TO_REFRESH) {
                        mRefreshState = REFRESHING;
                        if (isPullUp) {
                            changeFooterViewRefresh();
                            onPullUpRefresh();
                        }

                        if (isPullDown) {
                            changeHeaderViewRefresh();
                            onPullDownRefresh();
                        }
                    }
                }
                isTouch = false;
                isRotate = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新完成
     */
    public void onRefreshComplete() {
        mRefreshState = DONE;
        if (isPullDown) {
            mHeaderUpdateTextView.setText(mContext
                    .getString(R.string.recently_modified) + getLocalTime());
            changeHeaderViewRefresh();
        }

        if (isPullUp) {
            mFooterTipsTextView.setText(mContext
                    .getString(R.string.recently_modified) + getLocalTime());
            changeFooterViewRefresh();
        }

        // 刷新完成后还原上拉下拉标识位以及是否是listview底部标识
        isPullUp = false;
        isPullDown = false;
        isLastItem = false;
        isFirstItem = false;
    }

    private void onPullUpRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onPullUpRefresh();
        }
    }

    private void onPullDownRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onPullDownRefresh();
        }
    }

    /**
     * 设置数据源Adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        mHeaderUpdateTextView.setText(mContext
                .getString(R.string.recently_modified) + getLocalTime());
        mFooterUpdateTextView.setText(mContext
                .getString(R.string.recently_modified) + getLocalTime());
        super.setAdapter(adapter);
    }

    private String getLocalTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    private void changeFooterViewRefresh() {
        switch (mRefreshState) {
            case RELEASE_TO_REFRESH:
                mFooterImageView.setVisibility(View.VISIBLE);
                mFooterProgressBar.setVisibility(View.GONE);
                // 箭头旋转
                mFooterImageView.clearAnimation();
                mFooterImageView.startAnimation(mUpAnimation);
                mFooterTipsTextView.setText(R.string.release_load);
                break;
            case PULL_TO_REFRESH:
                mFooterImageView.setVisibility(View.VISIBLE);
                mFooterProgressBar.setVisibility(View.GONE);

                mFooterImageView.clearAnimation();
                if (isRotate) {
                    isRotate = false;
                    mFooterImageView.startAnimation(mDownAnimation);
                }
                mFooterTipsTextView.setText(R.string.pullup_refresh);
                break;
            case REFRESHING:
                mFooterViewLinearLayout.setPadding(0, 0, 0, 0);
                mFooterImageView.clearAnimation();
                mFooterImageView.setVisibility(View.GONE);
                mFooterProgressBar.setVisibility(View.VISIBLE);
                mFooterTipsTextView.setText(R.string.release_load);
                break;
            case DONE:
                mFooterViewLinearLayout.setPadding(0, 0, 0, -1 * mFooterHeight);
                mFooterProgressBar.setVisibility(View.GONE);
                mFooterImageView.clearAnimation();
                mFooterImageView.setImageResource(R.drawable.view_listview_footer_refresh);
                mFooterTipsTextView.setText(mContext
                        .getString(R.string.pullup_refresh));
                break;
        }
        mFooterUpdateTextView.setVisibility(View.VISIBLE);
    }

    private void changeHeaderViewRefresh() {
        switch (mRefreshState) {
            case RELEASE_TO_REFRESH:
                mHeaderImageView.setVisibility(View.VISIBLE);
                mHeaderProgressBar.setVisibility(View.GONE);

                mHeaderImageView.clearAnimation();
                mHeaderImageView.startAnimation(mUpAnimation);

                mHeaderTipsTextView.setText(mContext
                        .getString(R.string.release_refresh));
                break;
            case PULL_TO_REFRESH:
                mHeaderProgressBar.setVisibility(View.GONE);
                mHeaderImageView.clearAnimation();
                mHeaderImageView.setVisibility(View.VISIBLE);

                if (isRotate) {
                    isRotate = false;
                    mHeaderImageView.clearAnimation();
                    mHeaderImageView.startAnimation(mDownAnimation);
                    mHeaderTipsTextView.setText(mContext
                            .getString(R.string.pulldown_refresh));
                } else {
                    mHeaderTipsTextView.setText(mContext
                            .getString(R.string.pulldown_refresh));
                }
                break;
            case REFRESHING:
                mHeaderViewLinearLayout.setPadding(0, 0, 0, 0);

                mHeaderProgressBar.setVisibility(View.VISIBLE);
                mHeaderImageView.clearAnimation();
                mHeaderImageView.setVisibility(View.GONE);
                mHeaderTipsTextView
                        .setText(mContext.getString(R.string.refreshing));
                break;
            case DONE:
                mHeaderViewLinearLayout.setPadding(0, -1 * mHeaderHeight, 0, 0);
                mHeaderProgressBar.setVisibility(View.GONE);
                mHeaderImageView.clearAnimation();
                mHeaderImageView.setImageResource(R.drawable.view_listview_footer_refresh);
                mHeaderTipsTextView.setText(mContext
                        .getString(R.string.pulldown_refresh));
                break;
        }
        mHeaderUpdateTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 测量高度
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setonRefreshListener(onRefreshListener refreshListener) {
        mOnRefreshListener = refreshListener;
    }

    public interface onRefreshListener {

        void onPullUpRefresh();

        void onPullDownRefresh();

    }
}
