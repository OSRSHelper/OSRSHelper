package com.infonuascape.osrshelper.widget.grandexchange;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.grandexchange.RSBuddyPrice;
import com.infonuascape.osrshelper.network.NetworkStack;
import com.infonuascape.osrshelper.network.RSBuddyPriceApi;
import com.infonuascape.osrshelper.utils.Logger;

import java.text.NumberFormat;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final String TAG = "GrandExchangeWidgetRemoteViewsFactory";

    public final static String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private Context mContext;
    private int mAppWidgetId;
    private String itemId;
    private RSBuddyPrice rsBuddyPrice;
    private boolean isError;

    public GrandExchangeWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        itemId = intent.getStringExtra(EXTRA_ITEM_ID);
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Logger.add(TAG, ": onCreate");
        isError = false;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_grand_exchange_price);


        // set value into textview
        if(rsBuddyPrice != null) {
            if(getPrice(rsBuddyPrice) == 0) {
                rv.setTextViewText(R.id.item_price, mContext.getResources().getString(R.string.unavailable_right_now));
            } else {
                rv.setTextViewText(R.id.item_price, NumberFormat.getInstance().format(getPrice(rsBuddyPrice)) + "gp");
            }
        } else if(isError) {
            rv.setTextViewText(R.id.item_price, mContext.getResources().getString(R.string.error_when_fetching));
        }

        // Return the remote views object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        Logger.add(TAG, ": onDataSetChanged");
        try {
            if (NetworkStack.getInstance() == null) {
                NetworkStack.init(mContext.getApplicationContext());
            }
            RSBuddyPrice newRsBuddyPrice = RSBuddyPriceApi.fetch(itemId);

            if(newRsBuddyPrice != null) {
                rsBuddyPrice = newRsBuddyPrice;
            }
            isError = rsBuddyPrice == null;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    private long getPrice(final RSBuddyPrice rsBuddyPrice) {
        if(rsBuddyPrice == null) {
            return 0;
        }

        if(rsBuddyPrice.overall > 0) {
            return rsBuddyPrice.overall;
        }

        if(rsBuddyPrice.selling > 0) {
            return rsBuddyPrice.selling;
        }

        if(rsBuddyPrice.buying > 0) {
            return rsBuddyPrice.buying;
        }

        return 0;
    }
}