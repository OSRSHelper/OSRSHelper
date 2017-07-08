package com.infonuascape.osrshelper;

import android.app.Application;

import com.infonuascape.osrshelper.utils.http.NetworkStack;

/**
 * Created by marc_ on 2017-07-08.
 */

public class OSRSApp extends Application {
    private static final String TAG = "OSRSApp";

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkStack.init(this);
    }
}
