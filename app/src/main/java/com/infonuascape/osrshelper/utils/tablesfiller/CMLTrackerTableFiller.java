package com.infonuascape.osrshelper.utils.tablesfiller;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Utils;

import java.text.NumberFormat;

/**
 * Created by marc_ on 2018-01-14.
 */

public class CMLTrackerTableFiller {
    private Context context;
    private TableLayout tableLayout;

    public CMLTrackerTableFiller(final Context context, final TableLayout tableLayout) {
        this.context = context;
        this.tableLayout = tableLayout;
    }

    public void fill(final PlayerSkills playerSkills) {
        tableLayout.removeAllViews();
        tableLayout.addView(createHeadersRow());

        final boolean isShowVirtualLevels = Utils.isShowVirtualLevels(context, playerSkills);

        //Add skills individually to the table
        for (Skill s : playerSkills.skillList) {
            tableLayout.addView(createRow(s, isShowVirtualLevels));
        }
    }

    private TableRow createHeadersRow() {
        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.width = 0;
        params.topMargin = 10;
        params.bottomMargin = 10;
        params.gravity = Gravity.CENTER;

        // Skill
        TextView text = new TextView(context);
        text.setText(context.getString(R.string.skill));
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        // XP
        text = new TextView(context);
        text.setText(context.getString(R.string.xp));
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        // Gain
        text = new TextView(context);
        text.setText(context.getString(R.string.xp_gain));
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        // Rank diff
        text = new TextView(context);
        text.setText(context.getString(R.string.rank_diff));
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        return tableRow;
    }

    private TableRow createRow(final Skill s, final boolean isShowVirtualLevels) {
        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.width = 0;
        params.topMargin = 10;
        params.bottomMargin = 10;
        params.gravity = Gravity.CENTER;



        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(ll.VERTICAL);
        LinearLayout ll1 = new LinearLayout(context);
        LinearLayout ll2 = new LinearLayout(context);


        ImageView image = new ImageView(context);
        image.setImageResource(s.getDrawableInt());
        TableRow.LayoutParams image_params = new TableRow.LayoutParams();
        image_params.topMargin = 0;
        image.setLayoutParams(image_params);
        ll1.addView(image);

        TextView text = new TextView(context);
        text.setText((isShowVirtualLevels ? s.getVirtualLevel() : s.getLevel()) + "");
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        TableRow.LayoutParams level_params = new TableRow.LayoutParams();
        level_params.topMargin = 0;
        text.setLayoutParams(level_params);
        ll2.addView(text);

        ll.addView(ll1);
        ll.addView(ll2);
        ll.setLayoutParams(params);
        tableRow.addView(ll);


        // Current XP
        text = new TextView(context);
        text.setText(NumberFormat.getInstance().format(s.getExperience()));
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        text.setLayoutParams(params);
        tableRow.addView(text);

        // XP Gain
        text = new TextView(context);
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        int expDiff = s.getExperienceDiff();

        if (expDiff == 0) {
            text.setTextColor(context.getResources().getColor(R.color.dark_gray));
            text.setText(context.getString(R.string.gain_small, expDiff));
        } else {
            text.setTextColor(context.getResources().getColor(R.color.green));
            if (expDiff < 1000) {
                text.setText(context.getString(R.string.gain_small, expDiff));

            } else if (expDiff >= 1000 && expDiff < 10000) {
                text.setText(context.getString(R.string.gain_medium, expDiff / 1000.0f));

            } else {
                text.setText(context.getString(R.string.gain, expDiff / 1000));
            }
        }
        tableRow.addView(text);

        // Rank diff
        text = new TextView(context);
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);

        int rankDiff = s.getRankDiff();

        if (rankDiff == 0) {
            text.setTextColor(context.getResources().getColor(R.color.dark_gray));
            text.setText(context.getString(R.string.gain_small, rankDiff));
        } else {

            //set appropriate gain color
            if (rankDiff > 0)
                text.setTextColor(context.getResources().getColor(R.color.green));
            else
                text.setTextColor(context.getResources().getColor(R.color.red));


            //ranks "lost" AKA progress were made
            if (rankDiff > 0 && rankDiff < 1000)
                text.setText(context.getString(R.string.gain_small, rankDiff));

            else if (rankDiff >= 1000 && rankDiff < 10000)
                text.setText(context.getString(R.string.gain_medium, rankDiff / 1000.0f));

            else if (rankDiff > 10000)
                text.setText(context.getString(R.string.gain, rankDiff / 1000));


                //ranks "gained" AKA no progress were made
            else if (rankDiff < 0 && rankDiff > -1000)
                text.setText(context.getString(R.string.loss_small, Math.abs(rankDiff)));

            else if (rankDiff <= -1000 && rankDiff > -10000)
                text.setText(context.getString(R.string.loss_medium, Math.abs(rankDiff) / 1000.0f));

            else
                text.setText(context.getString(R.string.loss, Math.abs(rankDiff) / 1000));

        }
        tableRow.addView(text);

        return tableRow;
    }
}
