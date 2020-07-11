package com.infonuascape.osrshelper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.PointOfInterest;
import com.infonuascape.osrshelper.adapters.PointOfInterestHeader;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.TrendRate;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

import java.util.ArrayList;

public class Utils {

	public static final float getExpFromLevel(final int nextLvl, final boolean isVirtualLevel){
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
		if(skills == null) {
			return 0;
		}

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

	private static final int FOSSIL_ISLAND_OFFSET_Y = -190;
	private static final int ZEAH_OFFSET_X = 2534;
	private static final int ZEAH_OFFSET_Y = -167 + FOSSIL_ISLAND_OFFSET_Y;

	public static Point VARROCK_POINT = new Point(ZEAH_OFFSET_X + 3685, 2355 + ZEAH_OFFSET_Y);
	
	public static ArrayList<PointOfInterest> getCitiesPoI(){
		ArrayList<PointOfInterest> poi = new ArrayList<PointOfInterest>();

		poi.add(new PointOfInterestHeader("Cities", null));
		poi.add(new PointOfInterest("Al Kharid", new Point(ZEAH_OFFSET_X + 3932, 3126 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Ardougne", new Point(ZEAH_OFFSET_X + 1920, 2775 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Barbarian Village", new Point(ZEAH_OFFSET_X + 3285, 2400 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Brimhaven", new Point(ZEAH_OFFSET_X + 2400, 3110 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Burgh de Rott", new Point(ZEAH_OFFSET_X + 4555, 2993 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Burthope", new Point(ZEAH_OFFSET_X + 2760, 2025 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Camelot", new Point(ZEAH_OFFSET_X + 2328, 2192 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Canifis", new Point(ZEAH_OFFSET_X + 4535, 2200 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Catherby", new Point(ZEAH_OFFSET_X + 2520, 2355 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Darkmeyer", new Point(7450, 2230)));
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
		poi.add(new PointOfInterest("Slepe", new Point(7750, 2330)));
		poi.add(new PointOfInterest("Sophanem", new Point(ZEAH_OFFSET_X + 3945, 4325 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Tai Bwo Wannai", new Point(ZEAH_OFFSET_X + 2430, 3470 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Taverley", new Point(ZEAH_OFFSET_X + 2750, 2335 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Tirannwn", new Point(2551, 2725 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Tutorial Island", new Point(ZEAH_OFFSET_X + 3370, 3370 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Varrock", VARROCK_POINT));
		poi.add(new PointOfInterest("Waterbirth Island", new Point(ZEAH_OFFSET_X + 1645, 1440 + ZEAH_OFFSET_Y)));
		poi.add(new PointOfInterest("Weiss", new Point(5190, 500)));
		poi.add(new PointOfInterest("Yanille", new Point(ZEAH_OFFSET_X + 1780, 3395 + ZEAH_OFFSET_Y)));

		poi.add(new PointOfInterestHeader("Zeah", null));
		poi.add(new PointOfInterest("Arceuus House", new Point(1583, 1215 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Hosidius House", new Point(1750, 1825 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Lovakengj House", new Point(1000, 1165 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Piscarilius House", new Point(1986, 1261 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Shayzien House", new Point(1130, 1752 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Wintertodt", new Point(1470, 477 + FOSSIL_ISLAND_OFFSET_Y)));
		poi.add(new PointOfInterest("Mount Karuulm", new Point(500, 895)));
		poi.add(new PointOfInterest("Mount Quidamortem", new Point(315, 1825 + FOSSIL_ISLAND_OFFSET_Y)));

		poi.add(new PointOfInterestHeader("Fossil Island", null));
		poi.add(new PointOfInterest("Lithkren", new Point(7266, 303)));
		poi.add(new PointOfInterest("Fossil Island", new Point(7751, 892)));

		poi.add(new PointOfInterestHeader("Guilds", null));
		poi.add(new PointOfInterest("Cook (lvl 32)", new Point(6000, 1970)));
		poi.add(new PointOfInterest("Crafting (lvl 40)", new Point(5360, 2470)));
		poi.add(new PointOfInterest("Mining (lvl 60)", new Point(5630, 2300)));
		poi.add(new PointOfInterest("Prayer (lvl 31)", new Point(5725, 1845)));
		poi.add(new PointOfInterest("Farming (lvl 45)", new Point(316, 1100)));
		poi.add(new PointOfInterest("Fishing (lvl 68)", new Point(4365, 2100)));
		poi.add(new PointOfInterest("Woodcutting (lvl 60)", new Point(1400, 1835)));
		poi.add(new PointOfInterest("Wizard (lvl 66)", new Point(4340, 3050)));
		poi.add(new PointOfInterest("Ranging (lvl 40)", new Point(4575, 2030)));
		poi.add(new PointOfInterest("Champion", new Point(6140, 2240)));
		poi.add(new PointOfInterest("Heroes", new Point(5250, 1785)));
		poi.add(new PointOfInterest("Legends", new Point(4755, 2200)));
		poi.add(new PointOfInterest("Myths", new Point(3940, 3775)));
		poi.add(new PointOfInterest("Warriors (lvl 130 Att+Str)", new Point(5135, 1674)));

		return poi;
	}

	public static float convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}

	public static boolean isShowVirtualLevels(final Context context) {
		return PreferencesController.getBooleanPreference(context, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false);
	}

	public static int getAccountTypeResource(final AccountType type) {
		switch(type) {
			case IRONMAN:
				return R.drawable.ironman;
			case ULTIMATE_IRONMAN:
				return R.drawable.ult_ironman;
			case HARDCORE_IRONMAN:
				return R.drawable.hc_ironman;
		}

		return R.drawable.ic_launcher;
	}

	public static int getAccountTypeString(AccountType type) {
		switch(type) {
			case IRONMAN:
				return R.string.account_type_ironman;
			case ULTIMATE_IRONMAN:
				return R.string.account_type_ult_ironman;
			case HARDCORE_IRONMAN:
				return R.string.account_type_hc_ironman;
		}

		return R.string.account_type_regular;
	}

	public static TrendRate getTrendRateEnum(String trend) {
		if (trend.equals("positive")) return TrendRate.POSITIVE;
		if (trend.equals("negative")) return TrendRate.NEGATIVE;
		if (trend.equals("neutral")) return TrendRate.NEUTRAL;
		return TrendRate.POSITIVE;
	}

	public static void showKeyboard(final Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
	}

	public static void hideKeyboard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}

	public static void subscribeToNews(final Context context, boolean isSubscribed) {
		try {
			FirebaseApp.initializeApp(context.getApplicationContext());
			PreferencesController.setPreference(context, PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, isSubscribed);
			if (!isSubscribed) {
				FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
			} else {
				FirebaseMessaging.getInstance().subscribeToTopic("news");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
