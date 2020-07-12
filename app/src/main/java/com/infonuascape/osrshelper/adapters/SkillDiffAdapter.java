package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.players.SkillDiff;
import com.infonuascape.osrshelper.utils.Logger;

import java.text.NumberFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by marc_ on 2018-01-20.
 */

public class SkillDiffAdapter extends RecyclerView.Adapter<SkillDiffAdapter.DeltaHolder>{
    private static final String TAG = "SkillDiffAdapter";

    private Context context;
    private List<SkillDiff> skillDiffs;

    public SkillDiffAdapter(final Context context, final List<SkillDiff> skillDiffs) {
        this.context = context;
        this.skillDiffs = skillDiffs;
    }

    @Override
    public DeltaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logger.add(TAG, ": onCreateViewHolder:");
        View rootView = View.inflate(context, R.layout.profile_delta_skilldiff, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new DeltaHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DeltaHolder holder, int position) {
        Logger.add(TAG, ": onBindViewHolder:");
        SkillDiff delta = getItem(position);

        holder.experience.setText(NumberFormat.getInstance().format(delta.experience));
        holder.rank.setText(NumberFormat.getInstance().format(delta.rank));
        Glide.with(holder.icon).asBitmap().load(delta.skillType.getDrawableInt()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return skillDiffs.size();
    }

    public SkillDiff getItem(int position) {
        return skillDiffs.get(position);
    }

    class DeltaHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView experience;
        TextView rank;

        public DeltaHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.skill_image);
            experience = itemView.findViewById(R.id.experience);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}
