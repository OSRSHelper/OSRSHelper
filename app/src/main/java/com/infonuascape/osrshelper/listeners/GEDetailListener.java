package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.grandexchange.GrandExchangeDetailInfo;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface GEDetailListener {
    void onInfoFetched(final DataPoint[] datapoints, final DataPoint[] averages, final GrandExchangeDetailInfo itemInfo);
}
