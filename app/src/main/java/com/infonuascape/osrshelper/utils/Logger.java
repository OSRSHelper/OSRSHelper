package com.infonuascape.osrshelper.utils;

import android.util.Log;

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
        if (BuildConfig.DEBUG) {
            Log.d(TAG, log);
        }
    }

    public static void addException(final Throwable throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }
    }
}
