package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.GEDetailListener;
import com.infonuascape.osrshelper.models.grandexchange.GrandExchangeDetailInfo;
import com.infonuascape.osrshelper.network.GrandExchangeDetailInfoApi;
import com.infonuascape.osrshelper.network.GrandExchangeDetailPlotApi;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangeDetailPlotTask extends AsyncTask<Void, Void, Void> {
    private GEDetailListener listener;

    private GrandExchangeDetailPlotApi.GrandExchangeDetailPlotResults detailPlotResults;
    private GrandExchangeDetailInfo info;
    private String itemId;

    public GrandExchangeDetailPlotTask(final GEDetailListener listener, final String itemId) {
        this.listener = listener;
        this.itemId = itemId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        detailPlotResults = GrandExchangeDetailPlotApi.fetch(itemId);
        info = GrandExchangeDetailInfoApi.fetch(itemId);

        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        if(listener != null) {
            if (detailPlotResults != null && detailPlotResults.datapoints != null && info != null) {
                listener.onInfoFetched(detailPlotResults.datapoints, detailPlotResults.averages, info);
            } else {
                listener.onInfoFetched(null, null, null);
            }
        }
    }
}