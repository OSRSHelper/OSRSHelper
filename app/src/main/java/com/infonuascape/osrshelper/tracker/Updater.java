package com.infonuascape.osrshelper.tracker;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.API;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class Updater {

	public static JSONObject perform(final Context context, final String user) throws IOException, APIError {
		API api = null;
		try {
			api = new API(context, String.format("/track/update/%1$s", user));


		if (api.getStatusCode() == HTTPRequest.StatusCode.FOUND) {
			return api.getJson();
		} else {
			throw new APIError("Unexpected response from the server.");
		}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
