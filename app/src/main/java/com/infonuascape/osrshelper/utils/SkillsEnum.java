package com.infonuascape.osrshelper.utils;

/**
 * Created by maden on 9/9/14.
 */
public class SkillsEnum {
    public enum SkillType {
        Overall("Overall"),
        Attack("Attack"),
        Defence("Defence"),
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

        public String getAlternativeName() {
            return alternativeName;
        }

        private SkillType(String skillName) {
            this.skillName = skillName;
            this.alternativeName = skillName;
        }
        private SkillType(String skillName, String alternativeName) {
            this.skillName = skillName;
            this.alternativeName = alternativeName;
        }
    };
    Skill skillType;
    public SkillsEnum(Skill skillType) {
        this.skillType = skillType;
    }
}

