package com.infonuascape.osrshelper.utils;

import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class Utils {

	public static final float getXPToLvl(final int nextLvl){
		if(nextLvl == 100){
			return 0f;
		}

		float exp = 0f;
		for(float i = 1 ; i <= nextLvl - 1 ; i++) {
			exp += Math.floor(i + 300f * Math.pow(2f, i / 7f));
		}
		exp = (float) Math.floor(exp / 4f);
		
		return exp;
	}
	
	public static final int getCombatLvl(final PlayerSkills skills){
		double base = 0.25 * (skills.defence.getLevel() + skills.hitpoints.getLevel() + Math.floor(skills.prayer.getLevel() / 2));
		
		double melee = 0.325 * (skills.attack.getLevel() + skills.strength.getLevel());
		double range = 0.325 * (Math.floor(skills.ranged.getLevel() / 2) + skills.ranged.getLevel());
		double mage = 0.325 * (Math.floor(skills.magic.getLevel() / 2) + skills.magic.getLevel());
		
		int combatLvl = (int) Math.floor(base + Math.max(melee, Math.max(range, mage)));
		
		return combatLvl;
	}

}
