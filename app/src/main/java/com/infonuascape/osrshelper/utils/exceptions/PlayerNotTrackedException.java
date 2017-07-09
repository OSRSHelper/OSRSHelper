package com.infonuascape.osrshelper.utils.exceptions;

/**
 * Created by maden on 9/12/14.
 */
public class PlayerNotTrackedException extends Exception{
	private static final long serialVersionUID = 1L;
	private String userName;
	public PlayerNotTrackedException(String userName)
	{
		this.userName = userName;
	}
	public String getUserName()
	{
		return userName;
	}
}

