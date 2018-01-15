package com.infonuascape.osrshelper.utils.tablesfiller;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
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

public class HiscoresTableFiller {
    private Context context;
    private TableLayout tableLayout;

    public HiscoresTableFiller(final Context context, final TableLayout tableLayout) {
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

        // Lvl
        text = new TextView(context);
        text.setText(context.getString(R.string.level));
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

        // Rank
        text = new TextView(context);
        text.setText(context.getString(R.string.ranking));
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        return tableRow;
    }

    private TableRow createRow(final Skill skill, final boolean isShowVirtualLevels) {
        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.width = 0;
        params.topMargin = 10;
        params.bottomMargin = 10;
        params.gravity = Gravity.CENTER;

        // Skill image
        ImageView image = new ImageView(context);
        image.setImageResource(skill.getDrawableInt());
        image.setLayoutParams(params);
        tableRow.addView(image);


        // Lvl
        TextView text = new TextView(context);
        if (skill.getRank() != -1) {
            text.setText((isShowVirtualLevels ? skill.getVirtualLevel() : skill.getLevel()) + "");
        }
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        // XP
        text = new TextView(context);
        if (skill.getRank() != -1) {
            text.setText(NumberFormat.getInstance().format(skill.getExperience()));
        }

        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.text_normal));
        tableRow.addView(text);

        // Ranking
        text = new TextView(context);

        if (skill.getRank() != -1) {
            text.setText(NumberFormat.getInstance().format(skill.getRank()));
            text.setTextColor(context.getResources().getColor(R.color.text_normal));
        } else {
            text.setText(context.getString(R.string.not_ranked));
            text.setTextColor(context.getResources().getColor(R.color.red));
        }

        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        tableRow.addView(text);

        return tableRow;
    }
}
