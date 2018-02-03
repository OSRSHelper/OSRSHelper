package com.infonuascape.osrshelper.fragments;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by marc_ on 2018-01-20.
 */

public abstract class OSRSPagerFragment extends OSRSFragment {
    private static final String TAG = "OSRSPagerFragment";

    public abstract void onPageVisible();
}
