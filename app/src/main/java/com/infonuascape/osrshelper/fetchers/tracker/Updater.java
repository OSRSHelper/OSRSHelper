package com.infonuascape.osrshelper.fetchers.tracker;

import android.content.Context;
import android.net.Uri;

import com.infonuascape.osrshelper.utils.API;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Updater {

	public static JSONObject perform(final Context context, final String user) throws IOException, APIError {
		API api = null;
		try {
			api = new API(context, String.format("/track/update/%1$s", Uri.encode(user)));


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
