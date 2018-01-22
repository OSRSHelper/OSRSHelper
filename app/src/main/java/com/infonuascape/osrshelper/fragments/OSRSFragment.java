package com.infonuascape.osrshelper.fragments;


import android.support.v4.app.Fragment;

import com.infonuascape.osrshelper.activities.MainActivity;

/**
 * Created by marc_ on 2018-01-20.
 */

public abstract class OSRSFragment extends Fragment {
    public boolean onBackPressed() {
        return false;
    }

    public void refreshDataOnPreferencesChanged() {

    }
}
