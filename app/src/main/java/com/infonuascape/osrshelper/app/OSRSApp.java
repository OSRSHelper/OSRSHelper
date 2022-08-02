package com.infonuascape.osrshelper.app;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.infonuascape.osrshelper.controllers.NotificationController;
import com.infonuascape.osrshelper.db.OSRSDatabaseFacade;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.network.NetworkStack;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2017-07-08.
 */

public class OSRSApp extends Application {
    private static final String TAG = "OSRSApp";

    private OSRSDatabaseFacade databaseFacade;
    private NetworkStack networkStack;
    private static OSRSApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        networkStack = new NetworkStack(this);
        databaseFacade = new OSRSDatabaseFacade(this);
        FirebaseApp.initializeApp(this);

        NotificationController.initNotificationChannels(this);

        final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(this,
                PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
        Utils.subscribeToNews(this, isSubscribedToNews);

        PreferencesController.setPreference(this, PreferencesController.USER_PREF_HOVER_MENU_ENABLED, false);
        PreferencesController.setPreference(this, PreferencesController.USER_PREF_HOVER_MENU_SHOWN, false);
    }

    public static OSRSApp getInstance() {
        return instance;
    }

    public NetworkStack getNetworkStack() {
        return networkStack;
    }

    public OSRSDatabaseFacade getDatabaseFacade() {
        return databaseFacade;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.add(TAG, ": onConfigurationChanged: newConfig=", newConfig);
        if (PreferencesController.getBooleanPreference(this, PreferencesController.USER_PREF_HOVER_MENU_ENABLED, false)) {
            Utils.hideBubble(this);
            Utils.openBubble(this);
        }
    }
}
