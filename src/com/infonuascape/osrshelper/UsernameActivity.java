package com.infonuascape.osrshelper;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.infonuascape.osrshelper.adapters.StableArrayAdapter;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;

public class UsernameActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	public static final int HISCORES = 0;
	public static final int XP_TRACKER = 1;
	private OSRSHelperDataSource osrsHelperDataSource;
	private StableArrayAdapter adapter;
	private int type;

	public static void show(final Context context, int type){
		Intent i = new Intent(context, UsernameActivity.class);
		i.putExtra("type", type);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.username);

		type = getIntent().getIntExtra("type", HISCORES);

		findViewById(R.id.username_edit).clearFocus();

		findViewById(R.id.continue_btn).setOnClickListener(this);

		osrsHelperDataSource = new OSRSHelperDataSource(this);
	}

	public void onResume(){
		super.onResume();
		// Get all usernames
		osrsHelperDataSource.open();
		ArrayList<String> usernames = osrsHelperDataSource.getAllUsernames();
		osrsHelperDataSource.close();

		adapter = new StableArrayAdapter(this, R.layout.username_list_item, usernames);
		ListView list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
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

		if(type == HISCORES){
			HighScoreActivity.show(this, username);
		} else{
			XPTrackerActivity.show(this, username);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String username = adapter.getItem(position);
		closeActivity(username);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		final String username = adapter.getItem(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_username).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				osrsHelperDataSource.open();
				osrsHelperDataSource.deleteUsername(username);
				osrsHelperDataSource.close();
				adapter.remove(username);
			}
		}).setNegativeButton(R.string.cancel, null).create().show();
		return true;
	}
}
