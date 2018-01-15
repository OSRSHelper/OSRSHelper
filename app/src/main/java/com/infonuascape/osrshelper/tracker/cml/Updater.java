package com.infonuascape.osrshelper.tracker.cml;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import java.io.IOException;
import java.net.URLEncoder;

public class Updater {

	public static void perform(final Context context, final String user) throws IOException, PlayerNotFoundException {
		Log.i("Updater", "Hey! I'm updating!");
		String connectionString = "https://crystalmathlabs.com/tracker/api.php?type=update&player=" + URLEncoder.encode(user, "utf-8");
		HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(connectionString, Request.Method.GET);
		String output = httpRequest.getOutput();
		if (output.equals("2"))
		    throw new PlayerNotFoundException(user);
	}
}
