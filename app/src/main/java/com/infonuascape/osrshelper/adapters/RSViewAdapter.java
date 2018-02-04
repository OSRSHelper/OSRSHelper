package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Utils;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-01-14.
 */

public class RSViewAdapter extends RecyclerView.Adapter<RSViewAdapter.RSViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_DONT_SHOW_ICON = 2;
    private static final int VIEW_TYPE_DONT_SHOW_LEVEL = 3;

    private Context context;
    private final ArrayList<Skill> skills;
    private boolean isShowAbove99;
    private RecyclerItemClickListener listener;
    private boolean isShowLevel;

    public RSViewAdapter(final Context context, final PlayerSkills playerSkills, final RecyclerItemClickListener listener) {
        this.context = context;
        this.skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
        this.listener = listener;
        isShowAbove99 = Utils.isShowVirtualLevels(context);
        isShowLevel = true;
    }

    public RSViewAdapter(final Context context, final PlayerSkills playerSkills, final RecyclerItemClickListener listener, final boolean isShowLevel) {
        this.context = context;
        this.skills = PlayerSkills.getSkillsInOrderForRSView(playerSkills);
        this.listener = listener;
        isShowAbove99 = Utils.isShowVirtualLevels(context);
        this.isShowLevel = isShowLevel;
    }

    @Override
    public int getItemViewType(int position) {
        SkillType skillType = skills.get(position).getSkillType();

        if(!isShowLevel) {
            return VIEW_TYPE_DONT_SHOW_LEVEL;
        } else if(skillType == SkillType.Overall) {
            return VIEW_TYPE_DONT_SHOW_ICON;
        }

        return VIEW_TYPE_NORMAL;
    }

    @Override
    public RSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DONT_SHOW_ICON) {
            return new RSViewHolder(LayoutInflater.from(context).inflate(R.layout.rs_view_item_no_icon, null));
        } else if(viewType == VIEW_TYPE_DONT_SHOW_LEVEL) {
            return new RSViewHolder(LayoutInflater.from(context).inflate(R.layout.rs_view_item_no_level, null));
        } else {
            return new RSViewHolder(LayoutInflater.from(context).inflate(R.layout.rs_view_item, null));
        }
    }

    @Override
    public void onBindViewHolder(RSViewHolder holder, int position) {
        Skill skill = skills.get(position);
        int viewType = getItemViewType(position);

        if(viewType != VIEW_TYPE_DONT_SHOW_LEVEL) {
            holder.skillLvl.setText((isShowAbove99 ? skill.getVirtualLevel() : skill.getLevel()) + "");
        }

        if(viewType != VIEW_TYPE_DONT_SHOW_ICON) {
            holder.icon.setImageResource(skill.getSkillType().getDrawableInt());
        }
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public Skill getItem(final int position) {
        return skills.get(position);
    }

    protected class RSViewHolder extends RecyclerView.ViewHolder {
        public TextView skillLvl;
        public ImageView icon;

        public RSViewHolder(View itemView) {
            super(itemView);

            skillLvl = itemView.findViewById(R.id.skill_level);
            icon = itemView.findViewById(R.id.skill_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        listener.onItemClicked(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener != null) {
                        listener.onItemLongClicked(getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }
}
