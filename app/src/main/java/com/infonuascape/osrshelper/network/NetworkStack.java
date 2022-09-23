package com.infonuascape.osrshelper.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.utils.Logger;

/**
 * Created by marc_ on 2017-07-08.
 */

public class NetworkStack {
    private static final String TAG = "NetworkStack";
    public static final String ENDPOINT = "https://api.buying-gf.com/v2";
    private static NetworkStack instance;
    private final RequestQueue queue;
    private final Context context;

    private NetworkStack(final Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public static void init(final Context context) {
        if (instance == null) {
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
        Logger.add(TAG, ": performRequest: url=", url, ", requestMethod=", requestMethod);
        HTTPResult result = new HTTPResult();
        result.url = url;

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest stringRequest = new StringRequest(requestMethod, url, future, future);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1));
        queue.add(stringRequest);

        try {
            result.output = future.get();
            result.statusCode = StatusCode.FOUND;
        } catch (Exception e) {
            Logger.addException(e);
            result.statusCode = StatusCode.NOT_FOUND;
        }

        return result;
    }
}
