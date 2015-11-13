package com.infonuascape.osrshelper.adapters;

import java.util.ArrayList;
import java.util.Map;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;

public class PoIAdapter extends BaseAdapter {
    private final ArrayList<PointOfInterest> mData;

    public PoIAdapter(ArrayList<PointOfInterest> map) {
        mData = new ArrayList<PointOfInterest>();
        mData.addAll(map);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PointOfInterest getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_listitem, parent, false);
        } else {
            result = convertView;
        }

        PointOfInterest item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getName());

        return result;
    }
}