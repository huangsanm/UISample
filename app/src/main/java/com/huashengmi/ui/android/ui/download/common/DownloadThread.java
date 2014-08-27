package com.huashengmi.ui.android.ui.download.common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.huashengmi.ui.android.ui.download.db.DownloadColumn;
import com.huashengmi.ui.android.ui.download.db.DownloadManager;
import com.huashengmi.ui.android.ui.download.http.AndroidHttpClient;
import com.huashengmi.ui.android.utils.Globals;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;

/**
 * Created by huangsm on 2014/7/28 0028.
 * Email:huangsanm@foxmail.com
 */
public class DownloadThread extends Thread {

    private final static String TAG = "DownloadThread";

    private final static int NOTIFICATION_STATUS_FLAG_UNERROR = -2;
    private final static int NOTIFICATION_STATUS_FLAG_ERROR = -1;
    private final static int NOTIFICATION_STATUS_FLAG_NORMAL = 1;
    private final static int NOTIFICATION_STATUS_FLAG_PAUSE = 2;
    private final static int NOTIFICATION_STATUS_FLAG_DELETE = 3;
    private final static int NOTIFICATION_STATUS_FLAG_DONE = 4;

    private final static int BUFFER_SIZE = 1024 * 8;
    private final static int NOTIFY_INTERVAL = 1000;
    private final static String TEMP_SUFFIX = ".smdownload";

    private Context mContext;
    //private int mDownloadID;
    //private Notification mNotification;
    private NotificationCompat.Builder mBuilder;
    private File mTempFile;
    //private DownloadItem mDownloadItem;
    private DownloadManager mDownloadManager;
    private NotificationManager mNotificationManager;
    private int mTempSize;
    //标题
    private String mTitle;
    //下载ID
    private int mDownloadID;
    //下载地址
    private String mUri;
    //后缀名
    private String mSuffix;

    //4.0+
    public DownloadThread(Context context, int downloadID, String title, String uri, String suffix, NotificationCompat.Builder builder) {
        mContext = context;
        mDownloadID = downloadID;
        mUri = uri;
        mSuffix = suffix;
        mTitle = title;

        Globals.log(mDownloadID + "," + mUri + "," + mSuffix + "," + mTitle);

        mBuilder = builder;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mDownloadManager = new DownloadManager(mContext.getContentResolver());
        mTempFile = new File(DownloadUtils.getFileDir(), mTitle + TEMP_SUFFIX);


        /*mDownloadItem = mDownloadManager.queryTask(downloadID);
        Globals.log("DownloadThread:" + mDownloadItem);*/
       // if (mDownloadItem != null) {
            //download finish update path value

       // }

        //notify
        notifyNotification(NOTIFICATION_STATUS_FLAG_NORMAL, "", 100, 0);
    }


    /*public DownloadThread(Context context, DownloadItem item, NotificationCompat.Builder builder) {
        mContext = context;
        mBuilder = builder;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mDownloadManager = new DownloadManager(mContext.getContentResolver());
        mDownloadItem = item;
        Globals.log("DownloadThread:" + mDownloadItem);
        if (mDownloadItem != null) {
            //download finish update path value
            mTempFile = new File(mDownloadItem.getPath(), mDownloadItem.getName() + TEMP_SUFFIX);
        }

        //notify
        notifyNotification(NOTIFICATION_STATUS_FLAG_NORMAL, "", 100, 0);
    }*/

    /*//兼容低版本
    public DownloadThread(Context context, int downloadID, Notification notification){
        mContext = context;
        mDownloadID = downloadID;
        mNotification = notification;
    }*/

    @Override
    public void run() {
        AndroidHttpClient aClient = null;
        //HttpClient aClient = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            aClient = AndroidHttpClient.newInstance("Linux; Android");
            HttpGet request = new HttpGet(mUri);
            HttpResponse response = aClient.execute(request);
            long totalSize = response.getEntity().getContentLength();
            //check storage
            if (totalSize > 0) {
                //更新数据库
                ContentValues values = new ContentValues();
                values.put(DownloadColumn.TOTAL_BYTE, totalSize);
                int result = mDownloadManager.updateTask(mDownloadID, values);
                if (result > 0) {
                    Globals.log("update database:" + result);
                }
            }
            if (mTempFile.exists()) {
                mTempSize = (int) mTempFile.length();
                request.addHeader("RANGE", "bytes=" + mTempFile.length() + "-");
                aClient.close();
                aClient = AndroidHttpClient.newInstance("Linux; Android");
                response = aClient.execute(request);
            }
            long storage = DownloadUtils.getAvailableStorage();
            if (totalSize - mTempSize > storage) {
                notifyNotification(NOTIFICATION_STATUS_FLAG_ERROR, "您的手机内存不足", 0, 0);
                return;
            }
            RandomAccessFile outStream = new RandomAccessFile(mTempFile, "rwd");
            outStream.seek(mTempSize);

            InputStream is = response.getEntity().getContent();
            byte[] buffer = new byte[BUFFER_SIZE];
            bufferedInputStream = new BufferedInputStream(is, BUFFER_SIZE);
            int b = 0;
            long updateStart = System.currentTimeMillis();
            long updateDelta = 0;
            int progress = mTempSize;
            while (true) {
                //check download status
                if (checkDownloadCancel()) {
                    notifyNotification(NOTIFICATION_STATUS_FLAG_DELETE, "下载已被取消", 0, 0);
                    aClient.close();
                    break;
                }

                if (checkDownloadPause()) {
                    notifyNotification(NOTIFICATION_STATUS_FLAG_DELETE, "暂停下载", 0, 0);
                    aClient.close();
                    break;
                }

                b = bufferedInputStream.read(buffer, 0, BUFFER_SIZE);
                if (b == -1) {
                    break;
                }
                outStream.write(buffer, 0, b);
                progress += b;

                //1s 更新一次通知栏
                if (updateDelta > NOTIFY_INTERVAL) {
                    Globals.log(progress);
                    notifyNotification(NOTIFICATION_STATUS_FLAG_NORMAL, "", (int) totalSize, progress);
                    updateStart = System.currentTimeMillis();

                    //更新下载进度
                    updateProgress(progress);
                }
                //时间
                updateDelta = System.currentTimeMillis() - updateStart;
            }
            is.close();
            //下载完成
            if (progress == totalSize) {
                notifyNotification(NOTIFICATION_STATUS_FLAG_DONE, "下载完成，点此安装", progress, (int) totalSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            notifyNotification(NOTIFICATION_STATUS_FLAG_ERROR, "下载出错：" + e.getMessage(), 0, 0);
        } finally {
            try {
                aClient.close();
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查下载是否被删除
     *
     * @return
     */
    private boolean checkDownloadCancel() {
        return mDownloadManager.checkTaskStatus(mDownloadID, DownloadStatus.STATUS_DELETEED);
    }

    /**
     * 检查下载是否被暂停
     *
     * @return
     */
    private boolean checkDownloadPause() {
        return mDownloadManager.checkTaskStatus(mDownloadID, DownloadStatus.STATUS_PAUSED);
    }

    private void updateProgress(long progress) {
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.CURRENT_BYTE, progress);
        int result = mDownloadManager.updateTask(mDownloadID, values);
        if (result > 0) {
            Globals.log("updateProgress" + result);
        }
    }

    private void notifyNotification(int action, String msg, int totalByte, int currentByte) {
        switch (action) {
            case NOTIFICATION_STATUS_FLAG_UNERROR:
            case NOTIFICATION_STATUS_FLAG_ERROR:
                mBuilder.setContentTitle(mTitle);
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);
                break;
            case NOTIFICATION_STATUS_FLAG_NORMAL:
                mBuilder.setContentTitle(mTitle);
                mBuilder.setProgress(totalByte, currentByte, false);
                mBuilder.setContentText("");
                break;
            case NOTIFICATION_STATUS_FLAG_PAUSE:
                mBuilder.setContentTitle(mTitle);
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);
                break;
            case NOTIFICATION_STATUS_FLAG_DELETE:
                mBuilder.setContentTitle(mTitle);
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);
                mBuilder.setAutoCancel(true);
                break;
            case NOTIFICATION_STATUS_FLAG_DONE:
                downloadFinish(msg, currentByte, totalByte);
                break;
        }
        //mBuilder.setWhen(System.currentTimeMillis());
        mNotificationManager.notify(mDownloadID, mBuilder.build());
    }

    private void downloadFinish(String msg, int currentByte, int totalByte) {
        //update db
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.TOTAL_BYTE, totalByte);
        values.put(DownloadColumn.CURRENT_BYTE, currentByte);
        values.put(DownloadColumn.STATUS, DownloadStatus.STATUS_SUCCESSFUL);
        int row = mDownloadManager.updateTask(mDownloadID, values);
        if (row > 0) {
            //rename
            File file = new File(DownloadUtils.getFileDir(), mTitle + mSuffix);
            if (mTempFile.renameTo(file)) {
                //delete tempfile
                mTempFile.delete();
                PendingIntent pi = PendingIntent.getActivity(mContext, 0, DownloadUtils.installApk(mContext, file), PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mBuilder.setContentTitle(mTitle).setContentText(msg);
                mBuilder.setProgress(0, 0, false);//remove progressbar
            }
        }
    }
}
