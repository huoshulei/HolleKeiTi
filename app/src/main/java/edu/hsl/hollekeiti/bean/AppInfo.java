package edu.hsl.hollekeiti.bean;

import android.content.pm.PackageInfo;

/**
 * Created by Administrator on 2016/05/08.
 */
public class AppInfo {
  private PackageInfo mPackageInfo;
  private boolean isDel;

    public AppInfo(PackageInfo packageInfo) {
        this(packageInfo,false);
    }

    public AppInfo(PackageInfo packageInfo, boolean isDel) {
        mPackageInfo = packageInfo;
        this.isDel = isDel;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        mPackageInfo = packageInfo;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }
}
