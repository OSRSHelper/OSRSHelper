package com.infonuascape.osrshelper.views;

import java.text.NumberFormat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

public class HiscoresDialogFragment extends DialogFragment implements OnClickListener  {
	private Skill skill;

	public HiscoresDialogFragment() {
	}

	@SuppressLint("ValidFragment")
	public HiscoresDialogFragment(Skill skill){
		this.skill = skill;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hiscores_dialog, container, false);

		v.findViewById(R.id.container).setOnClickListener(this);
		v.findViewById(R.id.dialog_container).setOnClickListener(this);

		if(skill != null) {
			((TextView) v.findViewById(R.id.skill_name)).setText(skill.getSkillType().toString());
			((TextView) v.findViewById(R.id.skill_name)).setCompoundDrawablesWithIntrinsicBounds(skill.getDrawableInt(), 0, 0, 0);


			((TextView) v.findViewById(R.id.skill_lvl)).setText(getString(R.string.level) + " : " + skill.getLevel());
			((TextView) v.findViewById(R.id.skill_exp)).setText(getString(R.string.xp) + " : " + NumberFormat.getInstance().format(skill.getExperience()));
			if (skill.getSkillType() != SkillType.Overall && skill.getLevel() != 99) {
				((TextView) v.findViewById(R.id.skill_exp_to_lvl)).setText(getString(R.string.xp_to_lvl) + " : " + NumberFormat.getInstance().format(Utils.getXPToLvl(skill.getLevel() + 1) - skill.getExperience()));
			} else {
				((TextView) v.findViewById(R.id.skill_exp_to_lvl)).setText("");
			}
		}

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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.container || v.getId() == R.id.dialog_container){
			getActivity().onBackPressed();
		}
	}
}