package com.huashengmi.ui.android.ui.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huangsm on 2014/7/28 0028.
 * Email:huangsanm@foxmail.com
 */
public class DownloadDataBase {

    private static DatabaseHelper mDatabase;
    public synchronized static DatabaseHelper getInstance(Context context){
        if(mDatabase == null){
            mDatabase = new DatabaseHelper(context);
        }
        return mDatabase;
    }


    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "db_download.db";
        private static final int DB_VERSION = 1;

        public DatabaseHelper(final Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            onUpgrade(db, 0, DB_VERSION);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, int oldV, final int newV) {
            upgrade(db, oldV, newV);
        }

        //创建数据库
        private void upgrade(SQLiteDatabase db, int oldV, final int newV){
            db.execSQL("DROP TABLE IF EXISTS " + DownloadColumn.TABLE_NAME);
            db.execSQL("CREATE TABLE " + DownloadColumn.TABLE_NAME + " (" + DownloadColumn._ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DownloadColumn.NAME + " TEXT, "
                    + DownloadColumn.DOWNLOAD_ID + " INTEGER, "
                    + DownloadColumn.PKG_NAME + " TEXT, "
                    + DownloadColumn.URI + " TEXT, "
                    + DownloadColumn.PATH + " TEXT, "
                    + DownloadColumn.FILE_SIZE + " TEXT, "
                    + DownloadColumn.SUFFIX + " TEXT, "
                    + DownloadColumn.STATUS + " INTEGER, "
                    + DownloadColumn.TYPE + " INTEGER, "
                    + DownloadColumn.VERSION + " TEXT, "
                    + DownloadColumn.ICON_URI + " TEXT, "
                    + DownloadColumn.CURRENT_BYTE + " LONG, "
                    + DownloadColumn.TOTAL_BYTE + " LONG); ");
        }
    }

}
