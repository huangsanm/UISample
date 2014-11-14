package com.huashengmi.ui.android.ui.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.utils.Globals;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DragViewActivity extends RoboActivity {

    @InjectView(R.id.drag_logo)
    private ImageView mDragImageView;

    //设备分辨率
    private int mScreenWidth;
    private int mScreenHeight;
    private int lastX, lastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);

        buildComponent();
    }

    private void buildComponent(){
        //状态高度
        Rect statusBar = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBar);
        Globals.log("dddddddddddd:" + statusBar.top);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - 150;
        mDragImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        // 设置不能出界
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }

                        if (right > mScreenWidth) {
                            right = mScreenWidth;
                            left = right - v.getWidth();
                        }

                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }

                        if (bottom > mScreenHeight) {
                            bottom = mScreenHeight;
                            top = bottom - v.getHeight();
                        }

                        v.layout(left, top, right, bottom);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

}
