package com.infonuascape.osrshelper.enums;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum TrackerTime {
    Day(86400),
    Week(604800),
    Month(2592000),
    Year(31557600),
    All(0);

    public int getSeconds() {
        return seconds;
    }

    private final int seconds;
    private TrackerTime(int seconds) {
        this.seconds = seconds;
    }
}