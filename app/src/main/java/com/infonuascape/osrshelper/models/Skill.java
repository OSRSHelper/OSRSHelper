package com.infonuascape.osrshelper.models;


import com.infonuascape.osrshelper.enums.SkillType;

public class Skill {
	private SkillType skillType;

    public int getJagexIndex() {
        return JagexIndex;
    }

    int JagexIndex;
	private long experience = 0;
	private long experienceDiff = 0;
	private short level = 0;
    private short virtualLevel = 0;
	private long rank = 0;
	private long rankDiff = 0;
	private double EHP = 0.0;

	public Skill(SkillType skillType) {
		this.skillType = skillType;
	}

	public Skill(SkillType skillType, int Jindex) {
		this.skillType = skillType;
		this.JagexIndex = Jindex;
	}

	public Skill(SkillType skillType, long experience, short level) {
		this.skillType = skillType;
		this.experience = experience;
		this.level = level;
	}

	public Skill(SkillType skillType, long experience, short level, long rank) {
		this.skillType = skillType;
		this.experience = experience;
		this.level = level;
		this.rank = rank;
	}

	public Skill(SkillType skillType, long experience, long experienceDiff, short level, long rank,
				 long rankDiff, double EHP) {
		this.skillType = skillType;
		this.experience = experience;
		this.experienceDiff = experienceDiff;
		this.level = level;
		this.rank = rank;
		this.rankDiff = rankDiff;
		this.EHP = EHP;
	}


    public long getExperienceDiff() {
        return experienceDiff;
    }

    public void setExperienceDiff(long experienceDiff) {
        this.experienceDiff = experienceDiff;
    }

    public long getRankDiff() {
        return rankDiff;
    }

    public void setRankDiff(long rankDiff) {
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

	public void setExperience(long experience) {
		this.experience = experience;
		this.calculateLevel();
	}

    public short getVirtualLevel() {
        return virtualLevel;
    }

    public void setVirtualLevel(short virtualLevel) {
        this.virtualLevel = virtualLevel;
    }

    public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
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
