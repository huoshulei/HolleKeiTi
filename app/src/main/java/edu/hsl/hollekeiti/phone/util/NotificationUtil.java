package edu.hsl.hollekeiti.phone.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import edu.hsl.hollekeiti.R;

/**
 * Created by Administrator on 2016/05/09.
 */
public class NotificationUtil {
    public static final String TAG = "啊啊啊啊啊";
    private static NotificationManager sManager;
    private static Notification        sNotification;
    public static final int NOTIFI_APPICON_ID = 1;

    public static boolean isOpenNotification(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        return preferences.getBoolean("open", true);
    }

    public static void setOpenNotification(Context context, boolean b) {
        SharedPreferences        preferences = context.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor      = preferences.edit();
        editor.putBoolean("open", b);
        editor.commit();
    }

    public static void cancelAppIconNotification(Context context) {
        if (sManager == null) {
            sManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        sManager.cancel(NOTIFI_APPICON_ID);
    }

    public static void showAppIconNotification(Context context) {
        if (sNotification == null) {
            Log.d(TAG, "onCheckedChanged: 000000000000000000000000000000000000010");
            sNotification = new Notification();
        }
        sNotification.flags = Notification.FLAG_NO_CLEAR;
        sNotification.icon = R.mipmap.ic_launcher;
        sNotification.tickerText = "新通知";
        sNotification.when = System.currentTimeMillis();
        Log.d(TAG, "onCheckedChanged: 000000000000000000000000000000000000200");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification_appincon);
        sNotification.contentView = remoteViews;
        Intent        intent        = new Intent("edu.hsl.hollekeiti");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        sNotification.contentIntent = pendingIntent;
        if (sManager == null) {
            Log.d(TAG, "onCheckedChanged: 000000000000000000000000000000000300000");
            sManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Log.d(TAG, "onCheckedChanged: 000000000000000000000000000000000400000");
        sManager.notify(NOTIFI_APPICON_ID, sNotification);
    }
}
