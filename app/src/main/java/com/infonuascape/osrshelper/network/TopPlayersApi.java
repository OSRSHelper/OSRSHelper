package com.infonuascape.osrshelper.network;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.utils.exceptions.APIError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maden on 9/14/14.
 */
public class TopPlayersApi {
	private static final String API_URL = NetworkStack.ENDPOINT + "/track/top/%1$s/%2$s";

	private final static String KEY_TOP = "top";
	private final static String KEY_USERNAME = "username";
	private final static String KEY_EXPERIENCE_DIFF = "ExperienceDiff";

    public static List<PlayerExp> fetch(SkillType skillType, TrackerTime period) throws APIError, JSONException {
		String url = String.format(API_URL, skillType, period);
		HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

		if(httpResult.statusCode != StatusCode.FOUND) {
			throw new APIError("Unexpected response from the server.");
		}
		List<PlayerExp> playerList = new ArrayList<>();

		JSONArray top = httpResult.jsonObject.getJSONArray(KEY_TOP);
		for (int i = 0; top.length() > i; i++) {
			JSONObject userEntry = top.getJSONObject(i);
			playerList.add(new PlayerExp(userEntry.getString(KEY_USERNAME), userEntry.getLong(KEY_EXPERIENCE_DIFF)));
		}

		return playerList;
	}
}
