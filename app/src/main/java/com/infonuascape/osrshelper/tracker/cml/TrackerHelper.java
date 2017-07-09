package com.infonuascape.osrshelper.tracker.cml;

import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.utils.exceptions.*;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class TrackerHelper {
	String userName = null;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public PlayerSkills getPlayerStats() throws PlayerNotFoundException, ParserErrorException, APIError {
		TrackerFetcher tf = new TrackerFetcher(getUserName(), TrackerTimeEnum.TrackerTime.Day);
		return tf.getPlayerTracker();
	}

	public PlayerSkills getPlayerStats(TrackerTimeEnum.TrackerTime time) throws PlayerNotFoundException,
			ParserErrorException, APIError {
		TrackerFetcher tf = new TrackerFetcher(getUserName(), time);
		return tf.getPlayerTracker();
	}

}
