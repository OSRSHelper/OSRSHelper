package com.infonuascape.osrshelper.network;

import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.grandexchange.RSBuddyPrice;

import org.json.JSONException;

import java.util.Iterator;

public class RSBuddyPriceApi {
    private final static String API_URL = "https://api.rsbuddy.com/grandExchange?a=guidePrice&i=%s";

    private final static String KEY_OVERALL = "overall";
    private final static String KEY_BUYING = "buying";
    private final static String KEY_BUYING_QUANTITY = "buyingQuantity";
    private final static String KEY_SELLING = "selling";
    private final static String KEY_SELLING_QUANTITY = "sellingQuantity";

    public static RSBuddyPrice fetch(String itemId) {
        String url = String.format(API_URL, itemId);
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

        RSBuddyPrice rsBuddyPrice = null;
        if(httpResult.isParsingSuccessful) {
            try {
                rsBuddyPrice = new RSBuddyPrice();
                Iterator<String> keys = httpResult.jsonObject.keys();
                while(keys.hasNext()) {
                    String key = keys.next();

                    switch(key) {
                        case KEY_OVERALL:
                            rsBuddyPrice.overall = httpResult.jsonObject.getLong(key);
                            break;
                        case KEY_BUYING:
                            rsBuddyPrice.buying = httpResult.jsonObject.getLong(key);
                            break;
                        case KEY_BUYING_QUANTITY:
                            rsBuddyPrice.buyingQuantity = httpResult.jsonObject.getLong(key);
                            break;
                        case KEY_SELLING:
                            rsBuddyPrice.selling = httpResult.jsonObject.getLong(key);
                            break;
                        case KEY_SELLING_QUANTITY:
                            rsBuddyPrice.sellingQuantity = httpResult.jsonObject.getLong(key);
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
