package com.infonuascape.osrshelper.hiscore;

import android.content.Context;

import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class HiscoreHelper {
    private Context context;
    private String userName;
    private AccountType accountType;

    public HiscoreHelper(final Context context) {
        this.context = context;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public PlayerSkills getPlayerStats() throws PlayerNotFoundException {
        HiscoreFetcher hf = new HiscoreFetcher(context, userName, accountType);
        return hf.getPlayerSkills();
    }

    public enum AccountType {
        REGULAR, IRONMAN, ULTIMATE_IRONMAN, HARDCORE_IRONMAN;
    }
}
