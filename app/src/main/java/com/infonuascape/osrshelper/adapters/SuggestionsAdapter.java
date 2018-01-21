package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Utils;

public class SuggestionsAdapter extends CursorAdapter {

	public SuggestionsAdapter(Context context, Cursor c) {
		super(context, c);
	}

	class ViewHolder {
		TextView username;
		ImageView image;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		final View result;
		ViewHolder holder;
		result = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.username_list_item, viewGroup, false);
		holder = new ViewHolder();
		holder.username = result.findViewById(R.id.username);
		holder.image = result.findViewById(R.id.icon);
		result.setTag(holder);

		return result;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Account account = DBController.createAccountFromCursor(cursor);

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.username.setText(account.username);
		holder.image.setImageResource(Utils.getAccountTypeResource(account.type));
	}
}