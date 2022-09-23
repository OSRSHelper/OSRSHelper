package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.listeners.HiscoresFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.network.HiscoreApi;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.lang.ref.WeakReference;

/**
 * Created by marc_ on 2018-01-14.
 */

public class HiscoresFetcherTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "HiscoresFetcherTask";
    private WeakReference<Context> context;
    private HiscoresFetcherListener listener;
    private Account account;
    private PlayerSkills playerSkills;
    private String errorMessage;

    public HiscoresFetcherTask(final Context context, final HiscoresFetcherListener listener, final Account account) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String url = HiscoreApi.getQueryUrl(account.username);
            String output = DBController.getQueryCache(context.get(), url);
            if (!TextUtils.isEmpty(output)) {
                try {
                    playerSkills = HiscoreApi.parseResponse(context.get(), output, account.username, url);
                } catch (Exception e) {
                    Logger.addException(e);
                    //We shouldn't have saved that output
                    DBController.deleteOutputFromQueryCache(context.get(), url);
                }
                if (playerSkills != null) {
                    playerSkills.isNewlyTracked = false;
                    if (listener != null) {
                        listener.onHiscoresCacheFetched(playerSkills);
                    }
                }
            }
            if (Utils.isNetworkAvailable(context.get())) {
                playerSkills = HiscoreApi.fetch(context.get(), account.username);
            }
        } catch (PlayerNotFoundException e) {
            Logger.addException(e);
            errorMessage = context.get().getString(R.string.not_existing_player);
        } catch (APIError e) {
            Logger.addException(e);
            errorMessage = e.getMessage();
        } catch (Exception e) {
            Logger.addException(e);
            if (context.get() != null) {
                errorMessage = context.get().getString(R.string.internal_error);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (listener != null) {
            if (!TextUtils.isEmpty(errorMessage)) {
                listener.onHiscoresError(errorMessage);
            } else {
                listener.onHiscoresFetched(playerSkills);
            }
        }
    }
}
