package edu.hsl.hollekeiti.phone.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/05/09.
 * 工具类
 */
public class DeviceUtil {
    public static int dp2px(Context context, int dp) {
        float sc = context.getResources().getDisplayMetrics().density;
        return (int) (dp * sc + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        float sc = context.getResources().getDisplayMetrics().density;
        return (int) (px / sc + 0.5f);
    }

    /**
     * 屏幕宽
     */
    public static int getScreenWidthPX(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }

    /**
     * 屏幕高
     */
    public static int getScreenHightPX(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }
}
