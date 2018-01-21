package com.infonuascape.osrshelper.utils;

import android.content.Context;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/20/18.
 */

public class API {
    private String endpoint = "http://192.168.0.117:8080";
    private Context context;
    private String args;
    private HTTPRequest.StatusCode statusCode;
    private JSONObject json;

    public API(Context context, String args) throws JSONException {
        this.context = context;
        this.args = args;
        fetch();
    }

    private void fetch() throws JSONException {
        HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(endpoint + args, Request.Method.GET);
        statusCode = httpRequest.getStatusCode();
        if(statusCode == HTTPRequest.StatusCode.FOUND) {
            json = new JSONObject(httpRequest.getOutput());
        }
    }

    public JSONObject getJson() {
        return json;
    }

    public HTTPRequest.StatusCode getStatusCode() {
        return statusCode;
    }
}