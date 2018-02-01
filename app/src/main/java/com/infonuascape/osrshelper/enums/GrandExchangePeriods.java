package com.infonuascape.osrshelper.enums;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by marc_ on 2018-01-31.
 */

public enum GrandExchangePeriods {
    WEEK(7, 7, "EEE"), MONTH(30, 4, "MMM d"), THREE_MONTHS(90, 3, "MMM"), SIX_MONTHS(180, 6, "MMM");

    private final int days;
    private final int nbLabels;
    private final DateFormat format;

    private GrandExchangePeriods(int days, int nbLabels, String pattern) {
        this.days = days;
        this.nbLabels = nbLabels;
        this.format = new SimpleDateFormat(pattern);
    }

    public int getDays() {
        return days;
    }

    public int getNbLabels() {
        return nbLabels;
    }

    public DateFormat getFormat() {
        return format;
    }
}
