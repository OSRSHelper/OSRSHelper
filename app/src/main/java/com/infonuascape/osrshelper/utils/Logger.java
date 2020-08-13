package com.infonuascape.osrshelper.utils;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * Created by marc_ on 2018-02-05.
 */

public class Logger {
    private static final String TAG = "OSRS Helper";

    public static void add(final Object... logs) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object log : logs) stringBuilder.append(log);

        String log = stringBuilder.toString();
        try {
            FirebaseCrashlytics.getInstance().log(log);
        } catch (Exception e) {
            //Ignore
        }
        Log.d(TAG, log);
    }

    public static void addException(final String tag, final Throwable throwable) {
        String log = tag + ": exception=" + throwable;
        try {
            FirebaseCrashlytics.getInstance().log(log);
        } catch (Exception e) {
            //Ignore
        }
        Log.e(TAG, log);
    }
}
