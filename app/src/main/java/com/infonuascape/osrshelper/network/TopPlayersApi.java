package com.infonuascape.osrshelper.network;

import android.text.TextUtils;

import com.infonuascape.osrshelper.enums.AccountType;
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
    private static final String API_URL = NetworkStack.ENDPOINT + "/wom/top/%1$s/%2$s/%3$s";

	private static final String KEY_STATUS = "status";
	private static final String VALUE_OK = "OK";
	private static final String VALUE_SERVICE_TIMEOUT = "service_timeout";

    private final static String KEY_TOP = "tops";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_DISPLAY_NAME = "displayName";
    private final static String KEY_ACCOUNT_TYPE = "type";
    private final static String KEY_EXPERIENCE_GAINED = "gained";

    public static List<PlayerExp> fetch(SkillType skillType, AccountType accountType, TrackerTime period) throws APIError, JSONException {
        String url = String.format(API_URL, accountType.apiName, skillType.getApiName(), period.period);
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

        if (httpResult.statusCode != StatusCode.FOUND) {
            throw new APIError("Unexpected response from the server.");
        }
        List<PlayerExp> playerList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(httpResult.output);

		if (jsonObject.has(KEY_STATUS) && TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_SERVICE_TIMEOUT)) {
			throw new APIError("Top players are unavailable. Try again later.");
		} else if (jsonObject.has(KEY_STATUS) && TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_OK)) {
			JSONArray top = jsonObject.getJSONArray(KEY_TOP);

			for (int i = 0; top.length() > i; i++) {
				JSONObject userEntry = top.getJSONObject(i);
				final String username = userEntry.getString(KEY_USERNAME);
				final String displayName = userEntry.getString(KEY_DISPLAY_NAME);
				final long gained = userEntry.getLong(KEY_EXPERIENCE_GAINED);
				final AccountType type = AccountType.create(userEntry.getString(KEY_ACCOUNT_TYPE));
				playerList.add(new PlayerExp(username, displayName, type, gained));
			}
		}

        return playerList;
    }
}
