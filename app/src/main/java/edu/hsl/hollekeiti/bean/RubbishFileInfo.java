package edu.hsl.hollekeiti.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/05/12.
 */
public class RubbishFileInfo {
    private int      _id;
    private String   softCName;
    private String   softEName;
    private String   apkName;
    private String   filePath;
    private Drawable icon;
    private long     size;
    boolean isDel;

    public RubbishFileInfo(int _id, String softCName, String softEName, String apkName, String filePath) {
        this._id = _id;
        this.softCName = softCName;
        this.softEName = softEName;
        this.apkName = apkName;
        this.filePath = filePath;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSoftCName() {
        return softCName;
    }

    public void setSoftCName(String softCName) {
        this.softCName = softCName;
    }

    public String getSoftEName() {
        return softEName;
    }

    public void setSoftEName(String softEName) {
        this.softEName = softEName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "软件详细信息:_ID:" + _id + ",中文名:" + softCName + ",包名:" + apkName;
    }
}
