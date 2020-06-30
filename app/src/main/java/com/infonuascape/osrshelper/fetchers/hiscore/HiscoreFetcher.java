package com.infonuascape.osrshelper.fetchers.hiscore;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.API;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreFetcher {
	private Context context;
	private String userName;
    private AccountType accountType;

	public HiscoreFetcher(final Context context, String userName, AccountType accountType) {
		this.context = context;
		this.userName = Uri.encode(userName);
		this.accountType = accountType;

	}

	public String getUserName() {
		return userName;
	}

    public AccountType getAccountType() {
        return accountType;
    }

    public List<String> FetchHSdata() throws JSONException{
	    String OSRSHSAPICall = "https://secure.runescape.com/m=hiscore_oldschool/index_lite.ws?player=";
	    OSRSHSAPICall = OSRSHSAPICall + getUserName();

        HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(OSRSHSAPICall, Request.Method.GET);
        HTTPRequest.StatusCode statusCode = httpRequest.getStatusCode();
        if(statusCode == HTTPRequest.StatusCode.FOUND)
        {

        	String data = httpRequest.getOutput();
        	List<String> sep = Arrays.asList(data.split("\n"));

			return  sep;
        }
		return null;
	}


    public PlayerSkills getPlayerSkills() throws PlayerNotFoundException, JSONException, APIError {
		//JSONObject APIOutput = getDataFromAPI();
		List<String> JagexSkillsList = FetchHSdata();
		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = ps.skillList;
		//for each skill type,
		for (SkillType enumVal: SkillType.values()) {
			String skill = enumVal.getSkillName();
			//loop through a list of skills and
			for (Skill s: skillList) {
				if (s.getSkillType().equals(skill)) {
					int pos = s.getJagexIndex();
					String csv = JagexSkillsList.get(pos);
					Skill _skill_ = new Skill(csv,pos);
					int lvl = _skill_.getLevel();
					s.setVirtualLevel((short) _skill_.getVirtualLevel());
					s.setLevel((short) _skill_.getLevel());
				}
			}
		}

		//compute total level


		short totalVirtualLevel = 0;

		//add total level to appropriate Skill entry
		Skill overallSkill = skillList.get(0); //always first indexed
		overallSkill.setLevel(overallSkill.getLevel());
		overallSkill.setVirtualLevel(overallSkill.getLevel());

		return ps;
	}

	public PlayerSkills mapDataSet(String dataSet) {
		// split dataset, map to skills enum
		return new PlayerSkills(); // dummy return
	}

	private String getAPIEndpoint() {
		return String.format("/hiscore/%1$s/%2$s", getAccountType().name().toLowerCase(), userName);
    }

	private JSONObject getDataFromAPI() throws PlayerNotFoundException, JSONException, APIError {
		API api = new API(context, getAPIEndpoint());

		if (api.getStatusCode() == StatusCode.FOUND) {
			return api.getJson();
		} else if (api.getStatusCode() == StatusCode.NOT_FOUND) {
			throw new PlayerNotFoundException(getUserName());
		} else {
			throw new APIError("Unexpected response from the server.");
		}
	}
}
