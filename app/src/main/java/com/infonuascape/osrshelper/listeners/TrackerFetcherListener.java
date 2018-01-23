package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.players.PlayerSkills;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface TrackerFetcherListener {
    void onTrackingFetched(final PlayerSkills playerSkills, final boolean isUpdated);
    void onTrackingError(final String errorMessage);
}
