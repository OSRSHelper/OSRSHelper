package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.QuickAction;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by marc_ on 2018-01-21.
 */

public class AccountQuickActionsAdapter extends RecyclerView.Adapter<AccountQuickActionsAdapter.QuickActionViewHolder> {
    private Context context;
    private List<QuickAction> quickActions;
    private RecyclerItemClickListener listener;

    public AccountQuickActionsAdapter(final Context context, final List<QuickAction> quickActions, final RecyclerItemClickListener listener) {
        this.context = context;
        this.quickActions = quickActions;
        this.listener = listener;
    }

    @Override
    public QuickActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuickActionViewHolder(View.inflate(context, R.layout.quick_action_item, null));
    }

    @Override
    public void onBindViewHolder(QuickActionViewHolder holder, int position) {
        QuickAction quickAction = quickActions.get(position);

        int resId = 0;
        int nameId = 0;
        if(quickAction == QuickAction.HISCORES) {
            resId = R.drawable.hiscore;
            nameId = R.string.highscores;
        } else if(quickAction == QuickAction.XP_TRACKER) {
            resId = R.drawable.xptrack;
            nameId = R.string.xptracker;
        } else if(quickAction == QuickAction.COMBAT_CALC) {
            resId = R.drawable.calculator;
            nameId = R.string.combat_lvl_calculator;
        }

        holder.icon.setImageResource(resId);
        holder.name.setText(nameId);
    }

    public QuickAction getItem(final int position) {
        return quickActions.get(position);
    }

    @Override
    public int getItemCount() {
        return quickActions.size();
    }

    class QuickActionViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public QuickActionViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.quick_action_icon);
            name = itemView.findViewById(R.id.quick_action_title);

            itemView.findViewById(R.id.quick_action_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        listener.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}
