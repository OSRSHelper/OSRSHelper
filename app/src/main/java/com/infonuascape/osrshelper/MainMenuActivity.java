package com.infonuascape.osrshelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.crashlytics.android.answers.Answers;

public class MainMenuActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_menu);

		findViewById(R.id.highscore_btn).setOnClickListener(this);
		findViewById(R.id.world_map_btn).setOnClickListener(this);
		findViewById(R.id.wiki_btn).setOnClickListener(this);
		findViewById(R.id.rt_xptracker_btn).setOnClickListener(this);
		findViewById(R.id.cml_xptracker_btn).setOnClickListener(this);
		findViewById(R.id.combat_btn).setOnClickListener(this);
		findViewById(R.id.ge_btn).setOnClickListener(this);
		findViewById(R.id.donate_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.wiki_btn) {
			WebViewActivity.show(this, "http://2007.runescape.wikia.com/wiki/2007scape_Wiki");
		} else if (id == R.id.highscore_btn) {
			UsernameActivity.show(this, UsernameActivity.HISCORES);
		} else if (id == R.id.rt_xptracker_btn) {
			UsernameActivity.show(this, UsernameActivity.RT_XP_TRACKER);
		} else if (id == R.id.cml_xptracker_btn) {
			UsernameActivity.show(this, UsernameActivity.CML_XP_TRACKER);
		} else if (id == R.id.world_map_btn) {
			WorldMapActivity.show(this);
		} else if (id == R.id.combat_btn) {
			UsernameActivity.show(this, UsernameActivity.COMBAT);
		} else if (id == R.id.ge_btn) {
			SearchItemActivity.show(this);
		} else if (id == R.id.donate_btn) {
			DonationActivity.show(this);
			
		}
	}
}
