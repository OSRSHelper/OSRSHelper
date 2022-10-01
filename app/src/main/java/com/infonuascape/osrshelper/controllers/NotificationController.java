package com.infonuascape.osrshelper.controllers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;
import com.infonuascape.osrshelper.utils.Logger;

/**
 * Created by marc_ on 2018-02-03.
 */

public class NotificationController {
    private static final String TAG = "NotificationController";

    private final static String OSRS_HELPER_CHANNEL_ID = "OSRS_HELPER_CHANNEL_ID";
    private final static String OSRS_HELPER_CHANNEL_NAME = "OSRS News";

    private final static int OSRS_NEWS_ID = 9001;

    private static void initNotificationChannels(final Context context) {
        Logger.add(TAG, ": initNotificationChannels:");
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
        Logger.add(TAG, ": showOSRSNews: context=", context, ", title=", title, ", description=", description, ", url=", url);

        NotificationManager notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.areNotificationsEnabled()) {
            Logger.add(TAG, ": showOSRSNews: notifications are disabled");
        }

        initNotificationChannels(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, OSRS_HELPER_CHANNEL_ID);

        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.notif_small_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

        Intent resultIntent = MainActivity.getNewsIntent(context, url);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(resultPendingIntent);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{0,100,50,100});
        builder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));

        Notification notification = builder.build();

        notificationManager.notify(OSRS_NEWS_ID, notification);
    }

    public static Intent getNotificationsIntent(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        }

        return intent;
    }
}
