package com.infonuascape.osrshelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERNAME_OSRSHELPER = "username";

	public static final String TABLE_USERNAMES_OSRSHELPER = "osrshelper_usernames";
	public static final String COLUMN_TIME_USED_OSRSHELPER = "lastused";

	public static final String TABLE_CREDENTIALS_OSRSHELPER = "osrshelper_credentials";
	public static final String COLUMN_PASSWORD_OSRSHELPER = "password";
	
	public static final String TABLE_WIDGET_OSRSHELPER = "osrshelper_widgets";
	public static final String COLUMN_WIDGET_ID_OSRSHELPER = "widgetid";

	private static final String DATABASE_NAME = "osrshelper.db";
	private static final int DATABASE_VERSION = 3;

	// Database creation sql statement
	private static final String DATABASE_CREATE_USERNAMES = "create table if not exists " + TABLE_USERNAMES_OSRSHELPER + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_USERNAME_OSRSHELPER + " text not null, "
			+ COLUMN_TIME_USED_OSRSHELPER + " integer not null);";

	private static final String DATABASE_CREATE_CREDENTIALS = "create table if not exists " + TABLE_CREDENTIALS_OSRSHELPER + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_USERNAME_OSRSHELPER + " text not null, "
			+ COLUMN_PASSWORD_OSRSHELPER + " text not null);";
	
	private static final String DATABASE_CREATE_WIDGET = "create table if not exists " + TABLE_WIDGET_OSRSHELPER + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_WIDGET_ID_OSRSHELPER + " text not null, "
			+ COLUMN_USERNAME_OSRSHELPER + " text not null);";

	public DBController(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_USERNAMES);
		database.execSQL(DATABASE_CREATE_CREDENTIALS);
		database.execSQL(DATABASE_CREATE_WIDGET);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(DBController.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERNAMES_OSRSHELPER);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS_OSRSHELPER);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIDGET_OSRSHELPER);
		onCreate(db);
	}

}
