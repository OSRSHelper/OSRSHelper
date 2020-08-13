package com.infonuascape.osrshelper.network;

import android.net.Uri;
import android.text.TextUtils;

import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdaterApi {
    private static final String TAG = "UpdaterApi";

    private static final String API_URL = NetworkStack.ENDPOINT + "/wom/update/%1$s";
    private static final String KEY_STATUS = "status";
    private static final String VALUE_OK = "OK";

    public static boolean perform(final String username) {
        Logger.add(TAG, ": perform: username=", username);
        final String url = String.format(API_URL, Uri.encode(username));
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

        if (httpResult.statusCode == StatusCode.FOUND) {
            try {
                JSONObject jsonObject = new JSONObject(httpResult.output);
                return jsonObject.has(KEY_STATUS) && TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_OK);
            } catch (JSONException e) {
                Logger.addException(TAG, e);
            }
        }

        return false;
    }
}
