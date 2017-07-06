package com.infonuascape.osrshelper.grandexchange;

import com.android.volley.Request;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;

import android.content.Context;
import android.net.Uri;
import java.util.ArrayList;

public class GEFetcher {
    final String API_URL = "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/";

    public GEFetcher() {

    }



    public String search(Context context, String itemName, int pageNum) {
        HTTPRequest httpRequest = new HTTPRequest(context, API_URL + "items.json?category=1&alpha=" + itemName.replace(" ", "%20") + "&page=" + pageNum, Request.Method.GET);
        if (httpRequest.getStatusCode() == StatusCode.FOUND) { // got 200,
            return httpRequest.getOutput();
        }
        return null;
    }
}
