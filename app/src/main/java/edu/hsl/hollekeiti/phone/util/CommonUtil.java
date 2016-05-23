package edu.hsl.hollekeiti.phone.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/05/09.
 * 常用公式
 */
public class CommonUtil {
    public static String getStrTime(long filetime) {
        if (filetime == 0) {
            return "未知";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String           ftime  = format.format(new Date(filetime));
        return ftime;
    }

    public static String getStrTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String           ftime  = format.format(new Date(System.currentTimeMillis()));
        return ftime;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a == null ? false : a.equals(b));
    }

    private static DecimalFormat df = new DecimalFormat("#.00");

    public static String getFileSize(long filesize) {
        StringBuffer buffer = new StringBuffer();
        if (filesize < 1024) {
            buffer.append(filesize);
            buffer.append("B");
        } else if (filesize < 1024 * 1024) {
            buffer.append(df.format((double) filesize / 1024));
            buffer.append("K");
        } else if (filesize < 1024 * 1024 * 1024) {
            buffer.append(df.format((double) filesize / 1024 / 1024));
            buffer.append("M");
        } else {
            buffer.append(df.format((double) filesize / 1024 / 1024 / 1024));
            buffer.append("G");
        }
        return buffer.toString();
    }

}