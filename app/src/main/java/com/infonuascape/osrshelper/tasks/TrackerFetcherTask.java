package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.network.TrackerApi;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marc_ on 2018-01-14.
 */

public class TrackerFetcherTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "TrackerFetcherTask";
    private WeakReference<Context> context;
    private Account account;
    private TrackerFetcherListener listener;
    private Map<TrackerTime, PlayerSkills> trackings;
    private String lastUpdate;
    private int combatLvl;

    public TrackerFetcherTask(final Context context, final TrackerFetcherListener listener, final Account account) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            trackings = TrackerApi.fetch(account.username);
            if (trackings.size() > 0) {
                account.combatLvl = Utils.getCombatLvl(trackings.get(trackings.keySet().iterator().next()));

                for (TrackerTime trackerTime : trackings.keySet()) {
                    PlayerSkills playerSkills = trackings.get(trackerTime);
                    if (playerSkills != null && playerSkills.lastUpdate != null) {
                        lastUpdate = playerSkills.lastUpdate;
                        combatLvl = Utils.getCombatLvl(playerSkills);
                        break;
                    }
                }

                if (lastUpdate == null) {
                    throw new PlayerNotFoundException(account.username);
                } else {
                    try {
                        DBController.updateAccount(context.get(), account);
                    } catch (Exception e) {
                        //You can't access database via the widget
                    }
                }
            }
        } catch (PlayerNotFoundException e) {
            Logger.addException(TAG, e);
            if (listener != null) {
                if (context.get() != null) {
                    listener.onTrackingError(context.get().getString(R.string.now_tracking));
                } else {
                    listener.onTrackingError("Error");
                }
            }
        } catch (Exception e) {
            Logger.addException(TAG, e);
            if (listener != null) {
                if (context.get() != null) {
                    listener.onTrackingError(e.getMessage());
                } else {
                    listener.onTrackingError("Error");
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (listener != null) {
            if (trackings == null) {
                trackings = new HashMap<>();
            }
            listener.onTrackingFetched(trackings, lastUpdate, combatLvl);
        }
    }
}