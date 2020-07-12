package com.infonuascape.osrshelper.models;

import android.graphics.Point;

import java.util.Objects;


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

	@Override
	public int hashCode() {
		return Objects.hash(name, point);
	}
}
