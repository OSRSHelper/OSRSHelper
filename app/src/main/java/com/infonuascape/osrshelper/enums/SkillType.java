package com.infonuascape.osrshelper.enums;

import com.infonuascape.osrshelper.R;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum SkillType {
    Overall(R.drawable.overall, "Overall"),
    Attack(R.drawable.attack, "Attack"),
    Defence(R.drawable.defence, "Defence", "Defense"),
    Strength(R.drawable.strength, "Strength"),
    Hitpoints(R.drawable.constitution, "Hitpoints", "Constitution"),
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
    Construction(R.drawable.construction, "Construction");

    private final int drawableId;
    private final String skillName;
    private final String alternativeName;

    public String getSkillName() {
        return skillName;
    }

    public boolean equals(String skillName) {
        return (getAlternativeName().toLowerCase().equals(skillName.toLowerCase())) || (getSkillName().toLowerCase().equals(skillName.toLowerCase()));
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public int getDrawableInt() {
        return drawableId;
    }

    SkillType(int drawableId, String skillName) {
        this.drawableId = drawableId;
        this.skillName = skillName;
        this.alternativeName = skillName;
    }
    SkillType(int drawableId, String skillName, String alternativeName) {
        this.drawableId = drawableId;
        this.skillName = skillName;
        this.alternativeName = alternativeName;
    }
};
