package com.huashengmi.ui.android.ui.download.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DownLoadContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.huashengmi.ui.android.ui.download";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DownloadColumn.TABLE_NAME);

    // 返回集合类型
    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + DownloadColumn.TABLE_NAME;
    // 非集合类型
    private static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + DownloadColumn.TABLE_NAME;

    private static final int CODE_DOWNLOADS = 2;
    private static final int CODE_DOWNLOAD = 1;

    // 地址匹配
    private static final UriMatcher mUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private Context mContext;
    private DownloadDataBase.DatabaseHelper mDBHelper;

    static {
        // 操作整张表
        mUriMatcher.addURI(AUTHORITY, DownloadColumn.TABLE_NAME, CODE_DOWNLOADS);
        // 通过id查询
        mUriMatcher.addURI(AUTHORITY, DownloadColumn.TABLE_NAME + "/#", CODE_DOWNLOAD);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = -1;
        int flag = mUriMatcher.match(uri);
        if (flag == CODE_DOWNLOAD) {
            long id = ContentUris.parseId(uri);
            selection = DownloadColumn._ID + "=?";
            selectionArgs = new String[]{id + ""};
            result = mDBHelper.getWritableDatabase().delete(DownloadColumn.TABLE_NAME, selection, selectionArgs);
        } else if (flag == CODE_DOWNLOADS) {
            result = mDBHelper.getWritableDatabase().delete(DownloadColumn.TABLE_NAME, selection, selectionArgs);
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        final int matcherItem = mUriMatcher.match(uri);
        if (matcherItem == CODE_DOWNLOAD) {
            return CONTENT_TYPE_ITEM;
        } else if (matcherItem == CODE_DOWNLOADS) {
            return CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mDBHelper.getWritableDatabase().insert(DownloadColumn.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDBHelper = DownloadDataBase.getInstance(mContext);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int flag = mUriMatcher.match(uri);
        if (flag == CODE_DOWNLOAD) {
            long id = ContentUris.parseId(uri);
            selection = DownloadColumn._ID + "=?";
            selectionArgs = new String[]{id + ""};
            cursor = mDBHelper.getReadableDatabase().query(DownloadColumn.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        } else if (flag == CODE_DOWNLOADS) {
            cursor = mDBHelper.getReadableDatabase().query(DownloadColumn.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int result = -1;
        int flag = mUriMatcher.match(uri);
        if(flag == CODE_DOWNLOAD){
            long id = ContentUris.parseId(uri);
            selection = DownloadColumn._ID + "=?";
            selectionArgs = new String[]{id + ""};
            result = mDBHelper.getWritableDatabase().update(DownloadColumn.TABLE_NAME, values, selection, selectionArgs);
        }else{
            result = mDBHelper.getWritableDatabase().update(DownloadColumn.TABLE_NAME, values, selection, selectionArgs);
        }
        return result;
    }
}
