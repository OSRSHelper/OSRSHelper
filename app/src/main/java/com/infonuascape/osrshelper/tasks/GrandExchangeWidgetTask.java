package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.fetchers.grandexchange.GEDetailInfoFetcher;
import com.infonuascape.osrshelper.fetchers.grandexchange.GEDetailInfoResults;
import com.infonuascape.osrshelper.fetchers.grandexchange.RSBuddyPriceFetcher;
import com.infonuascape.osrshelper.listeners.GEDetailListener;
import com.infonuascape.osrshelper.listeners.RSBuddyPriceListener;
import com.infonuascape.osrshelper.models.RSBuddyPrice;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangeWidgetTask extends AsyncTask<Void, Void, Void> {
    private RSBuddyPriceListener listener;
    private RSBuddyPriceFetcher priceFetcher;
    private String itemId;
    private RSBuddyPrice rsBuddyPrice;

    public GrandExchangeWidgetTask(final Context context, final RSBuddyPriceListener listener, final String itemId) {
        priceFetcher = new RSBuddyPriceFetcher(context);
        this.listener = listener;
        this.itemId = itemId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        rsBuddyPrice = priceFetcher.fetch(itemId);

        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        if(listener != null) {
            if (rsBuddyPrice != null) {
                listener.onPriceFound(rsBuddyPrice);
            } else {
                listener.onPriceError();
            }
        }
    }
}