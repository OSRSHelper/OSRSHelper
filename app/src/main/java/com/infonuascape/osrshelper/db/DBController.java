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
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_ACCOUNT_TYPE = "account_type";

	public static final String TABLE_USERNAMES = "osrshelper_usernames";
	public static final String COLUMN_TIME_USED = "lastused";
	
	public static final String TABLE_WIDGET = "osrshelper_widgets";
	public static final String COLUMN_WIDGET_ID = "widgetid";

	private static final String DATABASE_NAME = "osrshelper.db";
	private static final int DATABASE_VERSION = 4;
	private final String[] allColumnsUsernames = {COLUMN_ID, COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED};

	// Database creation sql statement
	private static final String DATABASE_CREATE_USERNAMES = "CREATE TABLE " + TABLE_USERNAMES + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE + " TEXT, " + COLUMN_TIME_USED + " INTEGER);";
	
	private static final String DATABASE_CREATE_WIDGET = "CREATE TABLE " + TABLE_WIDGET + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WIDGET_ID + " TEXT, "
			+ COLUMN_ACCOUNT_TYPE + " TEXT, " + COLUMN_USERNAME + " TEXT);";

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
			db.execSQL("ALTER TABLE " + TABLE_USERNAMES + " ADD COLUMN " + COLUMN_ACCOUNT_TYPE + " TEXT");
			db.execSQL("ALTER TABLE " + TABLE_WIDGET + " ADD COLUMN " + COLUMN_ACCOUNT_TYPE + " TEXT");

			db.execSQL("UPDATE TABLE " + TABLE_USERNAMES + " SET " + COLUMN_ACCOUNT_TYPE + "='" + AccountType.REGULAR.name() + "'");
			db.execSQL("UPDATE TABLE " + TABLE_WIDGET + " SET " + COLUMN_ACCOUNT_TYPE + "='" + AccountType.REGULAR.name() + "'");
		}
	}

	public void addAccount(final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());
		values.put(COLUMN_TIME_USED, System.currentTimeMillis());

		final Account existingAccount = getAccountByUsername(account.username);
		SQLiteDatabase db = getWritableDatabase();
		if(existingAccount != null) {
			db.update(TABLE_USERNAMES, values, COLUMN_USERNAME + "=?", new String[]{account.username});
		} else {
			db.insert(TABLE_USERNAMES, null, values);
		}

		db.close();
	}

	public void setAccountForWidget(final int appWidgetId, final Account account) {
		SQLiteDatabase db = getWritableDatabase();
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());

		if(getAccountForWidget(appWidgetId) == null) {
			Log.i(TAG, "setAccountForWidget: insert: appWidgetId=" + appWidgetId + " username=" + account.username);
			values.put(COLUMN_WIDGET_ID, String.valueOf(appWidgetId));
			db.insert(TABLE_WIDGET, null, values);
		} else {
			Log.i(TAG, "setAccountForWidget: update: appWidgetId=" + appWidgetId + " username=" + account.username);
			db.update(TABLE_WIDGET, values, COLUMN_WIDGET_ID + "=?", new String[]{String.valueOf(appWidgetId)});
		}
		db.close();
	}

	public Account getAccountByUsername(final String username) {
		SQLiteDatabase db = getReadableDatabase();
		Account account = null;
		final Cursor cursor = db.query(TABLE_USERNAMES, allColumnsUsernames, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

		if(cursor.moveToFirst()) {
			account = createAccountFromCursor(cursor);
		}
		cursor.close();
		db.close();
		Log.i(TAG, "getAccountByUsername: account=" + account + " username=" + username);
		return account;
	}

	public Account getAccountForWidget(final int appWidgetId) {
		SQLiteDatabase db = getReadableDatabase();
		Account account = null;
		final Cursor cursor = db.query(TABLE_WIDGET, allColumnsUsernames, COLUMN_WIDGET_ID + "=?", new String[]{String.valueOf(appWidgetId)}, null, null, null);

		if(cursor.moveToFirst()) {
			account = createAccountFromCursor(cursor);
		}
		cursor.close();
		db.close();
		Log.i(TAG, "getAccountForWidget: account=" + account + " appWidgetId=" + appWidgetId);
		return account;
	}

	private Account createAccountFromCursor(final Cursor cursor) {
		final int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
		final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
		final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
		final long lastTimeUsed = cursor.getInt(cursor.getColumnIndex(COLUMN_TIME_USED));
		return new Account(id, username, AccountType.valueOf(accountType), lastTimeUsed);
	}

	public ArrayList<Account> getAllAccounts() {
		SQLiteDatabase db = getReadableDatabase();
		final ArrayList<Account> accounts = new ArrayList<>();

		final Cursor cursor = db.query(TABLE_USERNAMES, allColumnsUsernames, null, null,
				COLUMN_USERNAME, null, COLUMN_TIME_USED + " DESC");

		if(cursor.moveToFirst()) {
			do {
				final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
				final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
				accounts.add(new Account(username, AccountType.valueOf(accountType)));
			} while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return accounts;
	}

	public void deleteAccount(final Account account) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_USERNAMES, COLUMN_USERNAME + "=?", new String[]{account.username});
		db.close();
	}

	public void deleteAllAccounts() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_USERNAMES, null, null);
		db.close();
	}

}
