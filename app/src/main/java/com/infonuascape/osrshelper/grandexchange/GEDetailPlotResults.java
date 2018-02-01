package com.infonuascape.osrshelper.grandexchange;

import android.text.TextUtils;

import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class GEDetailPlotResults {
	public ArrayList<DataPoint> datapoints;
	public ArrayList<DataPoint> averages;

    public GEDetailPlotResults(String jsonObject) {

		datapoints = new ArrayList<>();
		averages = new ArrayList<>();

		if(!TextUtils.isEmpty(jsonObject)) {
			try {
				JSONObject json = new JSONObject(jsonObject).getJSONObject("daily");

				Iterator<String> keys = json.keys();
				while(keys.hasNext()) {
					String timestamp = keys.next();
					DataPoint datapoint = new DataPoint(Double.valueOf(timestamp), json.getDouble(timestamp));
					datapoints.add(datapoint);
				}

				json = new JSONObject(jsonObject).getJSONObject("average");

				keys = json.keys();
				while(keys.hasNext()) {
					String timestamp = keys.next();
					DataPoint datapoint = new DataPoint(new Date(Long.valueOf(timestamp)), json.getLong(timestamp));
					averages.add(datapoint);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }
}


