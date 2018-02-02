package com.infonuascape.osrshelper.grandexchange;

import android.text.TextUtils;

import com.infonuascape.osrshelper.models.grandexchange.GEItemInfo;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.models.grandexchange.Trend;
import com.infonuascape.osrshelper.models.grandexchange.TrendChange;
import com.infonuascape.osrshelper.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GEDetailInfoResults {
	public GEItemInfo itemInfo = null;

    public GEDetailInfoResults(String jsonObject) {

		if(!TextUtils.isEmpty(jsonObject)) {
			try {
				JSONObject json = new JSONObject(jsonObject).getJSONObject("item");

				itemInfo = new GEItemInfo();
				itemInfo.id = json.getString("id");
				itemInfo.type = json.getString("type");
				itemInfo.type = json.getString("type");
				itemInfo.description = json.getString("description");
				itemInfo.name = json.getString("name");
				itemInfo.iconLarge = "http://services.runescape.com/m=itemdb_oldschool/obj_big.gif?id=" + itemInfo.id;

				if (TextUtils.equals(json.getString("members"), "true")) {
					itemInfo.members = true;
				}

				//Trends
				itemInfo.today = parseTrend((JSONObject) json.get("today"));
				itemInfo.current = parseTrend((JSONObject) json.get("current"));
				itemInfo.day30 = parseTrendChange((JSONObject) json.get("day30"));
				itemInfo.day90 = parseTrendChange((JSONObject) json.get("day90"));
				itemInfo.day180 = parseTrendChange((JSONObject) json.get("day180"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }

	private TrendChange parseTrendChange(JSONObject jsonObject) {
		try {
			String change = jsonObject.getString("change");

			return new TrendChange(change, Utils.getTrendRateEnum((String) jsonObject.get("trend")));
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Trend parseTrend(JSONObject jsonObject) {
		try {
			Object priceObj = jsonObject.get("price");
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
			priceStr.replace("- ", "-");
			int price = Integer.parseInt(priceTemp);

			return new Trend(priceStr, price, Utils.getTrendRateEnum((String) jsonObject.get("trend")));
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}


