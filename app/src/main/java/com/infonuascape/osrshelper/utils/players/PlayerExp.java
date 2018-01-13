package com.infonuascape.osrshelper.utils.players;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

import java.util.ArrayList;
import java.util.List;

public class PlayerExp {
	public String playerName;
	public long experience;

	public PlayerExp(String playerName, long experience) {
		this.playerName = playerName;
		this.experience = experience;
	}
}
