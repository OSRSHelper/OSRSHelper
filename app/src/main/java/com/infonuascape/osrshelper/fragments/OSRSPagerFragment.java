package com.infonuascape.osrshelper.fragments;


import androidx.fragment.app.Fragment;

/**
 * Created by marc_ on 2018-01-20.
 */

public abstract class OSRSPagerFragment extends OSRSFragment {
    private static final String TAG = "OSRSPagerFragment";

    public abstract void onPageVisible();
}
