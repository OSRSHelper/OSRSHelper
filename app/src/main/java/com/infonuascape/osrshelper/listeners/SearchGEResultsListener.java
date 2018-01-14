package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.utils.grandexchange.Item;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-01-14.
 */

public interface SearchGEResultsListener {
    void onSearchResults(final String searchTerm, final int pageNum, final ArrayList<Item> results);
}
