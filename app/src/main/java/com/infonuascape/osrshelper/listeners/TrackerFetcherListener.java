package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

import java.util.Map;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface TrackerFetcherListener {
    void onTrackingFetched(final Map<TrackerTime, PlayerSkills> trackings, String lastUpdate, int combatLvl);
    void onTrackingError(final String errorMessage);
}
