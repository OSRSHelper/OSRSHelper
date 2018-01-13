package com.infonuascape.osrshelper.top;

import android.content.Context;

import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.tracker.cml.TrackerFetcher;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotTrackedException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class TopHelper {
	private Context context;
	private String userName;

	public TopHelper(final Context context) {
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
