package com.infonuascape.osrshelper.fetchers.top;

import android.content.Context;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.utils.API;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maden on 9/14/14.
 */
public class TopFetcher {
	final String API_URL = "https://crystalmathlabs.com/tracker/API.php?type=currenttop";
	private Context context;
	private SkillType skill;
	private TrackerTime period;

	public TopFetcher(Context context, SkillType skill, TrackerTime period) throws ParserErrorException, APIError {
		this.context = context;
		this.skill = skill;
		this.period = period;
	}

    public List<PlayerExp> processAPI() throws APIError, ParserErrorException, JSONException {
		JSONObject APIOutput = getDataFromAPI();
		List<PlayerExp> playerList = new ArrayList<>();

		JSONArray top = APIOutput.getJSONArray("top");
		for (int i = 0; top.length() > i; i++) {
			JSONObject userEntry = top.getJSONObject(i);

			playerList.add(new PlayerExp(userEntry.getString("username"), userEntry.getLong("ExperienceDiff")));
		}

		return playerList;
	}


	private String getAPIEndpoint() {
		return String.format("/track/top/%1$s/%2$s", skill, period);
	}

	private JSONObject getDataFromAPI() throws APIError, JSONException {
		API api = new API(context, getAPIEndpoint());

		if (api.getStatusCode() == StatusCode.FOUND) {
			return api.getJson();
		} else {
			throw new APIError("Unexpected response from the server.");
		}
    }
}
