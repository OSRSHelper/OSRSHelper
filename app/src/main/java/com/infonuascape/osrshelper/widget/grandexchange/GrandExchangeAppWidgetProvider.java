package com.infonuascape.osrshelper.widget.grandexchange;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.WidgetGrandExchangeSearchActivity;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.listeners.GEDetailListener;
import com.infonuascape.osrshelper.models.grandexchange.GEItemInfo;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.tasks.GrandExchangeWidgetTask;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Arrays;

public class GrandExchangeAppWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "GrandExchangeAppWidget";

	public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		Log.i(TAG,  "Updating widgets " + appWidgetIds);

		for (int i = 0; i < N; i++) {
			final int appWidgetId = appWidgetIds[i];
			refreshWidget(context, appWidgetManager, appWidgetId);
		}
	}

	private void refreshWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
		Intent intentSync = new Intent(context, GrandExchangeAppWidgetProvider.class);
		intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intentSync.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent pendingSync = PendingIntent.getBroadcast(context, appWidgetId, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);

		final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grand_exchange_layout);
		views.setOnClickPendingIntent(R.id.update_btn, pendingSync);

		//Username
		final Item item = DBController.getGrandExchangeByWidgetId(context, appWidgetId);
		if(item != null) {
			views.setTextViewText(R.id.item_name, item.name);
			views.setTextViewText(R.id.item_price, "");
			views.setViewVisibility(R.id.item_progress_bar, View.VISIBLE);
			views.setViewVisibility(R.id.item_member, item.members ? View.VISIBLE : View.GONE);

			AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.item_image, views, appWidgetId) {
				@Override
				public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
					super.onResourceReady(resource, transition);
				}
			};

			Glide
					.with(context.getApplicationContext())
					.asBitmap()
					.load(item.iconLarge)
					.into(appWidgetTarget);

			new GrandExchangeWidgetTask(context, new GEDetailListener() {
				@Override
				public void onInfoFetched(DataPoint[] datapoints, DataPoint[] averages, GEItemInfo itemInfo) {
					Log.i(TAG, "onInfoFetched: price=" + itemInfo.current.value);
					views.setViewVisibility(R.id.item_progress_bar, View.GONE);
					views.setTextViewText(R.id.item_price, itemInfo.current.value + " (" + itemInfo.today.value + ")");
					appWidgetManager.updateAppWidget(appWidgetId, views);
				}
			}, item.id).execute();
		}

		Log.i(TAG, "appWidgetId=" + appWidgetId);
		//Config
		Intent configIntent = WidgetGrandExchangeSearchActivity.getIntent(context, appWidgetId);
		configIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		configIntent.setAction(ACTION_WIDGET_CONFIGURE + Integer.toString(appWidgetId));
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
		views.setOnClickPendingIntent(R.id.change_item_btn, configPendingIntent);

		// Tell the AppWidgetManager to perform an update on the current app
		// widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), GrandExchangeAppWidgetProvider.class.getName());

		int[] appWidgetIds;
		int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		Log.i(TAG, "onReceive: appWidgetId=" + appWidgetId);
		if(appWidgetId != -1) {
			appWidgetIds = new int[]{appWidgetId};
		} else {
			appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		}

		onUpdate(context, appWidgetManager, appWidgetIds);
	}
}