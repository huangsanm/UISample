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
    private DownloadItem mDownloadItem;
    private DownloadManager mDownloadManager;
    private NotificationManager mNotificationManager;
    private int mTempSize;

    //private int mProgress;

    //4.0+
    public DownloadThread(Context context, int downloadID, NotificationCompat.Builder builder) {
        mContext = context;
        //mDownloadID = downloadID;
        mBuilder = builder;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mDownloadManager = new DownloadManager(mContext.getContentResolver());
        mDownloadItem = mDownloadManager.queryTask(downloadID);
        Globals.log("DownloadThread:" + mDownloadItem);
        if (mDownloadItem != null) {
            //download finish update path value
            mTempFile = new File(mDownloadItem.getPath(), mDownloadItem.getName() + TEMP_SUFFIX);
        }

        //notify
        notifyNotification(NOTIFICATION_STATUS_FLAG_NORMAL, "", 100, 0);
    }

    //4.0+
    public DownloadThread(Context context, DownloadItem item, NotificationCompat.Builder builder) {
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
    }

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
            //TODO:
            // aClient = new DefaultHttpClient();
            aClient = AndroidHttpClient.newInstance("Linux; Android");
            Globals.log("AndroidHttpClient:" + mDownloadItem.getUri());
            HttpGet request = new HttpGet(mDownloadItem.getUri());
            HttpResponse response = aClient.execute(request);
            //302、301下载跳转
            Globals.log("AndroidHttpClient:" + response.getStatusLine().getStatusCode());
            Header[] headers = response.getAllHeaders();
            for (Header h : headers) {
                Globals.log("AndroidHttpClient:name:" + h.getName() + ",value:" + h.getValue());
            }
            long totalSize = response.getEntity().getContentLength();
            Globals.log("AndroidHttpClient:" + totalSize);
            //check storage
            if (totalSize > 0) {
                //更新数据库
                ContentValues values = new ContentValues();
                values.put(DownloadColumn.TOTAL_BYTE, totalSize);
                int result = mDownloadManager.updateTask(mDownloadItem.getId(), values);
                if (result > 0) {
                    Log.i(TAG, "update database:" + result);
                }
            }
            if (mTempFile.exists()) {
                mTempSize = (int) mTempFile.length();
                totalSize -= mTempSize;
                request.addHeader("RANGE", "bytes=" + mTempFile.length() + "-");
                aClient.close();
                aClient = AndroidHttpClient.newInstance("Linux; Android");
                response = aClient.execute(request);
            }
            long storage = DownloadUtils.getAvailableStorage();
            if (totalSize - mTempFile.length() > storage) {
                notifyNotification(NOTIFICATION_STATUS_FLAG_ERROR, "您的手机内存不足", 0, 0);
                return;
            }
            RandomAccessFile outStream = new RandomAccessFile(mTempFile, "rwd");
            outStream.seek(outStream.length());

            InputStream is = response.getEntity().getContent();
            byte[] buffer = new byte[BUFFER_SIZE];
            bufferedInputStream = new BufferedInputStream(is, BUFFER_SIZE);
            int b = 0;
            long updateStart = System.currentTimeMillis();
            long updateDelta = 0;
            int progress = 0;
            //boolean finish = true;
            while (true) {
                //check download status
                Globals.log(checkDownloadCancel());
                if (checkDownloadCancel()) {
                    //finish = false;
                    notifyNotification(NOTIFICATION_STATUS_FLAG_DELETE, "下载已被取消", 0, 0);
                    aClient.close();
                    break;
                }

                Globals.log(checkDownloadPause());
                if (checkDownloadPause()) {
                    //finish = false;
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

                    //下载完成
                    Globals.log("finish:" + progress + "," + totalSize + ":" + (progress == totalSize));
                    if (progress == (int) totalSize) {
                        notifyNotification(NOTIFICATION_STATUS_FLAG_DONE, "下载完成，点此安装", progress, (int) totalSize);
                    }

                }

                //时间
                updateDelta = System.currentTimeMillis() - updateStart;
            }
            is.close();
            /*if(finish){
                notifyNotification(NOTIFICATION_STATUS_FLAG_DONE, "下载完成，点此安装", progress, (int) totalSize);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
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
        return mDownloadManager.checkTaskStatus(mDownloadItem.getId(), DownloadStatus.STATUS_DELETEED);
    }

    /**
     * 检查下载是否被暂停
     *
     * @return
     */
    private boolean checkDownloadPause() {
        return mDownloadManager.checkTaskStatus(mDownloadItem.getId(), DownloadStatus.STATUS_PAUSED);
    }

    private void updateProgress(long progress) {
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.CURRENT_BYTE, progress);
        int result = mDownloadManager.updateTask(mDownloadItem.getId(), values);
        if (result > 0) {
            Globals.log("updateProgress" + result);
        }
    }

    private void notifyNotification(int action, String msg, int totalByte, int currentByte) {
        switch (action) {
            case NOTIFICATION_STATUS_FLAG_UNERROR:
            case NOTIFICATION_STATUS_FLAG_ERROR:
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);
                break;
            case NOTIFICATION_STATUS_FLAG_NORMAL:
                mBuilder.setProgress(totalByte, currentByte, false);
                mBuilder.setContentText("");
                break;
            case NOTIFICATION_STATUS_FLAG_PAUSE:
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);
                break;
            case NOTIFICATION_STATUS_FLAG_DELETE:
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);
                mBuilder.setAutoCancel(true);
                break;
            case NOTIFICATION_STATUS_FLAG_DONE:
                downloadFinish(msg, currentByte, totalByte);
                break;
        }
        mNotificationManager.notify(mDownloadItem.getId(), mBuilder.build());
    }

    private void downloadFinish(String msg, int currentByte, int totalByte) {
        //update db
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.TOTAL_BYTE, totalByte);
        values.put(DownloadColumn.CURRENT_BYTE, currentByte);
        values.put(DownloadColumn.STATUS, DownloadStatus.STATUS_SUCCESSFUL);
        int row = mDownloadManager.updateTask(mDownloadItem.getId(), values);
        if (row > 0) {
            //rename
            File file = new File(mDownloadItem.getPath(), mDownloadItem.getName() + mDownloadItem.getSuffix());
            if (mTempFile.renameTo(file)) {
                //delete tempfile
                mTempFile.delete();
                PendingIntent pi = PendingIntent.getActivity(mContext, 0, DownloadUtils.installApk(mContext, file), PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mBuilder.setContentText(msg);
                mBuilder.setProgress(0, 0, false);//remove progressbar
            }
        }
    }
}