package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.fetchers.profile.ProfileInfoFetcher;
import com.infonuascape.osrshelper.listeners.ProfileInfoListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.Delta;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marc_ on 2018-01-14.
 */

public class ProfileInfoFetcherTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Context> context;
    private ProfileInfoListener listener;
    private Account account;
    private ArrayList<Delta> deltas;

    public ProfileInfoFetcherTask(final Context context, final ProfileInfoListener listener, final Account account) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        deltas = new ProfileInfoFetcher(context.get()).fetch(account.username, 60 * 60 * 24 * 30 * 3); //Last three months

        //Sort desc
        Collections.sort(deltas, new Comparator<Delta>() {
            @Override
            public int compare(Delta delta, Delta t1) {
                if(delta.timestampRecent > t1.timestampRecent) {
                    return -1;
                } else if(delta.timestampRecent == t1.timestampRecent) {
                    return 0;
                }

                return 1;
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(listener != null) {
            listener.onProfileInfoLoaded(deltas);
        }
    }
}
