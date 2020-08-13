package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.network.TopPlayersApi;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.exceptions.APIError;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class TopPlayersFetcherTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "TopPlayersFetcherTask";

    private WeakReference<TopPlayersListener> listener;
    private SkillType skillType;
    private TrackerTime period;
    private AccountType accountType;
    private List<PlayerExp> playerExps;

    public TopPlayersFetcherTask(final TopPlayersListener listener, final SkillType skillType, final TrackerTime period, final AccountType accountType) {
        this.listener = new WeakReference<>(listener);
        this.skillType = skillType;
        this.period = period;
        this.accountType = accountType;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            playerExps = TopPlayersApi.fetch(skillType, accountType, period);
        } catch (APIError | JSONException e) {
            Logger.addException(TAG, e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (listener.get() != null) {
            listener.get().onPlayersFetched(playerExps);
        }
    }
}
