package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.players.PlayerExp;

import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public interface TopPlayersListener {
    void onPlayersFetched(List<PlayerExp> playerList);
}
