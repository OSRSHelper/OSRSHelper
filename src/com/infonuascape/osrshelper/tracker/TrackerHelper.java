package com.infonuascape.osrshelper.tracker;

import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class TrackerHelper {
	String userName = null;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public PlayerSkills getPlayerStats() throws PlayerNotFoundException, ParserErrorException {
		TrackerFetcher tf = new TrackerFetcher(getUserName(), TrackerTimeEnum.TrackerTime.Day);
		return tf.getPlayerTracker();
	}

	public PlayerSkills getPlayerStats(TrackerTimeEnum.TrackerTime time) throws PlayerNotFoundException,
			ParserErrorException {
		TrackerFetcher tf = new TrackerFetcher(getUserName(), time);
		return tf.getPlayerTracker();
	}

}
