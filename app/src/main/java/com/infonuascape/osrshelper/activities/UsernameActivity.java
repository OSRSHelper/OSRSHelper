package com.infonuascape.osrshelper.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.UsernamesAdapter;
import com.infonuascape.osrshelper.db.OSRSDatabaseFacade;
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
		findViewById(R.id.runelite_tutorial_btn).setOnClickListener(this);
		((TextView) findViewById(R.id.page_name)).setText(action == ACTION_WIDGET ? R.string.username_widget : R.string.username_profile);
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
		ArrayList<Account> accounts = OSRSApp.getInstance().getDatabaseFacade().getAllAccounts(this);

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
				Account account = new Account(username);
				closeActivity(account);
			} else if (action == ACTION_PROFILE) {
				closeActivity(null);
			} else {
				Toast.makeText(this, R.string.username_error, Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.runelite_tutorial_btn) {
			showRuneLiteTutorial();
		}
	}

	private void showRuneLiteTutorial() {
		View view = LayoutInflater.from(this).inflate(R.layout.runelite_tutorial, null);
		ImageView tutorialGif = view.findViewById(R.id.runelite_tutorial_image);
		Glide.with(this).asGif().load(R.drawable.runelite_tutorial).into(tutorialGif);
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
				.setTitle(R.string.username_tutorial_runelite_title)
				.setMessage(R.string.username_tutorial_runelite_description)
				.setView(view);
		builder.show();
	}

	private void closeActivity(final Account account) {
		if (account != null) {
			OSRSApp.getInstance().getDatabaseFacade().addOrUpdateAccount(this, account);
		}

		if (action == ACTION_WIDGET) {
			OSRSApp.getInstance().getDatabaseFacade().setAccountForWidget(this, mAppWidgetId, account);
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, OSRSAppWidgetProvider.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
			sendBroadcast(intent);
		} else if (action == ACTION_PROFILE) {
			OSRSApp.getInstance().getDatabaseFacade().setProfileAccount(this, account);
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
				OSRSApp.getInstance().getDatabaseFacade().deleteAccount(getApplicationContext(), account);
				adapter.remove(account);
			}
		}).setNegativeButton(R.string.cancel, null).create().show();
		return true;
	}
}
