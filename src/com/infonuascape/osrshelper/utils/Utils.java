package com.infonuascape.osrshelper.utils;

import java.util.ArrayList;

import android.graphics.Point;

import com.infonuascape.osrshelper.adapters.PointOfInterest;
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
	
	public static ArrayList<PointOfInterest> getCitiesPoI(){
		ArrayList<PointOfInterest> poi = new ArrayList<PointOfInterest>();
		
		poi.add(new PointOfInterest("Al Kharid", new Point(3932, 3126)));
		poi.add(new PointOfInterest("Ardougne", new Point(1920, 2775)));
		poi.add(new PointOfInterest("Barbarian Village", new Point(3285, 2400)));
		poi.add(new PointOfInterest("Brimhaven", new Point(2400, 3110)));
		poi.add(new PointOfInterest("Burgh de Rott", new Point(4555, 2993)));
		poi.add(new PointOfInterest("Burthope", new Point(2760, 2025)));
		poi.add(new PointOfInterest("Camelot", new Point(2365, 2080)));
		poi.add(new PointOfInterest("Canifis", new Point(4535, 2200)));
		poi.add(new PointOfInterest("Catherby", new Point(2520, 2355)));
		poi.add(new PointOfInterest("Draynor Village", new Point(3360, 2880)));
		poi.add(new PointOfInterest("Edgeville", new Point(3330, 2200)));
		poi.add(new PointOfInterest("Falador", new Point(3050, 2580)));
		poi.add(new PointOfInterest("Grand Tree", new Point(1445, 2185)));
		poi.add(new PointOfInterest("Jatizso", new Point(1270, 1250)));
		poi.add(new PointOfInterest("Lumbridge", new Point(3760, 2983)));
		poi.add(new PointOfInterest("Miscellania", new Point(1685, 1045)));
		poi.add(new PointOfInterest("Mortton", new Point(4520, 2820)));
		poi.add(new PointOfInterest("Musa Point", new Point(2770, 3185)));
		poi.add(new PointOfInterest("Nardah", new Point(4340, 3940)));
		poi.add(new PointOfInterest("Neitiznot", new Point(1045, 1250)));
		poi.add(new PointOfInterest("Pollnivneach", new Point(4120, 3745)));
		poi.add(new PointOfInterest("Port Khazard", new Point(2015, 3175)));
		poi.add(new PointOfInterest("Port Phasmatys", new Point(5080, 2205)));
		poi.add(new PointOfInterest("Port Sarim", new Point(3130, 2990)));
		poi.add(new PointOfInterest("Rellekka", new Point(2020, 1635)));
		poi.add(new PointOfInterest("Rimmington", new Point(2915, 3000)));
		poi.add(new PointOfInterest("Seers' Village", new Point(2175, 2215)));
		poi.add(new PointOfInterest("Shilo Village", new Point(2590, 3740)));
		poi.add(new PointOfInterest("Sophanem", new Point(3945, 4325)));
		poi.add(new PointOfInterest("Tai Bwo Wannai", new Point(2430, 3470)));
		poi.add(new PointOfInterest("Taverley", new Point(2750, 2335)));
		poi.add(new PointOfInterest("Tutorial Island", new Point(3370, 3370)));
		poi.add(new PointOfInterest("Varrock", new Point(3685, 2355)));
		poi.add(new PointOfInterest("Waterbirth Island", new Point(1645, 1440)));
		poi.add(new PointOfInterest("Yanille", new Point(1780, 3395)));
		
		return poi;
	}

}
