package com.infonuascape.osrshelper.fetchers.tracker;

import android.content.Context;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.API;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotTrackedException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

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
public class TrackerFetcher {
	final String API_URL = "https://crystalmathlabs.com/tracker/API.php?multiquery=";

	private Context context;
	private String userName;
	private int lookupTime;
    PlayerSkills playerSkills;
	private long lastUpdate;

	public TrackerFetcher(Context context, String userName, int lookupTime) throws ParserErrorException, APIError, PlayerNotTrackedException, PlayerNotFoundException, JSONException {
		this.context = context;
		this.userName = userName.replace(" ", "%20");
		this.lookupTime = lookupTime;
		this.playerSkills = new PlayerSkills();
		processAPI();
	}

	public TrackerFetcher(Context context, String userName, TrackerTime trackerTime) throws ParserErrorException, APIError, PlayerNotTrackedException, PlayerNotFoundException, JSONException {
		this(context, userName, trackerTime.getSeconds());
	}

	public String getUserName() {
		return userName;
	}

	public int getLookupTime() {
		return lookupTime;
	}

    public PlayerSkills getPlayerSkills() {
        return playerSkills;
    }

    public void setPlayerSkills(PlayerSkills ps) {
        this.playerSkills = ps;
    }

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

    private void processAPI() throws PlayerNotTrackedException, APIError, ParserErrorException, PlayerNotFoundException, JSONException {
		PlayerSkills ps = getPlayerSkills();
		List<Skill> skillList = ps.skillList;
		JSONObject APIOutput = getDataFromAPI();
		JSONObject ehp = APIOutput.getJSONObject("ehp");

		long seconds = ehp.getLong("lastupdated");
		long millis = seconds * 1000;
		Date date = new Date(System.currentTimeMillis() - millis);
		ps.lastUpdate = SimpleDateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);

		JSONObject skills = ehp.getJSONObject("skills");
		for (Iterator<String> it = skills.keys(); it.hasNext(); ) {
			String skill = it.next();
			JSONObject skillJson = skills.getJSONObject(skill);

			for (Skill s : skillList) {
				if (s.getSkillType().equals(skill)) {
					s.setExperienceDiff(skillJson.getInt("ExperienceDiff"));
					s.setRankDiff(skillJson.getInt("RankDiff"));
					s.setExperience(skillJson.getLong("Experience"));
					s.setExperienceDiff(skillJson.getInt("ExperienceDiff"));
					s.setEHP(skillJson.getDouble("EHP"));
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

		setPlayerSkills(ps);
	}

	private String getAPIEndpoint() {
		return String.format("/track/ehp/%1$s/%2$s", getUserName(), getLookupTime());
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
