package com.infonuascape.osrshelper.utils;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

public class Skill {
    private SkillsEnum.SkillType skillType;
    private long experience;
    private short level;
    private int rank;

    public Skill(SkillType skillType) {
        this.skillType = skillType;
        this.experience = experience;
    }

    public Skill(SkillType skillType, long experience) {
        this.skillType = skillType;
        this.experience = experience;
    }

    public Skill(SkillType skillType, long experience, short level) {
        this.skillType = skillType;
        this.experience = experience;
        this.level = level;
    }
    public Skill(SkillType skillType, long experience, short level, int rank) {
        this.skillType = skillType;
        this.experience = experience;
        this.level = level;
        this.rank = rank;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public SkillsEnum.SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillsEnum.SkillType skillType) {
        this.skillType = skillType;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String toString() {
        return this.skillType.toString();
    }
}
