package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.grandexchange.RSBuddyPrice;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface RSBuddyPriceListener {
    void onPriceFound(final RSBuddyPrice rsBuddyPrice);
    void onPriceError();
}
