package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.ProfileInfoListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.network.ProfileInfoApi;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marc_ on 2018-01-14.
 */

public class ProfileInfoFetcherTask extends AsyncTask<Void, Void, Void> {
    private final static int THREE_MONTHS = 60 * 60 * 24 * 30 * 3;
    private ProfileInfoListener listener;
    private Account account;
    private ArrayList<Delta> deltas;

    public ProfileInfoFetcherTask(final ProfileInfoListener listener, final Account account) {
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            deltas = ProfileInfoApi.fetch(account.username, THREE_MONTHS);

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
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        } catch (APIError apiError) {
            apiError.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(listener != null) {
            listener.onProfileInfoLoaded(deltas);
        }
    }
}
