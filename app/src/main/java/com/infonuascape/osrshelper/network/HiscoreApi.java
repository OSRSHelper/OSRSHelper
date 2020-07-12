package com.infonuascape.osrshelper.network;

import android.net.Uri;

import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreApi {
	private static final String API_URL = NetworkStack.ENDPOINT + "/hiscore/%1$s/%2$s";

	private static final String KEY_SKILLS = "skills";
	private static final String KEY_EXPERIENCE = "Experience";
	private static final String KEY_RANK = "Rank";
	private static final String KEY_LEVEL = "Level";

	public static String getQueryUrl(String username, AccountType accountType) {
		return String.format(API_URL, accountType.getApiName(), Uri.encode(username));
	}

    public static PlayerSkills fetch(String username, AccountType accountType) throws PlayerNotFoundException, APIError {
		String url = getQueryUrl(username, accountType);
		HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url, true);

		if (httpResult.statusCode == StatusCode.NOT_FOUND) {
			throw new PlayerNotFoundException(username);
		} else if(httpResult.statusCode != StatusCode.FOUND) {
			throw new APIError("Unexpected response from the server.");
		}

		return parseResponse(httpResult.output);
	}

	public static PlayerSkills parseResponse(String output) {
		try {
			JSONObject jsonObject = new JSONObject(output);
			PlayerSkills ps = new PlayerSkills();
			List<Skill> skillList = ps.skillList;

			JSONObject jsonSkills = jsonObject.getJSONObject(KEY_SKILLS);
			for (Iterator<String> it = jsonSkills.keys(); it.hasNext(); ) {
				String skill = it.next();
				JSONObject skillJson = jsonSkills.getJSONObject(skill);

				for (Skill s : skillList) {
					if (s.getSkillType().equals(skill)) {
						s.setExperience(skillJson.getLong(KEY_EXPERIENCE));
						s.setRank(Integer.parseInt(skillJson.getString(KEY_RANK)));
						s.setLevel(Short.parseShort(skillJson.getString(KEY_LEVEL)));
					}
					if (!s.getSkillType().equals(SkillType.Overall)) {
						s.calculateLevel();
					}
				}
			}

			//compute total level
			short totalLevel = 0;
			short totalVirtualLevel = 0;
			for (Skill s : skillList) {
				if (s.getSkillType() != SkillType.Overall) {
					totalLevel += s.getLevel();
					totalVirtualLevel += s.getVirtualLevel();
				}
			}

			//add total level to appropriate Skill entry
			Skill overallSkill = skillList.get(0); //always first indexed
			overallSkill.setLevel(totalLevel);
			overallSkill.setVirtualLevel(totalVirtualLevel);

			return ps;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
