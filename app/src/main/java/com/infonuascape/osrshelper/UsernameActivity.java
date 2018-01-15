package com.infonuascape.osrshelper;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.infonuascape.osrshelper.adapters.StableArrayAdapter;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;
import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.widget.OSRSAppWidgetProvider;

public class UsernameActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private static final String EXTRA_TYPE = "EXTRA_TYPE";

	public static final int HISCORES = 0;
	public static final int RT_XP_TRACKER = 1;
	public static final int CML_XP_TRACKER = 4;
	public static final int CONFIGURATION = 2;
	public static final int COMBAT = 3;

	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private OSRSHelperDataSource osrsHelperDataSource;
	private StableArrayAdapter adapter;
	private int type;

	public static void show(final Context context, int type){
		Intent i = getIntent(context, type);
		context.startActivity(i);
	}

	public static Intent getIntent(Context context, int type) {
		Intent i = new Intent(context, UsernameActivity.class);
		i.putExtra(EXTRA_TYPE, type);
		return i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.username);

		type = getIntent().getIntExtra(EXTRA_TYPE, CONFIGURATION);

		checkIfConfiguration();

		findViewById(R.id.username_edit).clearFocus();
		findViewById(R.id.continue_btn).setOnClickListener(this);

        // if hiscore lookup, enable ironman selectors
        if (type == HISCORES) {
            for (int id : new int[]{R.id.ironman, R.id.ult_ironman, R.id.hc_ironman}) {
                View v = findViewById(id);
                v.setOnClickListener(this);
                v.setVisibility(View.VISIBLE);
            }
        }
		osrsHelperDataSource = new OSRSHelperDataSource(this);
	}

	private void checkIfConfiguration() {
		if(type == CONFIGURATION){
			setResult(RESULT_CANCELED);

			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			if (extras != null) {
				mAppWidgetId = extras.getInt(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
			}

			// If they gave us an intent without the widget id, just bail.
			if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
				finish();
			}
		}
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
				closeActivity(username, getSelectedAccountType());
			} else {
				Toast.makeText(this, R.string.username_error, Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.ironman) {
			findViewById(R.id.ult_ironman).setSelected(false);
			findViewById(R.id.hc_ironman).setSelected(false);
			v.setSelected(!v.isSelected());
        } else if (id == R.id.ult_ironman) {
            findViewById(R.id.ironman).setSelected(false);
		    findViewById(R.id.hc_ironman).setSelected(false);
            v.setSelected(!v.isSelected());
        } else if (id == R.id.hc_ironman) {
            findViewById(R.id.ironman).setSelected(false);
            findViewById(R.id.ult_ironman).setSelected(false);
            v.setSelected(!v.isSelected());
        }
	}

	private HiscoreHelper.AccountType getSelectedAccountType() {
        if (findViewById(R.id.ironman).isSelected())
            return HiscoreHelper.AccountType.IRONMAN;
        else if (findViewById(R.id.ult_ironman).isSelected())
            return HiscoreHelper.AccountType.ULTIMATE_IRONMAN;
        else if (findViewById(R.id.hc_ironman).isSelected())
            return HiscoreHelper.AccountType.HARDCORE_IRONMAN;
        else
            return HiscoreHelper.AccountType.REGULAR;
    }

	private void closeActivity(final String username, HiscoreHelper.AccountType accountType) {
		// Add the username to the db
		osrsHelperDataSource.open();
		osrsHelperDataSource.addUsername(username);
		osrsHelperDataSource.close();

		if(type == HISCORES){
			HighScoreActivity.show(this, username, accountType);
		} else if(type == CONFIGURATION){
			osrsHelperDataSource.open();
			osrsHelperDataSource.setUsernameForWidget(mAppWidgetId, username);
			osrsHelperDataSource.close();
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, OSRSAppWidgetProvider.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {mAppWidgetId});
			sendBroadcast(intent);
			finish();
		} else if (type == RT_XP_TRACKER) {
			RTXPTrackerActivity.show(this, username);
		} else if (type == CML_XP_TRACKER) {
			CMLXPTrackerActivity.show(this, username);
		} else if (type == COMBAT) {
			CombatCalcActivity.show(this, username);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String username = adapter.getItem(position);
		closeActivity(username, HiscoreHelper.AccountType.REGULAR);
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
