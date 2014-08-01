package com.huashengmi.ui.android.ui.download.db;

import android.provider.BaseColumns;

/**
 * Created by huangsm on 2014/7/28 0028.
 * Email:huangsanm@foxmail.com
 */
public class DownloadColumn implements BaseColumns {

    //表名
    public static final String TABLE_NAME = "download_info";

    //文件名称
    public static final String NAME = "d_name";

    //包名
    public static final String PKG_NAME = "d_pkg";

    //id
    public static final String DOWNLOAD_ID = "d_download_id";

    //下载地址
    public static final String URI = "d_uri";

    //保存路径
    public static final String PATH = "d_path";

    //文件大小
    public static final String FILE_SIZE = "d_file_size";

    //后缀
    public static final String SUFFIX = "d_suffix";

    //临时后缀  .sm
    public static final String TEMP_SUFFIX = "temp_suffix";

    //下载状态
    public static final String STATUS = "d_status";

    //下载是什么类型
    public static final String TYPE = "d_type";

    //文件版本
    public static final String VERSION = "d_version";

    //文件icon地址
    public static final String ICON_URI = "d_icon_uri";

    //下载进度
    public static final String CURRENT_BYTE = "d_current_byte";

    //文件总大小
    public static final String TOTAL_BYTE = "d_total_byte";
}