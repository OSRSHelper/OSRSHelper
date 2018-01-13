package com.infonuascape.osrshelper.tracker.cml;

import android.content.Context;

import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.utils.exceptions.*;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class TrackerHelper {
	private Context context;
	private String userName;

	public TrackerHelper(final Context context) {
		this.context = context;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public PlayerSkills getPlayerStats(TrackerTimeEnum.TrackerTime time) throws PlayerNotTrackedException,
			ParserErrorException, APIError {

        //Instanciate a new API fetcher
        TrackerFetcher tf = new TrackerFetcher(context, userName, time);



		return tf.getPlayerSkills();
	}

}
