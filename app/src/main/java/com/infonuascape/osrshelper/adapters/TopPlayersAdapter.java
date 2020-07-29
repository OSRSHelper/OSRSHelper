package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by marc_ on 2018-01-14.
 */

public class TopPlayersAdapter extends RecyclerView.Adapter<TopPlayersAdapter.TopHolder> {
    private Context context;
    private final ArrayList<Skill> skills;
    private RecyclerItemClickListener listener;

    public TopPlayersAdapter(final Context context, final RecyclerItemClickListener listener) {
        this.context = context;
        this.skills = PlayerSkills.getSkillsInOrderForRSView(new PlayerSkills());
        this.listener = listener;
    }

    @Override
    public TopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopHolder(LayoutInflater.from(context).inflate(R.layout.top_player_item, null));
    }

    @Override
    public void onBindViewHolder(TopHolder holder, int position) {
        Skill skill = skills.get(position);
        holder.icon.setImageResource(skill.getSkillType().getDrawableInt());
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public Skill getItem(final int position) {
        return skills.get(position);
    }

    protected class TopHolder extends RecyclerView.ViewHolder {
        public ImageView icon;

        public TopHolder(View itemView) {
            super(itemView);

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
