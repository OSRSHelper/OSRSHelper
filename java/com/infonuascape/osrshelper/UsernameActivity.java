package com.infonuascape.osrshelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class UsernameActivity extends Activity implements OnClickListener, OnItemClickListener {
	private OSRSHelperDataSource osrsHelperDataSource;
	private StableArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_username);

		findViewById(R.id.username_edit).clearFocus();

		findViewById(R.id.continue_btn).setOnClickListener(this);

		ListView list = (ListView) findViewById(android.R.id.list);
		osrsHelperDataSource = new OSRSHelperDataSource(this);

		// Get all usernames
		osrsHelperDataSource.open();
		ArrayList<String> usernames = osrsHelperDataSource.getAllUsernames();
		osrsHelperDataSource.close();

		adapter = new StableArrayAdapter(this, R.layout.username_list_item, usernames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.continue_btn) {
			String username = ((EditText) findViewById(R.id.username_edit)).getText().toString();
			if (!username.isEmpty()) {
				closeActivity(username);
			} else {
				Toast.makeText(this, R.string.username_error, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void closeActivity(final String username) {
		// Add the username to the db
		osrsHelperDataSource.open();
		osrsHelperDataSource.addUsername(username);
		osrsHelperDataSource.close();

		Intent data = new Intent();
		data.putExtra("username", username);
		setResult(RESULT_OK, data);
		finish();
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String username = adapter.getItem(position);
		closeActivity(username);
	}
}
