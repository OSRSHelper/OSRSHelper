package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    private String dailyTrendText;

    public SearchAdapter(Context context, ArrayList<Item> map) {
        super(context, R.layout.search_listitem, map);
        this.mContext = context;
        items = map;
        dailyTrendText = mContext.getResources().getString(R.string.daily_trend);
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


        int trendingColor = R.color.dark_gray;

        if(item.today.rate == Item.TrendRate.POSITIVE) {
            trendingColor = R.color.green;
        } else if(item.today.rate == Item.TrendRate.NEGATIVE) {
            trendingColor = R.color.red;
        }


        SpannableString text = new SpannableString(item.today.value + " " + dailyTrendText);
        text.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(trendingColor)), 0, item.today.value.length(), 0);
        holder.trending.setText(text);
        Glide.with(mContext).load(item.iconLarge).into(holder.image);

        if(item.members) {
            holder.member.setVisibility(View.VISIBLE);
        } else {
            holder.member.setVisibility(View.GONE);
        }

        return result;
    }
}