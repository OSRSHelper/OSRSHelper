package com.infonuascape.osrshelper.db;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class OSRSHelperDataSource {

	// Database fields
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

	public void deleteAllUsernames() {
		database.delete(DBController.TABLE_USERNAMES_OSRSHELPER, null, null);
	}

	public void createCredentials(final String username, final String password) {
		final ContentValues values = new ContentValues();
		values.put(DBController.COLUMN_USERNAME_OSRSHELPER, username);
		values.put(DBController.COLUMN_PASSWORD_OSRSHELPER, password);
		database.insert(DBController.TABLE_CREDENTIALS_OSRSHELPER, null, values);
	}

	public Credential getCredentials() {
		Credential credential = null;
		final Cursor cursor = database.query(DBController.TABLE_CREDENTIALS_OSRSHELPER, allColumnsCredentials, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			final String username = cursor.getString(0);
			final String password = cursor.getString(1);
			credential = new Credential(username, password);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return credential;
	}

	public void updateCredentials(final String username, final String password) {
		final ContentValues values = new ContentValues();
		values.put(DBController.COLUMN_USERNAME_OSRSHELPER, username);
		values.put(DBController.COLUMN_PASSWORD_OSRSHELPER, password);
		database.update(DBController.TABLE_CREDENTIALS_OSRSHELPER, values, DBController.COLUMN_ID + "=",
				new String[] { "1" });
	}

}