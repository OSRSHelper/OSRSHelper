package com.infonuascape.osrshelper.utils;

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

}
