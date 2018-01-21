package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;

/**
 * Created by marc_ on 2018-01-20.
 */

public class SwitchProfileFragment extends OSRSFragment {
    private static final String TAG = "SwitchProfileFragment";

    public static SwitchProfileFragment newInstance() {
        SwitchProfileFragment fragment = new SwitchProfileFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }
}
