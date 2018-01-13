package com.infonuascape.osrshelper.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class RSViewPopulate {
	private Activity activity;
	private final ArrayList<Skill> skills;
    private final PlayerSkills playerSkills;
	private int numberOfColumns;

	public RSViewPopulate(Activity activity, PlayerSkills playerSkills) {
		this.activity = activity;
		this.skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
        this.playerSkills = playerSkills;
		this.numberOfColumns = 3;
	}

    private boolean isShowVirtualLevels() {
        return playerSkills != null && playerSkills.hasOneAbove99
                && PreferencesController.getBooleanPreference(activity, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false);
    }

	public TableLayout populate(TableLayout layout){
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout.removeAllViews();
		TableRow currentRow = null;

		for(int i = 0 ; i < skills.size() ; i++){
			Skill skill = skills.get(i);

			if(i % numberOfColumns == 0){
				if(currentRow != null){
					layout.addView(currentRow);
				}
				currentRow = new TableRow(activity);
			}

			View currentSkill = inflater.inflate(R.layout.rs_view_item, null);

			// set value into textview
			TextView textView = (TextView) currentSkill.findViewById(R.id.skill_level);
			textView.setText((isShowVirtualLevels() ? skill.getVirtualLevel() : skill.getLevel()) + "");

			// set image based on selected text
			ImageView imageView = (ImageView) currentSkill.findViewById(R.id.skill_image);
			if(skill.getSkillType() != SkillType.Overall){
				imageView.setImageResource(skill.getDrawableInt());
			} else{
				imageView.setImageResource(R.drawable.overall_rsview);
			}


			currentSkill.setOnClickListener(new RSViewOnClickListener(activity, skill));
			currentRow.addView(currentSkill);
		}

		if(currentRow != null){
			layout.addView(currentRow);
		}

		return layout;
	}
	
	public GridView populate(GridView layout){
		layout.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for(int i = 0 ; i < skills.size() ; i++){
			Skill skill = skills.get(i);

			View currentSkill = new View(activity);

			currentSkill = inflater.inflate(R.layout.rs_view_item, null);

			// set value into textview
			TextView textView = (TextView) currentSkill.findViewById(R.id.skill_level);
			textView.setText(skill.getLevel() + "");

			// set image based on selected text
			ImageView imageView = (ImageView) currentSkill.findViewById(R.id.skill_image);
			if(skill.getSkillType() != SkillType.Overall){
				imageView.setImageResource(skill.getDrawableInt());
			} else{
				imageView.setImageResource(R.drawable.overall_rsview);
			}


			currentSkill.setOnClickListener(new RSViewOnClickListener(activity, skill));
			layout.addView(currentSkill);
		}

		return layout;
	}

}