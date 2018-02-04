package com.infonuascape.osrshelper.models.players;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-02-03.
 */

public class Delta {
    public long timestamp;
    public long timestampRecent;
    public ArrayList<SkillDiff> skillDiffs;

    public Delta() {
        skillDiffs = new ArrayList<>();
    }
}
