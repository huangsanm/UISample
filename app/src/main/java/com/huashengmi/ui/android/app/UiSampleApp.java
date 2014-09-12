package com.huashengmi.ui.android.app;

import android.app.Application;
import android.content.Context;

import com.huashengmi.ui.android.ui.download.common.DownloadItem;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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

        initImageLoader(this);
    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

}
