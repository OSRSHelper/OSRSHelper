package com.infonuascape.osrshelper;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.tracker.TrackerHelper;
import com.infonuascape.osrshelper.tracker.TrackerTimeEnum;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class XPTrackerActivity extends Activity implements OnItemSelectedListener {
	private final static String TAG = "XPTrackerActivity";
	private final static String EXTRA_USERNAME = "extra_username";
	private String username;
	private TextView header;
	private Spinner spinner;

	public static void show(final Context context, final String username) {
		Intent intent = new Intent(context, XPTrackerActivity.class);
		intent.putExtra(EXTRA_USERNAME, username);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_xptracker);

		username = getIntent().getStringExtra(EXTRA_USERNAME);

		header = (TextView) findViewById(R.id.header);
		header.setText(getString(R.string.loading_tracking, username));

		spinner = (Spinner) findViewById(R.id.time_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		spinner.setSelection(2);

		spinner.setOnItemSelectedListener(this);

		((TextView) findViewById(R.id.track_since)).setText(getString(R.string.tracking_since, ""));

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

	private class PopulateTable extends AsyncTask<String, Void, PlayerSkills> {
		private TrackerTimeEnum.TrackerTime time;
		private PlayerSkills hiscores;
		private PlayerSkills trackedSkills;

		public PopulateTable(TrackerTimeEnum.TrackerTime time) {
			this.time = time;
		}

		@Override
		protected PlayerSkills doInBackground(String... urls) {
			TrackerHelper trackerHelper = new TrackerHelper();
			trackerHelper.setUserName(username);
			HiscoreHelper hiscoreHelper = new HiscoreHelper();
			hiscoreHelper.setUserName(username);

			try {
				hiscores = hiscoreHelper.getPlayerStats();
				trackedSkills = trackerHelper.getPlayerStats(time);
			} catch (PlayerNotFoundException e) {
				changeHeaderText(getString(R.string.not_existing_player, username), View.GONE);

			} catch (Exception uhe) {
				uhe.printStackTrace();
				changeHeaderText(getString(R.string.network_error), View.GONE);
			}
			return trackedSkills;
		}

		@Override
		protected void onPostExecute(PlayerSkills playerSkillsCallback) {
			if (trackedSkills != null && hiscores != null) {
				populateTable(hiscores, trackedSkills);
			}
		}
	}

	private void populateTable(PlayerSkills hiscores, PlayerSkills trackedSkills) {
		changeHeaderText(getString(R.string.showing_tracking, username), View.GONE);

		((TextView) findViewById(R.id.track_since))
				.setText(getString(R.string.tracking_since, trackedSkills.sinceWhen));

		TableLayout table = (TableLayout) findViewById(R.id.table_tracking);
		table.removeAllViews();
		table.addView(createHeadersRow());
		table.addView(createRow(hiscores.overall, trackedSkills.overall, R.drawable.overall));
		table.addView(createRow(hiscores.attack, trackedSkills.attack, R.drawable.attack));
		table.addView(createRow(hiscores.defence, trackedSkills.defence, R.drawable.defence));
		table.addView(createRow(hiscores.strength, trackedSkills.strength, R.drawable.strength));
		table.addView(createRow(hiscores.hitpoints, trackedSkills.hitpoints, R.drawable.constitution));
		table.addView(createRow(hiscores.ranged, trackedSkills.ranged, R.drawable.ranged));
		table.addView(createRow(hiscores.prayer, trackedSkills.prayer, R.drawable.prayer));
		table.addView(createRow(hiscores.magic, trackedSkills.magic, R.drawable.magic));
		table.addView(createRow(hiscores.cooking, trackedSkills.cooking, R.drawable.cooking));
		table.addView(createRow(hiscores.woodcutting, trackedSkills.woodcutting, R.drawable.woodcutting));
		table.addView(createRow(hiscores.fletching, trackedSkills.fletching, R.drawable.fletching));
		table.addView(createRow(hiscores.fishing, trackedSkills.fishing, R.drawable.fishing));
		table.addView(createRow(hiscores.firemaking, trackedSkills.firemaking, R.drawable.firemaking));
		table.addView(createRow(hiscores.crafting, trackedSkills.crafting, R.drawable.crafting));
		table.addView(createRow(hiscores.smithing, trackedSkills.smithing, R.drawable.smithing));
		table.addView(createRow(hiscores.mining, trackedSkills.mining, R.drawable.mining));
		table.addView(createRow(hiscores.herblore, trackedSkills.herblore, R.drawable.herblore));
		table.addView(createRow(hiscores.agility, trackedSkills.agility, R.drawable.agility));
		table.addView(createRow(hiscores.thieving, trackedSkills.thieving, R.drawable.thieving));
		table.addView(createRow(hiscores.slayer, trackedSkills.slayer, R.drawable.slayer));
		table.addView(createRow(hiscores.farming, trackedSkills.farming, R.drawable.farming));
		table.addView(createRow(hiscores.runecraft, trackedSkills.runecraft, R.drawable.runecrafting));
		table.addView(createRow(hiscores.hunter, trackedSkills.hunter, R.drawable.hunter));
		table.addView(createRow(hiscores.construction, trackedSkills.construction, R.drawable.construction));
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
		text.setText(getString(R.string.gain));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		return tableRow;
	}

	private TableRow createRow(Skill skillHiscore, Skill skillTrack, int imageId) {
		TableRow tableRow = new TableRow(this);
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.weight = 1;
		params.width = 0;
		params.topMargin = 10;
		params.bottomMargin = 10;
		params.gravity = Gravity.CENTER;

		// Skill image
		ImageView image = new ImageView(this);
		image.setImageResource(imageId);
		image.setLayoutParams(params);
		tableRow.addView(image);

		// Lvl
		TextView text = new TextView(this);
		text.setText(skillHiscore.getLevel() + "");
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// XP
		text = new TextView(this);
		text.setText(getString(R.string.xp_item, NumberFormat.getInstance().format(skillHiscore.getExperience())));
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// Gain
		text = new TextView(this);
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);

		if (skillTrack.getExperience() <= 0) {
			text.setTextColor(getResources().getColor(R.color.Red));
			text.setText(getString(R.string.xp_gain_small, skillTrack.getExperience()));
		} else if (skillTrack.getExperience() < 100000) {
			text.setTextColor(getResources().getColor(R.color.DarkYellow));
			if (skillTrack.getExperience() < 1000) {
				text.setText(getString(R.string.xp_gain_small, skillTrack.getExperience()));
			} else if (skillTrack.getExperience() >= 1000 && skillTrack.getExperience() < 10000) {
				text.setText(getString(R.string.xp_gain_medium, skillTrack.getExperience() / 1000.0f));
			} else {
				text.setText(getString(R.string.xp_gain, skillTrack.getExperience() / 1000));
			}
		} else {
			text.setTextColor(getResources().getColor(R.color.Green));
			text.setText(getString(R.string.xp_gain, skillTrack.getExperience() / 1000));
		}
		tableRow.addView(text);

		return tableRow;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String text = ((TextView) view).getText().toString();
		TrackerTimeEnum.TrackerTime time = null;

		if (text.equals("Hour")) {
			time = TrackerTimeEnum.TrackerTime.Hour;
		} else if (text.equals("Day")) {
			time = TrackerTimeEnum.TrackerTime.Day;
		} else if (text.equals("Week")) {
			time = TrackerTimeEnum.TrackerTime.Week;
		} else if (text.equals("Month")) {
			time = TrackerTimeEnum.TrackerTime.Month;
		} else if (text.equals("Year")) {
			time = TrackerTimeEnum.TrackerTime.Year;
		}

		if (time != null) {
			((TableLayout) findViewById(R.id.table_tracking)).removeAllViews();
			changeHeaderText(getString(R.string.loading_tracking, username), View.VISIBLE);
			new PopulateTable(time).execute();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
