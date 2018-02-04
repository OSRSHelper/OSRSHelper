package com.infonuascape.osrshelper.fetchers.grandexchange;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.infonuascape.osrshelper.models.grandexchange.RSBuddyPrice;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class RSBuddyPriceFetcher {
    final String API_URL = "https://api.rsbuddy.com/grandExchange?a=guidePrice&i=%s";

    private Context context;

    public RSBuddyPriceFetcher(Context context) {
        this.context = context;
    }

    public RSBuddyPrice fetch(String itemId) {
        HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(String.format(API_URL, itemId), Request.Method.GET);
        String output = null;
        if (httpRequest.getStatusCode() == StatusCode.FOUND) { // got 200,
            output = httpRequest.getOutput();
        }

        RSBuddyPrice rsBuddyPrice = null;
        if(!TextUtils.isEmpty(output)) {
            try {
                JSONObject json = new JSONObject(output);
                rsBuddyPrice = new RSBuddyPrice();
                Iterator<String> keys = json.keys();
                while(keys.hasNext()) {
                    String key = keys.next();

                    switch(key) {
                        case "overall":
                            rsBuddyPrice.overall = json.getLong(key);
                            break;
                        case "buying":
                            rsBuddyPrice.buying = json.getLong(key);
                            break;
                        case "buyingQuantity":
                            rsBuddyPrice.buyingQuantity = json.getLong(key);
                            break;
                        case "selling":
                            rsBuddyPrice.selling = json.getLong(key);
                            break;
                        case "sellingQuantity":
                            rsBuddyPrice.sellingQuantity = json.getLong(key);
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rsBuddyPrice;
    }
}
