package com.infonuascape.osrshelper.views;

import java.text.NumberFormat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;

public class HiscoresDialogFragment extends DialogFragment  {
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


		((TextView) v.findViewById(R.id.skill_name)).setText(skill.getSkillType().toString());
		((TextView) v.findViewById(R.id.skill_name)).setCompoundDrawablesWithIntrinsicBounds(skill.getDrawableInt(), 0, 0, 0);


		((TextView) v.findViewById(R.id.skill_lvl)).setText(getString(R.string.level) + " : " + skill.getLevel());
		((TextView) v.findViewById(R.id.skill_exp)).setText(getString(R.string.xp) + " : " +  NumberFormat.getInstance().format(skill.getExperience()));
		return v;
	}

	@Override
	public void onResume(){
		super.onResume();

		final View v = getView().findViewById(R.id.dialog_container);
		final View v2 = getView().findViewById(R.id.container);

		final Animation anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
		anim.setDuration(200);
		v.startAnimation(anim);
		v2.startAnimation(anim);
	}
	
	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
}