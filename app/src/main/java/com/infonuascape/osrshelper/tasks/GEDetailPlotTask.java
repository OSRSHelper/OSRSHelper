package com.infonuascape.osrshelper.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.infonuascape.osrshelper.fetchers.grandexchange.GEDetailInfoFetcher;
import com.infonuascape.osrshelper.fetchers.grandexchange.GEDetailInfoResults;
import com.infonuascape.osrshelper.fetchers.grandexchange.GEDetailPlotFetcher;
import com.infonuascape.osrshelper.fetchers.grandexchange.GEDetailPlotResults;
import com.infonuascape.osrshelper.listeners.GEDetailListener;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GEDetailPlotTask extends AsyncTask<Void, Void, Void> {
    private GEDetailListener listener;

    private GEDetailPlotFetcher geDetailPlotFetcher;
    private GEDetailPlotResults detailPlotResults;

    private GEDetailInfoFetcher geDetailInfoFetcher;
    private GEDetailInfoResults detailInfoResults;
    private String itemId;

    public GEDetailPlotTask(final Context context, final GEDetailListener listener, final String itemId) {
        geDetailPlotFetcher = new GEDetailPlotFetcher(context);
        geDetailInfoFetcher = new GEDetailInfoFetcher(context);
        this.listener = listener;
        this.itemId = itemId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String output = geDetailPlotFetcher.fetch(itemId);
        detailPlotResults = new GEDetailPlotResults(output);

        output = geDetailInfoFetcher.fetch(itemId);
        detailInfoResults = new GEDetailInfoResults(output);

        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        if(detailPlotResults != null && detailPlotResults.datapoints != null
                && detailInfoResults != null && detailInfoResults.itemInfo != null) {
            if(listener != null) {
                final DataPoint[] dataPoints = detailPlotResults.datapoints.toArray(new DataPoint[detailPlotResults.datapoints.size()]);
                final DataPoint[] averages = detailPlotResults.averages.toArray(new DataPoint[detailPlotResults.averages.size()]);
                listener.onInfoFetched(dataPoints, averages, detailInfoResults.itemInfo);
            }
        }
    }
}