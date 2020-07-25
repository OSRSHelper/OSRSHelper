package com.infonuascape.osrshelper.network;

import android.net.Uri;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by maden on 9/14/14.
 */
public class TrackerApi {
	private static final String API_URL = NetworkStack.ENDPOINT + "/wom/lookup/%1$s";
	private static final String KEY_DATA = "data";
	private static final String KEY_LAST_UPDATED = "endsAt";
	private static final String KEY_EXPERIENCE = "experience";
	private static final String KEY_RANK = "rank";
	private static final String KEY_GAINED = "gained";
	private static final String KEY_END = "end";

    public static Map<TrackerTime, PlayerSkills> fetch(String username) throws APIError, PlayerNotFoundException, JSONException {
		final String url = String.format(API_URL, Uri.encode(username));
		HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

		if (httpResult.statusCode == StatusCode.NOT_FOUND) {
			throw new PlayerNotFoundException(username);
		} else if(httpResult.statusCode != StatusCode.FOUND) {
			throw new APIError("Unexpected response from the server.");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		Map<TrackerTime, PlayerSkills> trackings = new HashMap<>();
		JSONObject json = new JSONObject(httpResult.output);

		Iterator<String> keys = json.keys();
		while(keys.hasNext()) {
			final String period = keys.next();
			final TrackerTime trackerTime = TrackerTime.create(period);
			PlayerSkills ps = new PlayerSkills();

			final JSONObject jsonPeriod = json.getJSONObject(period);
			final JSONObject jsonData = jsonPeriod.getJSONObject(KEY_DATA);
			String dateString = jsonPeriod.getString(KEY_LAST_UPDATED);
			try {
				ps.lastUpdate = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(sdf.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Iterator<String> skillKeys = jsonData.keys();
			while(skillKeys.hasNext()) {
				String skill = skillKeys.next();
				JSONObject skillJson = jsonData.getJSONObject(skill);

				for (Skill s : ps.skillList) {
					if (s.getSkillType().equals(skill)) {
						s.setExperienceDiff(skillJson.getJSONObject(KEY_EXPERIENCE).getLong(KEY_GAINED));
						s.setRankDiff(skillJson.getJSONObject(KEY_RANK).getLong(KEY_GAINED));
						s.setExperience(skillJson.getJSONObject(KEY_EXPERIENCE).getLong(KEY_END));
						s.setEHP(0);
					}
				}
			}

			short totalLevel = 0;
			short totalVirtualLevel = 0;
			for (Skill s : ps.skillList) {
				if (s.getSkillType() != SkillType.Overall) {
					totalLevel += s.getLevel();
					totalVirtualLevel += s.getVirtualLevel();
				}
			}

			for (Skill s : ps.skillList) {
				if (s.getSkillType().equals(SkillType.Overall)) {
					s.setLevel(totalLevel);
					s.setVirtualLevel(totalVirtualLevel);
				}
			}

			trackings.put(trackerTime, ps);
		}

		return trackings;
	}
}
