package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tracker.rt.TrackerFetcher;
import com.infonuascape.osrshelper.tracker.rt.Updater;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.lang.ref.WeakReference;

/**
 * Created by marc_ on 2018-01-14.
 */

public class RuneTrackerFetcherTask extends AsyncTask<String, Void, PlayerSkills> {
    private WeakReference<Context> context;
    private TrackerFetcherListener listener;
    private Account account;
    private TrackerTime time;
    private boolean isUpdating;

    public RuneTrackerFetcherTask(final Context context, final TrackerFetcherListener listener, final Account account, TrackerTime time, boolean isUpdating) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
        this.time = time;
        this.isUpdating = isUpdating;
    }

    @Override
    protected PlayerSkills doInBackground(String... urls) {
        try {
            if (isUpdating) {
                Updater.perform(context.get(), account.username);
            }
            return new TrackerFetcher(context.get(), account.username, time).getPlayerTracker();
        } catch (PlayerNotFoundException e) {
            if(listener != null) {
                listener.onTrackingError(context.get().getString(R.string.not_existing_player, account.username));
            }

        } catch (Exception uhe) {
            uhe.printStackTrace();
            if(listener != null) {
                listener.onTrackingError(context.get().getString(R.string.internal_error));
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(PlayerSkills playerSkills) {
        if(listener != null) {
            listener.onTrackingFetched(playerSkills);
        }
    }
}
