package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.grandexchange.Item;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private ArrayList<Item> items;

    public SearchAdapter(Context context, ArrayList<Item> map) {
        super(context, R.layout.search_listitem, map);
        this.mContext = context;
        items = map;
    }

    class ViewHolder {
        TextView name;
        TextView description;
        ImageView image;
        ImageView member;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        ViewHolder holder;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_listitem, parent, false);
            holder = new ViewHolder();
            holder.name = result.findViewById(R.id.item_name);
            holder.description = result.findViewById(R.id.item_desc);
            holder.image = result.findViewById(R.id.item_image);
            holder.member = result.findViewById(R.id.item_member);
            result.setTag(holder);
        } else {
            result = convertView;
        }

        holder = (ViewHolder) result.getTag();
        Item item = items.get(position);

        holder.name.setText(item.name);
        holder.description.setText(item.description);

        Glide.with(mContext).asBitmap().load(item.iconLarge).into(holder.image);

        if(item.members) {
            holder.member.setVisibility(View.VISIBLE);
        } else {
            holder.member.setVisibility(View.GONE);
        }

        return result;
    }
}