package com.infonuascape.osrshelper.utils;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.infonuascape.osrshelper.BuildConfig;

/**
 * Created by marc_ on 2018-02-05.
 */

public class Logger {
    private static final String TAG = "OSRSHelper";

    public static void add(final Object... logs) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object log : logs) stringBuilder.append(log);

        String log = stringBuilder.toString();
        try {
            FirebaseCrashlytics.getInstance().log(log);
        } catch (Exception e) {
            //Ignore
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, log);
        }
    }

    public static void addException(final Throwable throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(throwable);
        } catch (Exception e) {
            //Ignore
        }
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }
    }
}
