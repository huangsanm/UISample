package com.huashengmi.ui.android.app;

import android.app.Application;

import com.huashengmi.ui.android.ui.download.common.DownloadItem;
import com.huashengmi.ui.android.ui.download.common.DownloadUtils;
import com.huashengmi.ui.android.utils.Globals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangsm on 2014/7/30 0030.
 * Email:huangsanm@foxmail.com
 */
public class UiSampleApp extends Application {

    public static Map<Integer, DownloadItem> mDownloadItem = new HashMap<Integer, DownloadItem>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
