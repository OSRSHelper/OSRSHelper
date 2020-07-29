package com.infonuascape.osrshelper.models.players;

import com.infonuascape.osrshelper.enums.AccountType;

public class PlayerExp {
	public String username;
	public String displayName;
	public AccountType accountType;
	public long experience;

	public PlayerExp(String username, String displayName, AccountType accountType, long experience) {
		this.username = username;
		this.displayName = displayName;
		this.accountType = accountType;
		this.experience = experience;
	}
}
