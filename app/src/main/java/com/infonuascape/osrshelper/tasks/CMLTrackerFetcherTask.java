package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tracker.TrackerFetcher;
import com.infonuascape.osrshelper.tracker.Updater;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotTrackedException;

import java.lang.ref.WeakReference;

/**
 * Created by marc_ on 2018-01-14.
 */

public class CMLTrackerFetcherTask extends AsyncTask<String, Void, PlayerSkills> {
    private WeakReference<Context> context;
    private Account account;
    private TrackerTime time;
    private boolean isUpdating;
    private TrackerFetcherListener listener;

    public CMLTrackerFetcherTask(final Context context, final TrackerFetcherListener listener, final Account account, final TrackerTime time, final boolean isUpdating) {
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
            return new TrackerFetcher(context.get(), account.username, time).getPlayerSkills();
        } catch (PlayerNotFoundException e) {
            if(listener != null) {
                listener.onTrackingError(context.get().getString(R.string.not_existing_player));
            }
        } catch (PlayerNotTrackedException e) {
            if(listener != null) {
                listener.onTrackingError(context.get().getString(R.string.not_tracked_player));
            }
        } catch (APIError e) {
            if(listener != null) {
                listener.onTrackingError(e.getMessage());
            }
        } catch (Exception uhe) {
            uhe.printStackTrace();
            if(listener != null) {
                listener.onTrackingError(uhe.getMessage());
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