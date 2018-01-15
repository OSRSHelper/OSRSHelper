package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.CMLTrackerFetcherTask;
import com.infonuascape.osrshelper.utils.tablesfiller.CMLTrackerTableFiller;

public class CMLXPTrackerActivity extends Activity implements OnItemSelectedListener, OnClickListener, CompoundButton.OnCheckedChangeListener, TrackerFetcherListener {
	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private Account account;
	private TextView header;
	private Spinner spinner;
	private CheckBox virtualLevelsCB;
	private PlayerSkills playerSkills;
	private TableLayout tableLayout;
	private CMLTrackerTableFiller tableFiller;

	public static void show(final Context context, final Account account) {
		Intent intent = new Intent(context, CMLXPTrackerActivity.class);
		intent.putExtra(EXTRA_ACCOUNT, account);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.xptracker);

		account = (Account) getIntent().getSerializableExtra(EXTRA_ACCOUNT);

		tableLayout = findViewById(R.id.table_tracking);
		tableFiller = new CMLTrackerTableFiller(this, tableLayout);

		header = (TextView) findViewById(R.id.header);
		header.setText(getString(R.string.loading_tracking, account.username));

		virtualLevelsCB = (CheckBox) findViewById(R.id.cb_virtual_levels);
		virtualLevelsCB.setOnCheckedChangeListener(this);
		virtualLevelsCB.setChecked(PreferencesController.getBooleanPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false));
		virtualLevelsCB.setVisibility(View.GONE);

		spinner = (Spinner) findViewById(R.id.time_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_array,
				R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(3);
		spinner.setOnItemSelectedListener(this);

		findViewById(R.id.update).setOnClickListener(this);
	}

	private void changeHeaderText(final String text, final int progressBarVisibility) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				findViewById(R.id.progressbar).setVisibility(progressBarVisibility);
				header.setText(text);
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		PreferencesController.setPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, isChecked);
		if(playerSkills != null) {
			populateTable();
		}
	}



	private void populateTable() {
		changeHeaderText(getString(R.string.showing_tracking, account.username), View.GONE);

		if (playerSkills.sinceWhen != null) {
			((TextView) findViewById(R.id.track_metadata)).setText(getString(R.string.tracking_since,
					playerSkills.sinceWhen));
		} else if (playerSkills.lastUpdate != null) {
			((TextView) findViewById(R.id.track_metadata)).setText(getString(R.string.last_update,
					playerSkills.lastUpdate));
		} else {
			((TextView) findViewById(R.id.track_metadata)).setText(getString(R.string.tracking_starting));
		}

		tableFiller.fill(playerSkills);

		virtualLevelsCB.setVisibility(playerSkills.hasOneAbove99 ? View.VISIBLE : View.GONE);
	}

	private void createAsyncTaskToPopulate(String selectedTime, boolean isUpdating) {
		TrackerTime time = null;

		if (selectedTime.equals("Hour")) {
			time = TrackerTime.Hour;
		} else if (selectedTime.equals("Day")) {
			time = TrackerTime.Day;
		} else if (selectedTime.equals("Week")) {
			time = TrackerTime.Week;
		} else if (selectedTime.equals("Month")) {
			time = TrackerTime.Month;
		} else if (selectedTime.equals("Year")) {
			time = TrackerTime.Year;
		}

		if (time != null) {
			((TableLayout) findViewById(R.id.table_tracking)).removeAllViews();
			((TextView) findViewById(R.id.track_metadata)).setText("");
			changeHeaderText(getString(R.string.loading_tracking, account.username), View.VISIBLE);
			new CMLTrackerFetcherTask(this, this, account, time, isUpdating).execute();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(spinner.getSelectedItem() instanceof String) {
			createAsyncTaskToPopulate((String) spinner.getSelectedItem(), false);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			createAsyncTaskToPopulate((String) spinner.getSelectedItem(), true);
		}
	}

	@Override
	public void onTrackingFetched(PlayerSkills playerSkills) {
		this.playerSkills = playerSkills;
		if (playerSkills != null) {
			populateTable();
		}
	}

	@Override
	public void onTrackingError(String errorMessage) {
		changeHeaderText(errorMessage, View.GONE);
	}
}
