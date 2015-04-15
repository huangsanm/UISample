package com.huashengmi.ui.android.baidu;

import android.content.Context;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.huashengmi.ui.android.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/4/2.
 */
public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {

    @Override
    public void onBind(Context context, int i, String s, String s2, String s3, String s4) {
        Globals.log("ddddddddddddonBind:" + s + ",s2:" + s2);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> strings, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s2) {
        Globals.log("ddddddddddddonMessage:" + s + ",s2:" + s2);
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s2, String s3) {

    }
}
