package com.infonuascape.osrshelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.models.Account;

import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper {
	private static final String TAG = "DBController";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERNAME_OSRSHELPER = "username";
	public static final String COLUMN_ACCOUNT_TYPE_OSRSHELPER = "account_type";

	public static final String TABLE_USERNAMES_OSRSHELPER = "osrshelper_usernames";
	public static final String COLUMN_TIME_USED_OSRSHELPER = "lastused";
	
	public static final String TABLE_WIDGET_OSRSHELPER = "osrshelper_widgets";
	public static final String COLUMN_WIDGET_ID_OSRSHELPER = "widgetid";

	private static final String DATABASE_NAME = "osrshelper.db";
	private static final int DATABASE_VERSION = 4;
	private final String[] allColumnsUsernames = { COLUMN_USERNAME_OSRSHELPER, COLUMN_ACCOUNT_TYPE_OSRSHELPER };

	// Database creation sql statement
	private static final String DATABASE_CREATE_USERNAMES = "CREATE TABLE " + TABLE_USERNAMES_OSRSHELPER + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME_OSRSHELPER + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE_OSRSHELPER + " TEXT, " + COLUMN_TIME_USED_OSRSHELPER + " INTEGER);";
	
	private static final String DATABASE_CREATE_WIDGET = "CREATE TABLE " + TABLE_WIDGET_OSRSHELPER + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WIDGET_ID_OSRSHELPER + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE_OSRSHELPER + " TEXT, " + COLUMN_USERNAME_OSRSHELPER + " TEXT);";

	private static DBController instance;

	private DBController(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DBController getInstance(final Context context) {
		if(instance == null) {
			instance = new DBController(context);
		}

		return instance;
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
			db.execSQL("ALTER TABLE " + TABLE_USERNAMES_OSRSHELPER + " ADD COLUMN " + COLUMN_ACCOUNT_TYPE_OSRSHELPER + " TEXT");
			db.execSQL("ALTER TABLE " + TABLE_WIDGET_OSRSHELPER + " ADD COLUMN " + COLUMN_ACCOUNT_TYPE_OSRSHELPER + " TEXT");

			db.execSQL("UPDATE TABLE " + TABLE_USERNAMES_OSRSHELPER + " SET " + COLUMN_ACCOUNT_TYPE_OSRSHELPER + "='" + AccountType.REGULAR.name() + "'");
			db.execSQL("UPDATE TABLE " + TABLE_WIDGET_OSRSHELPER + " SET " + COLUMN_ACCOUNT_TYPE_OSRSHELPER + "='" + AccountType.REGULAR.name() + "'");
		}
	}

	public void addAccount(final Account account) {
		SQLiteDatabase db = getWritableDatabase();
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME_OSRSHELPER, account.username);
		values.put(COLUMN_ACCOUNT_TYPE_OSRSHELPER, account.type.name());
		values.put(COLUMN_TIME_USED_OSRSHELPER, (int) System.currentTimeMillis());
		db.insert(TABLE_USERNAMES_OSRSHELPER, null, values);
		db.close();
	}

	public void setAccountForWidget(final int appWidgetId, final Account account) {
		SQLiteDatabase db = getWritableDatabase();
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME_OSRSHELPER, account.username);
		values.put(COLUMN_ACCOUNT_TYPE_OSRSHELPER, account.type.name());

		if(getAccountForWidget(appWidgetId) == null) {
			Log.i(TAG, "setAccountForWidget: insert: appWidgetId=" + appWidgetId + " username=" + account.username);
			values.put(COLUMN_WIDGET_ID_OSRSHELPER, String.valueOf(appWidgetId));
			db.insert(TABLE_WIDGET_OSRSHELPER, null, values);
		} else {
			Log.i(TAG, "setAccountForWidget: update: appWidgetId=" + appWidgetId + " username=" + account.username);
			db.update(TABLE_WIDGET_OSRSHELPER, values, COLUMN_WIDGET_ID_OSRSHELPER + "=?", new String[]{String.valueOf(appWidgetId)});
		}
		db.close();
	}

	public Account getAccountForWidget(final int appWidgetId) {
		SQLiteDatabase db = getReadableDatabase();
		Account account = null;
		final Cursor cursor = db.query(TABLE_WIDGET_OSRSHELPER, new String[]{COLUMN_USERNAME_OSRSHELPER, COLUMN_ACCOUNT_TYPE_OSRSHELPER}, COLUMN_WIDGET_ID_OSRSHELPER + "=?", new String[]{String.valueOf(appWidgetId)}, null, null, null);

		if(cursor.moveToFirst()) {
			final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME_OSRSHELPER));
			final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE_OSRSHELPER));
			account = new Account(username, AccountType.valueOf(accountType));
		}
		cursor.close();
		db.close();
		Log.i(TAG, "getAccountForWidget: account=" + account + " appWidgetId=" + appWidgetId);
		return account;
	}

	public ArrayList<Account> getAllAccounts() {
		SQLiteDatabase db = getReadableDatabase();
		final ArrayList<Account> accounts = new ArrayList<>();

		final Cursor cursor = db.query(TABLE_USERNAMES_OSRSHELPER, allColumnsUsernames, null, null,
				COLUMN_USERNAME_OSRSHELPER, null, COLUMN_TIME_USED_OSRSHELPER + " DESC");

		if(cursor.moveToFirst()) {
			do {
				final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME_OSRSHELPER));
				final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE_OSRSHELPER));
				accounts.add(new Account(username, AccountType.valueOf(accountType)));
			} while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return accounts;
	}

	public void deleteAccount(final Account account) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_USERNAMES_OSRSHELPER, COLUMN_USERNAME_OSRSHELPER + "=? AND " + COLUMN_ACCOUNT_TYPE_OSRSHELPER + "=?", new String[]{account.username, account.type.name()});
		db.close();
	}

	public void deleteAllAccounts() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_USERNAMES_OSRSHELPER, null, null);
		db.close();
	}

}
