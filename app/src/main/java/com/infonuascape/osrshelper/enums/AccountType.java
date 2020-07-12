package com.infonuascape.osrshelper.enums;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum AccountType {
    REGULAR, IRONMAN, ULTIMATE_IRONMAN, HARDCORE_IRONMAN;

    public String getApiName() {
        return name().toLowerCase();
    }
}