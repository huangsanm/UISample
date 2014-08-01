package com.huashengmi.ui.android.ui.download.common;

import java.io.Serializable;

/**
 * Created by huangsm on 2014/7/28 0028.
 * Email:huangsanm@foxmail.com
 */
public class DownloadItem implements Serializable {

    private int id;
    //文件名称
    private String name;
    //包名
    private String pkg;
    //id
    private int downloadID;
    //下载地址
    private String uri;
    //保存路径
    private String path;
    //文件大小
    private String fileSize;
    //后缀
    private String suffix;
    //下载状态
    private int status;
    //下载是什么类型
    private int type;
    //文件版本
    private String version;
    //文件icon地址
    private String iconUri;
    //下载进度
    private long currentByte;
    //文件总大小
    private long totalByte;
    //百分比
    private int percent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public int getDownloadID() {
        return downloadID;
    }

    public void setDownloadID(int downloadID) {
        this.downloadID = downloadID;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public long getCurrentByte() {
        return currentByte;
    }

    public void setCurrentByte(long currentByte) {
        this.currentByte = currentByte;
    }

    public long getTotalByte() {
        return totalByte;
    }

    public void setTotalByte(long totalByte) {
        this.totalByte = totalByte;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
