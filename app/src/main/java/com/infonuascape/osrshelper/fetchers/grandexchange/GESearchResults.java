package com.infonuascape.osrshelper.fetchers.grandexchange;

import android.text.TextUtils;

import com.infonuascape.osrshelper.models.grandexchange.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class GESearchResults {
	public ArrayList<Item> itemsSearch;

    public GESearchResults(String jsonObject) {

		itemsSearch = new ArrayList<>();

		if(!TextUtils.isEmpty(jsonObject)) {
			try {
				JSONObject json = new JSONObject(jsonObject).getJSONObject("matches");

				Iterator<String> keys = json.keys();
				while(keys.hasNext()) {
					String itemId = keys.next();
					JSONObject item = json.getJSONObject(itemId);
					Item iterItem = new Item();
					iterItem.id = itemId;
					iterItem.description = item.getString("description");
					iterItem.name = item.getString("name");
					iterItem.members = TextUtils.equals(item.getString("members"), "true");
					iterItem.iconLarge = "http://services.runescape.com/m=itemdb_oldschool/obj_big.gif?id=" + itemId;
					itemsSearch.add(iterItem);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }
}


