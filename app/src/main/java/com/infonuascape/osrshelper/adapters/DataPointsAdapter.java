package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.utils.Logger;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by marc_ on 2018-01-20.
 */

public class DataPointsAdapter extends RecyclerView.Adapter<DataPointsAdapter.DeltaHolder>{
    private static final String TAG = "ProfileDeltasAdapter";

    private Context context;
    private List<Delta> deltas;
    private SimpleDateFormat simpleDateFormat;

    public DataPointsAdapter(final Context context, final List<Delta> deltas) {
        this.context = context;
        this.deltas = deltas;
        simpleDateFormat = new SimpleDateFormat("MMM dd HH:mm");
    }

    @Override
    public DeltaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logger.add(TAG, ": onCreateViewHolder:");
        View rootView = View.inflate(context, R.layout.data_points_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new DeltaHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DeltaHolder holder, int position) {
        Logger.add(TAG, ": onBindViewHolder:");
        Delta delta = getItem(position);
        final String date1 = simpleDateFormat.format(new Date(delta.timestamp));
        final String date2 = simpleDateFormat.format(new Date(delta.timestampRecent));
        holder.date.setText(context.getResources().getString(R.string.profile_delta_on, date1, date2));

        if (delta.ehpValue > 0 || delta.ehpRank > 0) {
            holder.ehpContainer.setVisibility(View.VISIBLE);
            holder.ehpValue.setText(NumberFormat.getInstance().format(delta.ehpValue));

            if (delta.ehpRank > 0) {
                holder.ehpRankImage.setImageResource(R.drawable.delta_down);
            } else if (delta.ehpRank < 0) {
                holder.ehpRankImage.setImageResource(R.drawable.delta_up);
            } else {
                holder.ehpRankImage.setImageResource(R.drawable.delta_neutral);
            }
            holder.ehpRank.setText(NumberFormat.getInstance().format(Math.abs(delta.ehpRank)));
        } else {
            holder.ehpContainer.setVisibility(View.GONE);
        }

        holder.adapter = new SkillDiffAdapter(context, delta.skillDiffs);
        holder.deltasList.setAdapter(holder.adapter);
    }

    @Override
    public int getItemCount() {
        return deltas.size();
    }

    public Delta getItem(int position) {
        return deltas.get(position);
    }

    class DeltaHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView deltasList;
        private GridLayoutManager gridLayoutManager;
        private SkillDiffAdapter adapter;

        private View ehpContainer;
        private TextView ehpValue;
        private TextView ehpRank;
        private ImageView ehpRankImage;

        public DeltaHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            deltasList = itemView.findViewById(R.id.delta_list);

            gridLayoutManager = new GridLayoutManager(context ,2);
            deltasList.setLayoutManager(gridLayoutManager);

            ehpContainer = itemView.findViewById(R.id.ehp_container);
            ehpValue = itemView.findViewById(R.id.ehp_value);
            ehpRank = itemView.findViewById(R.id.ehp_rank);
            ehpRankImage = itemView.findViewById(R.id.ehp_rank_image);
        }
    }
}
