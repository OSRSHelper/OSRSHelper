package com.infonuascape.osrshelper.fetchers.grandexchange;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import android.content.Context;
import android.net.Uri;

public class GESearchFetcher {
    final String API_URL = "https://api.buying-gf.com/ge/search/%s";

    private Context context;

    public GESearchFetcher(Context context) {
        this.context = context;
    }

    public String search(String itemName) {
        HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(String.format(API_URL, Uri.encode(itemName)), Request.Method.GET);
        if (httpRequest.getStatusCode() == StatusCode.FOUND) { // got 200,
            return httpRequest.getOutput();
        }
        return null;
    }
}
