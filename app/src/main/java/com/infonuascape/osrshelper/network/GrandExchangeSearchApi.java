package com.infonuascape.osrshelper.network;

import android.net.Uri;
import android.text.TextUtils;

import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GrandExchangeSearchApi {
    private static final String TAG = "GrandExchangeSearchApi";

    private final static String API_URL = NetworkStack.ENDPOINT + "/ge/search/%s";

    private final static String ICON_ENDPOINT = "https://services.runescape.com/m=itemdb_oldschool/obj_big.gif?id=";
    private final static String KEY_MATCHES = "matches";
    private final static String KEY_NAME = "name";
    private final static String KEY_DESCRIPTION = "description";
    private final static String KEY_MEMBERS = "members";
    private final static String VALUE_TRUE = "true";

    public static List<Item> fetch(String itemName) {
        Logger.add(TAG, ": fetch: itemName=", itemName);
        List<Item> itemsSearch = new ArrayList<>();
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(String.format(API_URL, Uri.encode(itemName)));
        if (httpResult.statusCode == StatusCode.FOUND) {
            try {
                JSONObject json = new JSONObject(httpResult.output).getJSONObject(KEY_MATCHES);

                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String itemId = keys.next();
                    JSONObject itemJson = json.getJSONObject(itemId);
                    Item item = new Item();
                    item.id = itemId;
                    item.description = itemJson.getString(KEY_DESCRIPTION);
                    item.name = itemJson.getString(KEY_NAME);
                    item.members = TextUtils.equals(itemJson.getString(KEY_MEMBERS), VALUE_TRUE);
                    item.iconLarge = ICON_ENDPOINT + itemId;
                    itemsSearch.add(item);
                }
            } catch (JSONException e) {
                Logger.addException(e);
            }
        }
        return itemsSearch;
    }
}
