package com.infonuascape.osrshelper.network;

import android.net.Uri;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.models.players.SkillDiff;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProfileInfoApi {
    private final static String API_URL = NetworkStack.ENDPOINT + "/track/datapointsdelta/%s/%s";
    private final static String KEY_DATAPOINTS = "datapoints";
    private final static String KEY_T0 = "t0";
    private final static String KEY_T1 = "t1";
    private final static String KEY_DELTAS = "deltas";
    private final static String KEY_EXPERIENCE = "experience";
    private final static String KEY_RANK = "rank";

    public static ArrayList<Delta> fetch(final String username, final int seconds) throws PlayerNotFoundException, APIError {
        String url = String.format(API_URL, Uri.encode(username), seconds);
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

        if (httpResult.statusCode == StatusCode.NOT_FOUND) {
            throw new PlayerNotFoundException(username);
        } else if(httpResult.statusCode != StatusCode.FOUND) {
            throw new APIError("Unexpected response from the server.");
        }

        ArrayList<Delta> deltas = new ArrayList<>();
        if(httpResult.isParsingSuccessful) {
            try {
                JSONArray jsonArray = httpResult.jsonObject.getJSONArray(KEY_DATAPOINTS);
                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject deltaJson = jsonArray.getJSONObject(i);
                    Delta delta = new Delta();
                    delta.timestamp = deltaJson.getLong(KEY_T0) * 1000;
                    delta.timestampRecent = deltaJson.getLong(KEY_T1) * 1000;

                    JSONObject deltasJson = deltaJson.getJSONObject(KEY_DELTAS);
                    Iterator<String> keys = deltasJson.keys();
                    while(keys.hasNext()) {
                        String skillName = keys.next();
                        JSONObject skillNameJson = deltasJson.getJSONObject(skillName);
                        SkillDiff skillDiff = new SkillDiff();
                        skillDiff.skillType = SkillType.valueOf(skillName);
                        skillDiff.experience = skillNameJson.getLong(KEY_EXPERIENCE);
                        skillDiff.rank = skillNameJson.getLong(KEY_RANK);
                        delta.skillDiffs.add(skillDiff);
                    }
                    deltas.add(delta);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return deltas;
    }
}
