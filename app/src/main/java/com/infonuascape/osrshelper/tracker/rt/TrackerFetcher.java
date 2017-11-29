package com.infonuascape.osrshelper.tracker.rt;

import android.content.Context;

import com.android.volley.Request;
import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

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
	final String API_URL = "http://rscript.org/lookup.php?type=track&flag=07track&skill=all";

	private Context context;
	private String userName;
	private int lookupTime;

	public TrackerFetcher(Context context, String userName, int lookupTime) {
		this.context = context;
		this.userName = userName.replace(" ", "%20");
		this.lookupTime = lookupTime;
	}

	public TrackerFetcher(Context context, String userName, TrackerTimeEnum.TrackerTime trackerTime) {
		this(context, userName, trackerTime.getSeconds());
	}

	public String getUserName() {
		return userName;
	}

	public int getLookupTime() {
		return lookupTime;
	}

	public PlayerSkills getPlayerTracker() throws PlayerNotFoundException, ParserErrorException, APIError {
		final String APIOutput = getDataFromAPI();

		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = ps.skillList;

		String[] APILine = APIOutput.split("\n");

		try {
			//tokenize API output
			String[] tokenizer;
			for (String line : APILine) {
				tokenizer = line.split(":");

				//parse current exp
				if (tokenizer[0].equals("start")) {
                    String skillName = tokenizer[1];
                    for (int i = 0; i < skillList.size(); i++) {
                        if (skillName.equals(skillList.get(i).getSkillType().getSkillName()) ||
								skillName.equals(skillList.get(i).getSkillType().getAlternativeName())) {
                            skillList.get(i).setExperience(Integer.parseInt(tokenizer[2]));
							if(skillList.get(i).getSkillType() != SkillsEnum.SkillType.Overall) {
								skillList.get(i).calculateLevels();
							}
							break;
                        }
                    }
                }

                //parse exp gains
                else if (tokenizer[0].equals("gain")) {
					if (tokenizer.length == 4) {
						String skillName = tokenizer[1];
						for (int i = 0; i < skillList.size(); i++) {
							if (skillName.equals(skillList.get(i).getSkillType().getSkillName()) ||
									skillName.equals(skillList.get(i).getSkillType().getAlternativeName())) {
								skillList.get(i).setExperienceDiff(Integer.parseInt(tokenizer[3]));
							}
						}
					} else {
						throw new APIError("Error while parsing RuneTracker response");
					}
				}

				//parse start track time
				else if (tokenizer[0].equals("started")) {
					long seconds = Long.parseLong(tokenizer[1]);
					long millis = seconds * 1000;
					Date date = new Date(System.currentTimeMillis() - millis);
					SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a", Locale.ENGLISH);
					sdf.setTimeZone(TimeZone.getDefault());
					String formattedDate = sdf.format(date);
					ps.setSinceWhen(formattedDate);
				}
			}
		} catch (Exception e) {
			throw new APIError("Error while parsing RuneTracker response");
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

		return ps;
	}

	public PlayerSkills mapDataSet(String dataSet) {
		// split dataset, map to skills enum
		return new PlayerSkills(); // dummy return
	}

	private String getDataFromAPI() throws PlayerNotFoundException {
		String connectionString = API_URL + "&user=" + userName + "&time=" + lookupTime;
		HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(connectionString, Request.Method.GET);
		String output = httpRequest.getOutput();
		if (httpRequest.getStatusCode() == StatusCode.FOUND && output != null) {
			for (String line : output.split("\n")) {
				if (line == "0:-1") { // unknown name
					throw new PlayerNotFoundException(getUserName());
				}
			}
		} else {
			throw new PlayerNotFoundException(getUserName());
		}
		return output;
	}
}
