package com.infonuascape.osrshelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;

import java.util.ArrayList;

public class PoIAdapter extends RecyclerView.Adapter<PoIAdapter.ViewHolder> {
    private static final String TAG = "PoIAdapter";

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private final ArrayList<PointOfInterest> mData;
    private OnItemClickListener listener;

    public PoIAdapter(ArrayList<PointOfInterest> map) {
        mData = new ArrayList<>();
        mData.addAll(map);
    }

    public PointOfInterest getItem(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_listitem, parent, false), listener);
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_listitem_header, parent, false), null);
        }
    }

    @Override
    public int getItemViewType(int position) {
        PointOfInterest pointOfInterest = getItem(position);
        if (pointOfInterest instanceof PointOfInterestHeader) {
            return VIEW_TYPE_HEADER;
        }

        return VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(getItem(position).name);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);

            if (listener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClicked(getAdapterPosition());
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
}