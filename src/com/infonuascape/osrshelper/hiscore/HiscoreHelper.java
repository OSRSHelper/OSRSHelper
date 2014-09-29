package com.infonuascape.osrshelper.hiscore;

import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class HiscoreHelper {
    String userName = null;

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return this.userName;
    }
    public PlayerSkills getPlayerStats() throws PlayerNotFoundException {
        HiscoreFetcher hf = new HiscoreFetcher(this.getUserName());
        return hf.getPlayerSkills();
    }
}
