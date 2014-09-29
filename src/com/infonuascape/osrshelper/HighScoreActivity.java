package com.infonuascape.osrshelper;

import java.text.NumberFormat;

import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Skill;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HighScoreActivity extends Activity {
	private final static String TAG = "HighScoreActivity";
	private final static String EXTRA_USERNAME = "extra_username";
	private String username;
	private TextView header;
	private PlayerSkills playerSkills;

	public static void show(final Context context, final String username) {
		Intent intent = new Intent(context, HighScoreActivity.class);
		intent.putExtra(EXTRA_USERNAME, username);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_high_score);

		username = getIntent().getStringExtra(EXTRA_USERNAME);

		header = (TextView) findViewById(R.id.header);
		header.setText(getString(R.string.loading_highscores, username));

		HiscoreHelper hiscoreHelper = new HiscoreHelper();
		hiscoreHelper.setUserName(username);

		Log.i(TAG, "Populate table with downloaded skills");
		new PopulateTable().execute();

	}

	private void changeHeaderText(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				findViewById(R.id.progressbar).setVisibility(View.GONE);
				header.setText(text);
			}
		});
	}

	private class PopulateTable extends AsyncTask<String, Void, PlayerSkills> {

		@Override
		protected PlayerSkills doInBackground(String... urls) {
			HiscoreHelper hiscoreHelper = new HiscoreHelper();
			hiscoreHelper.setUserName(username);
			PlayerSkills playerSkills = null;

			try {
				playerSkills = hiscoreHelper.getPlayerStats();
			} catch (PlayerNotFoundException e) {
				changeHeaderText(getString(R.string.not_existing_player, username));

			} catch (Exception uhe) {
				uhe.printStackTrace();
				changeHeaderText(getString(R.string.network_error));
			}
			return playerSkills;
		}

		@Override
		protected void onPostExecute(PlayerSkills playerSkillsCallback) {
			if (playerSkillsCallback != null) {
				playerSkills = playerSkillsCallback;
				populateTable(playerSkills);
			}
		}
	}

	private void populateTable(PlayerSkills playerSkills) {
		changeHeaderText(getString(R.string.showing_results, username));

		TableLayout table = (TableLayout) findViewById(R.id.table_hiscores);
		table.addView(createRow(playerSkills.overall, R.drawable.overall));
		table.addView(createRow(playerSkills.attack, R.drawable.attack));
		table.addView(createRow(playerSkills.defence, R.drawable.defence));
		table.addView(createRow(playerSkills.strength, R.drawable.strength));
		table.addView(createRow(playerSkills.hitpoints, R.drawable.constitution));
		table.addView(createRow(playerSkills.ranged, R.drawable.ranged));
		table.addView(createRow(playerSkills.prayer, R.drawable.prayer));
		table.addView(createRow(playerSkills.magic, R.drawable.magic));
		table.addView(createRow(playerSkills.cooking, R.drawable.cooking));
		table.addView(createRow(playerSkills.woodcutting, R.drawable.woodcutting));
		table.addView(createRow(playerSkills.fletching, R.drawable.fletching));
		table.addView(createRow(playerSkills.fishing, R.drawable.fishing));
		table.addView(createRow(playerSkills.firemaking, R.drawable.firemaking));
		table.addView(createRow(playerSkills.crafting, R.drawable.crafting));
		table.addView(createRow(playerSkills.smithing, R.drawable.smithing));
		table.addView(createRow(playerSkills.mining, R.drawable.mining));
		table.addView(createRow(playerSkills.herblore, R.drawable.herblore));
		table.addView(createRow(playerSkills.agility, R.drawable.agility));
		table.addView(createRow(playerSkills.thieving, R.drawable.thieving));
		table.addView(createRow(playerSkills.slayer, R.drawable.slayer));
		table.addView(createRow(playerSkills.farming, R.drawable.farming));
		table.addView(createRow(playerSkills.runecraft, R.drawable.runecrafting));
		table.addView(createRow(playerSkills.hunter, R.drawable.hunter));
		table.addView(createRow(playerSkills.construction, R.drawable.construction));
	}

	private TableRow createRow(Skill skill, int imageId) {
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
		if (skill.getRank() != -1) {
			text.setText(skill.getLevel() + "");
		}
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// XP
		text = new TextView(this);
		if (skill.getRank() != -1) {
			text.setText(getString(R.string.xp_item, NumberFormat.getInstance().format(skill.getExperience())));
		}
		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(getResources().getColor(R.color.text_normal));
		tableRow.addView(text);

		// Ranking
		text = new TextView(this);

		if (skill.getRank() != -1) {
			text.setText(NumberFormat.getInstance().format(skill.getRank()));
			text.setTextColor(getResources().getColor(R.color.text_normal));
		} else {
			text.setText(getString(R.string.not_ranked));
			text.setTextColor(getResources().getColor(R.color.Red));
		}

		text.setLayoutParams(params);
		text.setGravity(Gravity.CENTER);
		tableRow.addView(text);

		return tableRow;
	}
}
