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
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.lang.ref.WeakReference;

/**
 * Created by marc_ on 2018-01-14.
 */

public class HiscoresFetcherTask extends AsyncTask<Void, Void, Void> {
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
            String output = DBController.getQueryCache(context.get(), HiscoreApi.getQueryUrl(account.username, account.type));
            if (!TextUtils.isEmpty(output)) {
                PlayerSkills playerSkills = HiscoreApi.parseResponse(output);
                if(playerSkills != null && listener != null) {
                    listener.onHiscoresCacheFetched(playerSkills);
                }
            }
            playerSkills = HiscoreApi.fetch(account.username, account.type);
            account.combatLvl = Utils.getCombatLvl(playerSkills);
            DBController.setCombatLvlForAccount(context.get(), account);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
            errorMessage = context.get().getString(R.string.not_existing_player);
        } catch (Exception uhe) {
            uhe.printStackTrace();
            errorMessage = context.get().getString(R.string.internal_error);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(listener != null) {
            if (!TextUtils.isEmpty(errorMessage)) {
                listener.onHiscoresError(errorMessage);
            } else {
                listener.onHiscoresFetched(playerSkills);
            }
        }
    }
}
