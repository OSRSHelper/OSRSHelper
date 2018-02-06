package com.infonuascape.osrshelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.utils.Logger;

public class OSRSDatabase extends SQLiteOpenHelper {
	private static final String TAG = "DBController";

	public static final String TABLE_USERNAMES = "osrshelper_usernames";
	public static final String TABLE_WIDGET = "osrshelper_widgets";
	public static final String TABLE_GRAND_EXCHANGE = "grand_exchange";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_ACCOUNT_TYPE = "account_type";
	public static final String COLUMN_TIME_USED = "lastused";
	public static final String COLUMN_IS_PROFILE = "is_profile";
	public static final String COLUMN_IS_FOLLOWING = "is_following";
	public static final String COLUMN_COMBAT_LVL = "combat_lvl";


	public static final String COLUMN_WIDGET_ID = "widgetid";

	public static final String COLUMN_ITEM_ID = "item_id";
	public static final String COLUMN_ITEM_NAME = "item_name";
	public static final String COLUMN_ITEM_DESCRIPTION = "item_description";
	public static final String COLUMN_ITEM_IMAGE = "item_image";
	public static final String COLUMN_IS_MEMBERS = "is_members";
	public static final String COLUMN_IS_STARRED = "is_starred";
	public static final String COLUMN_PRICE_WANTED = "price_wanted";

	private static final String DATABASE_NAME = "osrshelper.db";
	private static final int DATABASE_VERSION = 8;

	// Database creation sql statement
	private static final String DATABASE_CREATE_USERNAMES = "CREATE TABLE " + TABLE_USERNAMES + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE + " TEXT, " + COLUMN_TIME_USED + " INTEGER, " + COLUMN_COMBAT_LVL
			+ " INTEGER, " + COLUMN_IS_PROFILE + " INTEGER DEFAULT 0, " + COLUMN_IS_FOLLOWING + " INTEGER DEFAULT 0);";

	private static final String DATABASE_CREATE_WIDGET = "CREATE TABLE " + TABLE_WIDGET + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WIDGET_ID + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE + " TEXT, " + COLUMN_USERNAME + " TEXT);";

	private static final String DATABASE_CREATE_GRAND_EXCHANGE = "CREATE TABLE " + TABLE_GRAND_EXCHANGE + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ITEM_ID + " TEXT, " + COLUMN_ITEM_NAME + " TEXT, "
			+ COLUMN_ITEM_DESCRIPTION + " TEXT, " + COLUMN_ITEM_IMAGE + " TEXT, " + COLUMN_WIDGET_ID + " TEXT, "
			+ COLUMN_IS_STARRED + " INTEGER, " + COLUMN_IS_MEMBERS + " INTEGER, " + COLUMN_PRICE_WANTED + " INTEGER);";

	public static final String AUTHORITY = "com.infonuascape.osrshelper.provider";
	public static final String ACCOUNTS_TABLE = "ACCOUNTS_TABLE";
	public static final String WIDGETS_TABLE = "WIDGETS_TABLE";
	public static final String GRAND_EXCHANGE_TABLE = "GRAND_EXCHANGE_TABLE";

	public static final Uri ACCOUNTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ACCOUNTS_TABLE);
	public static final Uri WIDGETS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + WIDGETS_TABLE);
	public static final Uri GRAND_EXCHANGE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + GRAND_EXCHANGE_TABLE);

	public OSRSDatabase(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_USERNAMES);
		database.execSQL(DATABASE_CREATE_WIDGET);
		database.execSQL(DATABASE_CREATE_GRAND_EXCHANGE);
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

		if(oldVersion < 7) {
			db.execSQL(DATABASE_CREATE_GRAND_EXCHANGE);
		}

		if(oldVersion < 8) {
			db.execSQL("ALTER TABLE " + TABLE_USERNAMES + " ADD COLUMN " + COLUMN_COMBAT_LVL + " INTEGER");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		Logger.add(TAG, ": finalize");

		SQLiteDatabase dbWritable = getWritableDatabase();
		if (dbWritable != null) {
			Logger.add(TAG, ": finalize: closing db");
			dbWritable.close();
		}
		super.finalize();
	}
}
