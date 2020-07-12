package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.SearchGEResultsListener;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.network.GrandExchangeSearchApi;

import java.util.List;

/**
 * Created by marc_ on 2018-01-14.
 */

public class SearchGEResultsTask extends AsyncTask<Void, Void, Void> {
    private SearchGEResultsListener listener;
    private String searchTerm;
    private List<Item> searchResults;

    public SearchGEResultsTask(final SearchGEResultsListener listener, final String searchTerm) {
        this.listener = listener;
        this.searchTerm = searchTerm;
    }

    @Override
    protected Void doInBackground(Void... params) {
        searchResults = GrandExchangeSearchApi.fetch(searchTerm);

        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        if(searchResults != null) {
            if(listener != null) {
                listener.onSearchResults(searchTerm, searchResults);
            }
        }
    }
}