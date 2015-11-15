package com.infonuascape.osrshelper.adapters;

import android.content.Context;
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
    private final ArrayList<Item> mData;
    private Context mContext;

    public SearchAdapter(Context context, ArrayList<Item> map) {
        super(context, R.layout.search_listitem, map);
        this.mContext = context;
        mData = map;
    }

    class ViewHolder {
        TextView name;
        TextView description;
        ImageView image;
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
            result.setTag(holder);
        } else {
            result = convertView;
        }

        holder = (ViewHolder) result.getTag();
        Item item = getItem(position);

        holder.name.setText(item.name);
        holder.description.setText(item.description);
        holder.current.setText((item.current.rate == Item.TrendRate.POSITIVE ? "+" : (item.current.rate == Item.TrendRate.NEGATIVE ? "-" : "")) + item.current.change);
        holder.trending.setText((item.today.rate == Item.TrendRate.POSITIVE ? "+" : (item.today.rate == Item.TrendRate.NEGATIVE ? "-" : "")) + item.today.change);
        Picasso.with(mContext).load(item.iconLarge).into(holder.image);

        return result;
    }
}