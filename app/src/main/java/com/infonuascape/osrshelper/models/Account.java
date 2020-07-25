package com.infonuascape.osrshelper.models;

import android.text.TextUtils;

import com.infonuascape.osrshelper.enums.AccountType;

import java.io.Serializable;

/**
 * Created by marc_ on 2018-01-14.
 */

public class Account implements Serializable{
    public int id;
    public String username;
    public String displayName;
    public AccountType type;
    public long lastTimeUsed;
    public boolean isProfile;
    public boolean isFollowing;
    public int combatLvl;

    public Account(final String username) {
        this.username = username;
        this.type = AccountType.REGULAR;
    }

    public Account(final int id, final String username, final String displayName, final AccountType type, final long lastTimeUsed, final boolean isProfile, final boolean isFollowing, final int combatLvl) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.type = type;
        this.lastTimeUsed = lastTimeUsed;
        this.isProfile = isProfile;
        this.isFollowing = isFollowing;
        this.combatLvl = combatLvl;
    }

    public String getDisplayName() {
        return !TextUtils.isEmpty(displayName) ? displayName : username;
    }
}
