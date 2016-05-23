package edu.hsl.hollekeiti.phone.db;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hsl.hollekeiti.bean.AppInfo;
import edu.hsl.hollekeiti.bean.RuningAppInfo;

/**
 * Created by Administrator on 2016/05/08.
 */
public class AppInfoManager {
    private Context         mContext;
    private PackageManager  mPackageManager;
    private ActivityManager mActivityManager;
    private List<AppInfo> allP    = new ArrayList<>();
    private List<AppInfo> userP   = new ArrayList<>();
    private List<AppInfo> systemP = new ArrayList<>();

    public AppInfoManager(Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
        mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 清理所有程序
     */
    public void killAllProcesses() {
        List<RunningAppProcessInfo> appProcessInfos
                = mActivityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo app : appProcessInfos) {
            if (app.importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                String packageName = app.processName;
                try {
                    ApplicationInfo application = mPackageManager.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA
                                    | PackageManager.GET_SHARED_LIBRARY_FILES
                                    | PackageManager.GET_UNINSTALLED_PACKAGES);
                    if ((application.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        mActivityManager.killBackgroundProcesses(packageName);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 清理制定程序
     */
    public void killProcesses(String packageName) {
        mActivityManager.killBackgroundProcesses(packageName);
    }

    public static final int RUNING_APP_TYPE_SYS  = 0;
    public static final int RUNING_APP_TYPE_USER = 1;

    /**
     * 获取正在运行的程序
     */
    public Map<Integer, List<RuningAppInfo>> getRuningAppInfos() {
        Map<Integer, List<RuningAppInfo>> runingAppInfos = new HashMap<>();
        List<RuningAppInfo>               sysApp         = new ArrayList<>();
        List<RuningAppInfo>               userapp        = new ArrayList<>();
        List<RunningAppProcessInfo> appProcessInfos
                = mActivityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo app : appProcessInfos) {
            String packageName = app.processName;//名字
            int    pid         = app.pid;//ID
            int    importance  = app.importance;//程序级别
            if (importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                Drawable icon;//图标
                String   lableName;//名称
                long     size;//所占内存
                Debug.MemoryInfo[] memoryInfos
                        = mActivityManager.getProcessMemoryInfo(new int[]{pid});
                size = (memoryInfos[0].getTotalPrivateDirty()) * 1024;
                try {
                    icon = mPackageManager.getApplicationIcon(packageName);
                    ApplicationInfo applicationInfo
                            = mPackageManager.getApplicationInfo(packageName
                            , PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES
                                    | PackageManager.GET_UNINSTALLED_PACKAGES);
                    lableName = mPackageManager.getApplicationLabel(applicationInfo).toString();
                    RuningAppInfo runingAppInfo = new RuningAppInfo(packageName, icon, lableName, size);
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        runingAppInfo.setSystem(true);
                        runingAppInfo.setClear(false);
                        sysApp.add(runingAppInfo);
                    } else {
                        runingAppInfo.setSystem(false);
                        runingAppInfo.setClear(true);
                        userapp.add(runingAppInfo);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        runingAppInfos.put(RUNING_APP_TYPE_SYS, sysApp);
        runingAppInfos.put(RUNING_APP_TYPE_USER, userapp);
        return runingAppInfos;
    }

    /**
     * 用来返回本类的唯一对象(单态模块     且做了同步处理,还有话了一下同步处理)
     */
    private static AppInfoManager appManager = null;

    public static AppInfoManager getAppInfoManager(Context context) {
        if (appManager == null) {
            synchronized (context) {
                if (appManager == null) {
                    appManager = new AppInfoManager(context);
                }
            }
        }
        return appManager;
    }

    /**
     * 用来返回所有应用的列表
     */
    public List<AppInfo> getAllPackageInfo(boolean isReset) {
        if (isReset) {
            loadAllActivityPackager();
        }
        return allP;
    }

    /**
     * 返回所有系统程序列表
     */
    public List<AppInfo> getSystemPackageInfo(boolean isReset) {
        if (isReset) {
            loadAllActivityPackager();
            systemP.clear();
            for (AppInfo info : allP) {
                if ((info.getPackageInfo().applicationInfo.flags
                        & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    systemP.add(info);
                }
            }
        }
        return systemP;
    }

    public List<AppInfo> getUserPackageInfo(boolean isReset) {
        if (isReset) {
            loadAllActivityPackager();
            userP.clear();
            for (AppInfo info : allP) {
                if ((info.getPackageInfo().applicationInfo.flags
                        & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    userP.add(info);
                }
            }
        }
        return userP;
    }

    /**
     * 加载应用程序包
     */
    private void loadAllActivityPackager() {
        List<PackageInfo> infos = mPackageManager.getInstalledPackages(0);
        allP.clear();
        for (PackageInfo info : infos) {
            allP.add(new AppInfo(info));
        }
    }
}
