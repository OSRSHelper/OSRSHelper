package com.infonuascape.osrshelper.grandexchange;

import android.content.Context;

import com.infonuascape.osrshelper.SearchItemActivity;

import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GEHelper {
	private Context context;

	public GEHelper(final Context context) {
		this.context = context;
	}

	public GESearchResults search(String itemName, int pageNum) {
		GEFetcher geFetcher = new GEFetcher(context);

		String output = geFetcher.search(itemName, pageNum);

		GESearchResults geSearchResults = new GESearchResults(output);

		return geSearchResults;
    }
}


