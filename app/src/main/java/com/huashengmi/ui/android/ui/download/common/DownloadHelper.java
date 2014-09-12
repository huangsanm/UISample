package com.huashengmi.ui.android.ui.download.common;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.huashengmi.ui.android.ui.download.Game;
import com.huashengmi.ui.android.ui.download.db.DownloadColumn;
import com.huashengmi.ui.android.ui.download.db.DownloadManager;
import com.huashengmi.ui.android.utils.Globals;

import java.io.File;

/**
 * Created by huangsm on 2014/7/30 0030.
 * Email:huangsanm@foxmail.com
 */
public class DownloadHelper {

    private static Context mContext;
    private static DownloadHelper mHelper;
    private static DownloadManager mDownloadManager;

    public static DownloadHelper getInstance(Context context){
        if(mHelper == null){
            mContext = context;
            mHelper = new DownloadHelper();
            mDownloadManager = new DownloadManager(context.getContentResolver());
        }
        return mHelper;
    }

    /**
     * 开始下载
     * @param game
     */
    public void startDownload( Game game) {
        DownloadItem item = new DownloadItem();
        item.setName(game.getName());
        item.setIconUri(game.getIconUri());
        item.setUri(game.getUri());
        item.setSuffix(".apk");
        item.setStatus(DownloadStatus.STATUS_RUNNING);
        item.setPkg(game.getPkg());
        item.setType(DownloadType.TYPE_APK);
        item.setPath(DownloadUtils.getFileDir());

        long downloadID = mDownloadManager.addTask(item);
        Globals.log("startDownload:downloadID:" + downloadID);
        if (downloadID > 0) {
            item.setId((int) downloadID);

            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(DownloadColumn._ID, (int) downloadID);
            intent.putExtra(DownloadColumn.NAME, item.getName());
            intent.putExtra(DownloadColumn.URI, item.getUri());
            intent.putExtra(DownloadColumn.SUFFIX, item.getSuffix());
            intent.putExtra(DownloadColumn.STATUS, DownloadStatus.STATUS_RUNNING);
            mContext.startService(intent);
        }
    }

    /**
     * 暂停
     * @param downloadID
     */
    public void pauseDownload(int downloadID){
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.STATUS, DownloadStatus.STATUS_PAUSED);
        int result = mDownloadManager.updateTask(downloadID, values);
        if(result > 0){
            modifyDownloadStatus(downloadID, DownloadStatus.STATUS_PAUSED);
        }
    }

    /**
     * 删除
     * @param downloadID
     */
    public void deleteDownload(int downloadID, String path){
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.STATUS, DownloadStatus.STATUS_DELETEED);
        int result = mDownloadManager.updateTask(downloadID, values);
        if(result > 0){
            new File(path).deleteOnExit();
            modifyDownloadStatus(downloadID, DownloadStatus.STATUS_DELETEED);
        }
    }

    /**
     * 恢复
     * @param downloadID
     */
    public void resumeDownload(int downloadID){
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.STATUS, DownloadStatus.STATUS_RUNNING);
        int result = mDownloadManager.updateTask(downloadID, values);
        if(result > 0){
            modifyDownloadStatus(downloadID, DownloadStatus.STATUS_PENDING);
        }
    }

    private void modifyDownloadStatus(int downloadID, int status){
        DownloadItem item = mDownloadManager.queryTask(downloadID);
        if(item != null){
            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(DownloadColumn._ID, downloadID);
            intent.putExtra(DownloadColumn.NAME, item.getName());
            intent.putExtra(DownloadColumn.URI, item.getUri());
            intent.putExtra(DownloadColumn.SUFFIX, item.getSuffix());
            intent.putExtra(DownloadColumn.STATUS, DownloadStatus.STATUS_RUNNING);
            mContext.startService(intent);
        }
    }

}
