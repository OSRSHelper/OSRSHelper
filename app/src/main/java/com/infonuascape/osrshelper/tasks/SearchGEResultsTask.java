package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.grandexchange.GEFetcher;
import com.infonuascape.osrshelper.grandexchange.GESearchResults;
import com.infonuascape.osrshelper.listeners.SearchGEResultsListener;

/**
 * Created by marc_ on 2018-01-14.
 */

public class SearchGEResultsTask extends AsyncTask<Void, Void, Void> {
    private GEFetcher geFetcher;
    private SearchGEResultsListener listener;
    private String searchTerm;
    private GESearchResults searchResults;

    public SearchGEResultsTask(final Context context, final SearchGEResultsListener listener, final String searchTerm) {
        geFetcher = new GEFetcher(context);
        this.listener = listener;
        this.searchTerm = searchTerm;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String output = geFetcher.search(searchTerm);
        searchResults = new GESearchResults(output);

        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        if(searchResults != null && searchResults.itemsSearch != null) {
            if(listener != null) {
                listener.onSearchResults(searchTerm, searchResults.itemsSearch);
            }
        }
    }
}