package com.huashengmi.ui.android.ui.net.volley;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by huangsm on 2014/8/28 0028.
 * Email:huangsanm@foxmail.com
 *   volley.jar
 */
public class VolleyManager {

    private VolleyManager(){};

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private static VolleyManager mVolleyManager = new VolleyManager();
    public static VolleyManager getInstance(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
        int memory = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memory / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
        return mVolleyManager;
    }

    public static RequestQueue getRequestQueue(){
        if(mRequestQueue != null)
            return mRequestQueue;
        throw new RuntimeException("RequestQueue is null");
    }

    public static void addRequest(Request<?> request, Object tag){
        if(tag != null){
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

}
