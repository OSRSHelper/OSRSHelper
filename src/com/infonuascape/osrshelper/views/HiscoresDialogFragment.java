package com.infonuascape.osrshelper.views;

import java.text.NumberFormat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

public class HiscoresDialogFragment extends DialogFragment implements OnClickListener {
	private Skill skill;
	
	public HiscoresDialogFragment(Skill skill){
		this.skill = skill;
	}
	
	/**
	 * The system calls this to get the DialogFragment's layout, regardless of
	 * whether it's being displayed as a dialog or an embedded fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout to use as dialog or embedded fragment
		View v = inflater.inflate(R.layout.hiscores_dialog, container, false);
		
		v.findViewById(R.id.container).setOnClickListener(this);
		
		((TextView) v.findViewById(R.id.skill_name)).setText(skill.getSkillType().toString());
		if(skill.getSkillType() != SkillType.Overall){
			((TextView) v.findViewById(R.id.skill_name)).setCompoundDrawablesWithIntrinsicBounds(skill.getDrawableInt(), 0, 0, 0);
		}
		
		((TextView) v.findViewById(R.id.skill_lvl)).setText(getString(R.string.level) + " : " + skill.getLevel());
		((TextView) v.findViewById(R.id.skill_exp)).setText(getString(R.string.xp) + " : " +  NumberFormat.getInstance().format(skill.getExperience()));
		return v;
	}

	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.container){
			getActivity().onBackPressed();
		}
	}
}