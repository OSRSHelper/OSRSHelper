package com.infonuascape.osrshelper.top;

import android.content.Context;

import com.android.volley.Request;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.infonuascape.osrshelper.models.players.PlayerExp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maden on 9/14/14.
 */
public class TopFetcher {
	final String API_URL = "https://crystalmathlabs.com/tracker/api.php?type=currenttop";

	private Context context;
	private SkillType skill;
    private Period period = Period.Day;
    private List<PlayerExp> playerList;
    public enum Period {
        Day, Week, Month, Year;
    }

	public TopFetcher(Context context, SkillType skill, Period period) throws ParserErrorException, APIError {
		this.context = context;
		this.skill = skill;
		this.period = period;
	}

	/*public TopFetcher(Context context, String userName, TrackerTimeEnum.TrackerTime trackerTime) throws ParserErrorException, APIError, PlayerNotTrackedException {
		this(context, userName, trackerTime.getSeconds());
	}*/

    public List<PlayerExp> processAPI() throws APIError, ParserErrorException {
		// Fetch the results from the CML API
		String APIPayload = getDataFromAPI();

		if (APIPayload.equals("-3"))
			throw new APIError("Database error");

		if (APIPayload.equals("-4"))
			throw new APIError("API under heavy load");

		// Process API payloads respectively
		return processTopPayload(APIPayload);
	}

    public List<PlayerExp> processTopPayload(String payload) throws ParserErrorException, APIError {
		List<PlayerExp> playerList = new ArrayList<>();

		String[] APILine = payload.split("\n");
		String[] tokenizer;

        try {
            for (String line : APILine) {
                if (!line.equals("")) {
                    tokenizer = line.split(",");
                    playerList.add(new PlayerExp(tokenizer[0], Long.parseLong(tokenizer[1])));
                }
            }
        } catch(Exception e) {
            throw new ParserErrorException("API format error");
        }
        return playerList;
	}

	private String getDataFromAPI() throws APIError {
		//String connectionString = API_URL + "&player=" + userName + "&time=" + lookupTime;
		String connectionString = String.format("%s&skill=%s&timeperiod=%s", API_URL, skill, period.name());

		HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(connectionString, Request.Method.GET);
		String output = httpRequest.getOutput();
		if (httpRequest.getStatusCode() != StatusCode.FOUND || output == null) {
            throw new APIError("Error reaching the API");
		}
		return output;
	}
}
