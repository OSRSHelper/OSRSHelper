package com.infonuascape.osrshelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.models.Account;

import java.util.ArrayList;

import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ACCOUNT_TYPE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ID;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_PROFILE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_TIME_USED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_USERNAME;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_WIDGET_ID;

public class DBController {
	private static final String TAG = "DBController";

	public static void addAccount(final Context context, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());
		values.put(COLUMN_TIME_USED, System.currentTimeMillis());

		final Account existingAccount = getAccountByUsername(context, account.username);
		if(existingAccount != null) {
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
		} else {
			context.getContentResolver().insert(OSRSDatabase.ACCOUNTS_CONTENT_URI, values);
		}
	}

	public static void setAccountForWidget(final Context context, final int appWidgetId, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());

		Account existingAccount = getAccountForWidget(context, appWidgetId);

		if(existingAccount == null) {
			Log.i(TAG, "setAccountForWidget: insert: appWidgetId=" + appWidgetId + " username=" + account.username);
			values.put(COLUMN_WIDGET_ID, String.valueOf(appWidgetId));
			context.getContentResolver().insert(OSRSDatabase.WIDGETS_CONTENT_URI, values);
		} else {
			Log.i(TAG, "setAccountForWidget: update: appWidgetId=" + appWidgetId + " username=" + account.username);
			final String where = COLUMN_WIDGET_ID + "=?";
			final String[] whereArgs = new String[]{String.valueOf(appWidgetId)};

			context.getContentResolver().update(OSRSDatabase.WIDGETS_CONTENT_URI, values, where, whereArgs);
		}
	}

	public static Account getAccountByUsername(final Context context, final String username) {
		Account account = null;
		final String[] projection = new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED};
		final String where = COLUMN_USERNAME + "=?";
		final String[] whereArgs = new String[]{username};

		final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs, null);

		try {
			if (cursor.moveToFirst()) {
				account = createAccountFromCursor(cursor);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		Log.i(TAG, "getAccountByUsername: account=" + account + " username=" + username);
		return account;
	}

	public static void setProfileAccount(final Context context, final Account account) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_IS_PROFILE, 0);
		context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, null, null);

		values.put(COLUMN_IS_PROFILE, 1);
		context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
	}

	public static Account getProfileAccount(final Context context) {
		Account account = null;
		final String[] projection = new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED};
		final String where = COLUMN_IS_PROFILE + "=?";
		final String[] whereArgs = new String[]{String.valueOf(1)};

		final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs, null);

		try {
			if (cursor.moveToFirst()) {
				account = createAccountFromCursor(cursor);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		Log.i(TAG, "getProfileAccount: account=" + account);
		return account;
	}

	public static Account getAccountForWidget(final Context context, final int appWidgetId) {
		Account account = null;
		final Cursor cursor = context.getContentResolver().query(OSRSDatabase.WIDGETS_CONTENT_URI, new String[]{COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE}, COLUMN_WIDGET_ID + "=?", new String[]{String.valueOf(appWidgetId)}, null);
		try {
			if (cursor.moveToFirst()) {
				final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
				final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
				account = new Account(username, AccountType.valueOf(accountType));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		Log.i(TAG, "getAccountForWidget: account=" + account + " appWidgetId=" + appWidgetId);
		return account;
	}

	public static Account createAccountFromCursor(final Cursor cursor) {
		final int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
		final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
		final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
		final long lastTimeUsed = cursor.getInt(cursor.getColumnIndex(COLUMN_TIME_USED));
		return new Account(id, username, AccountType.valueOf(accountType), lastTimeUsed);
	}

	public static ArrayList<Account> getAllAccounts(final Context context) {
		final ArrayList<Account> accounts = new ArrayList<>();
		final String[] projection = new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED};

		final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, null, null,
				COLUMN_TIME_USED + " DESC");
		try {
			if (cursor.moveToFirst()) {
				do {
					final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
					final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
					accounts.add(new Account(username, AccountType.valueOf(accountType)));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return accounts;
	}

	public static void deleteAccount(final Context context, final Account account) {
		final String where = COLUMN_USERNAME + "=?";
		final String[] whereArgs = new String[]{account.username};

		context.getContentResolver().delete(OSRSDatabase.ACCOUNTS_CONTENT_URI, where, whereArgs);
	}

	public static void deleteAllAccounts(final Context context) {
		context.getContentResolver().delete(OSRSDatabase.ACCOUNTS_CONTENT_URI, null, null);
	}

	public static Cursor searchAccountsByUsername(final Context context, CharSequence query) {
		final String[] projection = new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED};


		String where = null;
		String[] whereArgs = null;

		if(!TextUtils.isEmpty(query)) {
			where = COLUMN_USERNAME + " LIKE ?";
			whereArgs = new String[]{"%" + query + "%"};
		}

		return context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs, COLUMN_TIME_USED + " DESC");
	}
}
