package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.top.TopFetcher;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class TopFetcherNetwork extends AsyncTask<String, Void, List<PlayerExp>> {
    private static final String TAG = "TopFetcherNetwork";

    private WeakReference<Context> context;
    private WeakReference<TopPlayersListener> listener;

    public TopFetcherNetwork(final Context context, final TopPlayersListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected List<PlayerExp> doInBackground(String... urls) {
        try {
            TopFetcher tf = new TopFetcher(context.get(), SkillType.Agility, TopFetcher.Period.Day);
            return tf.processAPI();
        } catch (ParserErrorException e) {
            e.printStackTrace();
        } catch (APIError apiError) {
            apiError.printStackTrace();
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
