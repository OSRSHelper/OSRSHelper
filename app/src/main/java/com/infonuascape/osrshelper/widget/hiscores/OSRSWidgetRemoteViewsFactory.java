package com.infonuascape.osrshelper.widget.hiscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.fetchers.hiscore.HiscoreFetcher;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-01-14.
 */

public class OSRSWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final String TAG = "OSRSWidgetRemoteViewsFactory";
    private ArrayList<Skill> skills;
    private Context mContext;
    private PlayerSkills playerSkills;
    private int mAppWidgetId;

    public OSRSWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        Logger.add(TAG, ": onCreate");
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

        Skill skill = skills.get(position);

        RemoteViews rv;

        if(skill.getSkillType() != SkillType.Overall) {
            rv = new RemoteViews(mContext.getPackageName(),
                    R.layout.rs_view_item);
        } else {
            rv = new RemoteViews(mContext.getPackageName(),
                    R.layout.rs_view_item_no_icon);
        }


        // set value into textview
        rv.setTextViewText(R.id.skill_level, (Utils.isShowVirtualLevels(mContext) ? skill.getVirtualLevel() : skill.getLevel()) + "");

        if(skill.getSkillType() != SkillType.Overall){
            rv.setImageViewResource(R.id.skill_image, skill.getSkillType().getDrawableInt());
        }

        // Return the remote views object.
        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        Logger.add(TAG, ": onDataSetChanged");
        final long identityToken = Binder.clearCallingIdentity();
        final Account account = DBController.getAccountForWidget(mContext, mAppWidgetId);
        Binder.restoreCallingIdentity(identityToken);

        try {
            playerSkills = new HiscoreFetcher(mContext, account.username, account.type).getPlayerSkills();
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