package com.huashengmi.ui.android.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by huangsm on 2014/7/4 0004.
 * Email:huangsanm@foxmail.com
 */
public class Globals {

    public static void log(Object args){
        Log.i("UiSample", "" + args);
    }


    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getDensity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

}
