package com.infonuascape.osrshelper.tracker;

import android.content.Context;

import java.io.IOException;
import java.net.URLEncoder;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;

/**
 * This class contains a single method that updates a user on RuneTracker. This
 * is defined as calling an HTTP endpoint which adds a point of data in the
 * user's stats history table.
 * 
 * @author Cynthia of irc.rscript.org
 */
public class Updater {
	/**
	 * Updates the given <code>user</code> on RuneTracker.
	 * 
	 * @param user
	 *            Name of the user to update. Since 2009-10-01, this is the
	 *            display name of the user, not his or her login name.
	 * @throws IOException
	 *             if the HTTP endpoint connection needed to update the
	 *             <code>user</code> throws <tt>IOException</tt>
	 * @throws PlayerNotFoundException
	 */
	public static void perform(final Context context, final String user) throws IOException, PlayerNotFoundException {
		android.util.Log.i("Updater", "Hey! I'm updating!");
		String connectionString = "http://runetracker.org/updateUser.php?user=" + URLEncoder.encode(user, "utf-8");
		new HTTPRequest(context, connectionString, Request.Method.GET);
	}
}
