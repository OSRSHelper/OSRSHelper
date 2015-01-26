package com.infonuascape.osrshelper;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import com.infonuascape.osrshelper.views.GridViewRSViewAdapter;
import com.infonuascape.osrshelper.views.HiscoresDialogFragment;

public class HighScoreActivity extends Activity implements OnItemClickListener, OnClickListener {
	private final static String EXTRA_USERNAME = "extra_username";
	private String username;
	private TextView header;
	private PlayerSkills playerSkills;
	private GridView rsView;
	private TableLayout table;
	private ScrollView tableScroll;
	private boolean mIsLargeLayout;
	private GridViewRSViewAdapter gridAdapter;
	private TextView tvList;
	private TextView tvRSView;

	public static void show(final Context context, final String username) {
		Intent intent = new Intent(context, HighScoreActivity.class);
		intent.putExtra(EXTRA_USERNAME, username);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.hiscores);

		mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

		username = getIntent().getStringExtra(EXTRA_USERNAME);

		header = (TextView) findViewById(R.id.header);
		header.setText(getString(R.string.loading_highscores, username));

		rsView = (GridView) findViewById(R.id.rs_view);
		table = (TableLayout) findViewById(R.id.table_hiscores);
		tableScroll = (ScrollView) findViewById(R.id.scroll_table);

		tvList = (TextView) findViewById(R.id.rs_switch_list);
		tvList.setOnClickListener(this);
		
		tvRSView = (TextView) findViewById(R.id.rs_switch_rs);
		tvRSView.setOnClickListener(this);

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

		ArrayList<Skill> skills = PlayerSkills.getSkillsInOrder(playerSkills);
		for (Skill skill : skills) {
			table.addView(createRow(skill));
		}

		gridAdapter = new GridViewRSViewAdapter(this, PlayerSkills.getSkillsInOrderForRSView(playerSkills));
		rsView.setAdapter(gridAdapter);
		rsView.setOnItemClickListener(this);
	}

	private TableRow createRow(Skill skill) {
		TableRow tableRow = new TableRow(this);
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.weight = 1;
		params.width = 0;
		params.topMargin = 10;
		params.bottomMargin = 10;
		params.gravity = Gravity.CENTER;

		// Skill image
		ImageView image = new ImageView(this);
		image.setImageResource(skill.getDrawableInt());
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
			text.setText(NumberFormat.getInstance().format(skill.getExperience()));
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

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		showDialog(gridAdapter.getItem(position));
	}

	public void showDialog(Skill skill) {
		FragmentManager fragmentManager = getFragmentManager();
		HiscoresDialogFragment newFragment = new HiscoresDialogFragment(skill);

		if (mIsLargeLayout) {
			// The device is using a large layout, so show the fragment as a
			// dialog
			newFragment.show(fragmentManager, "dialog");
		} else {
			// The device is smaller, so show the fragment fullscreen
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			// For a little polish, specify a transition animation
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			// To make it fullscreen, use the 'content' root view as the
			// container
			// for the fragment, which is always the root view for the activity
			transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rs_switch_rs) {
			rsView.setVisibility(View.VISIBLE);
			tableScroll.setVisibility(View.GONE);
			tvList.setBackgroundColor(getResources().getColor(R.color.WhiteSmoke));
			tvRSView.setBackgroundColor(getResources().getColor(R.color.green_bt));
		} else if(v.getId() == R.id.rs_switch_list) {
			rsView.setVisibility(View.GONE);
			tableScroll.setVisibility(View.VISIBLE);
			tvList.setBackgroundColor(getResources().getColor(R.color.green_bt));
			tvRSView.setBackgroundColor(getResources().getColor(R.color.WhiteSmoke));
		}
	}
}
