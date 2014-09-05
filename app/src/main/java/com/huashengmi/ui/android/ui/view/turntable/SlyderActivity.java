package com.huashengmi.ui.android.ui.view.turntable;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SlyderActivity extends Activity {

    private SlyderView slyderView;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slyderView = new SlyderView(this);
        setContentView(slyderView);
        slyderView.play();
        handler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //停止在那个位置
            slyderView.stop(3);
        }
    }
}