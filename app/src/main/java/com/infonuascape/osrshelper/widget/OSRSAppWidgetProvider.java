package com.infonuascape.osrshelper.widget;

import java.util.Arrays;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.WidgetUsernameActivity;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class OSRSAppWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "OSRSAppWidgetProvider";

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
			final Account account = DBController.getAccountForWidget(context, appWidgetId);
			if(account != null) {
				views.setTextViewText(R.id.username, account.username);
				views.setViewVisibility(R.id.icon, View.VISIBLE);
				switch (account.type) {
					case REGULAR:
						views.setViewVisibility(R.id.icon, View.GONE);
						break;
					case IRONMAN:
						views.setImageViewResource(R.id.icon, R.drawable.ironman);
						break;
					case ULTIMATE_IRONMAN:
						views.setImageViewResource(R.id.icon, R.drawable.ult_ironman);
						break;
					case HARDCORE_IRONMAN:
						views.setImageViewResource(R.id.icon, R.drawable.hc_ironman);
						break;
				}
			}

			Log.i(TAG, "appWidgetId=" + appWidgetId);
			//Config
			Intent configIntent = WidgetUsernameActivity.getIntent(context, appWidgetId);
	        configIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	        configIntent.setAction(ACTION_WIDGET_CONFIGURE + Integer.toString(appWidgetId));
			PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
			views.setOnClickPendingIntent(R.id.username_btn, configPendingIntent);

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