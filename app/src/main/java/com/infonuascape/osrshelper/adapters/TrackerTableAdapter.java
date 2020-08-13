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

public class TrackerTableAdapter {
    private Context context;
    private TableLayout tableLayout;

    public TrackerTableAdapter(final Context context, final TableLayout tableLayout) {
        this.context = context;
        this.tableLayout = tableLayout;
    }

    public boolean isEmpty() {
        return tableLayout.getChildCount() == 0;
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
        View view = View.inflate(context, R.layout.tracker_table_row, null);

        ((ImageView) view.findViewById(R.id.table_item_icon)).setImageResource(s.getSkillType().getDrawableInt());
        ((TextView) view.findViewById(R.id.table_item_lvl)).setText(String.valueOf(isShowVirtualLevels ? s.getVirtualLevel() : s.getLevel()));
        if (s.getEHP() == -1) {
            ((TextView) view.findViewById(R.id.table_item_ehp)).setText(R.string.not_available);
        } else {
            ((TextView) view.findViewById(R.id.table_item_ehp)).setText(NumberFormat.getInstance().format(s.getEHP()));
        }

        long expDiff = s.getExperienceDiff();
        int textColorResId;
        String text;
        if (expDiff == 0) {
            textColorResId = R.color.dark_gray;
        } else {
            textColorResId = R.color.green;
        }
        ((TextView) view.findViewById(R.id.table_item_diff_xp)).setTextColor(context.getResources().getColor(textColorResId));
        ((TextView) view.findViewById(R.id.table_item_diff_xp)).setText(NumberFormat.getInstance().format(expDiff));


        long rankDiff = s.getRankDiff();

        if (rankDiff == 0) {
            textColorResId = R.color.dark_gray;
            text = context.getString(R.string.gain_small, NumberFormat.getInstance().format(rankDiff));
        } else {

            //set appropriate gain color
            if (rankDiff > 0) {
                textColorResId = R.color.red;
            } else {
                textColorResId = R.color.green;
            }


            //ranks
            long rankDiffAbs = Math.abs(rankDiff);
            if (rankDiffAbs > 0 && rankDiffAbs < 1000) {
                text = context.getString(R.string.gain_small, NumberFormat.getInstance().format(rankDiffAbs));
            } else if (rankDiffAbs >= 1000 && rankDiffAbs < 10000) {
                text = context.getString(R.string.gain_medium, rankDiffAbs / 1000.0f);
            } else {
                text = context.getString(R.string.gain, rankDiffAbs / 1000);
            }
        }
        ((TextView) view.findViewById(R.id.table_item_diff_rank)).setTextColor(context.getResources().getColor(textColorResId));
        ((TextView) view.findViewById(R.id.table_item_diff_rank)).setText(text);

        if (rankDiff > 0) {
            ((ImageView) view.findViewById(R.id.table_item_diff_rank_image)).setImageResource(R.drawable.delta_down);
        } else if(rankDiff < 0) {
            ((ImageView) view.findViewById(R.id.table_item_diff_rank_image)).setImageResource(R.drawable.delta_up);
        } else {
            ((ImageView) view.findViewById(R.id.table_item_diff_rank_image)).setImageResource(R.drawable.delta_neutral);
        }

        return view;
    }
}
