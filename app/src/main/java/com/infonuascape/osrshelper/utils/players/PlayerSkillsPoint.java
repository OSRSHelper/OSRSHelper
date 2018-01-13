package com.infonuascape.osrshelper.utils.players;

/**
 * Created by maden on 9/14/14.
 */
public class PlayerSkillsPoint extends PlayerSkills {
    private int epoch = 0;

    public PlayerSkillsPoint() {
        super();
    }

    public int getEpoch() {
        return epoch;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }
}
