package com.infonuascape.osrshelper.network;

import android.net.Uri;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.HiscoreBoss;
import com.infonuascape.osrshelper.models.HiscoreBountyHunter;
import com.infonuascape.osrshelper.models.HiscoreClueScroll;
import com.infonuascape.osrshelper.models.HiscoreLms;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreApi {
	private static final String API_URL = NetworkStack.ENDPOINT + "/hiscore/%1$s/%2$s";

	private static final String KEY_SKILLS = "skills";
	private static final String KEY_SKILLS_EXPERIENCE = "Experience";
	private static final String KEY_SKILLS_RANK = "Rank";
	private static final String KEY_SKILLS_LEVEL = "Level";

	private static final String KEY_RANK = "rank";
	private static final String KEY_SCORE = "score";

	private static final String KEY_BOUNTY_HUNTER = "bounty_hunter";
	private static final String KEY_CLUE_SCROLLS = "clue_scrolls";
	private static final String KEY_LMS = "lms";
	private static final String KEY_BOSSES = "bosses";

	public static String getQueryUrl(String username, AccountType accountType) {
		return String.format(API_URL, accountType.getApiName(), Uri.encode(username));
	}

    public static PlayerSkills fetch(String username, AccountType accountType) throws PlayerNotFoundException, APIError {
		String url = getQueryUrl(username, accountType);
		HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url, true);

		if (httpResult.statusCode == StatusCode.NOT_FOUND) {
			throw new PlayerNotFoundException(username);
		} else if(httpResult.statusCode != StatusCode.FOUND) {
			throw new APIError("Unexpected response from the server.");
		}

		return parseResponse(httpResult.output);
	}

	public static PlayerSkills parseResponse(String output) {
		try {
			JSONObject jsonObject = new JSONObject(output);
			PlayerSkills ps = new PlayerSkills();
			List<Skill> skillList = ps.skillList;

			JSONObject jsonSkills = jsonObject.getJSONObject(KEY_SKILLS);
			for (Iterator<String> it = jsonSkills.keys(); it.hasNext(); ) {
				String skill = it.next();
				JSONObject skillJson = jsonSkills.getJSONObject(skill);

				for (Skill s : skillList) {
					if (s.getSkillType().equals(skill)) {
						s.setExperience(skillJson.getLong(KEY_SKILLS_EXPERIENCE));
						s.setRank(Integer.parseInt(skillJson.getString(KEY_SKILLS_RANK)));
						s.setLevel(Short.parseShort(skillJson.getString(KEY_SKILLS_LEVEL)));
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


			Pattern patternBountyHunter = Pattern.compile("(Bounty Hunter - )(\\w*)");
			JSONObject jsonBountyHunters = jsonObject.getJSONObject(KEY_BOUNTY_HUNTER);
			for (Iterator<String> it = jsonBountyHunters.keys(); it.hasNext(); ) {
				String name = it.next();
				JSONObject jsonBountyHunter = jsonBountyHunters.getJSONObject(name);
				long rank = jsonBountyHunter.getLong(KEY_RANK);
				long score = jsonBountyHunter.getLong(KEY_SCORE);

				if (rank != -1 && score != -1) {
					HiscoreBountyHunter bountyHunter = new HiscoreBountyHunter();
					Matcher matcher = patternBountyHunter.matcher(name);
					if (matcher.find()) {
						bountyHunter.name = matcher.group(2);
					} else {
						bountyHunter.name = name;
					}
					bountyHunter.rank = rank;
					bountyHunter.score = score;
					ps.bountyHunterList.add(bountyHunter);
				}
			}

			JSONObject jsonBosses = jsonObject.getJSONObject(KEY_BOSSES);
			for (Iterator<String> it = jsonBosses.keys(); it.hasNext(); ) {
				String name = it.next();
				JSONObject jsonBoss = jsonBosses.getJSONObject(name);
				long rank = jsonBoss.getLong(KEY_RANK);
				long score = jsonBoss.getLong(KEY_SCORE);

				if (rank != -1 && score != -1) {
					HiscoreBoss boss = new HiscoreBoss();
					boss.name = name;
					boss.rank = rank;
					boss.score = score;
					ps.bossesList.add(boss);
				}
			}

			JSONObject jsonClueScrolls = jsonObject.getJSONObject(KEY_CLUE_SCROLLS);
			Pattern patternClueScroll = Pattern.compile("(Clue Scrolls \\()(\\w*)([)])");
			for (Iterator<String> it = jsonClueScrolls.keys(); it.hasNext(); ) {
				String name = it.next();
				JSONObject jsonClueScroll = jsonClueScrolls.getJSONObject(name);
				long rank = jsonClueScroll.getLong(KEY_RANK);
				long score = jsonClueScroll.getLong(KEY_SCORE);

				if (rank != -1 && score != -1) {
					HiscoreClueScroll clueScroll = new HiscoreClueScroll();
					Matcher matcher = patternClueScroll.matcher(name);
					if (matcher.find()) {
						clueScroll.name = matcher.group(2);
					} else {
						clueScroll.name = name;
					}
					clueScroll.rank = rank;
					clueScroll.score = score;
					ps.clueScrollsList.add(clueScroll);
				}
			}

			JSONObject jsonLms = jsonObject.getJSONObject(KEY_LMS);
			if (jsonLms.length() > 0) {
				String key = jsonLms.keys().next();
				JSONObject json = jsonLms.getJSONObject(key);
				long rank = json.getLong(KEY_RANK);
				long score = json.getLong(KEY_SCORE);

				if (rank != -1 && score != -1) {
					HiscoreLms lms = new HiscoreLms();
					lms.rank = rank;
					lms.score = score;
					ps.hiscoreLms = lms;
				}
			}

			return ps;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
