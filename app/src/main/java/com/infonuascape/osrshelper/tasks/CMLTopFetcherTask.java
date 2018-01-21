package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.enums.Period;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.top.TopFetcher;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class CMLTopFetcherTask extends AsyncTask<String, Void, List<PlayerExp>> {
    private static final String TAG = "CMLTopFetcherTask";

    private WeakReference<Context> context;
    private WeakReference<TopPlayersListener> listener;
    private SkillType skillType;
    private Period period;

    public CMLTopFetcherTask(final Context context, final TopPlayersListener listener, final SkillType skillType, final Period period) {
        this.context = new WeakReference<>(context);
        this.listener = new WeakReference<>(listener);
        this.skillType = skillType;
        this.period = period;
    }

    @Override
    protected List<PlayerExp> doInBackground(String... urls) {
        try {
            TopFetcher tf = new TopFetcher(context.get(), skillType, period);
            return tf.processAPI();
        } catch (ParserErrorException e) {
            e.printStackTrace();
        } catch (APIError apiError) {
            apiError.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<PlayerExp> playerList) {
        if(listener.get() != null) {
            listener.get().onPlayersFetched(playerList);
        }
    }
}
