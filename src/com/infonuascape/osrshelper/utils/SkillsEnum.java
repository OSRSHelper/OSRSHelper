package com.infonuascape.osrshelper.utils;

/**
 * Created by maden on 9/9/14.
 */
public class SkillsEnum {
    public enum SkillType { Overall("Overall"),
        Attack("Attack"),
        Defence("Defence"),
        Strength("Strength"),
        Hitpoints("Hitpoints"),
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
        Runecraft("Runecrafting"),
        Hunter("Hunter"),
        Construction("Construction");

        private final String skillName;
        public String toString() {
            return skillName;
        }
        private SkillType(String skillName) {
            this.skillName = skillName;
        }
    };
    Skill skillType;
    public SkillsEnum(Skill skillType) {
        this.skillType = skillType;
    }
}

