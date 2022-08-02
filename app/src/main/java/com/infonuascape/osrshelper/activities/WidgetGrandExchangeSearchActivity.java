package com.infonuascape.osrshelper.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.GrandExchangeSearchAdapter;
import com.infonuascape.osrshelper.db.OSRSDatabaseFacade;
import com.infonuascape.osrshelper.listeners.SearchGEResultsListener;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.tasks.SearchGEResultsTask;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.widget.grandexchange.GrandExchangeAppWidgetProvider;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

public class WidgetGrandExchangeSearchActivity extends Activity implements OnItemClickListener, SearchView.OnQueryTextListener, SearchGEResultsListener, View.OnFocusChangeListener {
	private static final String TAG = "WidgetGrandExchangeSear";

	protected AsyncTask<Void, Void, Void> asyncTask;

	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private GrandExchangeSearchAdapter adapter;
	private SearchView searchView;
	private String searchText;
	private ListView list;
	private Handler handler;

	public static Intent getIntent(Context context, final int appWidgetId) {
		Intent i = new Intent(context, WidgetGrandExchangeSearchActivity.class);
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		return i;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_ge);

		handler = new Handler();

		searchView = findViewById(R.id.searchView);
		searchView.setOnQueryTextListener(this);
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint(getResources().getString(R.string.grand_exchange_lookup_hint));
		searchView.setOnQueryTextFocusChangeListener(this);
		searchView.requestFocus();

		list = findViewById(android.R.id.list);
		list.setOnItemClickListener(this);

		initWidgetId();
	}


	@Override
	public void onStart() {
		super.onStart();
		search();
	}

	private void initWidgetId() {
		if(getIntent() != null){
			mAppWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

			// If they gave us an intent without the widget id, just bail.
			if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
				setResult(RESULT_CANCELED);
				finish();
			}
		}
	}


	@Override
	public boolean onQueryTextSubmit(String searchTerm) {
		this.searchText = searchTerm;
		handler.removeCallbacks(waitForSearchRunnable);
		handler.postDelayed(waitForSearchRunnable, 300);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String searchTerm) {
		this.searchText = searchTerm;
		handler.removeCallbacks(waitForSearchRunnable);
		handler.postDelayed(waitForSearchRunnable, 300);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Utils.hideKeyboard(this);
		final Item item = adapter.getItem(position);
		OSRSApp.getInstance().getDatabaseFacade().addGrandExchangeItem(this, item);
		OSRSApp.getInstance().getDatabaseFacade().setGrandExchangeWidgetIdToItem(this, item.id, String.valueOf(mAppWidgetId));
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, GrandExchangeAppWidgetProvider.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {mAppWidgetId});
		sendBroadcast(intent);
		finish();
	}

	@Override
	public void onSearchResults(final String searchTerm, List<Item> searchResults) {
		findViewById(R.id.progress_loading).setVisibility(View.GONE);

		if(TextUtils.equals(searchTerm, searchText)) {
			adapter = new GrandExchangeSearchAdapter(this, searchResults);
			list.setAdapter(adapter);
		}
	}

	private Runnable waitForSearchRunnable = new Runnable() {
		@Override
		public void run() {
			search();
		}
	};

	private void search() {
		if (adapter != null) {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		if (searchText != null && searchText.length() >= 3) {
			startSearchTask();
		} else {
			adapter = new GrandExchangeSearchAdapter(this, OSRSApp.getInstance().getDatabaseFacade().getGrandExchangeItems(this));
			list.setAdapter(adapter);
		}
	}

	private void startSearchTask() {
		killAsyncTaskIfStillRunning();
		asyncTask = new SearchGEResultsTask(this, searchText);
		asyncTask.execute();
		findViewById(R.id.progress_loading).setVisibility(View.VISIBLE);
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if(hasFocus) {
			Utils.showKeyboard(this);
		}
	}

	protected void killAsyncTaskIfStillRunning() {
		if(asyncTask != null) {
			if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
				Logger.add(TAG, ": killAsyncTaskIfStillRunning: running=true");
				asyncTask.cancel(true);
				asyncTask = null;
			}
		}
	}
}
