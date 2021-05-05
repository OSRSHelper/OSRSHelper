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
    private boolean isFromCache;

    public HiscoresFetcherTask(final Context context, final HiscoresFetcherListener listener, final Account account) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String output = DBController.getQueryCache(context.get(), HiscoreApi.getQueryUrl(account.username));
            if (!TextUtils.isEmpty(output)) {
                playerSkills = HiscoreApi.parseResponse(context.get(), output, account.username);
                if (playerSkills != null) {
                    playerSkills.isNewlyTracked = false;
                    isFromCache = true;
                    return null;
                }
            }
            if (Utils.isNetworkAvailable(context.get())) {
                playerSkills = HiscoreApi.fetch(context.get(), account.username);
            }
        } catch (PlayerNotFoundException e) {
            Logger.addException(TAG, e);
            errorMessage = context.get().getString(R.string.not_existing_player);
        } catch (APIError e) {
            Logger.addException(TAG, e);
            errorMessage = e.getMessage();
        } catch (Exception e) {
            Logger.addException(TAG, e);
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
            } else if (isFromCache) {
                listener.onHiscoresCacheFetched(playerSkills);
            } else {
                listener.onHiscoresFetched(playerSkills);
            }
        }
    }
}
