package com.infonuascape.osrshelper.grandexchange;

import android.util.Log;

import com.infonuascape.osrshelper.utils.grandexchange.Item;

import java.util.ArrayList;
import org.json.*;

public class GESearchResults {
	public ArrayList<Item> itemsSearch;

    public GESearchResults(String JSONObject) {

		JSONObject json = null;
		itemsSearch = new ArrayList<Item>();
		try {
			json = new JSONObject(JSONObject);

			JSONArray items = (JSONArray) json.get("items");

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
				iterItem.today = parseTrend((JSONObject)currItem.get("today"));
				iterItem.current = parseTrend((JSONObject)currItem.get("current"));

				itemsSearch.add(iterItem);
			}
		} catch (JSONException e) {
			e.printStackTrace();
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


			priceStr = priceStr.replaceAll("[- ,.]", "");
			priceStr = priceStr.replace("+", "");
			priceStr = priceStr.replace("k", "00");
			priceStr = priceStr.replace("m", "00000");
			priceStr = priceStr.replace("b", "00000000");

			int price = Integer.parseInt(priceStr);

			return new Item().new Trend(price, Item.getTrendRateEnum((String) jsonObject.get("trend")));
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
    }
}


