package com.infonuascape.osrshelper.tasks;

import android.os.AsyncTask;

import com.infonuascape.osrshelper.listeners.NewsFetcherListener;
import com.infonuascape.osrshelper.models.News;
import com.infonuascape.osrshelper.network.NewsApi;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class OSRSNewsTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<NewsFetcherListener> listener;
    private List<News> news;
    private int pageNum;

    public OSRSNewsTask(final NewsFetcherListener listener, final int pageNum) {
        this.listener = new WeakReference<>(listener);
        this.pageNum = pageNum;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        news = NewsApi.fetch(pageNum);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (listener.get() != null) {
            if (news.isEmpty() && pageNum == 1) {
                listener.get().onNewsFetchingError();
            } else {
                listener.get().onNewsFetched(news);
            }
        }
    }
}
