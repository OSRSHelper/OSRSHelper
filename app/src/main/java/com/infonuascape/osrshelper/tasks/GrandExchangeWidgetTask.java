package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.RSBuddyPriceListener;
import com.infonuascape.osrshelper.models.grandexchange.RSBuddyPrice;
import com.infonuascape.osrshelper.network.RSBuddyPriceApi;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangeWidgetTask extends AsyncTask<Void, Void, Void> {
    private RSBuddyPriceListener listener;
    private String itemId;
    private RSBuddyPrice rsBuddyPrice;

    public GrandExchangeWidgetTask(final RSBuddyPriceListener listener, final String itemId) {
        this.listener = listener;
        this.itemId = itemId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        rsBuddyPrice = RSBuddyPriceApi.fetch(itemId);

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