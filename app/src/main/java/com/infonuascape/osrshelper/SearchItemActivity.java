package com.infonuascape.osrshelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.infonuascape.osrshelper.adapters.EndlessScrollListener;
import com.infonuascape.osrshelper.adapters.SearchAdapter;
import com.infonuascape.osrshelper.adapters.StableArrayAdapter;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;
import com.infonuascape.osrshelper.grandexchange.GEHelper;
import com.infonuascape.osrshelper.grandexchange.GESearchResults;
import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.grandexchange.Item;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import com.infonuascape.osrshelper.widget.OSRSAppWidgetProvider;

import java.util.ArrayList;

public class SearchItemActivity extends Activity implements OnItemClickListener, SearchView.OnQueryTextListener {
	private SearchAdapter adapter;
	private GEHelper geHelper;
	private SearchView editText;
	private PopulateSearchResults runnableSearch;
	private int pageNum;
	private String searchText;
	private boolean isRestartAdapter;
    private boolean isContinueToLoad;
	private ListView list;


	public static void show(final Context context){
		Intent i = new Intent(context, SearchItemActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.search_ge);

		editText = ((SearchView) findViewById(R.id.searchView));
		editText.setOnQueryTextListener(this);
		editText.setIconified(false);

		list = (ListView) findViewById(android.R.id.list);

		runnableSearch = new PopulateSearchResults();

		geHelper = new GEHelper();
	}

	public void onResume(){
		super.onResume();

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {
                if(isContinueToLoad) {
                    if (!runnableSearch.isCancelled()) {
                        runnableSearch.cancel(true);
                    }
                    runnableSearch = new PopulateSearchResults();
                    runnableSearch.execute(searchText);
                }
				return true;
			}
		});
		list.setOnItemClickListener(this);
	}

	@Override
	public boolean onQueryTextSubmit(String s) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String s) {
		if(s.length() > 0) {
			if(!runnableSearch.isCancelled()) {
				runnableSearch.cancel(true);
			}
			isRestartAdapter = true;
			isContinueToLoad = true;
			list.scrollTo(0, 0);
			runnableSearch = new PopulateSearchResults();
			searchText = s;
			pageNum = 1;
			runnableSearch.execute(searchText);
		} else if (adapter != null) {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}
		return false;
	}

	private class PopulateSearchResults extends AsyncTask<String, Void, GESearchResults> {

		@Override
		protected GESearchResults doInBackground(String... urls) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.progress_loading).setVisibility(View.VISIBLE);
                }
            });
			return geHelper.search(getApplicationContext(), urls[0], pageNum);
		}

		@Override
		protected void onPostExecute(final GESearchResults searchResults) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
                    if(searchResults.itemsSearch.size() == 0) {
                        isContinueToLoad = false;
                    }

                    findViewById(R.id.progress_loading).setVisibility(View.GONE);
					if(isRestartAdapter) {
						adapter = new SearchAdapter(SearchItemActivity.this, searchResults.itemsSearch);
						list.setAdapter(adapter);
                        isRestartAdapter = false;
						isContinueToLoad = true;
					} else {
						adapter.addAll(searchResults.itemsSearch);
						adapter.notifyDataSetChanged();
					}

					pageNum++;
				}
			});
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Item item = adapter.getItem(position);
	}
}
