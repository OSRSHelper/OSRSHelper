package com.infonuascape.osrshelper.network;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maden on 9/14/14.
 */
public class TrackerApi {
	private static final String API_URL = NetworkStack.ENDPOINT + "/track/lookup/%1$s/%2$s";
	private static final String KEY_EHP = "ehp";
	private static final String KEY_EHP_CAP = "EHP";
	private static final String KEY_SKILLS = "skills";
	private static final String KEY_LAST_UPDATED = "lastupdated";
	private static final String KEY_EXPERIENCE_DIFF = "ExperienceDiff";
	private static final String KEY_RANK_DIFF = "RankDiff";
	private static final String KEY_EXPERIENCE = "Experience";

	public static PlayerSkills fetch(String userName, TrackerTime trackerTime) throws JSONException, APIError, PlayerNotFoundException {
		return fetch(userName, trackerTime.getSeconds());
	}

    public static PlayerSkills fetch(String username, int lookupTime) throws APIError, PlayerNotFoundException, JSONException {
		final String url = String.format(API_URL, username, lookupTime);
		HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

		if (httpResult.statusCode == StatusCode.NOT_FOUND) {
			throw new PlayerNotFoundException(username);
		} else if(httpResult.statusCode != StatusCode.FOUND) {
			throw new APIError("Unexpected response from the server.");
		}

		JSONObject ehp = httpResult.jsonObject.getJSONObject(KEY_EHP);

		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = ps.skillList;
		long seconds = ehp.getLong(KEY_LAST_UPDATED);
		long millis = seconds * 1000;
		Date date = new Date(System.currentTimeMillis() - millis);
		ps.lastUpdate = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(date);

		JSONObject skills = ehp.getJSONObject(KEY_SKILLS);
		for (Iterator<String> it = skills.keys(); it.hasNext(); ) {
			String skill = it.next();
			JSONObject skillJson = skills.getJSONObject(skill);

			for (Skill s : skillList) {
				if (s.getSkillType().equals(skill)) {
					s.setExperienceDiff(skillJson.getLong(KEY_EXPERIENCE_DIFF));
					s.setRankDiff(skillJson.getLong(KEY_RANK_DIFF));
					s.setExperience(skillJson.getLong(KEY_EXPERIENCE));
					s.setEHP(skillJson.getDouble(KEY_EHP_CAP));
				}
			}
		}

		short totalLevel = 0;
		short totalVirtualLevel = 0;
		for (Skill s : skillList) {
			if (s.getSkillType() != SkillType.Overall) {
				totalLevel += s.getLevel();
				totalVirtualLevel += s.getVirtualLevel();
			}
		}

		for (Skill s : skillList) {
			if (s.getSkillType().equals(SkillType.Overall)) {
				s.setLevel(totalLevel);
				s.setVirtualLevel(totalVirtualLevel);
			}
		}

		return ps;
	}
}
