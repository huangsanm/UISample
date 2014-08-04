package com.huashengmi.ui.android.ui.download.common;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.download.MultiDownloadActivity;
import com.huashengmi.ui.android.ui.download.db.DownloadColumn;
import com.huashengmi.ui.android.ui.download.db.DownloadManager;
import com.huashengmi.ui.android.utils.Globals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {

    //保存下载内容  key:downloadID
    //private Map<Integer, DownloadItem> mDownloadMap;
    //线程池
    private ExecutorService mExecutor;
    private Context mContext;
    private NotificationCompat.Builder mBuilder;
    private DownloadManager mDownloadManager;

    @Override
    public void onCreate() {
        mContext = this;
        //mDownloadMap = Collections.synchronizedMap(new HashMap<Integer, DownloadItem>());
        mExecutor = Executors.newCachedThreadPool();
        mBuilder = new NotificationCompat.Builder(mContext);
        mDownloadManager = new DownloadManager(getContentResolver());
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this){
            //Integer downloadID = intent.getIntExtra(DownloadColumn._ID, 0);
            int status = intent.getIntExtra(DownloadColumn.STATUS, 0);
            int downloadId = intent.getIntExtra(DownloadColumn._ID, 0);
            String uri = intent.getStringExtra(DownloadColumn.URI);
            String suffix = intent.getStringExtra(DownloadColumn.SUFFIX);
            String title = intent.getStringExtra(DownloadColumn.NAME);
            Globals.log("params:::downloadID:" + downloadId);
            if (downloadId > 0) {
                Globals.log("modifyDownloadStatus:" + status);
                switch (status) {
                    case DownloadStatus.STATUS_RUNNING:
                        download(title, downloadId, uri, suffix);
                        break;
                    case DownloadStatus.STATUS_DELETEED:
                        modifyDownloadStatus(downloadId, DownloadStatus.STATUS_DELETEED);
                        break;
                    case DownloadStatus.STATUS_PAUSED:
                        modifyDownloadStatus(downloadId, DownloadStatus.STATUS_PAUSED);
                        break;
                    case DownloadStatus.STATUS_PENDING:
                        modifyDownloadStatus(downloadId, DownloadStatus.STATUS_RUNNING);
                        download(title, downloadId, uri, suffix);
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开始下载
     */
    private void download(String title, int downloadID, String uri, String suffix) {
        Globals.log("title:" + title);
        Intent intent = new Intent(mContext, MultiDownloadActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle(title).setTicker("下载提醒");
        setNotification();
        mExecutor.execute(new DownloadThread(mContext, downloadID, title, uri, suffix, mBuilder));
    }

    private void modifyDownloadStatus(int downloadID, int status){
        ContentValues values  = new ContentValues();
        values.put(DownloadColumn.STATUS, status);
        int i = mDownloadManager.updateTask(downloadID, values);
        if(i > 0){
            Globals.log("modifyDownloadStatus:status:" + i + ":" + status);
        }
    }

    /*private Notification createNotification(String title){
        Notification n = new Notification();
        n.icon = R.drawable.download;
        n.tickerText = "下载";
        n.when = System.currentTimeMillis();
        n.flags = Notification.FLAG_ONGOING_EVENT;
        n.contentView = new RemoteViews(getPackageName(), R.layout.download_notification_progress);
        n.contentView.setProgressBar(
                R.id.download_notification_progress, 100, 0, false);
        n.contentView.setTextViewText(
                R.id.download_notification_title, title);
        Intent intent = new Intent(mContext, MultiDownloadActivity.class);
        n.contentIntent = PendingIntent.getActivity(mContext, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return n;
    }*/

    private void setNotification() {
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo));
        mBuilder.setSmallIcon(R.drawable.download);
    }

}
