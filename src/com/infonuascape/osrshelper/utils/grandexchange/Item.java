package com.infonuascape.osrshelper.utils.grandexchange;

public class Item {
    public int id;
    public String type;
    public String description;
    public String name;
    public String typeIcon = ""; //seemingly unused
    public String icon;
    public String iconLarge;
    public boolean members;
    public Trend day180;
    public Trend day90;
    public Trend day30;
    public Trend current;
    public Trend today;

    public Item() {

    }


    public class Trend {
	public Trend(int change, TrendRate rate) { //temp string
	    this.change = change;
	    this.rate = rate;
	}

	int change;
	TrendRate rate;
    }
    
    public enum TrendRate {
	POSITIVE, NEGATIVE, NEUTRAL
    };

    public static TrendRate getTrendRateEnum(String trend) {
	if (trend.equals("positive")) return TrendRate.POSITIVE;
	if (trend.equals("negative")) return TrendRate.NEGATIVE;
	if (trend.equals("neutral")) return TrendRate.NEUTRAL;
	return TrendRate.POSITIVE;
    }
    

    public String toString() {
	String output;
	output = "Item data:\n";
	output += "ID:"+ id+",\nType:"+type+",\nDescription:"+description+"\nName:"+name+"\ntypeIcon:"+typeIcon+"\nicon:"+icon+"\niconLarge:"+iconLarge+"\nmembers:"+(members ? "yes" : "no")+"\n\n\n";
	output += "Trend data\n";
	output += "Today:"+today.change+"\n";
	output += "Current:"+current.change+"\n";

	return output;
    }
}
