package com.infonuascape.osrshelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.utils.Logger;

import java.util.ArrayList;

import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ACCOUNT_TYPE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_COMBAT_LVL;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ID;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_FOLLOWING;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_MEMBERS;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_PROFILE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_STARRED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_DESCRIPTION;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_ID;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_IMAGE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_NAME;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_PRICE_WANTED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_TIME_USED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_USERNAME;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_WIDGET_ID;

public class DBController {
	private static final String TAG = "DBController";
	private static final String[] ACCOUNTS_PROJECTION = new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED, COLUMN_IS_FOLLOWING, COLUMN_IS_PROFILE, COLUMN_COMBAT_LVL};
	private static final String[] GRAND_EXCHANGE_PROJECTION = new String[]{COLUMN_ID, COLUMN_ITEM_ID, COLUMN_ITEM_NAME, COLUMN_ITEM_DESCRIPTION, COLUMN_ITEM_IMAGE, COLUMN_IS_MEMBERS, COLUMN_IS_STARRED, COLUMN_WIDGET_ID};

	public static void addOrUpdateAccount(final Context context, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());
		values.put(COLUMN_TIME_USED, System.currentTimeMillis());
		values.put(COLUMN_IS_PROFILE, account.isProfile ? 1 : 0);
		values.put(COLUMN_IS_FOLLOWING, account.isFollowing ? 1 : 0);

		if(context != null) {
			final Account existingAccount = getAccountByUsername(context, account.username);
			if (existingAccount != null) {
				context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
			} else {
				context.getContentResolver().insert(OSRSDatabase.ACCOUNTS_CONTENT_URI, values);
			}
		}
	}

	public static void updateAccount(final Context context, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());
		values.put(COLUMN_IS_PROFILE, account.isProfile ? 1 : 0);
		values.put(COLUMN_IS_FOLLOWING, account.isFollowing ? 1 : 0);

		if(context != null) {
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
		}
	}

	public static void setAccountForWidget(final Context context, final int appWidgetId, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());

		Account existingAccount = getAccountForWidget(context, appWidgetId);

		if(context != null) {
			if (existingAccount == null) {
				Logger.add(TAG, ": setAccountForWidget: insert: appWidgetId=" + appWidgetId + " username=" + account.username);
				values.put(COLUMN_WIDGET_ID, String.valueOf(appWidgetId));
				context.getContentResolver().insert(OSRSDatabase.WIDGETS_CONTENT_URI, values);
			} else {
				Logger.add(TAG, ": setAccountForWidget: update: appWidgetId=" + appWidgetId + " username=" + account.username);
				final String where = COLUMN_WIDGET_ID + "=?";
				final String[] whereArgs = new String[]{String.valueOf(appWidgetId)};

				context.getContentResolver().update(OSRSDatabase.WIDGETS_CONTENT_URI, values, where, whereArgs);
			}
		}
	}

	public static Account getAccountByUsername(final Context context, final String username) {
		Account account = null;
		final String[] projection = ACCOUNTS_PROJECTION;
		final String where = COLUMN_USERNAME + "=?";
		final String[] whereArgs = new String[]{username};

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs, null);

			try {
				if (cursor != null && cursor.moveToFirst()) {
					account = createAccountFromCursor(cursor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		Logger.add(TAG, ": getAccountByUsername: account=" + account + " username=" + username);
		return account;
	}

	public static void setProfileAccount(final Context context, final Account account) {
		ContentValues values = new ContentValues();
		if(context != null) {
			values.put(COLUMN_IS_PROFILE, 0);
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, null, null);

			values.put(COLUMN_IS_PROFILE, 1);
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
		}

	}

	public static void setCombatLvlForAccount(final Context context, final Account account) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_COMBAT_LVL, account.combatLvl);
		if(context != null) {
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
		}
	}

	public static Account getProfileAccount(final Context context) {
		Account account = null;
		final String[] projection = ACCOUNTS_PROJECTION;
		final String where = COLUMN_IS_PROFILE + "=?";
		final String[] whereArgs = new String[]{String.valueOf(1)};

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs, null);

			try {
				if (cursor != null && cursor.moveToFirst()) {
					account = createAccountFromCursor(cursor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		Logger.add(TAG, ": getProfileAccount: account=" + account);
		return account;
	}

	public static Account getAccountForWidget(final Context context, final int appWidgetId) {
		Account account = null;

		if(context != null) {
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
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		Log.d(TAG, "getAccountForWidget: account=" + account + " appWidgetId=" + appWidgetId);
		return account;
	}

	public static Account createAccountFromCursor(final Cursor cursor) {
		try {
			final int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
			final String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
			final String accountType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_TYPE));
			final long lastTimeUsed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_USED));
			final boolean isProfile = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PROFILE)) == 1;
			final boolean isFollowing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FOLLOWING)) == 1;
			final int combatLvl = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMBAT_LVL));
			return new Account(id, username, AccountType.valueOf(accountType), lastTimeUsed, isProfile, isFollowing, combatLvl);
		} catch(IllegalArgumentException iae) {
			iae.printStackTrace();
		}

		return null;
	}

	public static ArrayList<Account> getAllAccounts(final Context context) {
		final ArrayList<Account> accounts = new ArrayList<>();
		final String[] projection = ACCOUNTS_PROJECTION;

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, null, null,
					COLUMN_TIME_USED + " DESC");
			try {
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Account account = createAccountFromCursor(cursor);
						if(account != null) {
							accounts.add(account);
						}
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		return accounts;
	}

	public static ArrayList<Account> getAllFollowingAccounts(final Context context) {
		final ArrayList<Account> accounts = new ArrayList<>();
		final String[] projection = ACCOUNTS_PROJECTION;
		final String where = COLUMN_IS_FOLLOWING + "=?";
		final String[] whereArgs = new String[]{String.valueOf(1)};

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs,
					COLUMN_TIME_USED + " DESC");
			try {
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Account account = createAccountFromCursor(cursor);
						if(account != null) {
							accounts.add(account);
						}
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		return accounts;
	}

	public static void deleteAccount(final Context context, final Account account) {
		final String where = COLUMN_USERNAME + "=?";
		final String[] whereArgs = new String[]{account.username};

		if(context != null) {
			context.getContentResolver().delete(OSRSDatabase.ACCOUNTS_CONTENT_URI, where, whereArgs);
		}
	}

	public static void deleteAllAccounts(final Context context) {
		if(context != null) {
			context.getContentResolver().delete(OSRSDatabase.ACCOUNTS_CONTENT_URI, null, null);
		}
	}

	public static Cursor searchAccountsByUsername(final Context context, CharSequence query) {
		final String[] projection = ACCOUNTS_PROJECTION;
		String where = null;
		String[] whereArgs = null;

		if(!TextUtils.isEmpty(query)) {
			where = COLUMN_USERNAME + " LIKE ?";
			whereArgs = new String[]{"%" + query + "%"};
		}

		if(context != null) {
			return context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, projection, where, whereArgs, COLUMN_TIME_USED + " DESC");
		}

		return null;
	}

	/****
	 * GRAND EXCHANGE
	 */

	public static Item createGrandExchangeItemFromCursor(final Cursor cursor) {
		final Item item = new Item();
		item.id = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ID));
		item.name = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
		item.description = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION));
		item.iconLarge = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_IMAGE));
		item.widgetId = cursor.getString(cursor.getColumnIndex(COLUMN_WIDGET_ID));
		item.members = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_MEMBERS)) == 1;
		return item;
	}

	public static void addGrandExchangeItem(final Context context, Item item) {
		final boolean isExist = doesGrandExchangeItemExist(context, item.id);
		if(!isExist && context != null) {
			final ContentValues values = new ContentValues();
			values.put(COLUMN_ITEM_ID, item.id);
			values.put(COLUMN_ITEM_NAME, item.name);
			values.put(COLUMN_ITEM_DESCRIPTION, item.description);
			values.put(COLUMN_ITEM_IMAGE, item.iconLarge);
			values.put(COLUMN_IS_MEMBERS, item.members ? 1 : 0);
			context.getContentResolver().insert(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values);
		}
	}

	public static ArrayList<Item> getGrandExchangeItems(final Context context) {
		final ArrayList<Item> items = new ArrayList<>();
		final String[] projection = GRAND_EXCHANGE_PROJECTION;
		final String sortOrder = COLUMN_IS_STARRED + " DESC, " + COLUMN_ITEM_NAME + " ASC";

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, projection, null, null,
					sortOrder);
			try {
				if (cursor.moveToFirst()) {
					do {
						items.add(createGrandExchangeItemFromCursor(cursor));
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		return items;
	}

	public static void setGrandExchangeWidgetIdToItem(final Context context, String itemId, final String widgetId) {
		ContentValues values = new ContentValues();

		if(context != null) {
			values.put(COLUMN_WIDGET_ID, "");
			context.getContentResolver().update(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values, COLUMN_WIDGET_ID + "=?", new String[]{widgetId});

			values.put(COLUMN_WIDGET_ID, widgetId);
			context.getContentResolver().update(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values, COLUMN_ITEM_ID + "=?", new String[]{itemId});
		}

		Logger.add(TAG, ": setGrandExchangeWidgetIdToItem:");
	}


	public static void setGrandExchangeStarred(final Context context, String itemId, final boolean isStarred, final long price) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_IS_STARRED, isStarred ? 1 : 0);
		values.put(COLUMN_PRICE_WANTED, price);

		if(context != null) {
			context.getContentResolver().update(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values, COLUMN_ITEM_ID + "=?", new String[]{itemId});
		}
	}

	private static boolean doesGrandExchangeItemExist(Context context, String itemId) {
		final String where = COLUMN_ITEM_ID + "=?";
		final String[] whereArgs = new String[]{itemId};

		boolean isExist = false;
		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, new String[]{"COUNT(*)"}, where, whereArgs, null);
			try {
				if (cursor.moveToFirst()) {
					isExist = cursor.getInt(0) > 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		Logger.add(TAG, ": doesGrandExchangeItemExist: isExist=" + isExist + " itemId=" + itemId);
		return isExist;
	}

	public static Item getGrandExchangeByWidgetId(Context context, int appWidgetId) {
		Item item = null;
		final String[] projection = GRAND_EXCHANGE_PROJECTION;
		final String where = COLUMN_WIDGET_ID + "=?";
		final String[] whereArgs = new String[]{String.valueOf(appWidgetId)};

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, projection, where, whereArgs,
					null);
			try {
				if (cursor.moveToFirst()) {
					item = createGrandExchangeItemFromCursor(cursor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		return item;
	}
}
