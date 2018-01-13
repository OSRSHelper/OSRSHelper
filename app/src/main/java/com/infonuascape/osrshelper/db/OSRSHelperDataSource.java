package com.infonuascape.osrshelper.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OSRSHelperDataSource {

	// Database fields
	private final String TAG = "OSRSHelperDataSource";
	private SQLiteDatabase database;
	private final DBController dbHelper;
	private final String[] allColumnsUsernames = { DBController.COLUMN_USERNAME_OSRSHELPER };
	private final String[] allColumnsCredentials = { DBController.COLUMN_USERNAME_OSRSHELPER,
			DBController.COLUMN_PASSWORD_OSRSHELPER };

	public OSRSHelperDataSource(final Context context) {
		dbHelper = new DBController(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addUsername(final String username) {
		final ContentValues values = new ContentValues();
		values.put(DBController.COLUMN_USERNAME_OSRSHELPER, username);
		values.put(DBController.COLUMN_TIME_USED_OSRSHELPER, (int) System.currentTimeMillis());
		database.insert(DBController.TABLE_USERNAMES_OSRSHELPER, null, values);
	}
	
	public void setUsernameForWidget(final int appWidgetId, final String username) {
		final ContentValues values = new ContentValues();

		values.put(DBController.COLUMN_USERNAME_OSRSHELPER, username);
		
		if(getUsernameForWidget(appWidgetId) == null) {
			Log.i(TAG, "setUsernameForWidget: insert: appWidgetId=" + appWidgetId + " username=" + username);
			values.put(DBController.COLUMN_WIDGET_ID_OSRSHELPER, String.valueOf(appWidgetId));
			database.insert(DBController.TABLE_WIDGET_OSRSHELPER, null, values);
		} else {
			Log.i(TAG, "setUsernameForWidget: update: appWidgetId=" + appWidgetId + " username=" + username);
			database.update(DBController.TABLE_WIDGET_OSRSHELPER, values, DBController.COLUMN_WIDGET_ID_OSRSHELPER + "=?", new String[]{String.valueOf(appWidgetId)});
		}
	}
	
	public String getUsernameForWidget(final int appWidgetId) {
		String username = null;
		final Cursor cursor = database.query(DBController.TABLE_WIDGET_OSRSHELPER, new String[]{DBController.COLUMN_USERNAME_OSRSHELPER}, DBController.COLUMN_WIDGET_ID_OSRSHELPER + "=?", new String[]{String.valueOf(appWidgetId)}, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			username = cursor.getString(0);
			break;
		}
		cursor.close();
		Log.i(TAG, "getUsernameForWidget: username=" + username + " appWidgetId=" + appWidgetId);
		return username;
	}

	public ArrayList<String> getAllUsernames() {
		final ArrayList<String> usernames = new ArrayList<String>();

		final Cursor cursor = database.query(DBController.TABLE_USERNAMES_OSRSHELPER, allColumnsUsernames, null, null,
				DBController.COLUMN_USERNAME_OSRSHELPER, null, DBController.COLUMN_TIME_USED_OSRSHELPER + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			final String username = cursor.getString(0);
			usernames.add(username);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return usernames;
	}
	
	public void deleteUsername(final String username) {
		database.delete(DBController.TABLE_USERNAMES_OSRSHELPER, DBController.COLUMN_USERNAME_OSRSHELPER + "=?", new String[]{username});
	}

	public void deleteAllUsernames() {
		database.delete(DBController.TABLE_USERNAMES_OSRSHELPER, null, null);
	}

}