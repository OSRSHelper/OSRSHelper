package com.infonuascape.osrshelper.utils;

import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

public class Skill {
	private SkillsEnum.SkillType skillType;
	private int experience = -1;
	private int experienceDiff = -1;
	private short level = 0;
    private short virtualLevel = 0;
	private int rank = -1;
	private int rankDiff = -1;
	private int drawableId;
	private double EHP = 0.0;

	public Skill(SkillType skillType, int drawableId) {
		this.skillType = skillType;
		this.drawableId = drawableId;
	}

	public Skill(SkillType skillType, int experience, short level) {
		this.skillType = skillType;
		this.experience = experience;
		this.level = level;
	}

	public Skill(SkillType skillType, int experience, short level, int rank) {
		this.skillType = skillType;
		this.experience = experience;
		this.level = level;
		this.rank = rank;
	}

	public Skill(SkillType skillType, int experience, int experienceDiff, short level, int rank,
				 int rankDiff, int EHP) {
		this.skillType = skillType;
		this.experience = experience;
		this.experienceDiff = experienceDiff;
		this.level = level;
		this.rank = rank;
		this.rankDiff = rankDiff;
		this.EHP = EHP;
	}


    public int getExperienceDiff() {
        return experienceDiff;
    }

    public void setExperienceDiff(int experienceDiff) {
        this.experienceDiff = experienceDiff;
    }

    public int getRankDiff() {
        return rankDiff;
    }

    public void setRankDiff(int rankDiff) {
        this.rankDiff = rankDiff;
    }

    public double getEHP() {
        return EHP;
    }

    public void setEHP(double EHP) {
        this.EHP = EHP;
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

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

    public short getVirtualLevel() {
        return virtualLevel;
    }

    public void setVirtualLevel(short virtualLevel) {
        this.virtualLevel = virtualLevel;
    }

    public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getDrawableInt() {
		return drawableId;
	}

	@Override
	public String toString() {
		return skillType.toString();
	}
}
