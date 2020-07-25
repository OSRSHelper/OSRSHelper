package com.infonuascape.osrshelper.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum TrackerTime {
    Day("day"),
    Week("week"),
    Month("month"),
    Year("year");

    private final String period;
    private final static Map<String, TrackerTime> map = new HashMap<>();

    TrackerTime(String period) {
        this.period = period;
    }

    static {
        for (TrackerTime trackerTime : values()) {
            map.put(trackerTime.period, trackerTime);
        }
    }

    public static TrackerTime create(final String period) {
        return map.get(period);
    }
}