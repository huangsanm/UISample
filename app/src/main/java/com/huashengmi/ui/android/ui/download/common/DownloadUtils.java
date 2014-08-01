package com.huashengmi.ui.android.ui.download.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by huangsm on 2014/7/29 0029.
 * Email:huangsanm@foxmail.com
 */
public class DownloadUtils {

    public static final String SD_DIR = "UISample";

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getFileDir() {
        return Environment.getExternalStorageDirectory() + File.separator + SD_DIR + File.separator;
    }

    public static long getAvailableStorage() {
        String storageDirectory = null;
        storageDirectory = Environment.getExternalStorageDirectory().toString();
        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    public static Intent installApk(Context context, File file){
        //下载完成，自动安装
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //context.startActivity(intent);
        return intent;
    }

    /**
     * @param apkPath
     */
    public static void autoInstallApk(Context context, String apkPath){
        //下载完成，自动安装
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static int getProgressValue(long totalBytes, long currentBytes) {
        if (totalBytes == -1 || totalBytes == 0) {
            return 0;
        }
        return (int) (currentBytes * 100 / totalBytes);
    }
}
