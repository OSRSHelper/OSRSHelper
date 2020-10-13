package com.infonuascape.osrshelper.db;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesController {
    private static final String PREF_NAME = "preferencescontroller";

    public static final String USER_PREF_SHOW_VIRTUAL_LEVELS = "USER_PREF_SHOW_VIRTUAL_LEVELS";
    public static final String USER_PREF_IS_SUBSCRIBED_TO_NEWS = "USER_PREF_IS_SUBSCRIBED_TO_NEWS";
    public static final String USER_PREF_HOVER_MENU_ENABLED = "USER_PREF_HOVER_MENU_ENABLED";
    public static final String USER_PREF_HOVER_MENU_SHOWN = "USER_PREF_HOVER_MENU_SHOWN";
    // ADD CONSTANT KEYS HERE

    public static void setPreference(final Context context, final String preference, final String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(preference, value);
        editor.commit();
    }

    public static void setPreference(final Context context, final String preference, final int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(preference, value);
        editor.commit();
    }

    public static void setPreference(final Context context, final String preference, final boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(preference, value);
        editor.commit();
    }

    public static void setPreference(final Context context, final String preference, final long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(preference, value);
        editor.commit();
    }

    public static void setPreference(final Context context, final String preference, final float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(preference, value);
        editor.commit();
    }

    public static String getStringPreference(final Context context, final String preference) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String restoredText = prefs.getString(preference, null);
        return restoredText;
    }

    public static int getIntPreference(final Context context, final String preference, final int defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int restoredText = prefs.getInt(preference, defaultValue);
        return restoredText;
    }

    public static boolean getBooleanPreference(final Context context, final String preference, final boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean restoredText = prefs.getBoolean(preference, defaultValue);
        return restoredText;
    }

    public static float getFloatPreference(final Context context, final String preference, final float defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        float restoredText = prefs.getFloat(preference, defaultValue);
        return restoredText;
    }

    public static long getLongPreference(final Context context, final String preference, final long defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long restoredText = prefs.getLong(preference, defaultValue);
        return restoredText;
    }
}