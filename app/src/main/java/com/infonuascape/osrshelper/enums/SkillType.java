package com.infonuascape.osrshelper.enums;

import com.infonuascape.osrshelper.R;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum SkillType {
    Overall(R.drawable.overall, "Overall"),
    Attack(R.drawable.attack, "Attack"),
    Defence(R.drawable.defence, "Defence"),
    Strength(R.drawable.strength, "Strength"),
    Hitpoints(R.drawable.constitution, "Hitpoints"),
    Ranged(R.drawable.ranged, "Ranged"),
    Prayer(R.drawable.prayer, "Prayer"),
    Magic(R.drawable.magic, "Magic"),
    Cooking(R.drawable.cooking, "Cooking"),
    Woodcutting(R.drawable.woodcutting, "Woodcutting"),
    Fletching(R.drawable.fletching, "Fletching"),
    Fishing(R.drawable.fishing, "Fishing"),
    Firemaking(R.drawable.firemaking, "Firemaking"),
    Crafting(R.drawable.crafting, "Crafting"),
    Smithing(R.drawable.smithing, "Smithing"),
    Mining(R.drawable.mining, "Mining"),
    Herblore(R.drawable.herblore, "Herblore"),
    Agility(R.drawable.agility, "Agility"),
    Thieving(R.drawable.thieving, "Thieving"),
    Slayer(R.drawable.slayer, "Slayer"),
    Farming(R.drawable.farming, "Farming"),
    Runecrafting(R.drawable.runecrafting, "Runecrafting"),
    Hunter(R.drawable.hunter, "Hunter"),
    Construction(R.drawable.construction, "Construction"),
    Vorkath(R.drawable.vorkath, "Vorkath", Boolean.TRUE),
    CoX(R.drawable.chambers_of_xeric, "Chambers of Xeric", Boolean.TRUE),
    ToB(R.drawable.theatre_of_blood, "Theatre of Blood", Boolean.TRUE);


    private final int drawableId;
    private final String skillName;
    private final Boolean isBoss;


    public String getSkillName() {
        return skillName;
    }

    public boolean equals(String skillName) {
        return (getBoss().equals(skillName)) || (getSkillName().equals(skillName));
    }

    public Boolean getBoss() {
        return isBoss;
    }

    public int getDrawableInt() {
        return drawableId;
    }

    SkillType(int drawableId, String skillName) {
        this.drawableId = drawableId;
        this.skillName = skillName;
        this.isBoss = Boolean.FALSE;
    }
    SkillType(int drawableId, String skillName, Boolean isBoss) {
        this.drawableId = drawableId;
        this.skillName = skillName;
        this.isBoss = isBoss;
    }
};
