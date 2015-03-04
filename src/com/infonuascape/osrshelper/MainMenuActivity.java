package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class MainMenuActivity extends Activity implements OnClickListener {
	private final static int RESULT_HIGHSCORE = 1;
	private final static int RESULT_XP_TRACKER = 2;
	private final static int RESULT_ZYBEZ_SEARCH = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_menu);

		findViewById(R.id.highscore_btn).setOnClickListener(this);
		findViewById(R.id.world_map_btn).setOnClickListener(this);
		findViewById(R.id.wiki_btn).setOnClickListener(this);
		findViewById(R.id.xptracker_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.wiki_btn) {
			Uri uri = Uri.parse("http://2007.runescape.wikia.com/wiki/2007scape_Wiki");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} else if (id == R.id.highscore_btn) {
			Intent intent = new Intent(this, UsernameActivity.class);
			startActivityForResult(intent, RESULT_HIGHSCORE);
		} else if (id == R.id.xptracker_btn) {
			Intent intent = new Intent(this, UsernameActivity.class);
			startActivityForResult(intent, RESULT_XP_TRACKER);
		} else if (id == R.id.world_map_btn) {
			WorldMapActivity.show(this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == RESULT_HIGHSCORE) {
				HighScoreActivity.show(this, data.getExtras().getString("username"));
			} else if (requestCode == RESULT_XP_TRACKER) {
				XPTrackerActivity.show(this, data.getExtras().getString("username"));
			}
		}
	}
}
