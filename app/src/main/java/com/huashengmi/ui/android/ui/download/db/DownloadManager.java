package com.huashengmi.ui.android.ui.download.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.huashengmi.ui.android.ui.download.common.DownloadItem;
import com.huashengmi.ui.android.ui.download.common.DownloadStatus;
import com.huashengmi.ui.android.ui.download.common.DownloadUtils;
import com.huashengmi.ui.android.utils.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsm on 2014/7/28 0028.
 * Email:huangsanm@foxmail.com
 */
public class DownloadManager {

    private ContentResolver mResolver;
    public DownloadManager (ContentResolver resolver){
        mResolver = resolver;
    }

    public Long addTask(DownloadItem item){
        ContentValues values = new ContentValues();
        values.put(DownloadColumn.NAME, item.getName());
        values.put(DownloadColumn.DOWNLOAD_ID, item.getDownloadID());
        values.put(DownloadColumn.FILE_SIZE, item.getFileSize());
        values.put(DownloadColumn.ICON_URI, item.getIconUri());
        values.put(DownloadColumn.PATH, item.getPath());
        values.put(DownloadColumn.PKG_NAME, item.getPkg());
        values.put(DownloadColumn.VERSION, item.getVersion());
        values.put(DownloadColumn.STATUS, item.getStatus());
        values.put(DownloadColumn.SUFFIX,item.getSuffix());
        values.put(DownloadColumn.URI, item.getUri());
        Uri uri = mResolver.insert(DownLoadContentProvider.CONTENT_URI, values);
        notifyChange();
        return Long.valueOf(uri.getLastPathSegment());
    }

    public int deleteTask(int downloadID){
        Uri uri = ContentUris.withAppendedId(DownLoadContentProvider.CONTENT_URI, downloadID);
        notifyChange();
        return mResolver.delete(uri, null, null);
    }

    public int updateTask(int downloadID, ContentValues values){
        Uri uri = ContentUris.withAppendedId(DownLoadContentProvider.CONTENT_URI, downloadID);
        notifyChange();
        return mResolver.update(uri, values, null, null);
    }

    public DownloadItem queryTask(int downloadID){
        DownloadItem item = null;
        notifyChange();
        Uri uri = ContentUris.withAppendedId(DownLoadContentProvider.CONTENT_URI, downloadID);
        Cursor cursor = mResolver.query(uri, null, null, null, null);
        try {
            if(cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                item = convertTaskItem(cursor);
            }
        }finally {
            cursor.close();
        }
        return item;
    }

    public boolean checkTaskStatus(int downloadID, int status){
        Globals.log("checkTaskStatus:" + downloadID + ",status:" + status);
        int checkResult = 0;
        Cursor cursor = mResolver.query(DownLoadContentProvider.CONTENT_URI, new String[]{DownloadColumn.STATUS}, DownloadColumn._ID + "=?", new String[]{downloadID + ""}, null);
        try {
            if(cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                checkResult = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadColumn.STATUS));
            }
        } finally {
            cursor.close();
        }
        return checkResult == status;
    }

    public Cursor queryTask(){
        return mResolver.query(DownLoadContentProvider.CONTENT_URI, null, DownloadColumn.STATUS + "<>" + DownloadStatus.STATUS_DELETEED, null, null);
    }

    public String getStringVal(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public int getIntVal(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public DownloadItem convertTaskItem(Cursor cursor){
        DownloadItem item = new DownloadItem();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DownloadColumn._ID)));
        item.setName(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.NAME)));
        item.setPkg(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.PKG_NAME)));
        item.setDownloadID(cursor.getInt(cursor.getColumnIndexOrThrow(DownloadColumn.DOWNLOAD_ID)));
        item.setUri(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.URI)));
        item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.PATH)));
        item.setFileSize(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.FILE_SIZE)));
        item.setSuffix(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.SUFFIX)));
        item.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(DownloadColumn.STATUS)));
        item.setType(cursor.getInt(cursor.getColumnIndexOrThrow(DownloadColumn.TYPE)));
        item.setVersion(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.VERSION)));
        item.setIconUri(cursor.getString(cursor.getColumnIndexOrThrow(DownloadColumn.ICON_URI)));
        item.setCurrentByte(cursor.getLong(cursor.getColumnIndexOrThrow(DownloadColumn.CURRENT_BYTE)));
        item.setTotalByte(cursor.getLong(cursor.getColumnIndexOrThrow(DownloadColumn.TOTAL_BYTE)));
        item.setPercent(DownloadUtils.getProgressValue(item.getTotalByte(), item.getCurrentByte()));
        return item;
    }

    private void notifyChange(){
        mResolver.notifyChange(DownLoadContentProvider.CONTENT_URI, null);
    }
}