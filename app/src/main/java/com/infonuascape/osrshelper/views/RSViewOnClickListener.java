package com.infonuascape.osrshelper.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum;
import com.infonuascape.osrshelper.utils.Utils;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;

public class RSViewOnClickListener implements OnClickListener {
	private Skill skill;
	private WeakReference<Context> context;
	
	public RSViewOnClickListener(final Context context, final Skill skill){
		this.skill = skill;
		this.context = new WeakReference<>(context);
	}

	@Override
	public void onClick(View v) {
		if(context.get() != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context.get());
			final View dialogView = ((LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.hiscores_dialog, null, false);

			if (skill != null) {
				((TextView) dialogView.findViewById(R.id.skill_name)).setText(skill.getSkillType().toString());
				((ImageView) dialogView.findViewById(R.id.skill_image)).setImageResource(skill.getDrawableInt());

				boolean isShowVirtualLevels = skill.getVirtualLevel() > 99
						&& PreferencesController.getBooleanPreference(context.get(), PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false);

				short level = (isShowVirtualLevels ? skill.getVirtualLevel() : skill.getLevel());
				((TextView) dialogView.findViewById(R.id.skill_lvl)).setText(String.valueOf(level));

				((TextView) dialogView.findViewById(R.id.skill_exp)).setText(String.valueOf(NumberFormat.getInstance().format(skill.getExperience())));

				if (skill.getSkillType() != SkillsEnum.SkillType.Overall && (isShowVirtualLevels || skill.getLevel() != 99)) {
					String xpToLevel = NumberFormat.getInstance().format(Utils.getXPToLvl(level + 1, isShowVirtualLevels) - skill.getExperience());
					((TextView) dialogView.findViewById(R.id.skill_exp_to_lvl)).setText(String.valueOf(xpToLevel));
				} else {
					dialogView.findViewById(R.id.skill_exp_to_lvl_title).setVisibility(View.GONE);
					dialogView.findViewById(R.id.skill_exp_to_lvl).setVisibility(View.GONE);
				}
			}

			builder.setView(dialogView);
			final AlertDialog dialog = builder.create();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.show();

			dialog.findViewById(R.id.container).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = (int) Utils.convertDpToPixel(275, context.get());
			dialog.getWindow().setAttributes(lp);
		}
	}

}
