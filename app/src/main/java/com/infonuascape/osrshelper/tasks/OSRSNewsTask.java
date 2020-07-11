package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.infonuascape.osrshelper.listeners.NewsFetcherListener;
import com.infonuascape.osrshelper.models.OSRSNews;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.infonuascape.osrshelper.utils.rss.NewsRSSParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class OSRSNewsTask extends AsyncTask<Void, Void, Void> {
    private final static String API_ENDPOINT = "https://services.runescape.com/m=news/latest_news.rss?oldschool=true";
    private WeakReference<Context> context;
    private WeakReference<NewsFetcherListener> listener;
    private List<OSRSNews> osrsNews;

    public OSRSNewsTask(final Context context, final NewsFetcherListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = new WeakReference<>(listener);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        if(listener.get() != null) {
            listener.get().onNewsFetchingStarted();
        }

        HTTPRequest httpRequest = NetworkStack.getInstance(context.get()).performRequest(API_ENDPOINT, Request.Method.GET);
        if(httpRequest.getStatusCode() == HTTPRequest.StatusCode.FOUND) {
            NewsRSSParser parser = new NewsRSSParser();
            try {
                osrsNews = parser.parse(httpRequest.getOutput());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //If we reach here, we were not able to parse the rss feed
        if(listener.get() != null) {
            listener.get().onNewsFetchingError();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(listener.get() != null) {
            listener.get().onNewsFetched(osrsNews);
        }
    }
}
