package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.Account;

import java.util.List;

public class UsernamesAdapter extends ArrayAdapter<Account> {
	private List<Account> accounts;

	public UsernamesAdapter(Context context, List<Account> accounts) {
		super(context, R.layout.username_list_item, accounts);
		this.accounts = accounts;
	}

	class ViewHolder {
		TextView username;
		ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View result;
		ViewHolder holder;

		if (convertView == null) {
			result = LayoutInflater.from(parent.getContext()).inflate(R.layout.username_list_item, parent, false);
			holder = new ViewHolder();
			holder.username = result.findViewById(R.id.username);
			holder.image = result.findViewById(R.id.icon);
			result.setTag(holder);
		} else {
			result = convertView;
		}

		holder = (ViewHolder) result.getTag();
		Account account = accounts.get(position);

		holder.username.setText(account.username);

		switch(account.type) {
			case REGULAR:
				holder.image.setImageResource(0);
				break;
			case IRONMAN:
				holder.image.setImageResource(R.drawable.ironman);
				break;
			case ULTIMATE_IRONMAN:
				holder.image.setImageResource(R.drawable.ult_ironman);
				break;
			case HARDCORE_IRONMAN:
				holder.image.setImageResource(R.drawable.hc_ironman);
				break;
		}

		return result;
	}
}