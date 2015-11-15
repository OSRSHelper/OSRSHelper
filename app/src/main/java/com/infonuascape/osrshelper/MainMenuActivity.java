package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class MainMenuActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_menu);

		findViewById(R.id.highscore_btn).setOnClickListener(this);
		findViewById(R.id.world_map_btn).setOnClickListener(this);
		findViewById(R.id.wiki_btn).setOnClickListener(this);
		findViewById(R.id.xptracker_btn).setOnClickListener(this);
		findViewById(R.id.combat_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.wiki_btn) {
			Uri uri = Uri.parse("http://2007.runescape.wikia.com/wiki/2007scape_Wiki");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} else if (id == R.id.highscore_btn) {
			UsernameActivity.show(this, UsernameActivity.HISCORES);
		} else if (id == R.id.xptracker_btn) {
			UsernameActivity.show(this, UsernameActivity.XP_TRACKER);
		} else if (id == R.id.world_map_btn) {
			WorldMapActivity.show(this);
		} else if (id == R.id.combat_btn) {
			CombatCalcActivity.show(this);
		}
	}
}
