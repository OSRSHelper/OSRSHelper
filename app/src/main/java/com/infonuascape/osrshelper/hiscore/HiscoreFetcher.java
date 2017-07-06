package com.infonuascape.osrshelper.hiscore;

import android.content.Context;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreFetcher {
	final String API_URL = "http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=";

	private String userName;

	public HiscoreFetcher(String userName) {
		this.userName = userName.replace(" ", "%20");
	}

	public String getUserName() {
		return userName;
	}

	public PlayerSkills getPlayerSkills(final Context context) throws PlayerNotFoundException {
		String APIOutput = getDataFromAPI(context);
		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = new ArrayList<Skill>();
		skillList.add(ps.overall);
		skillList.add(ps.attack);
		skillList.add(ps.defence);
		skillList.add(ps.strength);
		skillList.add(ps.hitpoints);
		skillList.add(ps.ranged);
		skillList.add(ps.prayer);
		skillList.add(ps.magic);
		skillList.add(ps.cooking);
		skillList.add(ps.woodcutting);
		skillList.add(ps.fletching);
		skillList.add(ps.fishing);
		skillList.add(ps.firemaking);
		skillList.add(ps.crafting);
		skillList.add(ps.smithing);
		skillList.add(ps.mining);
		skillList.add(ps.herblore);
		skillList.add(ps.agility);
		skillList.add(ps.thieving);
		skillList.add(ps.slayer);
		skillList.add(ps.farming);
		skillList.add(ps.runecraft);
		skillList.add(ps.hunter);
		skillList.add(ps.construction);
		String[] skillArray = APIOutput.split("\n");
		String[] dataSeparator;
		for (int i = 0; i <= skillList.size() - 1; i++) {
			dataSeparator = skillArray[i].split(",");
			skillList.get(i).setRank(Integer.parseInt(dataSeparator[0]));
			skillList.get(i).setLevel(Short.parseShort(dataSeparator[1]));
			skillList.get(i).setExperience(Long.parseLong(dataSeparator[2]));
		}
		return ps;
	}

	public PlayerSkills mapDataSet(String dataSet) {
		// split dataset, map to skills enum
		return new PlayerSkills(); // dummy return
	}

	private String getDataFromAPI(final Context context) throws PlayerNotFoundException {
		HTTPRequest httpRequest = new HTTPRequest(context, API_URL + getUserName(), Request.Method.GET);
		if (httpRequest.getStatusCode() == StatusCode.FOUND) {
			return httpRequest.getOutput();
		} else {
			throw new PlayerNotFoundException(getUserName());
		}
	}
}
