package com.infonuascape.osrshelper.grandexchange;

import com.infonuascape.osrshelper.utils.grandexchange.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GESearchResults {
	public ArrayList<Item> itemsSearch;

    public GESearchResults(String jsonObject) {

		itemsSearch = new ArrayList<Item>();

		if(jsonObject != null) {
			JSONObject json = null;
			try {
				json = new JSONObject(jsonObject);

				JSONArray items = (JSONArray) json.get("items");

				if (items != null) {
					for (int i = 0; i < items.length(); i++) {
						Item iterItem = new Item();
						JSONObject currItem = (JSONObject) items.get(i);
						iterItem.id = (Integer) currItem.get("id");
						iterItem.type = (String) currItem.get("type");
						iterItem.description = (String) currItem.get("description");
						iterItem.name = (String) currItem.get("name");
						iterItem.icon = (String) currItem.get("icon");
						iterItem.iconLarge = (String) currItem.get("icon_large");

						if (currItem.get("members").equals("true")) {
							iterItem.members = true;
						}

						//Trends
						iterItem.today = parseTrend((JSONObject) currItem.get("today"));
						iterItem.current = parseTrend((JSONObject) currItem.get("current"));

						itemsSearch.add(iterItem);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }

    private Item.Trend parseTrend(JSONObject jsonObject) {
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

			return new Item().new Trend(priceStr, price, Item.getTrendRateEnum((String) jsonObject.get("trend")));
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
    }
}


