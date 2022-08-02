package com.infonuascape.osrshelper.network;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.infonuascape.osrshelper.app.OSRSApp;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdaterApi {
    private static final String TAG = "UpdaterApi";

    private static final String API_URL = NetworkStack.ENDPOINT + "/wom/update/%1$s";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String VALUE_OK = "OK";

    public static Response perform(final String username) {
        Logger.add(TAG, ": perform: username=", username);
        final String url = String.format(API_URL, Uri.encode(username));
        HTTPResult httpResult = OSRSApp.getInstance().getNetworkStack().performGetRequest(url);
        Response response = new Response();

        if (httpResult.statusCode == StatusCode.FOUND) {
            try {
                JSONObject jsonObject = new JSONObject(httpResult.output);
                response.isSuccess = jsonObject.has(KEY_STATUS) && TextUtils.equals(jsonObject.getString(KEY_STATUS), VALUE_OK);
                if (jsonObject.has(KEY_MESSAGE)) {
                    response.errorMessage = convertErrorMessage(jsonObject.getString(KEY_MESSAGE));
                }
            } catch (JSONException e) {
                Logger.addException(TAG, e);
            }
        }

        return response;
    }

    private static String convertErrorMessage(final String errorMessage) {
        if (errorMessage != null && errorMessage.endsWith("seconds ago.")) {
            return "Updated less than 60 seconds ago.";
        }

        return errorMessage;
    }

    public static class Response {
        public boolean isSuccess;
        public String errorMessage;

        @NonNull
        @Override
        public String toString() {
            return "[isSuccess=" + isSuccess + ", errorMessage=" + errorMessage + "]";
        }
    }
}
