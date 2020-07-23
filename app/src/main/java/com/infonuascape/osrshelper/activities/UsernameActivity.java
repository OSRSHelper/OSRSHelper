package com.infonuascape.osrshelper.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.UsernamesAdapter;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.widget.hiscores.OSRSAppWidgetProvider;

import java.util.ArrayList;

public class UsernameActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private static final String TAG = "WidgetUsernameActivity";

	private static final String EXTRA_ACTION = "EXTRA_ACTION";
	private static final int ACTION_WIDGET = 0;
	private static final int ACTION_PROFILE = 1;

	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private UsernamesAdapter adapter;
	private int action;

	public static Intent getIntent(Context context, final int appWidgetId) {
		Intent i = new Intent(context, UsernameActivity.class);
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		i.putExtra(EXTRA_ACTION, ACTION_WIDGET);
		return i;
	}

	public static void showForProfileForResult(final Activity activity, final int requestCode) {
		Intent i = new Intent(activity, UsernameActivity.class);
		i.putExtra(EXTRA_ACTION, ACTION_PROFILE);
		activity.startActivityForResult(i, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.username);

		action = getIntent().getIntExtra(EXTRA_ACTION, ACTION_WIDGET);
		Logger.add(TAG, ": onCreate: action=" + action);
		initWidgetId();

		findViewById(R.id.username_edit).clearFocus();
		findViewById(R.id.continue_btn).setOnClickListener(this);
		((TextView) findViewById(R.id.page_name)).setText(action == ACTION_WIDGET ? R.string.username_widget : R.string.username_profile);

        // if hiscore lookup, enable ironman selectors
		for (int id : new int[]{R.id.ironman, R.id.ult_ironman, R.id.hc_ironman}) {
			View v = findViewById(id);
			v.setOnClickListener(this);
			v.setVisibility(View.VISIBLE);
		}
	}

	private void initWidgetId() {
		if(action == ACTION_WIDGET && getIntent() != null){
			mAppWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

			// If they gave us an intent without the widget id, just bail.
			if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
				setResult(RESULT_CANCELED);
				finish();
			}
		}
	}

	public void onResume(){
		super.onResume();
		// Get all usernames
		ArrayList<Account> accounts = DBController.getAllAccounts(this);

		adapter = new UsernamesAdapter(this, accounts);
		ListView list = findViewById(android.R.id.list);
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
				Account account = new Account(username, getSelectedAccountType());
				closeActivity(account);
			} else if (action == ACTION_PROFILE) {
				closeActivity(null);
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

	private AccountType getSelectedAccountType() {
        if (findViewById(R.id.ironman).isSelected())
            return AccountType.IRONMAN;
        else if (findViewById(R.id.ult_ironman).isSelected())
            return AccountType.ULTIMATE_IRONMAN;
        else if (findViewById(R.id.hc_ironman).isSelected())
            return AccountType.HARDCORE_IRONMAN;
        else
            return AccountType.REGULAR;
    }

	private void closeActivity(final Account account) {
		if (account != null) {
			DBController.addOrUpdateAccount(this, account);
		}

		if (action == ACTION_WIDGET) {
			DBController.setAccountForWidget(this, mAppWidgetId, account);
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, OSRSAppWidgetProvider.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
			sendBroadcast(intent);
		} else if (action == ACTION_PROFILE) {
			DBController.setProfileAccount(this, account);
			setResult(RESULT_OK);
		}
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Account account = adapter.getItem(position);
		closeActivity(account);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Account account = adapter.getItem(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_username).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBController.deleteAccount(getApplicationContext(), account);
				adapter.remove(account);
			}
		}).setNegativeButton(R.string.cancel, null).create().show();
		return true;
	}
}
