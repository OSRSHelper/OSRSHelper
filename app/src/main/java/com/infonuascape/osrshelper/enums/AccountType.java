package com.infonuascape.osrshelper.enums;

import android.text.TextUtils;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum AccountType {
    REGULAR("regular", "Regular"), IRONMAN("ironman", "Ironman"), ULTIMATE_IRONMAN("ultimate", "Ultimate Ironman"), HARDCORE_IRONMAN("hardcore", "Hardcore Ironman");

    public String apiName;
    public String displayName;

    AccountType(String apiName, String displayName) {
        this.apiName = apiName;
        this.displayName = displayName;
    }

    public static AccountType create(String apiName) {
        if (TextUtils.equals("ultimate", apiName)) {
            return ULTIMATE_IRONMAN;
        } else if (TextUtils.equals("hardcore", apiName)) {
            return HARDCORE_IRONMAN;
        } else if (TextUtils.equals("ironman", apiName)) {
            return IRONMAN;
        }

        return REGULAR;
    }
}