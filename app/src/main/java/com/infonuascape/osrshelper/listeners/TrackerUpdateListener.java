package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.network.UpdaterApi;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface TrackerUpdateListener {
    void onUpdatingDone(final UpdaterApi.Response response);
}
