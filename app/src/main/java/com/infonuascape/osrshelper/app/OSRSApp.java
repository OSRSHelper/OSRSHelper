package com.infonuascape.osrshelper.app;

import android.app.Application;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.infonuascape.osrshelper.controllers.NotificationController;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.network.NetworkStack;

/**
 * Created by marc_ on 2017-07-08.
 */

public class OSRSApp extends Application {
    private static final String TAG = "OSRSApp";

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        NetworkStack.init(this);

        NotificationController.initNotificationChannels(this);

        final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(this,
                PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
        Utils.subscribeToNews(this, isSubscribedToNews);
    }
}
