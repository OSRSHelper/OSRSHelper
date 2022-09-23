package com.infonuascape.osrshelper.network;

import android.net.Uri;

import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.utils.Logger;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GrandExchangeDetailPlotApi {
    private static final String TAG = "GrandExchangeDetailPlotApi";

    private final static String API_URL = "https://services.runescape.com/m=itemdb_oldschool/api/graph/%s.json";

    private final static String KEY_DAILY = "daily";
    private final static String KEY_AVERAGE = "average";

    public static GrandExchangeDetailPlotResults fetch(String itemId) {
        Logger.add(TAG, ": fetch: itemId=", itemId);
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(String.format(API_URL, Uri.encode(itemId)));
        if (httpResult.statusCode == StatusCode.FOUND) {
            List<DataPoint> datapoints = new ArrayList<>();
            List<DataPoint> averages = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(httpResult.output);
                JSONObject jsonDaily = json.getJSONObject(KEY_DAILY);

                Iterator<String> keys = jsonDaily.keys();
                while (keys.hasNext()) {
                    String timestamp = keys.next();
                    DataPoint datapoint = new DataPoint(Double.parseDouble(timestamp), jsonDaily.getDouble(timestamp));
                    datapoints.add(datapoint);
                }

                JSONObject jsonAverage = json.getJSONObject(KEY_AVERAGE);

                keys = jsonAverage.keys();
                while (keys.hasNext()) {
                    String timestamp = keys.next();
                    DataPoint datapoint = new DataPoint(new Date(Long.parseLong(timestamp)), jsonAverage.getLong(timestamp));
                    averages.add(datapoint);
                }

                GrandExchangeDetailPlotResults results = new GrandExchangeDetailPlotResults();
                results.datapoints = datapoints.toArray(new DataPoint[0]);
                results.averages = averages.toArray(new DataPoint[0]);
                return results;
            } catch (JSONException e) {
                Logger.addException(e);
            }
        }
        return null;
    }

    public static class GrandExchangeDetailPlotResults {
        public DataPoint[] datapoints;
        public DataPoint[] averages;
    }
}
