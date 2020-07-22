package com.infonuascape.osrshelper.widget.grandexchange;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.grandexchange.GrandExchangeDetailInfo;
import com.infonuascape.osrshelper.network.GrandExchangeDetailInfoApi;
import com.infonuascape.osrshelper.network.NetworkStack;
import com.infonuascape.osrshelper.utils.Logger;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final String TAG = "GrandExchangeWidgetRemoteViewsFactory";

    public final static String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private Context mContext;
    private int mAppWidgetId;
    private String itemId;
    private GrandExchangeDetailInfo grandExchangeDetailInfo;
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
        if(grandExchangeDetailInfo != null) {
            if(grandExchangeDetailInfo.current == null || TextUtils.isEmpty(grandExchangeDetailInfo.current.value)) {
                rv.setTextViewText(R.id.item_price, mContext.getResources().getString(R.string.unavailable_right_now));
            } else {
                rv.setTextViewText(R.id.item_price, grandExchangeDetailInfo.current.value);
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
            GrandExchangeDetailInfo newGrandExchangeDetailInfo = GrandExchangeDetailInfoApi.fetch(itemId);

            if(newGrandExchangeDetailInfo != null) {
                grandExchangeDetailInfo = newGrandExchangeDetailInfo;
            }
            isError = grandExchangeDetailInfo == null;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }
}