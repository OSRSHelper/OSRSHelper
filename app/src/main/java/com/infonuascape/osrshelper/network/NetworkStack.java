package com.infonuascape.osrshelper.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by marc_ on 2017-07-08.
 */

public class NetworkStack {
    private static final String TAG = "NetworkStack";
    public static final String ENDPOINT = "https://api.buying-gf.com/v2";
    private static NetworkStack instance;
    private final RequestQueue queue;

    private NetworkStack(final Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static void init(final Context context) {
        if(instance == null) {
            instance = new NetworkStack(context);
        }
    }

    public static NetworkStack getInstance() {
        return instance;
    }

    public HTTPResult performGetRequest(String url) {
        return performRequest(url, Request.Method.GET);
    }

    public HTTPResult performRequest(String url, int requestMethod) {
        Log.d(TAG, "performRequest: url=" + url);
        HTTPResult result = new HTTPResult();
        result.url = url;

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest stringRequest = new StringRequest(requestMethod, url, future, future);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1 ,1));
        queue.add(stringRequest);

        try {
            result.output = future.get();
            try {
                result.jsonObject = new JSONObject(result.output);
                result.isParsingSuccessful = result.jsonObject.length() != 0;
            } catch (JSONException ignored) {
                //Certain APIs returns an array instead of an object
            }

            result.statusCode = StatusCode.FOUND;
        } catch (InterruptedException e) {
            result.statusCode = StatusCode.NOT_FOUND;
            //Don't show interrupted exception, it's user-triggered
        } catch (ExecutionException e) {
            result.statusCode = StatusCode.NOT_FOUND;
            e.printStackTrace();
        }

        return result;
    }
}
