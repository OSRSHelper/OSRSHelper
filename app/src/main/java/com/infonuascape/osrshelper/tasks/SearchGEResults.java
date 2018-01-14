package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.grandexchange.GEFetcher;
import com.infonuascape.osrshelper.grandexchange.GEHelper;
import com.infonuascape.osrshelper.grandexchange.GESearchResults;
import com.infonuascape.osrshelper.listeners.SearchGEResultsListener;

/**
 * Created by marc_ on 2018-01-14.
 */

public class SearchGEResults extends AsyncTask<Void, Void, GESearchResults> {
    private GEHelper geHelper;
    private SearchGEResultsListener listener;
    private int pageNum;
    private String searchTerm;

    public SearchGEResults(final Context context, final SearchGEResultsListener listener, final int pageNum, final String searchTerm) {
        geHelper = new GEHelper(context);
        this.listener = listener;
        this.pageNum = pageNum;
        this.searchTerm = searchTerm;
    }

    @Override
    protected GESearchResults doInBackground(Void... params) {
        return geHelper.search(searchTerm, pageNum);
    }

    @Override
    protected void onPostExecute(final GESearchResults searchResults) {
        if(searchResults != null && searchResults.itemsSearch != null) {
            if(listener != null) {
                listener.onSearchResults(searchTerm, pageNum, searchResults.itemsSearch);
            }
        }
    }
}