package com.infonuascape.osrshelper.controllers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;

/**
 * Created by marc_ on 2018-02-03.
 */

public class NotificationController {
    private static final String TAG = "NotificationController";

    private final static String OSRS_HELPER_CHANNEL_ID = "OSRS_HELPER_CHANNEL_ID";
    private final static String OSRS_HELPER_CHANNEL_NAME = "OSRS News";

    private final static int OSRS_NEWS_ID = 9001;

    public static void initNotificationChannels(final Context context) {
        Log.i(TAG, "initNotificationChannels:");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(OSRS_HELPER_CHANNEL_ID, OSRS_HELPER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0,100,50,100});
            notificationChannel.setShowBadge(true);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        }
    }

    public static void showOSRSNews(final Context context, final String title, final String description, final String url) {
        NotificationCompat.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, OSRS_HELPER_CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.ic_launcher);

        Intent resultIntent = MainActivity.getNewsIntent(context, url);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{0,100,50,100});
        builder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));

        Notification notification = builder.build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(title.hashCode(), notification);
    }
}
