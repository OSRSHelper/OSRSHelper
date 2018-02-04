package com.infonuascape.osrshelper.models;

import com.infonuascape.osrshelper.enums.AccountType;

import java.io.Serializable;

/**
 * Created by marc_ on 2018-01-14.
 */

public class Account implements Serializable{
    public int id;
    public String username;
    public AccountType type;
    public long lastTimeUsed;
    public boolean isProfile;
    public boolean isFollowing;
    public int combatLvl;

    public Account(final String username, final AccountType type) {
        this.username = username;
        this.type = type;
    }

    public Account(final int id, final String username, final AccountType type, final long lastTimeUsed, final boolean isProfile, final boolean isFollowing, final int combatLvl) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.lastTimeUsed = lastTimeUsed;
        this.isProfile = isProfile;
        this.isFollowing = isFollowing;
        this.combatLvl = combatLvl;
    }
}
