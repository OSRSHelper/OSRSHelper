package com.infonuascape.osrshelper.models;


import com.infonuascape.osrshelper.enums.SkillType;

public class Skill {
	private SkillType skillType;
	private long experience = 0;
	private int experienceDiff = 0;
	private short level = 0;
    private short virtualLevel = 0;
	private int rank = 0;
	private int rankDiff = 0;
	private int drawableId;
	private double EHP = 0.0;

	public Skill(SkillType skillType, int drawableId) {
		this.skillType = skillType;
		this.drawableId = drawableId;
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

	public Skill(SkillType skillType, long experience, int experienceDiff, short level, int rank,
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

	public SkillType getSkillType() {
		return skillType;
	}

	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}

	public long getExperience() {
		return experience;
	}

	public void setExperience(Long experience) {
		this.experience = experience;
		this.calculateLevel();
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

	public void calculateLevel() {
		short level=0;
		Long curr_xp = 0L;
		double points = 0;
		double dividend = 1;

		while (curr_xp <= experience) {

			Double placeholder = dividend/7;
			points = points + Math.floor(dividend + 300* Math.pow(2, placeholder));
			curr_xp = (long)Math.floor(points / 4.0);
			dividend++;
			level++;
		}

		this.level = (short) Math.min(level, 99);
		this.virtualLevel = level;
	}
}
