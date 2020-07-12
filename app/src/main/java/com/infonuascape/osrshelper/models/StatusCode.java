package com.infonuascape.osrshelper.models;

import android.util.SparseArray;

public enum StatusCode {
    FOUND(200), NOT_FOUND(404), ERROR(500), REQUEST_NOT_SENT(-1);
    public int value;

    StatusCode(int value) {
        this.value = value;
    }

    private static final SparseArray<StatusCode> lookup = new SparseArray<>();

    static {
        for (StatusCode sc : StatusCode.values()) {
            lookup.put(sc.value, sc);
        }
    }

    public static StatusCode get(int value) { // reverse lookup
        return (StatusCode) lookup.get(value);
    }
}
