package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.OSRSNews;

import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public interface NewsFetcherListener {
    void onNewsFetchingStarted();
    void onNewsFetchingError();
    void onNewsFetched(final List<OSRSNews> news);
}
