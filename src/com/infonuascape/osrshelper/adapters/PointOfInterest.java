package com.infonuascape.osrshelper.adapters;

import android.graphics.Point;


public class PointOfInterest {
	public String name;
	public Point point;
	
	public PointOfInterest(String name, Point point){
		this.name = name;
		this.point = point;
	}
	
	public String getName(){
		return name;
	}
	
	public Point getPoint(){
		return point;
	}

}
