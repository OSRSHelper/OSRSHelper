package com.infonuascape.osrshelper.models;

import com.infonuascape.osrshelper.enums.AccountType;

import java.io.Serializable;

/**
 * Created by marc_ on 2018-01-14.
 */

public class Account implements Serializable{
    public String username;
    public AccountType type;

    public Account(final String username, final AccountType type) {
        this.username = username;
        this.type = type;
    }
}
