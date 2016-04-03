package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.grandexchange.GESearchResults;
import com.infonuascape.osrshelper.utils.grandexchange.Item;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        TextView current;
        TextView trending;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        ViewHolder holder;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_listitem, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) result.findViewById(R.id.item_name);
            holder.description = (TextView) result.findViewById(R.id.item_desc);
            holder.current = (TextView) result.findViewById(R.id.item_current);
            holder.trending = (TextView) result.findViewById(R.id.item_trend);
            holder.image = (ImageView) result.findViewById(R.id.item_image);
            holder.member = (ImageView) result.findViewById(R.id.item_member);
            result.setTag(holder);
        } else {
            result = convertView;
        }

        holder = (ViewHolder) result.getTag();
        Item item = items.get(position);

        holder.name.setText(item.name);
        holder.description.setText(item.description);
        holder.current.setText(item.current.value);


        if(item.today.rate == Item.TrendRate.POSITIVE) {
            holder.trending.setTextColor(mContext.getResources().getColor(R.color.Green));
        } else if(item.today.rate == Item.TrendRate.NEGATIVE) {
            holder.trending.setTextColor(mContext.getResources().getColor(R.color.Red));
        } else {
            holder.trending.setTextColor(mContext.getResources().getColor(R.color.DarkGray));
        }
        holder.trending.setText(item.today.value);
        Picasso.with(mContext).load(item.iconLarge).into(holder.image);

        if(item.members) {
            holder.member.setVisibility(View.VISIBLE);
        } else {
            holder.member.setVisibility(View.GONE);
        }

        return result;
    }
}