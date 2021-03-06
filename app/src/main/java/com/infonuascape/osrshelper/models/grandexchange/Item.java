package com.infonuascape.osrshelper.models.grandexchange;

public class Item {
    public String id;
    public String description;
    public String name;
    public String iconLarge;
    public String widgetId;
    public boolean members;

    public Item() {

    }

    public String toString() {
        String output;
        output = "Item data:\n";
        output += "ID:"+ id+",\nDescription:"+description+"\nName:"+name+"\nmembers:"+(members ? "yes" : "no")+"\n\n\n";
        output += "Trend data\n";

        return output;
    }
}
