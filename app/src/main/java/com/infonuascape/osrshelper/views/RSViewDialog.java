package com.infonuascape.osrshelper.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.Utils;

import java.text.NumberFormat;

/**
 * Created by marc_ on 2018-01-14.
 */

public class RSViewDialog {
    public static void showDialog(final Context context, final Skill skill) {
        if(context != null && skill != null && skill.getLevel() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.hiscores_dialog, null, false);

            ((TextView) dialogView.findViewById(R.id.skill_name)).setText(skill.getSkillType().getSkillName());
            ((ImageView) dialogView.findViewById(R.id.skill_image)).setImageResource(skill.getSkillType().getDrawableInt());

            boolean isShowVirtualLevels = Utils.isShowVirtualLevels(context);

            short level = (isShowVirtualLevels ? skill.getVirtualLevel() : skill.getLevel());
            ((TextView) dialogView.findViewById(R.id.skill_lvl)).setText(String.valueOf(level));

            ((TextView) dialogView.findViewById(R.id.skill_exp)).setText(NumberFormat.getInstance().format(skill.getExperience()));

            if (skill.getSkillType() != SkillType.Overall && (isShowVirtualLevels || skill.getLevel() != 99)) {
                String xpToLevel = NumberFormat.getInstance().format(Utils.getExpFromLevel(level + 1, isShowVirtualLevels) - skill.getExperience());
                ((TextView) dialogView.findViewById(R.id.skill_exp_to_lvl)).setText(String.valueOf(xpToLevel));
            } else {
                dialogView.findViewById(R.id.skill_exp_to_lvl_title).setVisibility(View.GONE);
                dialogView.findViewById(R.id.skill_exp_to_lvl).setVisibility(View.GONE);
            }

            if(skill.getRank() > 0) {
                ((TextView) dialogView.findViewById(R.id.skill_rank)).setText(NumberFormat.getInstance().format(skill.getRank()) + context.getResources().getString(R.string.hiscore_rank_item));
            } else {
                dialogView.findViewById(R.id.skill_rank_title).setVisibility(View.GONE);
                dialogView.findViewById(R.id.skill_rank).setVisibility(View.GONE);
            }

            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            dialog.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = (int) Utils.convertDpToPixel(250, context);
            dialog.getWindow().setAttributes(lp);
        }
    }
}
