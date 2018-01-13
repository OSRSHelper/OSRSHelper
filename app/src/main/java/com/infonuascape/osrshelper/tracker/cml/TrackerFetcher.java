package com.infonuascape.osrshelper.tracker.cml;

import android.content.Context;

import com.android.volley.Request;
import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotTrackedException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.players.PlayerSkillsPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by maden on 9/14/14.
 */
public class TrackerFetcher {
	final String API_URL = "https://crystalmathlabs.com/tracker/api.php?multiquery=";

	private Context context;
	private String userName;
	private int lookupTime;
    PlayerSkills playerSkills;

	public TrackerFetcher(Context context, String userName, int lookupTime) throws ParserErrorException, APIError, PlayerNotTrackedException {
		this.context = context;
		this.userName = userName.replace(" ", "%20");
		this.lookupTime = lookupTime;
		this.playerSkills = new PlayerSkills();
		processAPI();
	}

	public TrackerFetcher(Context context, String userName, TrackerTimeEnum.TrackerTime trackerTime) throws ParserErrorException, APIError, PlayerNotTrackedException {
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

    private void processAPI() throws PlayerNotTrackedException, APIError, ParserErrorException {
        // Fetch the results from the CML API
		String APIPayload = getDataFromAPI();

        // Split payload by "~~" which is response delimiter for CML
        String[] APIResponses = APIPayload.split("~~\n");

        // disregard last, always empty, payload
        if (APIResponses.length == 2) {

            // Order should be:
            // - Tracker
            // - Datapoints
            String trackerPayload = APIResponses[0];
            String dataPointPayload = APIResponses[1];

            if (trackerPayload.equals("-1") || dataPointPayload.equals("-1"))
                throw new PlayerNotTrackedException(getUserName());

            if (trackerPayload.equals("-4") || dataPointPayload.equals("-4"))
                throw new APIError("API under heavy load");

            // Process API payloads respectively
            processTrackerPayload(trackerPayload);
            processDataPointPayload(dataPointPayload);

        } else {
            throw new ParserErrorException("Error parsing API");
        }
    }

    public void processTrackerPayload(String payload) throws ParserErrorException, APIError, PlayerNotTrackedException {
        PlayerSkills ps = getPlayerSkills();
		List<Skill> skillList = ps.skillList;

		String[] APILine = payload.split("\n");
		String[] tokenizer;

		int skillId = 0;
		int lastUpdate = 0;

		for (String line : APILine) {
			tokenizer = line.split(",");

			//last track time
			if (tokenizer.length == 1) {
				long seconds = Long.parseLong(tokenizer[0]);
				long millis = seconds * 1000;
				Date date = new Date(System.currentTimeMillis() - millis);
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a", Locale.ENGLISH);
				sdf.setTimeZone(TimeZone.getDefault());
				String formattedDate = sdf.format(date);
				ps.setLastUpdate(formattedDate);
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
					Long experience = Long.parseLong(tokenizer[2]);
					int experienceDiff = Integer.parseInt(tokenizer[3]);

					//fill skill list with parsed data
					skillList.get(skillId).setExperienceDiff(expDiff);
					skillList.get(skillId).setRankDiff(-rankDiff); // inverse sign
					skillList.get(skillId).setExperience(experience);
					skillList.get(skillId).setRank(experienceDiff);
                    if(skillList.get(skillId).getSkillType() != SkillsEnum.SkillType.Overall) {
                        skillList.get(skillId).calculateLevels();
                    }
					skillId++; //pass to next skill in list
				} catch (Exception e) {
					throw new ParserErrorException("API format error");
				}
			} else {
				throw new ParserErrorException("API format error");
			}
		}

		//compute total level
		short totalLevel = 0;
        short totalVirtualLevel = 0;
		for (Skill s : skillList) {
            if (s.getSkillType() != SkillsEnum.SkillType.Overall) {
                totalLevel += s.getLevel();
                totalVirtualLevel += s.getVirtualLevel();
            }
        }

        //add total level to appropriate Skill entry
        Skill overallSkill = skillList.get(0); //always first indexed
        overallSkill.setLevel(totalLevel);
        overallSkill.setVirtualLevel(totalVirtualLevel);

        ps.calculateIfVirtualLevelsNecessary();
        setPlayerSkills(ps);
	}

    public void processDataPointPayload(String payload) throws ParserErrorException, APIError, PlayerNotTrackedException {
		List<PlayerSkillsPoint> playerSkillsPointList = new ArrayList<>();

		String[] APILine = payload.split("\n");
		String[] tokenizer;

		try {
			for (String skillsPointEntry : APILine) {
				// initiate entry at point in time
				PlayerSkillsPoint playerSkillsPoint = new PlayerSkillsPoint();

				tokenizer = skillsPointEntry.split(" ");

				//store epoch to represent skills at point in time
				int pointEpoch = Integer.parseInt(tokenizer[0]);
				playerSkillsPoint.setEpoch(pointEpoch);

				String skillsEntries = tokenizer[1];
				String[] skillsTokenizer = skillsEntries.split(",");

                String ranksEntries = tokenizer[2];
                String[] ranksTokenizer = ranksEntries.split(",");

				for (int skillId = 0; skillId < skillsTokenizer.length; skillId++) {
					// Extract experience at point in time
					Long experienceAtPoint = Long.parseLong(skillsTokenizer[skillId]);
                    int rankAtPoint = Integer.parseInt(ranksTokenizer[skillId]);

					// Store experience at point in time
					playerSkillsPoint.skillList.get(skillId).setExperience(experienceAtPoint);
                    playerSkillsPoint.skillList.get(skillId).setRank(rankAtPoint);
				}
				playerSkillsPointList.add(playerSkillsPoint);
			}
		} catch (Exception e) {
			throw new ParserErrorException("Error parsing the API");
		}

		System.out.println(playerSkillsPointList);
    }

	private String getDataFromAPI() throws PlayerNotTrackedException, APIError {
		//String connectionString = API_URL + "&player=" + userName + "&time=" + lookupTime;
		String connectionString = String.format("%s[{\"type\":\"trackehp\",\"player\":\"%s\",\"time\":\"%d\"},{\"type\":\"datapoints\",\"player\":\"%s\",\"time\":\"%d\"}]",
				API_URL, userName, lookupTime, userName, lookupTime);

		HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(connectionString, Request.Method.GET);
		String output = httpRequest.getOutput();
		if (httpRequest.getStatusCode() != StatusCode.FOUND || output == null) {
            throw new APIError("Error reaching the API");
		}
		return output;
	}
}
