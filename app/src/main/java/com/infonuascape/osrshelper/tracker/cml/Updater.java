package com.infonuascape.osrshelper.tracker.cml;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import java.io.IOException;
import java.net.URLEncoder;

public class Updater {

	public static void perform(final String user) throws IOException, PlayerNotFoundException {
		android.util.Log.i("Updater", "Hey! I'm updating!");
		String connectionString = "https://crystalmathlabs.com/tracker/api.php?type=update&player=" + URLEncoder.encode(user, "utf-8");
		NetworkStack.getInstance().performRequest(connectionString, Request.Method.GET);
	}
}
