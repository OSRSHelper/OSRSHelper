package com.infonuascape.osrshelper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.infonuascape.osrshelper.adapters.StableArrayAdapter;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;

public class UsernameActivity extends Activity implements OnClickListener, OnItemClickListener {
	private OSRSHelperDataSource osrsHelperDataSource;
	private StableArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.username);

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String username = adapter.getItem(position);
		closeActivity(username);
	}
}
