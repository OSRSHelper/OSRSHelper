package com.infonuascape.osrshelper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.infonuascape.osrshelper.adapters.StableArrayAdapter;

public class ZybezSearchActivity extends Activity implements OnItemClickListener {
	private StableArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.zybez_search);

		((EditText) findViewById(R.id.edit_search)).addTextChangedListener(new TextWatch());

		modifyListView("mind rune");
	}

	private void modifyListView(String item) {
		ListView list = (ListView) findViewById(android.R.id.list);

		ArrayList<String> items = new ArrayList<String>();
		items.add(item);

		adapter = new StableArrayAdapter(this, R.layout.username_list_item, items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	private void closeActivity(final String item) {

		Intent data = new Intent();
		data.putExtra("item", item);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String username = adapter.getItem(position);
		closeActivity(username);
	}

	private class TextWatch implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (s.length() >= 4) {
				modifyListView(s.toString());
			}
		}

	}
}
