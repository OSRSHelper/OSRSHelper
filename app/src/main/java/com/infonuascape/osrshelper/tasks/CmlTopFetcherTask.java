package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.network.TopPlayersApi;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class CmlTopFetcherTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "CmlTopFetcherTask";

    private WeakReference<TopPlayersListener> listener;
    private SkillType skillType;
    private TrackerTime period;
    private List<PlayerExp> playerExps;

    public CmlTopFetcherTask(final TopPlayersListener listener, final SkillType skillType, final TrackerTime period) {
        this.listener = new WeakReference<>(listener);
        this.skillType = skillType;
        this.period = period;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            playerExps = TopPlayersApi.fetch(skillType, period);
        } catch (APIError | JSONException apiError) {
            apiError.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(listener.get() != null) {
            listener.get().onPlayersFetched(playerExps);
        }
    }
}
