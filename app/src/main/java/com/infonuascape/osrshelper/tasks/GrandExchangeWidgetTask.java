package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.grandexchange.GEDetailInfoFetcher;
import com.infonuascape.osrshelper.grandexchange.GEDetailInfoResults;
import com.infonuascape.osrshelper.listeners.GEDetailListener;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangeWidgetTask extends AsyncTask<Void, Void, Void> {
    private GEDetailListener listener;
    private GEDetailInfoFetcher geDetailInfoFetcher;
    private GEDetailInfoResults detailInfoResults;
    private String itemId;

    public GrandExchangeWidgetTask(final Context context, final GEDetailListener listener, final String itemId) {
        geDetailInfoFetcher = new GEDetailInfoFetcher(context);
        this.listener = listener;
        this.itemId = itemId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String output = geDetailInfoFetcher.fetch(itemId);
        detailInfoResults = new GEDetailInfoResults(output);

        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        if(detailInfoResults != null && detailInfoResults.itemInfo != null) {
            if(listener != null) {
                listener.onInfoFetched(null, null, detailInfoResults.itemInfo);
            }
        }
    }
}