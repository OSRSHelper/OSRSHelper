package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.players.PlayerSkills;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface HiscoresFetcherListener {
    void onHiscoresCacheFetched(final PlayerSkills playerSkills);
    void onHiscoresFetched(final PlayerSkills playerSkills);
    void onHiscoresError(final String errorMessage);
}
