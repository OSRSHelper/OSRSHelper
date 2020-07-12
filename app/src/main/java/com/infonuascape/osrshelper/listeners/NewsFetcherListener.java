package com.infonuascape.osrshelper.listeners;

import com.infonuascape.osrshelper.models.News;

import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public interface NewsFetcherListener {
    void onNewsFetchingError();
    void onNewsFetched(final List<News> news);
}
