package com.infonuascape.osrshelper.grandexchange;

import com.infonuascape.osrshelper.utils.grandexchange.Item;
import com.infonuascape.osrshelper.utils.grandexchange.Item.Trend;
import com.infonuascape.osrshelper.utils.grandexchange.Item.TrendRate;

import java.util.ArrayList;
import org.json.*;

public class GESearchResults {
    public int length = 0;

    public GESearchResults(String JSONObject) {

	JSONObject json = new JSONObject(JSONObject);
	JSONArray items = (JSONArray) json.get("items");

	this.length = items.length();

	if (this.length > 0) { //received items

	    ArrayList<Item> itemList = new ArrayList<>();

	    Item iterItem = new Item();

	    System.out.println("Found " + this.length + " item(s)");

	    for (int i = 0; i < items.length(); i++) {
		JSONObject currItem = (JSONObject) items.get(i);
		iterItem.id = (int) currItem.get("id");
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

		System.out.println("Item #"+i);
		System.out.println(iterItem.toString());
		System.out.println("\n\n\n");
		
	    }
	}



    }

    private Item.Trend parseTrend(JSONObject jsonObject) {
	String trendStr = (String)jsonObject.get("trend");

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

	int price = Integer.parseInt(priceStr); 

	return new Item().new Trend(price, Item.getTrendRateEnum((String)jsonObject.get("trend")));
    }
}


