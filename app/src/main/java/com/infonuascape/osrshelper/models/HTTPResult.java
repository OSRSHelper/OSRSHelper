package com.infonuascape.osrshelper.models;

import org.json.JSONObject;

public class HTTPResult {
    public String url;
    public String output;
    public JSONObject jsonObject;
    public StatusCode statusCode = StatusCode.REQUEST_NOT_SENT;
    public boolean isParsingSuccessful;
}
