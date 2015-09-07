package com.infonuascape.osrshelper.widget;

import java.util.Arrays;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;

public class OSRSAppWidgetProvider extends AppWidgetProvider {
	public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		Log.i("OSRSAppWidgetProvider",  "Updating widgets " + Arrays.asList(appWidgetIds));

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ExampleActivity
			Intent intentSync = new Intent(context, OSRSAppWidgetProvider.class);
			intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			PendingIntent pendingSync = PendingIntent.getBroadcast(context, 0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT); //You need to specify a proper flag for the intent. Or else the intent will become deleted.


			Intent intentService = new Intent(context, OSRSWidgetService.class);
			// Add the app widget ID to the intent extras.
			intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intentService.setData(Uri.parse(intentService.toUri(Intent.URI_INTENT_SCHEME)));

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			views.setOnClickPendingIntent(R.id.update_btn, pendingSync);
			
			//Username
			OSRSHelperDataSource osrsHelperDataSource = new OSRSHelperDataSource(context);
			osrsHelperDataSource.open();
			final String username = osrsHelperDataSource.getUsernameForWidget(appWidgetId);
			osrsHelperDataSource.close();
			views.setTextViewText(R.id.username, username);
			
			//Config
			Intent configIntent = new Intent(context, OSRSWidgetConfigurationActivity.class);
	        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	        PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
	        views.setOnClickPendingIntent(R.id.username_btn, configPendingIntent);
	        configIntent.setAction(ACTION_WIDGET_CONFIGURE + Integer.toString(appWidgetId));

			views.setRemoteAdapter(R.id.grid_view, intentService);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.grid_view);

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), OSRSAppWidgetProvider.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

		appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.grid_view);
		
		onUpdate(context, appWidgetManager, appWidgetIds);
	}
}