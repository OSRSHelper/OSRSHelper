package com.infonuascape.osrshelper.models.grandexchange;

import com.infonuascape.osrshelper.enums.TrendRate;

/**
 * Created by marc_ on 2018-01-31.
 */

public class TrendChange {
    public String change;
    public TrendRate rate;

    public TrendChange(String change, TrendRate rate) {
        this.change = change;
        this.rate = rate;
    }
}

