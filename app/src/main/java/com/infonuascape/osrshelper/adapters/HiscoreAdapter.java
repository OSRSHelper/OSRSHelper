package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.HiscoreItem;
import com.infonuascape.osrshelper.models.HiscoreRankScoreItem;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc_ on 2018-01-14.
 */

public class HiscoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<HiscoreAdapter.HeaderHolder> {
    private final boolean isShowVirtualLevels;
    private Context context;
    private List<HiscoreItem> hiscoreItems = new ArrayList<>();

    private final int VIEW_TYPE_SKILLS = 0;
    private final int VIEW_TYPE_RANK_SCORE = 1;

    public HiscoreAdapter(final Context context) {
        this.context = context;
        isShowVirtualLevels = Utils.isShowVirtualLevels(context);
    }

    public void setHiscoreItems(List<HiscoreItem> hiscoreItems) {
        this.hiscoreItems = hiscoreItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SKILLS) {
            return new SkillHolder(LayoutInflater.from(context).inflate(R.layout.hiscore_skill_item, parent, false));
        } else if (viewType == VIEW_TYPE_RANK_SCORE) {
            return new RankScoreHolder(LayoutInflater.from(context).inflate(R.layout.hiscore_rank_score_item, parent, false));
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (hiscoreItems.get(position) instanceof Skill) {
            return VIEW_TYPE_SKILLS;
        }
        return VIEW_TYPE_RANK_SCORE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_SKILLS) {
            bindViewHolder((SkillHolder) holder, (Skill) hiscoreItems.get(position));
        } else if (viewType == VIEW_TYPE_RANK_SCORE) {
            bindViewHolder((RankScoreHolder) holder, (HiscoreRankScoreItem) hiscoreItems.get(position));
        }
    }

    private void bindViewHolder(SkillHolder holder, Skill skill) {
        holder.icon.setImageResource(skill.getSkillType().getDrawableInt());
        holder.name.setText(skill.getSkillType().getSkillName());
        holder.level.setText(String.valueOf(isShowVirtualLevels ? skill.getVirtualLevel() : skill.getLevel()));
        holder.experience.setText(context.getString(R.string.hiscore_experience, NumberFormat.getInstance().format(skill.getExperience())));
        holder.rank.setText(NumberFormat.getInstance().format(skill.getRank()));
    }

    private void bindViewHolder(RankScoreHolder holder, HiscoreRankScoreItem hiscoreRankScoreItem) {
        if (!TextUtils.isEmpty(hiscoreRankScoreItem.name)) {
            holder.name.setText(hiscoreRankScoreItem.name);
            holder.name.setVisibility(View.VISIBLE);
        } else {
            holder.name.setVisibility(View.GONE);
        }
        holder.score.setText(NumberFormat.getInstance().format(hiscoreRankScoreItem.score));
        holder.rank.setText(NumberFormat.getInstance().format(hiscoreRankScoreItem.rank));
    }

    @Override
    public long getHeaderId(int position) {
        return hiscoreItems.get(position).getTitleName();
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.hiscore_header_item, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder headerHolder, int i) {
        headerHolder.title.setText(hiscoreItems.get(i).getTitleName());
    }

    @Override
    public int getItemCount() {
        return hiscoreItems.size();
    }

    protected class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    protected class RankScoreHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView score;
        public TextView rank;

        public RankScoreHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            rank = itemView.findViewById(R.id.rank);
        }
    }

    protected class SkillHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView level;
        public TextView experience;
        public TextView rank;

        public SkillHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.skill_image);
            name = itemView.findViewById(R.id.skill_name);
            level = itemView.findViewById(R.id.skill_level);
            experience = itemView.findViewById(R.id.skill_experience);
            rank = itemView.findViewById(R.id.skill_rank);
        }
    }
}
