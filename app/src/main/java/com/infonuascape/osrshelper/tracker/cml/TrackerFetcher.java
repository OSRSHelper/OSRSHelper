package com.infonuascape.osrshelper.tracker.cml;

import com.android.volley.Request;
import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotTrackedException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

import java.util.List;

/**
 * Created by maden on 9/14/14.
 */
public class TrackerFetcher {
	final String API_URL = "https://crystalmathlabs.com/tracker/api.php?type=trackehp";

	private String userName;
	private int lookupTime;

	public TrackerFetcher(String userName, int lookupTime) {
		this.userName = userName.replace(" ", "%20");
		this.lookupTime = lookupTime;
	}

	public TrackerFetcher(String userName, TrackerTimeEnum.TrackerTime trackerTime) {
		this.userName = userName.replace(" ", "%20");
		lookupTime = trackerTime.getSeconds();
	}

	public String getUserName() {
		return userName;
	}

	public int getLookupTime() {
		return lookupTime;
	}

	public PlayerSkills getPlayerTracker() throws ParserErrorException, APIError, PlayerNotTrackedException {
		final String APIOutput = getDataFromAPI();

		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = ps.skillList;

		String[] APILine = APIOutput.split("\n");
		String[] tokenizer;

		int lastTrackTime = -1;
		int skillId = 0;

		for (String line : APILine) {
			tokenizer = line.split(",");

			//last track time
			if (tokenizer.length == 1) {
				lastTrackTime = Integer.parseInt(tokenizer[0]);
			}
			// total EHP
			else if (tokenizer.length == 3) {

			}
			// skill entry
			else if (tokenizer.length == 5) {
				try {
					//parse data
					int expDiff = Integer.parseInt(tokenizer[0]);
					int rankDiff = Integer.parseInt(tokenizer[1]);
					int experience = Integer.parseInt(tokenizer[2]);
					int experienceDiff = Integer.parseInt(tokenizer[3]);

					//fill skill list with parsed data
					skillList.get(skillId).setExperienceDiff(expDiff);
					skillList.get(skillId).setRankDiff(-rankDiff); // inverse sign
					skillList.get(skillId).setExperience(experience);
					skillList.get(skillId).setRank(experienceDiff);

					skillId++; //pass to next skill in list
				} catch (Exception e) {
					throw new APIError("API format error");
				}
			} else {
				throw new APIError("API format error");
			}
		}

		//compute total level
		short totalLevel = 0;
        short totalVirtualLevel = 0;
		for (Skill s : skillList) {
            if (s.getSkillType() != SkillsEnum.SkillType.Overall) {
                totalLevel += Utils.getLevelFromXP(s.getExperience());
                totalVirtualLevel += Utils.getLevelFromXP(s.getExperience(), true);
            }
        }

        //add total level to appropriate Skill entry
        Skill overallSkill = skillList.get(0); //always first indexed
        overallSkill.setLevel(totalLevel);
        overallSkill.setVirtualLevel(totalVirtualLevel);

		return ps;
	}

	public PlayerSkills mapDataSet(String dataSet) {
		// split dataset, map to skills enum
		return new PlayerSkills(); // dummy return
	}

	private String getDataFromAPI() throws PlayerNotTrackedException, APIError {
		String connectionString = API_URL + "&player=" + userName + "&time=" + lookupTime;
		HTTPRequest httpRequest = NetworkStack.getInstance().performRequest(connectionString, Request.Method.GET);
		String output = httpRequest.getOutput();
		if (httpRequest.getStatusCode() == StatusCode.FOUND && output != null) {
			if (output.equals("-1"))
				throw new PlayerNotTrackedException(getUserName());
			if (output.equals("-4"))
				throw new APIError("API under heavy load");
		} else {
			throw new APIError("Error parsing API");
		}
		return output;
	}
}
