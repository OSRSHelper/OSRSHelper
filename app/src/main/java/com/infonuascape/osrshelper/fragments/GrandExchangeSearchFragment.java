package com.infonuascape.osrshelper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.SearchAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.listeners.SearchGEResultsListener;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.tasks.SearchGEResultsTask;
import com.infonuascape.osrshelper.utils.Utils;

import java.util.ArrayList;

public class GrandExchangeSearchFragment extends OSRSFragment implements OnItemClickListener, SearchView.OnQueryTextListener, SearchGEResultsListener {
	private SearchAdapter adapter;
	private SearchView searchView;
	private String searchText;
	private ListView list;

	public static GrandExchangeSearchFragment newInstance(){
		GrandExchangeSearchFragment fragment = new GrandExchangeSearchFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.search_ge, null);

		searchView = view.findViewById(R.id.searchView);
		searchView.setOnQueryTextListener(this);
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint(getResources().getString(R.string.grand_exchange_lookup_hint));

		list = view.findViewById(android.R.id.list);
		list.setOnItemClickListener(this);

		return view;
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
		Utils.hideKeyboard(getActivity());
		final Item item = adapter.getItem(position);
		MainFragmentController.getInstance().showFragment(GrandExchangeDetailFragment.newInstance(item.id));
	}

	@Override
	public void onSearchResults(final String searchTerm, ArrayList<Item> searchResults) {
		getView().findViewById(R.id.progress_loading).setVisibility(View.GONE);

		if(TextUtils.equals(searchTerm, searchText)) {
			adapter = new SearchAdapter(getContext(), searchResults);
			list.setAdapter(adapter);
		}
	}

	private void search(final String searchText) {
		this.searchText = searchText;

		if (adapter != null) {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		if(searchText != null && searchText.length() >= 3) {
			startSearchTask();
		}
	}

	private void startSearchTask() {
		killAsyncTaskIfStillRunning();
		asyncTask = new SearchGEResultsTask(getContext(), this, searchText);
		asyncTask.execute();
		getView().findViewById(R.id.progress_loading).setVisibility(View.VISIBLE);
	}
}
