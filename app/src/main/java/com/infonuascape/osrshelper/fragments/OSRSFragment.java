package com.infonuascape.osrshelper.fragments;


import android.os.AsyncTask;

import com.infonuascape.osrshelper.utils.Logger;

import androidx.fragment.app.Fragment;

/**
 * Created by marc_ on 2018-01-20.
 */

public abstract class OSRSFragment extends Fragment {
    private static final String TAG = "OSRSFragment";

    protected AsyncTask<Void, Void, Void> asyncTask;

    public boolean onBackPressed() {
        return false;
    }

    public void refreshDataOnPreferencesChanged() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        killAsyncTaskIfStillRunning();
    }

    protected void killAsyncTaskIfStillRunning() {
        if(asyncTask != null) {
            if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.add(TAG, ": killAsyncTaskIfStillRunning: running=true");
                asyncTask.cancel(true);
                asyncTask = null;
            }
        }
    }

}
