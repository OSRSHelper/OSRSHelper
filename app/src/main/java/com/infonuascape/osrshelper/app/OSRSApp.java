package com.infonuascape.osrshelper.app;

import android.app.Application;

import com.infonuascape.osrshelper.BuildConfig;
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
        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }
}
