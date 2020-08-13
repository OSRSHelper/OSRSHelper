package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.DataPointsListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.network.DataPointsApi;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-01-14.
 */

public class DatapointsFetcherTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "DatapointsFetcherTask";
    private DataPointsListener listener;
    private Account account;
    private ArrayList<Delta> deltas;

    public DatapointsFetcherTask(final DataPointsListener listener, final Account account) {
        this.listener = listener;
        this.account = account;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            deltas = DataPointsApi.fetch(account.username);
        } catch (PlayerNotFoundException e) {
            Logger.addException(TAG, e);
        } catch (APIError e) {
            Logger.addException(TAG, e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (listener != null) {
            listener.onDataPointsLoaded(deltas);
        }
    }
}
