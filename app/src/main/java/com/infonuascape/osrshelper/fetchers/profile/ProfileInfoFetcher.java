package com.infonuascape.osrshelper.fetchers.profile;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Request;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.grandexchange.GEItemInfo;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.models.players.SkillDiff;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProfileInfoFetcher {
    final String API_URL = "https://api.buying-gf.com/track/datapointsdelta/%s/%s";

    private Context context;

    public ProfileInfoFetcher(Context context) {
        this.context = context;
    }

    public ArrayList<Delta> fetch(final String username, final int seconds) {
        HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(String.format(API_URL, Uri.encode(username), String.valueOf(seconds)), Request.Method.GET);
        String output = null;
        if (httpRequest.getStatusCode() == StatusCode.FOUND) { // got 200,
            output = httpRequest.getOutput();
        }

        ArrayList<Delta> deltas = new ArrayList<>();
        if(!TextUtils.isEmpty(output)) {
            try {
                JSONArray jsonArray = new JSONObject(output).getJSONArray("datapoints");
                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject deltaJson = jsonArray.getJSONObject(i);
                    Delta delta = new Delta();
                    delta.timestamp = deltaJson.getLong("t0") * 1000;
                    delta.timestampRecent = deltaJson.getLong("t1") * 1000;

                    Iterator<String> keys = deltaJson.getJSONObject("deltas").keys();
                    while(keys.hasNext()) {
                        String skillName = keys.next();
                        SkillDiff skillDiff = new SkillDiff();
                        skillDiff.skillType = SkillType.valueOf(skillName);
                        skillDiff.experience = deltaJson.getJSONObject("deltas").getJSONObject(skillName).getLong("experience");
                        skillDiff.rank = deltaJson.getJSONObject("deltas").getJSONObject(skillName).getLong("rank");
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
