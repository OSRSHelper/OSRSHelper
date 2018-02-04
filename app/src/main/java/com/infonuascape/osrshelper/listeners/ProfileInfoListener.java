package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.players.Delta;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-02-03.
 */

public interface ProfileInfoListener {
    void onProfileInfoLoaded(final ArrayList<Delta> deltas);
}
