package com.infonuascape.osrshelper.network;

import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Request;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.models.grandexchange.GrandExchangeDetailInfo;
import com.infonuascape.osrshelper.models.grandexchange.Trend;
import com.infonuascape.osrshelper.models.grandexchange.TrendChange;
import com.infonuascape.osrshelper.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class GrandExchangeDetailInfoApi {
    private final static String API_URL = "https://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=%s";

    private final static String ICON_ENDPOINT = "http://services.runescape.com/m=itemdb_oldschool/obj_big.gif?id=";
    private final static String KEY_ITEM = "item";
    private final static String KEY_NAME = "name";
    private final static String KEY_DESCRIPTION = "description";
    private final static String KEY_ID = "id";
    private final static String KEY_TYPE = "type";
    private final static String KEY_MEMBERS = "members";
    private final static String KEY_PRICE = "price";
    private final static String KEY_CHANGE = "change";

    private final static String KEY_TREND = "trend";
    private final static String KEY_TREND_TODAY = "today";
    private final static String KEY_TREND_CURRENT = "current";
    private final static String KEY_TREND_DAY_30 = "day30";
    private final static String KEY_TREND_DAY_90 = "day90";
    private final static String KEY_TREND_DAY_180 = "day180";

    private final static String VALUE_TRUE = "true";

    public static GrandExchangeDetailInfo fetch(String itemId) {
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(String.format(API_URL, Uri.encode(itemId)));
        if (httpResult.statusCode == StatusCode.FOUND) {
            try {
                JSONObject json = new JSONObject(httpResult.output).getJSONObject(KEY_ITEM);

                GrandExchangeDetailInfo itemInfo = new GrandExchangeDetailInfo();
                itemInfo.id = json.getString(KEY_ID);
                itemInfo.type = json.getString(KEY_TYPE);
                itemInfo.description = json.getString(KEY_DESCRIPTION);
                itemInfo.name = json.getString(KEY_NAME);
                itemInfo.iconLarge = ICON_ENDPOINT + itemInfo.id;

                if (TextUtils.equals(json.getString(KEY_MEMBERS), VALUE_TRUE)) {
                    itemInfo.members = true;
                }

                //Trends
                itemInfo.today = parseTrend((JSONObject) json.get(KEY_TREND_TODAY));
                itemInfo.current = parseTrend((JSONObject) json.get(KEY_TREND_CURRENT));
                itemInfo.day30 = parseTrendChange((JSONObject) json.get(KEY_TREND_DAY_30));
                itemInfo.day90 = parseTrendChange((JSONObject) json.get(KEY_TREND_DAY_90));
                itemInfo.day180 = parseTrendChange((JSONObject) json.get(KEY_TREND_DAY_180));
                return itemInfo;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static TrendChange parseTrendChange(JSONObject jsonObject) {
        try {
            String change = jsonObject.getString(KEY_CHANGE);

            return new TrendChange(change, Utils.getTrendRateEnum((String) jsonObject.get(KEY_TREND)));
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Trend parseTrend(JSONObject jsonObject) {
        try {
            Object priceObj = jsonObject.get(KEY_PRICE);
            String priceStr = "";
            if (priceObj instanceof Integer) {
                priceStr = String.valueOf(priceObj);
            } else {
                priceStr = (String) priceObj;
            }

            String priceTemp = priceStr;
            priceTemp = priceTemp.replaceAll("[- ,.]", "");
            priceTemp = priceTemp.replace("+", "");
            priceTemp = priceTemp.replace("k", "00");
            priceTemp = priceTemp.replace("m", "00000");
            priceTemp = priceTemp.replace("b", "00000000");

            if(!priceStr.endsWith("k") && !priceStr.endsWith("b") && !priceStr.endsWith("m")) {
                priceStr += "gp";
            }
            priceStr = priceStr.replace("- ", "-");
            int price = Integer.parseInt(priceTemp);

            return new Trend(priceStr, price, Utils.getTrendRateEnum((String) jsonObject.get(KEY_TREND)));
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
