package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.network.TrackerApi;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.lang.ref.WeakReference;

/**
 * Created by marc_ on 2018-01-14.
 */

public class CmlTrackerFetcherTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Context> context;
    private Account account;
    private TrackerTime time;
    private TrackerFetcherListener listener;
    private PlayerSkills playerSkills;

    public CmlTrackerFetcherTask(final Context context, final TrackerFetcherListener listener, final Account account, final TrackerTime time) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
        this.time = time;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            playerSkills = TrackerApi.fetch(account.username, time);
        } catch (PlayerNotFoundException e) {
            if(listener != null) {
                if(context.get() != null) {
                    listener.onTrackingError(context.get().getString(R.string.not_existing_player));
                } else {
                    listener.onTrackingError("Error");
                }
            }
        } catch (APIError e) {
            if(listener != null) {
                if(context.get() != null) {
                    listener.onTrackingError(e.getMessage());
                } else {
                    listener.onTrackingError("Error");
                }
            }
        } catch (Exception uhe) {
            uhe.printStackTrace();
            if(listener != null) {
                if(context.get() != null) {
                    listener.onTrackingError(uhe.getMessage());
                } else {
                    listener.onTrackingError("Error");
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(listener != null) {
            listener.onTrackingFetched(playerSkills);
        }
    }
}