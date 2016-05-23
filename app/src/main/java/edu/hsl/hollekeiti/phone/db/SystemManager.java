package edu.hsl.hollekeiti.phone.db;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/05/10.
 */
public class SystemManager {
    public static final  String basicInfos[]
                                    = {"设备型号:", "系统版本:", "手机串号:", "运营商:", "是否ROOT:"};
    public static final  String CPUInfos[]
                                    = {"CPU型号:", "CPU核心数:", "最高频率:", "最低频率:", "当前频率:"};
    public static final  String resolutionInfos[]
                                    = {"摄像头像素:", "照片最大尺寸:", "闪光灯:"};
    public static final  String pixelInfos[]
                                    = {"屏幕分辨率:", "像素密度:", "多点触控:"};
    public static final  String WIFIInfos[]
                                    = {"WIFI连接到:", "WIFI地址:", "WIFI连接速度:", "MAC地址:", "蓝牙状态:"};
    private static final String TAG = "才才踩踩踩为";
    private        Context             mContext;
    private        TelephonyManager    tm;
    private        WifiManager         wm;
    private        ConnectivityManager cm;
    private static SystemManager       sm;
    private        ActivityManager     am;

    public SystemManager(Context context) {
        mContext = context;
        tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    public static SystemManager getSystemManager(Context context) {
        if (sm == null) {
            sm = new SystemManager(context);
        }
        return sm;
    }

    /**
     * 检测基本信息
     */
    public boolean BasicInfo(final String[] datas) {
        if (datas == null || datas.length < basicInfos.length) {
            return false;
        }
        datas[0] = basicInfos[0] + Build.MODEL;
        datas[1] = basicInfos[1] + Build.VERSION.RELEASE;
        datas[2] = basicInfos[2] + (tm.getDeviceId() == null ? "无" : tm.getDeviceId());
        datas[3] = basicInfos[3] + getProvidersName();
        datas[4] = basicInfos[4] + (isRoot() ? "是" : "否");
        return true;
    }

    /**
     * 检测CPU信息
     */
    public boolean CPUInfo(final String datas[]) {
        if (datas == null || datas.length < CPUInfos.length) {
            return false;
        }
        datas[0] = CPUInfos[0] + getCpuName();
        datas[1] = CPUInfos[1] + getNumCores();
        datas[2] = CPUInfos[2] + getMaxCpuFreq() + "KHZ";
        datas[3] = CPUInfos[3] + getMinCpuFreq() + "KHZ";
        datas[4] = CPUInfos[4] + getCurCpuFreq() + "KHZ";
        return true;
    }

    /**
     * 分辨吧信息
     */
    public boolean resolutionInfo(final String[] datas) {
        if (datas == null || datas.length < resolutionInfos.length) {
            return false;
        }
        datas[0] = resolutionInfos[0] + getCameraResolution();
        datas[1] = resolutionInfos[1] + getMaxPhoneSize();
        datas[2] = resolutionInfos[2] + (getFlashMode() == null ? "无" : getFlashMode());
        return true;
    }

    /**
     * 检测像素
     */
    public boolean pixelInfo(final String[] datas) {
        if (datas == null || datas.length < pixelInfos.length) {
            return false;
        }
        datas[0] = pixelInfos[0] + getResolution();
        datas[0] = pixelInfos[0] + getPixDensity();
        datas[0] = pixelInfos[0] + (isSupportMultiTouch() ? "支持" : "不支持");
        return true;
    }

    /**
     * 检测WiFi信息
     */
    public boolean WiFiInfo(final String[] datas) {
        if (datas == null || datas.length < WIFIInfos.length) {
            return false;
        }
        WifiInfo wifiInfo = wm.getConnectionInfo();
        datas[0] = WIFIInfos[0] + (wifiInfo == null ? "未连接" : wifiInfo.getSSID());
        datas[1] = WIFIInfos[1] + (wifiInfo == null ? "无" : wifiInfo.getIpAddress());
        datas[2] = WIFIInfos[2] + (wifiInfo == null ? "0" : wifiInfo.getLinkSpeed());
        datas[3] = WIFIInfos[3] + (wifiInfo == null ? "无" : wifiInfo.getMacAddress());
        datas[4] = WIFIInfos[4] + getBlueToothState();
        return true;
    }

    /**
     * Role Telecom service providers 获取手机服务商信息
     * 需要加入READ_PHONE_STATE权限
     */
    public String getProvidersName() {
        String ProvidersName = null;
        String IMSI          = tm.getSubscriberId();
        if (IMSI == null) {
            return "无";
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        } else {
            return "无";
        }
        return ProvidersName;
    }

    /**
     * 判断手机是否Root
     */
    public boolean isRoot() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * CPU数量
     */
    public int getPhoneCPUNumber() {
        class CpuFilter implements FileFilter {

            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            File   dir   = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public String getCpuName() {
        try {
            FileReader     fr    = new FileReader("/proc/cpuinfo");
            BufferedReader br    = new BufferedReader(fr);
            String         text  = br.readLine();
            String[]       array = text.split(":\\s+", 2);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得CPU核心数
     * path"/sys/devices/system/cpu"
     */
    private int getNumCores() {
        class CpuFilter implements FileFilter {

            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            File   dir   = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * // 获取CPU最大频率（单位KHZ）
     * // "/system/bin/cat" 命令行
     * // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
     */
    public String getMaxCpuFreq() {
        String         result = "";
        ProcessBuilder cmd;
        String[] args = {"/system/bin/cat"
                , "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
        try {
            cmd = new ProcessBuilder(args);
            Process     process = cmd.start();
            InputStream in      = process.getInputStream();
            byte[]      re      = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException e) {
            result = "N/A";
            e.printStackTrace();
        }
        return result.trim();
    }

    public String getMinCpuFreq() {
        String         result = "";
        ProcessBuilder cmd;
        String[] args = {"/system/bin/cat"
                , "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
        try {
            cmd = new ProcessBuilder(args);
            Process     process = cmd.start();
            InputStream in      = process.getInputStream();
            byte[]      re      = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException e) {
            result = "N/A";
            e.printStackTrace();
        }
        return result.trim();
    }

    public String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br   = new BufferedReader(fr);
            String         text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 屏幕最大尺寸
     */
    public String getCameraResolution() {
        String            cameraResolution = "";
        Camera            camera           = Camera.open();
        Camera.Parameters parameters       = camera.getParameters();
        List<Camera.Size> sizes            = parameters.getSupportedPictureSizes();
        Camera.Size       size             = null;
        for (Camera.Size s : sizes) {
            if (size == null) {
                size = s;
            } else if (size.width * size.height < s.height * s.width) {
                size = s;
            }
        }
        cameraResolution = (size.height * size.width) / 10000 + "万像素";
        camera.release();
        return cameraResolution;
    }

    /**
     * 获取照片最大分辨率
     */
    public String getMaxPhoneSize() {
        String maxSize = "";
        try {
            Camera            camera     = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes      = parameters.getSupportedPictureSizes();
            Camera.Size       size       = null;
            for (Camera.Size s : sizes) {
                if (size == null) {
                    size = s;
                } else if (size.height * size.width < s.width * s.height) {
                    size = s;
                }

            }
            maxSize = size.height + "*" + size.width;
            camera.release();
        } catch (Exception e) {
            maxSize = "未知";
        }
        return maxSize;
    }

    /**
     * 闪光灯状态
     */
    public String getFlashMode() {
        String            flashMode  = "";
        Camera            camera     = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        flashMode = parameters.getFlashMode();
        camera.release();
        return flashMode;
    }

    /**
     * 获取手机分辨率
     */
    public String getResolution() {
        String        resolution = "";
        Point         size       = new Point();
        WindowManager manger     = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display       display    = manger.getDefaultDisplay();
        display.getSize(size);
        resolution = size.x + "*" + size.y;
        return resolution;
    }

    /**
     * 获取像素密度
     */
    public float getPixDensity() {
        float density = 0;
        density = mContext.getResources().getDisplayMetrics().density;
        return density;
    }

    /**
     * 是否支持多点触控
     */
    public boolean isSupportMultiTouch() {
        PackageManager pm = mContext.getPackageManager();
        boolean isSupportMulTTouch
                = pm.hasSystemFeature(PackageManager
                .FEATURE_TOUCHSCREEN_MULTITOUCH);
        return isSupportMulTTouch;
    }

    /**
     * 获取蓝牙状态
     */
    public String getBlueToothState() {
        BluetoothAdapter a = BluetoothAdapter.getDefaultAdapter();
        if (a == null) {
            return "设备不支持蓝牙";
        }
        int state = a.getState();
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_OFF:
                return "蓝牙关闭中";

            case BluetoothAdapter.STATE_TURNING_ON:
                return "蓝牙开启中";

            case BluetoothAdapter.STATE_OFF:
                return "蓝牙关闭";

            case BluetoothAdapter.STATE_ON:
                return "蓝牙开启";

        }
        return "未知";
    }

    /**
     * 手机品牌
     */
    public static String getPhoneName() {
        return Build.BRAND;
    }

    /**
     * 设备型号
     */
    public static String getPhoneModel() {
        return Build.MODEL + "Android" + getPhoneSystemVersion();
    }

    /**
     * 系统版本
     */
    public static String getPhoneSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取WIFI名
     */
    public String getPhoneWiFiName() {
        WifiInfo info = wm.getConnectionInfo();
        return info.getSSID() + "";
    }

    /**
     * 获取WIFI的IP
     */
    public String getPhoneWiFiIP() {
        WifiInfo info = wm.getConnectionInfo();
        long     ip   = info.getIpAddress();
        return longToIP(ip);
    }

    /**
     * 讲long数据转换成IP
     */
    private String longToIP(long ip) {
        StringBuffer sb = new StringBuffer("");
        //将高24位置0
        sb.append(String.valueOf((ip & 0x000000FF)));
        sb.append(".");
        //将高1位置0,然后右移8位
        sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高8位置0,然后右移16位
        sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0,然后右移24位
        sb.append(String.valueOf((ip >>> 24)));
        return sb.toString();
    }

    /**
     * 获取WIFI的速度
     */
    public String getPhoneWifiSpeed() {
        WifiInfo info = wm.getConnectionInfo();
        return info.getLinkSpeed() + "";
    }

    /**
     * 获取WIFI的MAC
     */
    public String getPhoneWifiMac() {
        WifiInfo info = wm.getConnectionInfo();
        return info.getMacAddress() + "";
    }

    /**
     * 获取设备连接类型
     */
    public String getPhoneNetworkType() {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (isOnline(info)) {
            return "OFFLINE";
        }
        if (info != null) {
            return info.getTypeName();
        } else {
            return "OFFLINE";
        }
    }

    private boolean isOnline(NetworkInfo info) {
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取电话号码
     */
    public String getPhoneNumber() {
        return tm.getLine1Number();
    }

    /**
     * 获取运营商
     */
    public String getPhoneTelSimName() {
        return tm.getSimOperatorName();
    }

    /**
     * 获取手机IMEI号
     */
    public String getPhoneIMEI() {
//        if (PackageManager.PERMISSION_GRANTED == mContext.getPackageManager()
//                .checkPermission(Manifest.permission.READ_PHONE_STATE, mContext.getPackageName())){
//        }
        try {
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设备系统基带版本
     */
    public String getPhoneSystemBasebandVersion() {
        return Build.RADIO;
    }

    /**
     * 设备系统SDK版本号
     */
    public int getPhoneSystemVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 设备设置版本号
     */
    public String getPhoneSystemVersionID() {
        return Build.ID;
    }

    /**
     * CPU类型名称
     */
    public String getPhoneCPUName() {
        return Build.CPU_ABI;
    }

    /**
     * 设备制造商
     */
    public String getPhoneMadeName() {
        return Build.MANUFACTURER;
    }

    /**
     * 设备型号名称
     */
    public String getPhoneModelName() {
        return Build.MODEL;
    }


}
