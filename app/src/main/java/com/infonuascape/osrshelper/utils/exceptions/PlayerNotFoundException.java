package com.infonuascape.osrshelper.utils.exceptions;

/**
 * Created by maden on 9/12/14.
 */
public class PlayerNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	private String userName;
	public PlayerNotFoundException(String userName)
	{
		this.userName = userName;
	}
	public String getUserName()
	{
		return userName;
	}
}

