package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.players.Delta;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-02-03.
 */

public interface DataPointsListener {
    void onDataPointsLoaded(final ArrayList<Delta> deltas);
}
