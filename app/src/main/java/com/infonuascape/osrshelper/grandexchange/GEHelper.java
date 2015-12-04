package com.infonuascape.osrshelper.grandexchange;

import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GEHelper {
    public GESearchResults search(String itemName, int pageNum) {
		GEFetcher geFetcher = new GEFetcher();

		String output = geFetcher.search(itemName, pageNum);

		GESearchResults geSearchResults = new GESearchResults(output);

		return geSearchResults;
    }
}


