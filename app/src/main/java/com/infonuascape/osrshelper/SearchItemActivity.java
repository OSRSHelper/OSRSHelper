package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.infonuascape.osrshelper.adapters.EndlessScrollListener;
import com.infonuascape.osrshelper.adapters.SearchAdapter;
import com.infonuascape.osrshelper.listeners.SearchGEResultsListener;
import com.infonuascape.osrshelper.tasks.SearchGEResultsTask;
import com.infonuascape.osrshelper.models.grandexchange.Item;

import java.util.ArrayList;

public class SearchItemActivity extends Activity implements OnItemClickListener, SearchView.OnQueryTextListener, SearchGEResultsListener {
	private SearchAdapter adapter;
	private SearchView searchView;
	private String searchText;
    private boolean isContinueToLoad;
	private ListView list;
	private SearchGEResultsTask runnableSearch;

	public static void show(final Context context){
		Intent i = new Intent(context, SearchItemActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.search_ge);

		searchView = findViewById(R.id.searchView);
		searchView.setOnQueryTextListener(this);
		searchView.setIconified(false);

		list = findViewById(android.R.id.list);
		list.setOnScrollListener(endlessScrollListener);
		list.setOnItemClickListener(this);
	}

	@Override
	public boolean onQueryTextSubmit(String searchTerm) {
		search(searchTerm);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String searchTerm) {
		search(searchTerm);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Item item = adapter.getItem(position);
		//ToDo add activity to view detailed results for GE
	}

	@Override
	public void onSearchResults(final String searchTerm, final int pageNum, ArrayList<Item> searchResults) {
		if(searchResults.size() == 0) {
			isContinueToLoad = false;
		}

		findViewById(R.id.progress_loading).setVisibility(View.GONE);

		if(TextUtils.equals(searchTerm, searchText)) {
			if (adapter == null) {
				adapter = new SearchAdapter(this, searchResults);
				list.setAdapter(adapter);
			} else {
				adapter.addAll(searchResults);
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void search(final String searchText) {
		this.searchText = searchText;

		if (adapter != null) {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		if(searchText.length() > 0) {
			isContinueToLoad = true;
			startSearchTask(1);
		}
	}

	private void startSearchTask(int page) {
		if (runnableSearch != null && !runnableSearch.isCancelled()) {
			runnableSearch.cancel(true);
		}
		runnableSearch = new SearchGEResultsTask(this, this, page, searchText);
		runnableSearch.execute();
		findViewById(R.id.progress_loading).setVisibility(View.VISIBLE);
	}

	private EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
		@Override
		public boolean onLoadMore(int page, int totalItemsCount) {
			if(!TextUtils.isEmpty(searchText)) {
				startSearchTask(page);
			}
			return isContinueToLoad;
		}
	};
}
