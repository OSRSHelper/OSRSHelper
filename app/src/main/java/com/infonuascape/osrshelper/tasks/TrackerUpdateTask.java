package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.TrackerUpdateListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.network.UpdaterApi;

/**
 * Created by marc_ on 2018-01-14.
 */

public class TrackerUpdateTask extends AsyncTask<Void, Void, Void> {
    private Account account;
    private TrackerUpdateListener listener;
    private UpdaterApi.Response response;

    public TrackerUpdateTask(final TrackerUpdateListener listener, final Account account) {
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        response = UpdaterApi.perform(account.username);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (listener != null) {
            listener.onUpdatingDone(response);
        }
    }
}