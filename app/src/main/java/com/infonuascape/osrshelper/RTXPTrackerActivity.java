package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.tracker.rt.TrackerHelper;
import com.infonuascape.osrshelper.tracker.rt.Updater;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

import java.text.NumberFormat;

public class RTXPTrackerActivity extends Activity implements OnItemSelectedListener, OnClickListener, CompoundButton.OnCheckedChangeListener {
	private final static String EXTRA_USERNAME = "extra_username";
	private String username;
	private TextView header;
	private Spinner spinner;

	private CheckBox virtualLevelsCB;
	private PlayerSkills playerSkills;

	public static void show(final Context context, final String username) {
		Intent intent = new Intent(context, RTXPTrackerActivity.class);
		intent.putExtra(EXTRA_USERNAME, username);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.xptracker);

		username = getIntent().getStringExtra(EXTRA_USERNAME);

		header = (TextView) findViewById(R.id.header);
		header.setText(getString(R.string.loading_tracking, username));

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

	private void changeHeaderText(final String text, final int visibility) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				findViewById(R.id.progressbar).setVisibility(visibility);
				header.setText(text);
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		PreferencesController.setPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, isChecked);
		if(playerSkills != null) {
			populateTable(playerSkills);
		}
	}

	private boolean isShowVirtualLevels() {
		return playerSkills != null && playerSkills.hasOneAbove99
				&& PreferencesController.getBooleanPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false);
	}

	private class PopulateTable extends AsyncTask<String, Void, PlayerSkills> {
		private TrackerTimeEnum.TrackerTime time;
		private boolean isUpdating;

		public PopulateTable(TrackerTimeEnum.TrackerTime time, boolean isUpdating) {
			this.time = time;
			this.isUpdating = isUpdating;
		}

		@Override
		protected PlayerSkills doInBackground(String... urls) {
			TrackerHelper trackerHelper = new TrackerHelper(getApplicationContext());
			trackerHelper.setUserName(username);

			try {
				if (isUpdating) {
					Updater.perform(getApplicationContext(), username);
				}
				return trackerHelper.getPlayerStats(time);
			} catch (PlayerNotFoundException e) {
				changeHeaderText(getString(R.string.not_existing_player, username), View.GONE);

			} catch (Exception uhe) {
				uhe.printStackTrace();
				changeHeaderText(getString(R.string.internal_error), View.GONE);
			}
			return null;
		}

		@Override
		protected void onPostExecute(PlayerSkills playerSkillsCallback) {
			playerSkills = playerSkillsCallback;
			if (playerSkills != null) {
				populateTable(playerSkills);
			}
		}
	}

	private void populateTable(PlayerSkills trackedSkills) {
		changeHeaderText(getString(R.string.showing_tracking, username), View.GONE);
		if (trackedSkills.sinceWhen != null) {
			((TextView) findViewById(R.id.track_metadata)).setText(getString(R.string.tracking_since,
					trackedSkills.sinceWhen));
		} else {
			((TextView) findViewById(R.id.track_metadata)).setText(getString(R.string.tracking_starting));
		}

		TableLayout table = (TableLayout) findViewById(R.id.table_tracking);
		table.removeAllViews();
		table.addView(createHeadersRow());

        //Add skills individually to the table
        for (Skill s : playerSkills.skillList) {
            table.addView(createRow(s));
        }

        virtualLevelsCB.setVisibility(playerSkills.hasOneAbove99 ? View.VISIBLE : View.GONE);
	}

	private TableRow createHeadersRow() {
		TableRow tableRow = new TableRow(this);
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.weight = 1;
		params.width = 0;
		params.topMargin = 10;
		params.bottomMargin = 10;
		params.gravity = Gravity.CENTER;

		// Skill
		TextView text = new TextView(this);
		text.setText(getString(R.string.skill));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// Lvl
		text = new TextView(this);
		text.setText(getString(R.string.level));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// XP
		text = new TextView(this);
		text.setText(getString(R.string.xp));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// Gain
		text = new TextView(this);
		text.setText(getString(R.string.xp_gain));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		return tableRow;
	}

	private TableRow createRow(Skill s) {
		TableRow tableRow = new TableRow(this);
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.weight = 1;
		params.width = 0;
		params.topMargin = 10;
		params.bottomMargin = 10;
		params.gravity = Gravity.CENTER;

		// Skill image
		ImageView image = new ImageView(this);
		image.setImageResource(s.getDrawableInt());
		image.setLayoutParams(params);
		tableRow.addView(image);

		// Lvl
		TextView text = new TextView(this);
		text.setText((isShowVirtualLevels() ? s.getVirtualLevel() : s.getLevel()) + "");
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// XP
		text = new TextView(this);
		text.setText(NumberFormat.getInstance().format(s.getExperience()));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// Gain
		text = new TextView(this);
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		int expDiff = s.getExperienceDiff();
		
		if (expDiff == 0) {
			text.setTextColor(getResources().getColor(R.color.dark_gray));
			text.setText(getString(R.string.gain_small, expDiff));
		} else {
			text.setTextColor(getResources().getColor(R.color.green));
			if (expDiff < 1000) {
				text.setText(getString(R.string.gain_small, expDiff));
			} else if (expDiff >= 1000 && expDiff < 10000) {
				text.setText(getString(R.string.gain_medium, expDiff / 1000.0f));
			} else {
				text.setText(getString(R.string.gain, expDiff / 1000));
			}
		}
		tableRow.addView(text);

		return tableRow;
	}

	private void createAsyncTaskToPopulate(String selectedTime, boolean isUpdating) {
		TrackerTimeEnum.TrackerTime time = null;

		if (selectedTime.equals("Hour")) {
			time = TrackerTimeEnum.TrackerTime.Hour;
		} else if (selectedTime.equals("Day")) {
			time = TrackerTimeEnum.TrackerTime.Day;
		} else if (selectedTime.equals("Week")) {
			time = TrackerTimeEnum.TrackerTime.Week;
		} else if (selectedTime.equals("Month")) {
			time = TrackerTimeEnum.TrackerTime.Month;
		} else if (selectedTime.equals("Year")) {
			time = TrackerTimeEnum.TrackerTime.Year;
		}

		if (time != null) {
			((TableLayout) findViewById(R.id.table_tracking)).removeAllViews();
			((TextView) findViewById(R.id.track_metadata)).setText("");
			changeHeaderText(getString(R.string.loading_tracking, username), View.VISIBLE);
			new PopulateTable(time, isUpdating).execute();
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
}
