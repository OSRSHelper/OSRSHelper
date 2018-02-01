package com.infonuascape.osrshelper.views;

import android.content.Context;

import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.DateFormat;

/**
 * Created by marc_ on 2018-01-31.
 */

public class GrandExchangeAxisFormatter extends DateAsXAxisLabelFormatter {
    public GrandExchangeAxisFormatter(Context context) {
        super(context);
    }

    public GrandExchangeAxisFormatter(Context context, DateFormat dateFormat) {
        super(context, dateFormat);
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        if(isValueX) {
            return super.formatLabel(value, isValueX);
        } else {
            if(value > 1000000) {
                return String.format("%.1fM  ", value/1000000);
            } else if(value > 1000) {
                return String.format("%.1fK  ", value/1000);
            }

            return ((int) value) + "gp  ";
        }
    }
}
