package com.infonuascape.osrshelper.hiscore;

import android.content.Context;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.API;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreFetcher {
	private Context context;
	private String userName;
    private AccountType accountType;

	public HiscoreFetcher(final Context context, String userName, AccountType accountType) {
		this.context = context;
		this.userName = userName.replace(" ", "%20");
		this.accountType = accountType;
	}

	public String getUserName() {
		return userName;
	}

    public AccountType getAccountType() {
        return accountType;
    }

    public PlayerSkills getPlayerSkills() throws PlayerNotFoundException, JSONException, APIError {
		JSONObject APIOutput = getDataFromAPI();
		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = ps.skillList;

		for (Iterator<String> it = APIOutput.keys(); it.hasNext(); ) {
			String skill = it.next();
			JSONObject skillJson = APIOutput.getJSONObject(skill);

			for (Skill s: skillList) {
				if (s.getSkillType().equals(skill)) {
					s.setExperience(skillJson.getLong("Experience"));
					s.setRank(Integer.parseInt(skillJson.getString("Rank")));
					s.setLevel(Short.parseShort(skillJson.getString("Level")));
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

        ps.calculateIfVirtualLevelsNecessary();

		return ps;
	}

	public PlayerSkills mapDataSet(String dataSet) {
		// split dataset, map to skills enum
		return new PlayerSkills(); // dummy return
	}

	private String getAPIEndpoint() {
		return String.format("/hiscore/%1$s/%2$s", getAccountType().name().toLowerCase(), getUserName());
    }

	private JSONObject getDataFromAPI() throws PlayerNotFoundException, JSONException, APIError {
		API api = new API(context, getAPIEndpoint());

		if (api.getStatusCode() == StatusCode.FOUND) {
			return api.getJson();
		} else if (api.getStatusCode() == StatusCode.NOT_FOUND) {
			throw new PlayerNotFoundException(getUserName());
		} else {
			throw new APIError("Unexpected response from the server.");
		}
	}
}
