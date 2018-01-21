package com.infonuascape.osrshelper.enums;

/**
 * Created by marc_ on 2018-01-14.
 */

public enum SkillType {
    Overall("Overall"),
    Attack("Attack"),
    Defence("Defence", "Defense"),
    Strength("Strength"),
    Hitpoints("Hitpoints", "Constitution"),
    Ranged("Ranged"),
    Prayer("Prayer"),
    Magic("Magic"),
    Cooking("Cooking"),
    Woodcutting("Woodcutting"),
    Fletching("Fletching"),
    Fishing("Fishing"),
    Firemaking("Firemaking"),
    Crafting("Crafting"),
    Smithing("Smithing"),
    Mining("Mining"),
    Herblore("Herblore"),
    Agility("Agility"),
    Thieving("Thieving"),
    Slayer("Slayer"),
    Farming("Farming"),
    Runecraft("Runecrafting", "Runecraft"),
    Hunter("Hunter"),
    Construction("Construction");

    private final String skillName;
    private final String alternativeName;

    public String getSkillName() {
        return skillName;
    }

    public boolean equals(String skillName) {
        return (getAlternativeName().equals(skillName)) || (getSkillName().equals(skillName));
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    SkillType(String skillName) {
        this.skillName = skillName;
        this.alternativeName = skillName;
    }
    SkillType(String skillName, String alternativeName) {
        this.skillName = skillName;
        this.alternativeName = alternativeName;
    }
};
