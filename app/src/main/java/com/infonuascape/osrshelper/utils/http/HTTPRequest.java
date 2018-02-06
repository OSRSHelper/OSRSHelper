package com.infonuascape.osrshelper.utils.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infonuascape.osrshelper.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HTTPRequest {
	private static final String TAG = "HTTPRequest";

	public enum StatusCode {
		FOUND(200), NOT_FOUND(404), ERROR(500), REQUEST_NOT_SENT(-1);
		public int value;

		private StatusCode(int value) {
			this.value = value;
		}

		private static final Map lookup = new HashMap();

		public int getStatusCodeValue() {
			return value;
		}

		static {
			for (StatusCode sc : StatusCode.values()) {
				lookup.put(sc.getStatusCodeValue(), sc);
			}
		}

		public static StatusCode get(int value) { // reverse lookup
			return (StatusCode) lookup.get(value);
		}
	};

	private String output;
	private StatusCode statusCode = StatusCode.REQUEST_NOT_SENT;

	public HTTPRequest(RequestQueue queue, String url, int requestMethod) {
		performRequest(queue, url, requestMethod);
	}

	private void performRequest(RequestQueue queue, String url, int requestMethod) {
		statusCode = StatusCode.FOUND;
		// Instantiate the RequestQueue.
		RequestFuture<String> future = RequestFuture.newFuture();

		Logger.add(TAG, ": ", url);
		StringRequest stringRequest = new StringRequest(requestMethod, url, future, future);
		queue.add(stringRequest);

		try {
			output = future.get();
		} catch (InterruptedException e) {
			statusCode = StatusCode.NOT_FOUND;
			//Don't show interrupted exception, it's user-triggered
		} catch (ExecutionException e) {
			statusCode = StatusCode.NOT_FOUND;
			e.printStackTrace();
		}
	}

	public String getOutput() {
		return output;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}
}
