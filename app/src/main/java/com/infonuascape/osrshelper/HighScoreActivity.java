package com.infonuascape.osrshelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.hiscore.HiscoreFetcher;
import com.infonuascape.osrshelper.listeners.HiscoresFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.tasks.HiscoresFetcherTask;
import com.infonuascape.osrshelper.utils.ShareImageUtils;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.tablesfiller.HiscoresTableFiller;
import com.infonuascape.osrshelper.views.RSView;
import com.infonuascape.osrshelper.views.RSViewDialog;

import java.text.NumberFormat;
import java.util.ArrayList;

public class HighScoreActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, RecyclerItemClickListener, HiscoresFetcherListener {
	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private static final int NUM_PAGES = 2;
	private static final int WRITE_PERMISSION_REQUEST_CODE = 9001;
	private Account account;
	private TextView header;
	private TextView combatText;
	private PlayerSkills playerSkills;
	private RSView rsView;
	private TableLayout table;
	private ArrayList<ImageView> dots;
	private ViewPager mViewPager;
	private HiscoresTableFiller tableFiller;

	private CheckBox virtualLevelsCB;

    public static void show(final Context context, final Account account) {
		Intent intent = new Intent(context, HighScoreActivity.class);
		intent.putExtra(EXTRA_ACCOUNT, account);
		context.startActivity(intent);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.hiscores);

		account = (Account) getIntent().getSerializableExtra(EXTRA_ACCOUNT);
		if(account == null) {
			finish();
		}

		header = (TextView) findViewById(R.id.header);
		header.setText(getString(R.string.loading_highscores, account.username));
		
		combatText = (TextView) findViewById(R.id.combat);

		virtualLevelsCB = (CheckBox) findViewById(R.id.cb_virtual_levels);
		virtualLevelsCB.setOnCheckedChangeListener(this);
		virtualLevelsCB.setChecked(PreferencesController.getBooleanPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false));
		virtualLevelsCB.setVisibility(View.GONE);

		rsView = (RSView) findViewById(R.id.rs_view);
		table = (TableLayout) findViewById(R.id.table_hiscores);
		tableFiller = new HiscoresTableFiller(this, table);

		WizardPagerAdapter adapter = new WizardPagerAdapter();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		addDots();

		findViewById(R.id.share_btn).setOnClickListener(this);

		new HiscoresFetcherTask(this, this, account).execute();

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
	
	private void changeCombatText(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				combatText.setVisibility(View.VISIBLE);
				combatText.setText(getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
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

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.share_btn) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
			} else {
				ShareImageUtils.shareHiscores(this, account.username, playerSkills);
			}
		}
	}

	@Override
	public void onItemClicked(int position) {
		Skill skill = rsView.getItem(position);
		RSViewDialog.showDialog(this, skill);
	}

	@Override
	public void onItemLongClicked(int position) {

	}

	private void populateTable(PlayerSkills playerSkills) {
		changeHeaderText(getString(R.string.showing_results, account.username));
		changeCombatText();

        virtualLevelsCB.setVisibility(playerSkills.hasOneAbove99 ? View.VISIBLE : View.GONE);

        tableFiller.fill(playerSkills);
		rsView.populateView(playerSkills, this);
	}

	public void addDots() {
		dots = new ArrayList<>();
		LinearLayout dotsLayout = (LinearLayout)findViewById(R.id.dots);

		for(int i = 0; i < NUM_PAGES; i++) {
			ImageView dot = new ImageView(this);
			dot.setImageDrawable(getResources().getDrawable(R.drawable.pager_dot_not_selected));

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT
			);
			dotsLayout.addView(dot, params);

			dots.add(dot);
		}

		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				selectDot(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		selectDot(0);
	}

	public void selectDot(int idx) {
		Resources res = getResources();
		for(int i = 0; i < NUM_PAGES; i++) {
			int drawableId = (i==idx)?(R.drawable.pager_dot_selected):(R.drawable.pager_dot_not_selected);
			Drawable drawable = res.getDrawable(drawableId);
			dots.get(i).setImageDrawable(drawable);
		}
	}

	@Override
	public void onHiscoresFetched(PlayerSkills playerSkills) {
		this.playerSkills = playerSkills;
		if (playerSkills != null) {
			populateTable(playerSkills);
		}
	}

	@Override
	public void onHiscoresError(String errorMessage) {
		changeHeaderText(errorMessage);
	}


	class WizardPagerAdapter extends PagerAdapter {

		public Object instantiateItem(ViewGroup collection, int position) {

			int resId = 0;
			switch (position) {
				case 0:
					resId = R.id.scroll_table;
					break;
				case 1:
					resId = R.id.scroll_table_rs_view;
					break;
			}
			return findViewById(resId);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == WRITE_PERMISSION_REQUEST_CODE) {
			for(int i=0 ; i < permissions.length; i++) {
				if(TextUtils.equals(permissions[i], Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
						ShareImageUtils.shareHiscores(this, account.username, playerSkills);
					}
				}
			}
		}
	}
}
