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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.infonuascape.osrshelper.adapters.SearchAdapter;
import com.infonuascape.osrshelper.adapters.StableArrayAdapter;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;
import com.infonuascape.osrshelper.grandexchange.GEHelper;
import com.infonuascape.osrshelper.grandexchange.GESearchResults;
import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.grandexchange.Item;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import com.infonuascape.osrshelper.widget.OSRSAppWidgetProvider;

import java.util.ArrayList;

public class SearchItemActivity extends Activity implements OnItemClickListener, TextWatcher {
	private SearchAdapter adapter;
	private GEHelper geHelper;
	private EditText editText;
	private PopulateSearchResults runnableSearch;


	public static void show(final Context context){
		Intent i = new Intent(context, SearchItemActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.search_ge);

		editText = ((EditText) findViewById(R.id.search_edit));
		editText.addTextChangedListener(this);
		editText.clearFocus();

		runnableSearch = new PopulateSearchResults();

		geHelper = new GEHelper();
	}

	public void onResume(){
		super.onResume();

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(this);
	}

	private class PopulateSearchResults extends AsyncTask<String, Void, GESearchResults> {

		@Override
		protected GESearchResults doInBackground(String... urls) {
			return geHelper.search(urls[0], 0);
		}

		@Override
		protected void onPostExecute(final GESearchResults searchResults) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter = new SearchAdapter(SearchItemActivity.this, searchResults.getItemsSearch());
					ListView list = (ListView) findViewById(android.R.id.list);
					list.setAdapter(adapter);
				}
			});
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Item item = adapter.getItem(position);
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void afterTextChanged(Editable editable) {
		if(editText.getText().length() > 2) {
			if(!runnableSearch.isCancelled()) {
				runnableSearch.cancel(true);
			}
			runnableSearch = new PopulateSearchResults();
			runnableSearch.execute(editText.getText().toString());
		}
	}
}
