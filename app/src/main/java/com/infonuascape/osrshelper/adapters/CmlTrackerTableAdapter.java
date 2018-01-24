package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Utils;

import java.text.NumberFormat;

/**
 * Created by marc_ on 2018-01-14.
 */

public class CmlTrackerTableAdapter {
    private Context context;
    private TableLayout tableLayout;

    public CmlTrackerTableAdapter(final Context context, final TableLayout tableLayout) {
        this.context = context;
        this.tableLayout = tableLayout;
    }

    public void fill(final PlayerSkills playerSkills) {
        tableLayout.removeAllViews();

        final boolean isShowVirtualLevels = Utils.isShowVirtualLevels(context);

        //Add skills individually to the table
        for (Skill s : playerSkills.skillList) {
            tableLayout.addView(getView(s, isShowVirtualLevels));
        }
    }

    private View getView(final Skill s, final boolean isShowVirtualLevels) {
        View view = View.inflate(context, R.layout.cml_tracker_table_row, null);

        ((ImageView) view.findViewById(R.id.cml_table_item_icon)).setImageResource(s.getDrawableInt());
        ((TextView) view.findViewById(R.id.cml_table_item_lvl)).setText(String.valueOf(isShowVirtualLevels ? s.getVirtualLevel() : s.getLevel()));
        ((TextView) view.findViewById(R.id.cml_table_item_xp)).setText(NumberFormat.getInstance().format(s.getExperience()));

        int expDiff = s.getExperienceDiff();
        int textColorResId;
        String text;
        if (expDiff == 0) {
            textColorResId = R.color.dark_gray;
            text = context.getString(R.string.gain_small, expDiff);
        } else {
            textColorResId = R.color.green;
            if (expDiff < 1000) {
                text = context.getString(R.string.gain_small, expDiff);

            } else if (expDiff >= 1000 && expDiff < 10000) {
                text = context.getString(R.string.gain_medium, expDiff / 1000.0f);

            } else {
                text = context.getString(R.string.gain, expDiff / 1000);
            }
        }
        ((TextView) view.findViewById(R.id.cml_table_item_diff_xp)).setTextColor(context.getResources().getColor(textColorResId));
        ((TextView) view.findViewById(R.id.cml_table_item_diff_xp)).setText(text);


        int rankDiff = s.getRankDiff();

        if (rankDiff == 0) {
            textColorResId = R.color.dark_gray;
            text = context.getString(R.string.gain_small, rankDiff);
        } else {

            //set appropriate gain color
            if (rankDiff > 0) {
                textColorResId = R.color.green;
            } else {
                textColorResId = R.color.red;
            }


            //ranks "lost" AKA progress were made
            if (rankDiff > 0 && rankDiff < 1000) {
                text = context.getString(R.string.gain_small, rankDiff);
            } else if (rankDiff >= 1000 && rankDiff < 10000) {
                text = context.getString(R.string.gain_medium, rankDiff / 1000.0f);
            } else if (rankDiff > 10000) {
                text = context.getString(R.string.gain, rankDiff / 1000);
            }

            //ranks "gained" AKA no progress were made
            else if (rankDiff < 0 && rankDiff > -1000) {
                text = context.getString(R.string.loss_small, Math.abs(rankDiff));
            } else if (rankDiff <= -1000 && rankDiff > -1000) {
                text = context.getString(R.string.loss_medium, Math.abs(rankDiff) / 1000.0f);
            } else {
                text = context.getString(R.string.loss, Math.abs(rankDiff) / 1000);
            }
        }
        ((TextView) view.findViewById(R.id.cml_table_item_diff_rank)).setTextColor(context.getResources().getColor(textColorResId));
        ((TextView) view.findViewById(R.id.cml_table_item_diff_rank)).setText(text);

        return view;
    }
}
