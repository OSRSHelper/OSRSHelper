package com.infonuascape.osrshelper.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;
import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

import java.util.ArrayList;

public class OSRSWidgetService extends RemoteViewsService {
	
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private final String TAG = "OSRSWidgetService";
	private ArrayList<Skill> skills;
	private Context mContext;
	private PlayerSkills playerSkills;
	private int mAppWidgetId;

	public StackRemoteViewsFactory(Context context, Intent intent) {
		mContext = context;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	public void onCreate() {
		Log.i(TAG, "onCreate");
		skills = new ArrayList<Skill>();
		playerSkills = new PlayerSkills();
		skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
	}

	public void onDestroy() {
		// In onDestroy() you should tear down anything that was setup for your
		// data source,
		// eg. cursors, connections, etc.
		skills.clear();
	}

	public int getCount() {
		return skills.size();
	}
	
	public RemoteViews getViewAt(int position) {
		// position will always range from 0 to getCount() - 1.

		// We construct a remote views item based on our widget item xml file,
		// and set the
		// text based on the position.
		RemoteViews rv = new RemoteViews(mContext.getPackageName(),
				R.layout.rs_view_item);
		
		Skill skill = skills.get(position);
		
		// set value into textview
		rv.setTextViewText(R.id.skill_level, skill.getLevel() + "");
		
		if(skill.getSkillType() != SkillType.Overall){
			rv.setImageViewResource(R.id.skill_image, skill.getDrawableInt());
		} else {
			rv.setImageViewResource(R.id.skill_image, R.drawable.overall_rsview);
		}

		// Return the remote views object.
		return rv;
	}

	public RemoteViews getLoadingView() {
		return null;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public long getItemId(int position) {
		return position;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void onDataSetChanged() {
		Log.i(TAG, "onDataSetChanged");
		OSRSHelperDataSource osrsHelperDataSource = new OSRSHelperDataSource(mContext);
		osrsHelperDataSource.open();
		final String username = osrsHelperDataSource.getUsernameForWidget(mAppWidgetId);
		osrsHelperDataSource.close();
		HiscoreHelper hiscoreHelper = new HiscoreHelper();
		hiscoreHelper.setUserName(username);

		try {
			playerSkills = hiscoreHelper.getPlayerStats();
			skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
		} catch (PlayerNotFoundException e) {
			if(playerSkills == null) {
				playerSkills = new PlayerSkills();
				skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
			}
			e.printStackTrace();
		} catch (Exception uhe) {
			if(playerSkills == null) {
				playerSkills = new PlayerSkills();
				skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
			}
			uhe.printStackTrace();
		}
	}
}