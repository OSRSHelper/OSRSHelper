package com.infonuascape.osrshelper.models.grandexchange;

import com.infonuascape.osrshelper.enums.TrendRate;

/**
 * Created by marc_ on 2018-01-31.
 */

public class Trend {
    public Trend(String value, int change, TrendRate rate) {
        this.value = value;
        this.change = change;
        this.rate = rate;
    }

    public String value;
    public int change;
    public TrendRate rate;
}

