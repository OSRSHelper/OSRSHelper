package com.infonuascape.osrshelper.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2018-01-14.
 */

public class CombatCalcDialog {
    public static void showDialog(final Context context, final PlayerSkills playerSkills) {
        if(context != null && playerSkills != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.combat_calc_dialog, null, false);
            boolean isOneShown = false;

            int combatLvl = Utils.getCombatLvl(playerSkills);

            int missingAttStr = Utils.getMissingAttackStrengthUntilNextCombatLvl(playerSkills);
            TextView attStrText = dialogView.findViewById(R.id.attack_strength_needed);
            if (playerSkills.attack.getLevel() + playerSkills.strength.getLevel() + missingAttStr < 199) {
                attStrText.setText(context.getResources().getString(R.string.attack_strength_lvl_needed, missingAttStr));
                attStrText.setVisibility(View.VISIBLE);
                isOneShown = true;
            } else {
                attStrText.setVisibility(View.GONE);
            }

            int missingHpDef = Utils.getMissingHPDefenceUntilNextCombatLvl(playerSkills);

            TextView hpDefText = dialogView.findViewById(R.id.hitpoint_defence_needed);
            if (playerSkills.hitpoints.getLevel() + playerSkills.defence.getLevel() + missingHpDef < 199) {
                hpDefText.setText(context.getResources().getString(R.string.hitpoint_defence_lvl_needed, missingHpDef));
                hpDefText.setVisibility(View.VISIBLE);
                isOneShown = true;
            } else {
                hpDefText.setVisibility(View.GONE);
            }

            int missingMage = Utils.getMissingMagicUntilNextCombatLvl(playerSkills);
            TextView mageText = dialogView.findViewById(R.id.magic_needed);
            if (playerSkills.magic.getLevel() + missingMage <= 99) {
                mageText.setText(context.getResources().getString(R.string.magic_lvl_needed, missingMage));
                mageText.setVisibility(View.VISIBLE);
                isOneShown = true;
            } else {
                mageText.setVisibility(View.GONE);
            }

            int missingRanged = Utils.getMissingRangingUntilNextCombatLvl(playerSkills);
            TextView rangeText = dialogView.findViewById(R.id.ranging_needed);
            if (playerSkills.ranged.getLevel() + missingRanged <= 99) {
                rangeText.setText(context.getResources().getString(R.string.ranging_lvl_needed, missingRanged));
                rangeText.setVisibility(View.VISIBLE);
                isOneShown = true;
            } else {
                rangeText.setVisibility(View.GONE);
            }

            int missingPrayer = Utils.getMissingPrayerUntilNextCombatLvl(playerSkills);
            TextView prayerText = dialogView.findViewById(R.id.prayer_needed);
            if (playerSkills.prayer.getLevel() + missingPrayer <= 99) {
                prayerText.setText(context.getResources().getString(R.string.prayer_lvl_needed, missingPrayer));
                prayerText.setVisibility(View.VISIBLE);
                isOneShown = true;
            } else {
                prayerText.setVisibility(View.GONE);
            }

            TextView lvlNeededText = dialogView.findViewById(R.id.number_lvl_needed);
            lvlNeededText.setVisibility(View.VISIBLE);
            if (!isOneShown) {
                lvlNeededText.setText(R.string.maxed_out);
            } else {
                lvlNeededText.setText(context.getResources().getString(R.string.lvl_need, combatLvl + 1));
            }

            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            dialog.findViewById(R.id.container).setOnClickListener(view -> dialog.dismiss());

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = (int) Utils.convertDpToPixel(250, context);
            dialog.getWindow().setAttributes(lp);
        }
    }
}
