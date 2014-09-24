package com.infonuascape.osrshelper.utils;

/**
 * Created by maden on 9/9/14.
 */
public class SkillsEnum {
    public enum SkillType { Overall,
                        Attack,
                        Defence,
                        Strength,
                        Hitpoints,
                        Ranged,
                        Prayer,
                        Magic,
                        Cooking,
                        Woodcutting,
                        Fletching,
                        Fishing,
                        Firemaking,
                        Crafting,
                        Smithing,
                        Mining,
                        Herblore,
                        Agility,
                        Thieving,
                        Slayer,
                        Farming,
                        Runecraft,
                        Hunter,
                        Construction };
    Skill skillType;
    public SkillsEnum(Skill skillType) {
        this.skillType = skillType;
    }
}

