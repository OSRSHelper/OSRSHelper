package com.infonuascape.osrshelper.db;

public class Credential {
	private String username;
	private String password;

	public Credential(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
