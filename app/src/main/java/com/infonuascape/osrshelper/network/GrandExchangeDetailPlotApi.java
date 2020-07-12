package com.infonuascape.osrshelper.network;

import android.net.Uri;

import com.android.volley.Request;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GrandExchangeDetailPlotApi {
    private final static String API_URL = "https://services.runescape.com/m=itemdb_oldschool/api/graph/%s.json";

    private final static String KEY_DAILY = "daily";
    private final static String KEY_AVERAGE = "average";

    public static GrandExchangeDetailPlotResults fetch(String itemId) {
        HTTPResult httpResult = NetworkStack.getInstance().performRequest(String.format(API_URL, Uri.encode(itemId)), Request.Method.GET);
        if (httpResult.isParsingSuccessful) {
            List<DataPoint> datapoints = new ArrayList<>();
            List<DataPoint> averages = new ArrayList<>();
            try {
                JSONObject json = httpResult.jsonObject.getJSONObject(KEY_DAILY);

                Iterator<String> keys = json.keys();
                while(keys.hasNext()) {
                    String timestamp = keys.next();
                    DataPoint datapoint = new DataPoint(Double.parseDouble(timestamp), json.getDouble(timestamp));
                    datapoints.add(datapoint);
                }

                json = httpResult.jsonObject.getJSONObject(KEY_AVERAGE);

                keys = json.keys();
                while(keys.hasNext()) {
                    String timestamp = keys.next();
                    DataPoint datapoint = new DataPoint(new Date(Long.parseLong(timestamp)), json.getLong(timestamp));
                    averages.add(datapoint);
                }

                GrandExchangeDetailPlotResults results = new GrandExchangeDetailPlotResults();
                results.datapoints = datapoints.toArray(new DataPoint[0]);
                results.averages = averages.toArray(new DataPoint[0]);
                return results;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class GrandExchangeDetailPlotResults {
        public DataPoint[] datapoints;
        public DataPoint[] averages;
    }
}
