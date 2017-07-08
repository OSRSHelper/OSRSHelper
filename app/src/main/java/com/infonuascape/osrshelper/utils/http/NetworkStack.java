package com.infonuascape.osrshelper.utils.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by marc_ on 2017-07-08.
 */

public class NetworkStack {
    private static NetworkStack instance;
    private final RequestQueue queue;

    private NetworkStack(final Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static void init(final Context context) {
        instance = new NetworkStack(context);
    }

    public static NetworkStack getInstance() {
        if(instance == null) {
            throw new IllegalStateException("NetworkStack must be initialized with application context");
        }
        return instance;
    }

    public HTTPRequest performRequest(String url, int requestMethod) {
        return new HTTPRequest(queue, url, requestMethod);
    }
}
