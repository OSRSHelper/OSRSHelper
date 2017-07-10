package com.infonuascape.osrshelper.utils;

import java.util.ArrayList;

import android.graphics.Point;
import android.net.Uri;

import com.infonuascape.osrshelper.adapters.PointOfInterest;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class Utils {

	public static final float getXPToLvl(final int nextLvl, final boolean isVirtualLevel){
		if(!isVirtualLevel && nextLvl == 100){
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
	
	public static final int getMissingAttackStrengthUntilNextCombatLvl(final PlayerSkills skills){
		double base = 0.25 * (skills.defence.getLevel() + skills.hitpoints.getLevel() + Math.floor(skills.prayer.getLevel() / 2));
		
		double melee = 0.325 * (skills.attack.getLevel() + skills.strength.getLevel());
		
		
		double range = 0.325 * (Math.floor(skills.ranged.getLevel() / 2) + skills.ranged.getLevel());
		double mage = 0.325 * (Math.floor(skills.magic.getLevel() / 2) + skills.magic.getLevel());
		
		double max = Math.max(melee, Math.max(range, mage));
		
		int combatLvl = (int) Math.floor(base + max);
		
		int needed = 0;
		
		for(double start = base + melee; start < (combatLvl + 1); start += 0.325){
			needed += 1;
		}
		
		return needed;
	}
	
	public static final int getMissingHPDefenceUntilNextCombatLvl(final PlayerSkills skills){
		double base = 0.25 * (skills.defence.getLevel() + skills.hitpoints.getLevel() + Math.floor(skills.prayer.getLevel() / 2));
		
		double melee = 0.325 * (skills.attack.getLevel() + skills.strength.getLevel());
		
		
		double range = 0.325 * (Math.floor(skills.ranged.getLevel() / 2) + skills.ranged.getLevel());
		double mage = 0.325 * (Math.floor(skills.magic.getLevel() / 2) + skills.magic.getLevel());
		
		double max = Math.max(melee, Math.max(range, mage));
		
		int combatLvl = (int) Math.floor(base + max);
		
		int needed = 0;
		
		
		for(double start = base + max; start < (combatLvl + 1); start += 0.25){
			needed += 1;
		}
		
		return needed;
	}
	
	public static final int getMissingPrayerUntilNextCombatLvl(final PlayerSkills skills){
		double base = 0.25 * (skills.defence.getLevel() + skills.hitpoints.getLevel() + Math.floor(skills.prayer.getLevel() / 2));
		
		double melee = 0.325 * (skills.attack.getLevel() + skills.strength.getLevel());
		
		
		double range = 0.325 * (Math.floor(skills.ranged.getLevel() / 2) + skills.ranged.getLevel());
		double mage = 0.325 * (Math.floor(skills.magic.getLevel() / 2) + skills.magic.getLevel());
		
		double max = Math.max(melee, Math.max(range, mage));
		
		int combatLvl = (int) Math.floor(base + max);
		
		int needed = 0;
		
		for(double start = base + max; start < (combatLvl + 1); start += 0.125){
			needed += 1;
		}
		
		if(skills.prayer.getLevel() % 2 == 0){
			needed += 1;
		}
		
		return needed;
	}
	
	public static final int getMissingRangingUntilNextCombatLvl(final PlayerSkills skills){
		double base = 0.25 * (skills.defence.getLevel() + skills.hitpoints.getLevel() + Math.floor(skills.prayer.getLevel() / 2));
		
		double melee = 0.325 * (skills.attack.getLevel() + skills.strength.getLevel());
		
		
		double range = 0.325 * (Math.floor(skills.ranged.getLevel() / 2) + skills.ranged.getLevel());
		double mage = 0.325 * (Math.floor(skills.magic.getLevel() / 2) + skills.magic.getLevel());
		
		double max = Math.max(melee, Math.max(range, mage));
		
		int combatLvl = (int) Math.floor(base + max);
		
		int needed = 0;
		double current = skills.ranged.getLevel();
		double initial = current;
		current = Math.floor(initial * 1.5) * 0.325;
		
		while((current + base) < (combatLvl + 1)){
			current = Math.floor((initial + ++needed) * 1.5d) * 0.325d;
		}
		
		return needed;
	}
	
	public static final int getMissingMagicUntilNextCombatLvl(final PlayerSkills skills){
		double base = 0.25 * (skills.defence.getLevel() + skills.hitpoints.getLevel() + Math.floor(skills.prayer.getLevel() / 2));
		
		double melee = 0.325 * (skills.attack.getLevel() + skills.strength.getLevel());
		
		
		double range = 0.325 * (Math.floor(skills.ranged.getLevel() / 2) + skills.ranged.getLevel());
		double mage = 0.325 * (Math.floor(skills.magic.getLevel() / 2) + skills.magic.getLevel());
		
		double max = Math.max(melee, Math.max(range, mage));
		
		int combatLvl = (int) Math.floor(base + max);
		
		int needed = 0;
		double start = skills.magic.getLevel();
		double initial = start;
		start = Math.floor(initial * 1.5) * 0.325;
		
		while((start + base) < (combatLvl + 1)){
			start = Math.floor((initial + ++needed) * 1.5) * 0.325;
		}
		
		return needed;
	}

	private static final int ZEAH_OFFSET_X = 2534;
	private static final int ZEAH_OFFSET_Y = -167;

	public static Point VARROCK_POINT = new Point(ZEAH_OFFSET_X + 3685, 2355 + ZEAH_OFFSET_Y);
	
	public static ArrayList<PointOfInterest> getCitiesPoI(){
		ArrayList<PointOfInterest> poi = new ArrayList<PointOfInterest>();

		poi.add(new PointOfInterest("Al Kharid", new Point(ZEAH_OFFSET_X + 3932, 3126 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Ardougne", new Point(ZEAH_OFFSET_X + 1920, 2775 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Barbarian Village", new Point(ZEAH_OFFSET_X + 3285, 2400 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Brimhaven", new Point(ZEAH_OFFSET_X + 2400, 3110 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Burgh de Rott", new Point(ZEAH_OFFSET_X + 4555, 2993 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Burthope", new Point(ZEAH_OFFSET_X + 2760, 2025 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Camelot", new Point(ZEAH_OFFSET_X + 2328, 2192 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Canifis", new Point(ZEAH_OFFSET_X + 4535, 2200 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Catherby", new Point(ZEAH_OFFSET_X + 2520, 2355 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Draynor Village", new Point(ZEAH_OFFSET_X + 3360, 2880 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Edgeville", new Point(ZEAH_OFFSET_X + 3330, 2200 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Falador", new Point(ZEAH_OFFSET_X + 3050, 2580 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Grand Tree", new Point(ZEAH_OFFSET_X + 1445, 2185 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Jatizso", new Point(ZEAH_OFFSET_X + 1270, 1250 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Lumbridge", new Point(ZEAH_OFFSET_X + 3760, 2983 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Miscellania", new Point(ZEAH_OFFSET_X + 1685, 1045 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Mortton", new Point(ZEAH_OFFSET_X + 4520, 2820 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Musa Point", new Point(ZEAH_OFFSET_X + 2770, 3185 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Nardah", new Point(ZEAH_OFFSET_X + 4340, 3940 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Neitiznot", new Point(ZEAH_OFFSET_X + 1045, 1250 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Pollnivneach", new Point(ZEAH_OFFSET_X + 4120, 3745 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Port Khazard", new Point(ZEAH_OFFSET_X + 2015, 3175 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Port Phasmatys", new Point(ZEAH_OFFSET_X + 5080, 2205 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Port Sarim", new Point(ZEAH_OFFSET_X + 3130, 2990 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Rellekka", new Point(ZEAH_OFFSET_X + 2020, 1635 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Rimmington", new Point(ZEAH_OFFSET_X + 2915, 3000 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Seers' Village", new Point(ZEAH_OFFSET_X + 2175, 2215 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Shilo Village", new Point(ZEAH_OFFSET_X + 2590, 3740 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Sophanem", new Point(ZEAH_OFFSET_X + 3945, 4325 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Tai Bwo Wannai", new Point(ZEAH_OFFSET_X + 2430, 3470 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Taverley", new Point(ZEAH_OFFSET_X + 2750, 2335 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Tirannwn", new Point(2551, 2725)));
		poi.add(new PointOfInterest("Tutorial Island", new Point(ZEAH_OFFSET_X + 3370, 3370 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Varrock", VARROCK_POINT));
		poi.add(new PointOfInterest("Waterbirth Island", new Point(ZEAH_OFFSET_X + 1645, 1440 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Yanille", new Point(ZEAH_OFFSET_X + 1780, 3395 + ZEAH_OFFSET_Y)));

		poi.add(new PointOfInterest("Zeah's Arceuus House", new Point(1583, 1215)));
		poi.add(new PointOfInterest("Zeah's Hosidius House", new Point(1750, 1825)));
		poi.add(new PointOfInterest("Zeah's Lovakengj House", new Point(1000, 1165)));
		poi.add(new PointOfInterest("Zeah's Piscarilius House", new Point(1986, 1261)));
		poi.add(new PointOfInterest("Zeah's Shayzien House", new Point(1130, 1752)));
		poi.add(new PointOfInterest("Zeah's Wintertodt", new Point(1470, 477)));
		poi.add(new PointOfInterest("Zeah's Mount Quidamortem", new Point(315, 1825)));

		return poi;
	}

}
