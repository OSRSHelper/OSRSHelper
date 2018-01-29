package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.TrackerUpdateListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.tracker.Updater;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by marc_ on 2018-01-14.
 */

public class CmlTrackerUpdateTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Context> context;
    private Account account;
    private TrackerUpdateListener listener;
    private boolean isSuccess;

    public CmlTrackerUpdateTask(final Context context, final TrackerUpdateListener listener, final Account account) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
        isSuccess = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            JSONObject jsonObject = Updater.perform(context.get(), account.username);
            if(jsonObject.has("success")) {
                isSuccess = jsonObject.getInt("success") == 1;
            }
        } catch (Exception uhe) {
            uhe.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(listener != null) {
            listener.onUpdatingDone(isSuccess);
        }
    }
}