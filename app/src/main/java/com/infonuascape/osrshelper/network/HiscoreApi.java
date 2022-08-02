package com.infonuascape.osrshelper.network;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.infonuascape.osrshelper.app.OSRSApp;
import com.infonuascape.osrshelper.db.OSRSDatabaseFacade;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.HiscoreBoss;
import com.infonuascape.osrshelper.models.HiscoreBountyHunter;
import com.infonuascape.osrshelper.models.HiscoreClueScroll;
import com.infonuascape.osrshelper.models.HiscoreEfficiency;
import com.infonuascape.osrshelper.models.HiscoreLeaguePoints;
import com.infonuascape.osrshelper.models.HiscoreLms;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreApi {
    private static final String TAG = "HiscoreApi";
    private static final String API_URL = NetworkStack.ENDPOINT + "/wom/hiscore/%1$s";

    private static final String KEY_LATEST_SNAPSHOT = "latestSnapshot";
    private static final String KEY_SKILLS_EHP = "ehp";
    private static final String KEY_SKILLS_EXPERIENCE = "experience";
    private static final String KEY_SKILLS_RANK = "rank";

    private static final String KEY_STATUS = "status";
    private static final String VALUE_OK = "OK";
    private static final String VALUE_NOW_TRACKING = "now_tracking";
    private static final String VALUE_UNKNOWN_PLAYER = "unknown_player";
    private static final String VALUE_INVALID_USERNAME = "invalid_username";
    private static final String VALUE_SERVICE_TIMEOUT = "service_timeout";

    private static final String KEY_EHP = "ehp";
    private static final String KEY_EHB = "ehb";
    private static final String KEY_LMS = "last_man_standing";
    private static final String KEY_LEAGUE_POINTS = "league_points";
    private static final String KEY_CLUE_SCROLL = "clue_scrolls_";
    private static final String KEY_BOUNTY_HUNTER = "bounty_hunter";

    private static final String KEY_DISPLAY_NAME = "displayName";
    private static final String KEY_TYPE = "type";
    private static final String KEY_COMBAT_LEVEL = "combatLevel";

    private static final String KEY_RANK = "rank";
    private static final String KEY_SCORE = "score";
    private static final String KEY_VALUE = "value";
    private static final String KEY_KILLS = "kills";

    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_IMPORTED_AT = "importedAt";

    public static String getQueryUrl(String username) {
        return String.format(API_URL, Uri.encode(username));
    }

    public static PlayerSkills fetch(Context context, String username) throws PlayerNotFoundException, APIError {
        Logger.add(TAG, ": fetch: context=", context, ", username=", username);
        String url = getQueryUrl(username);
        HTTPResult httpResult = OSRSApp.getInstance().getNetworkStack().performGetRequest(url, true);

        if (httpResult.statusCode == StatusCode.NOT_FOUND) {
            throw new PlayerNotFoundException(username);
        } else if (httpResult.statusCode != StatusCode.FOUND) {
            throw new APIError("Unexpected response from the server.");
        }

        return parseResponse(context, httpResult.output, username);
    }

    public static PlayerSkills parseResponse(Context context, String output, String username) throws PlayerNotFoundException, APIError {
        try {
            JSONObject jsonObject = new JSONObject(output);
            PlayerSkills ps = new PlayerSkills();
            List<Skill> skillList = ps.skillList;

            if (jsonObject.has(KEY_STATUS) && TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_NOW_TRACKING)) {
                ps.isNewlyTracked = true;
            }

            if (jsonObject.has(KEY_STATUS) && (TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_UNKNOWN_PLAYER) || TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_INVALID_USERNAME))) {
                throw new PlayerNotFoundException(username);
            } else if (jsonObject.has(KEY_STATUS) && TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_SERVICE_TIMEOUT)) {
                throw new APIError("Hiscores are unavailable. Try again later.");
            } else if (jsonObject.has(KEY_STATUS) && (TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_OK) || TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_NOW_TRACKING))) {
                JSONObject jsonLatestSnapshot = jsonObject.getJSONObject(KEY_LATEST_SNAPSHOT);
                loop:
                for (Iterator<String> it = jsonLatestSnapshot.keys(); it.hasNext(); ) {
                    String key = it.next();

                    if (key.startsWith(KEY_CLUE_SCROLL)) {
                        JSONObject jsonClueScroll = jsonLatestSnapshot.getJSONObject(key);
                        long rank = jsonClueScroll.getLong(KEY_RANK);
                        long score = jsonClueScroll.getLong(KEY_SCORE);

                        if (rank != -1 && score != -1) {
                            HiscoreClueScroll clueScroll = new HiscoreClueScroll();
                            clueScroll.name = Utils.capitalizeString(key.replace(KEY_CLUE_SCROLL, ""));
                            clueScroll.rank = rank;
                            clueScroll.score = score;
                            ps.clueScrollsList.add(clueScroll);
                        }
                    } else if (key.startsWith(KEY_BOUNTY_HUNTER)) {
                        JSONObject jsonBountyHunter = jsonLatestSnapshot.getJSONObject(key);
                        long rank = jsonBountyHunter.getLong(KEY_RANK);
                        long score = jsonBountyHunter.getLong(KEY_SCORE);

                        if (rank != -1 && score != -1) {
                            HiscoreBountyHunter bountyHunter = new HiscoreBountyHunter();
                            bountyHunter.name = Utils.capitalizeString(key.replace(KEY_BOUNTY_HUNTER, ""));
                            bountyHunter.rank = rank;
                            bountyHunter.score = score;
                            ps.bountyHunterList.add(bountyHunter);
                        }
                    } else if (TextUtils.equals(key, KEY_LMS)) {
                        JSONObject json = jsonLatestSnapshot.getJSONObject(key);
                        long rank = json.getLong(KEY_RANK);
                        long score = json.getLong(KEY_SCORE);

                        if (rank != -1 && score != -1) {
                            HiscoreLms lms = new HiscoreLms();
                            lms.name = Utils.capitalizeString(key);
                            lms.rank = rank;
                            lms.score = score;
                            ps.hiscoreLms = lms;
                        }
                    } else if (TextUtils.equals(key, KEY_EHP) || TextUtils.equals(key, KEY_EHB)) {
                        JSONObject json = jsonLatestSnapshot.getJSONObject(key);
                        long rank = json.getLong(KEY_RANK);
                        double value = json.getDouble(KEY_VALUE);

                        if (rank != -1 && value != 0) {
                            HiscoreEfficiency efficiency = new HiscoreEfficiency();
                            efficiency.name = key.toUpperCase();
                            efficiency.rank = rank;
                            efficiency.score = (long) value;
                            ps.efficiencyList.add(efficiency);
                        }
                    } else if (TextUtils.equals(key, KEY_LEAGUE_POINTS)) {
                        JSONObject json = jsonLatestSnapshot.getJSONObject(key);
                        long rank = json.getLong(KEY_RANK);
                        long score = json.getLong(KEY_SCORE);

                        if (rank != -1 && score != -1) {
                            HiscoreLeaguePoints leaguePoints = new HiscoreLeaguePoints();
                            leaguePoints.rank = rank;
                            leaguePoints.score = score;
                            ps.hiscoreLeaguePoints = leaguePoints;
                        }
                    } else if (TextUtils.equals(key, KEY_CREATED_AT)) {
                        String dateString = jsonLatestSnapshot.getString(KEY_CREATED_AT);
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                            ps.lastUpdate = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(sdf.parse(dateString));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (!TextUtils.equals(key, KEY_IMPORTED_AT)) {
                        for (Skill s : skillList) {
                            if (s.getSkillType().equals(key)) {
                                JSONObject skillJson = jsonLatestSnapshot.getJSONObject(key);
                                s.setExperience(skillJson.getLong(KEY_SKILLS_EXPERIENCE));
                                s.setRank(Integer.parseInt(skillJson.getString(KEY_SKILLS_RANK)));
                                s.setEHP(Double.parseDouble(skillJson.getString(KEY_SKILLS_EHP)));

                                if (!s.getSkillType().equals(SkillType.Overall)) {
                                    s.calculateLevel();
                                }
                                continue loop;
                            }
                        }

                        JSONObject jsonItem = jsonLatestSnapshot.getJSONObject(key);
                        if (jsonItem.has(KEY_KILLS)) {
                            //It's a boss
                            long rank = jsonItem.getLong(KEY_RANK);
                            long kills = jsonItem.getLong(KEY_KILLS);

                            if (rank != -1 && kills != -1) {
                                HiscoreBoss boss = new HiscoreBoss();
                                boss.name = Utils.capitalizeString(key);
                                boss.rank = rank;
                                boss.score = kills;
                                ps.bossesList.add(boss);
                            }
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

                //Update the account with the proper username and type
                if (jsonObject.has(KEY_COMBAT_LEVEL)) {
                    ps.combatLvl = jsonObject.getInt(KEY_COMBAT_LEVEL);
                } else {
                    ps.combatLvl = Utils.getCombatLvl(ps);
                }
                final String displayName = jsonObject.getString(KEY_DISPLAY_NAME);
                final AccountType type = AccountType.create(jsonObject.getString(KEY_TYPE));

                try {
                    OSRSApp.getInstance().getDatabaseFacade().updateAccount(context, username, displayName, type, ps.combatLvl);
                } catch (SecurityException e) {
                    //You can't access database via the widget
                }

                return ps;
            }
        } catch (JSONException e) {
			Logger.addException(TAG, e);
        }

        return null;
    }
}
