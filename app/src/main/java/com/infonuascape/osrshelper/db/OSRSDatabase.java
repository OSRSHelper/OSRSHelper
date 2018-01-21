package com.infonuascape.osrshelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.infonuascape.osrshelper.enums.AccountType;

public class OSRSDatabase extends SQLiteOpenHelper {
	private static final String TAG = "DBController";

	public static final String TABLE_USERNAMES = "osrshelper_usernames";
	public static final String TABLE_WIDGET = "osrshelper_widgets";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_ACCOUNT_TYPE = "account_type";
	public static final String COLUMN_TIME_USED = "lastused";
	public static final String COLUMN_IS_PROFILE = "is_profile";
	public static final String COLUMN_IS_FOLLOWING = "is_following";

	public static final String COLUMN_WIDGET_ID = "widgetid";

	private static final String DATABASE_NAME = "osrshelper.db";
	private static final int DATABASE_VERSION = 6;

	// Database creation sql statement
	private static final String DATABASE_CREATE_USERNAMES = "CREATE TABLE " + TABLE_USERNAMES + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE + " TEXT, " + COLUMN_TIME_USED + " INTEGER, " + COLUMN_IS_PROFILE + " INTEGER DEFAULT 0, " + COLUMN_IS_FOLLOWING + " INTEGER DEFAULT 0);";

	private static final String DATABASE_CREATE_WIDGET = "CREATE TABLE " + TABLE_WIDGET + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WIDGET_ID + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE + " TEXT, " + COLUMN_USERNAME + " TEXT);";

	public static final String AUTHORITY = "com.infonuascape.osrshelper.provider";
	public static final String ACCOUNTS_TABLE = "ACCOUNTS_TABLE";
	public static final String WIDGETS_TABLE = "WIDGETS_TABLE";

	public static final Uri ACCOUNTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ACCOUNTS_TABLE);
	public static final Uri WIDGETS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + WIDGETS_TABLE);

	public OSRSDatabase(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_USERNAMES);
		database.execSQL(DATABASE_CREATE_WIDGET);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
		if(oldVersion < 4) {
			db.execSQL("ALTER TABLE " + TABLE_USERNAMES + " ADD COLUMN " + COLUMN_ACCOUNT_TYPE + " TEXT");
			db.execSQL("ALTER TABLE " + TABLE_WIDGET + " ADD COLUMN " + COLUMN_ACCOUNT_TYPE + " TEXT");

			db.execSQL("UPDATE " + TABLE_USERNAMES + " SET " + COLUMN_ACCOUNT_TYPE + "='" + AccountType.REGULAR.name() + "'");
			db.execSQL("UPDATE " + TABLE_WIDGET + " SET " + COLUMN_ACCOUNT_TYPE + "='" + AccountType.REGULAR.name() + "'");
		}

		if(oldVersion < 5) {
			db.execSQL("ALTER TABLE " + TABLE_USERNAMES + " ADD COLUMN " + COLUMN_IS_PROFILE + " INTEGER DEFAULT 0");
		}

		if(oldVersion < 6) {
			db.execSQL("ALTER TABLE " + TABLE_USERNAMES + " ADD COLUMN " + COLUMN_IS_FOLLOWING+ " INTEGER DEFAULT 0");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		Log.i(TAG , " finalize");

		SQLiteDatabase dbWritable = getWritableDatabase();
		if (dbWritable != null) {
			Log.i(TAG , "finalize: closing db");
			dbWritable.close();
		}
		super.finalize();
	}
}
