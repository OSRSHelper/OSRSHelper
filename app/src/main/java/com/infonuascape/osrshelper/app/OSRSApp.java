package com.infonuascape.osrshelper.app;

import android.app.Application;
import android.app.NotificationManager;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.infonuascape.osrshelper.BuildConfig;
import com.infonuascape.osrshelper.controllers.NotificationController;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by marc_ on 2017-07-08.
 */

public class OSRSApp extends Application {
    private static final String TAG = "OSRSApp";

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics.Builder().core(
                new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .answers(new Answers()).build());

        NotificationController.initNotificationChannels(this);

        final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(this,
                PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
        Utils.subscribeToNews(this, isSubscribedToNews);
    }
}
