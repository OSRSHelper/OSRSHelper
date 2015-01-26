package com.infonuascape.osrshelper.views;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;

public class GridViewRSViewAdapter extends BaseAdapter {
	private Context context;
	private final ArrayList<Skill> skills;

	public GridViewRSViewAdapter(Context context, ArrayList<Skill> skills) {
		this.context = context;
		this.skills = skills;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			Skill skill = skills.get(position);

			convertView = new View(context);

			convertView = inflater.inflate(R.layout.rs_view_item, null);

			// set value into textview
			TextView textView = (TextView) convertView.findViewById(R.id.skill_level);
			textView.setText(skill.getLevel() + "");

			// set image based on selected text
			ImageView imageView = (ImageView) convertView.findViewById(R.id.skill_image);
			imageView.setImageResource(skill.getDrawableInt());

		}

		return convertView;
	}

	@Override
	public int getCount() {
		return skills.size();
	}

	@Override
	public Skill getItem(int position) {
		return skills.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}