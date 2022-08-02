package com.infonuascape.osrshelper.db;

import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ACCOUNT_TYPE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_COMBAT_LVL;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_DISPLAY_NAME;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ID;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_FOLLOWING;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_MEMBERS;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_PROFILE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_IS_STARRED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_DESCRIPTION;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_ID;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_IMAGE;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_ITEM_NAME;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_OUTPUT;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_PRICE_WANTED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_QUERY;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_TIME_USED;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_USERNAME;
import static com.infonuascape.osrshelper.db.OSRSDatabase.COLUMN_WIDGET_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.grandexchange.Item;
import com.infonuascape.osrshelper.utils.Logger;

import java.util.ArrayList;

public class OSRSDatabaseFacade {
	private static final String TAG = "OSRSDatabaseFacade";

	private static final String[] ACCOUNTS_PROJECTION = new String[] {COLUMN_ID, COLUMN_USERNAME, COLUMN_DISPLAY_NAME, COLUMN_ACCOUNT_TYPE, COLUMN_TIME_USED, COLUMN_IS_FOLLOWING, COLUMN_IS_PROFILE, COLUMN_COMBAT_LVL};
	private static final String[] GRAND_EXCHANGE_PROJECTION = new String[] {COLUMN_ID, COLUMN_ITEM_ID, COLUMN_ITEM_NAME, COLUMN_ITEM_DESCRIPTION, COLUMN_ITEM_IMAGE, COLUMN_IS_MEMBERS, COLUMN_IS_STARRED, COLUMN_WIDGET_ID};

	private Context context;

	public OSRSDatabaseFacade(final Context context) {
		this.context = context;
	}

	public void addOrUpdateAccount(final Context context, final Account account) {
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

	public void updateAccount(final Context context, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());
		values.put(COLUMN_IS_PROFILE, account.isProfile ? 1 : 0);
		values.put(COLUMN_IS_FOLLOWING, account.isFollowing ? 1 : 0);

		if(context != null) {
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
		}
	}

	public void updateAccount(final Context context, final String username, final String displayName, final AccountType accountType, final int combatLvl) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_DISPLAY_NAME, displayName);
		values.put(COLUMN_ACCOUNT_TYPE, accountType.name());
		values.put(COLUMN_COMBAT_LVL, combatLvl);

		if (context != null) {
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{username});
			updateWidgetsByUsername(context, username, displayName, accountType);
		}
	}

	public void setAccountForWidget(final Context context, final int appWidgetId, final Account account) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, account.username);
		values.put(COLUMN_ACCOUNT_TYPE, account.type.name());
		values.put(COLUMN_DISPLAY_NAME, account.displayName);

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

	public void updateWidgetsByUsername(final Context context, final String username, final String displayName, final AccountType accountType) {
		if(context != null) {
			final ContentValues values = new ContentValues();
			values.put(COLUMN_ACCOUNT_TYPE, accountType.name());
			values.put(COLUMN_DISPLAY_NAME, displayName);
			if (!isUsernameInWidgets(context, username)) {
				Logger.add(TAG, ": updateWidgetsByUsername: insert: username=" + username );
				values.put(COLUMN_USERNAME, String.valueOf(username));
				context.getContentResolver().insert(OSRSDatabase.WIDGETS_CONTENT_URI, values);
			} else {
				Logger.add(TAG, ": updateWidgetsByUsername: update: username=" + username);
				final String where = COLUMN_USERNAME + "=?";
				final String[] whereArgs = new String[]{username};

				context.getContentResolver().update(OSRSDatabase.WIDGETS_CONTENT_URI, values, where, whereArgs);
			}
		}
	}

	public Account getAccountByUsername(final Context context, final String username) {
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

	public void setProfileAccount(final Context context, final Account account) {
		ContentValues values = new ContentValues();
		if(context != null) {
			values.put(COLUMN_IS_PROFILE, 0);
			context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, null, null);

			if (account != null) {
				values.put(COLUMN_IS_PROFILE, 1);
				context.getContentResolver().update(OSRSDatabase.ACCOUNTS_CONTENT_URI, values, COLUMN_USERNAME + "=?", new String[]{account.username});
			}
		}

	}

	public Account getProfileAccount(final Context context) {
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

	public Account getAccountForWidget(final Context context, final int appWidgetId) {
		Account account = null;

		if(context != null) {
			final String[] projection = new String[]{COLUMN_USERNAME, COLUMN_DISPLAY_NAME, COLUMN_ACCOUNT_TYPE};
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.WIDGETS_CONTENT_URI, projection, COLUMN_WIDGET_ID + "=?", new String[]{String.valueOf(appWidgetId)}, null);
			try {
				if (cursor.moveToFirst()) {
					final String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
					final String displayName = cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAY_NAME));
					final String accountType = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
					account = new Account(username, displayName, AccountType.valueOf(accountType));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		Logger.add(TAG, "getAccountForWidget: account=" + account + " appWidgetId=" + appWidgetId);
		return account;
	}

	public boolean isUsernameInWidgets(final Context context, final String username) {
		boolean isFound = false;

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.WIDGETS_CONTENT_URI, new String[]{"COUNT(*)"}, COLUMN_USERNAME + "=?", new String[]{username}, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					isFound = cursor.getInt(0) > 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		Logger.add(TAG, "isUsernameInWidgets: isFound=" + isFound);
		return isFound;
	}

	public static Account createAccountFromCursor(final Cursor cursor) {
		try {
			final int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
			final String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
			final String displayName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISPLAY_NAME));
			final String accountType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_TYPE));
			final long lastTimeUsed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_USED));
			final boolean isProfile = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PROFILE)) == 1;
			final boolean isFollowing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FOLLOWING)) == 1;
			final int combatLvl = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMBAT_LVL));
			return new Account(id, username, displayName, AccountType.valueOf(accountType), lastTimeUsed, isProfile, isFollowing, combatLvl);
		} catch(IllegalArgumentException iae) {
			iae.printStackTrace();
		}

		return null;
	}

	public ArrayList<Account> getAllAccounts(final Context context) {
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

	public ArrayList<Account> getAllFollowingAccounts(final Context context) {
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

	public void deleteAccount(final Context context, final Account account) {
		final String where = COLUMN_USERNAME + "=?";
		final String[] whereArgs = new String[]{account.username};

		if(context != null) {
			context.getContentResolver().delete(OSRSDatabase.ACCOUNTS_CONTENT_URI, where, whereArgs);
		}
	}

	public void deleteAllAccounts(final Context context) {
		if(context != null) {
			context.getContentResolver().delete(OSRSDatabase.ACCOUNTS_CONTENT_URI, null, null);
		}
	}

	public Cursor searchAccountsByUsername(final Context context, CharSequence query) {
		String where = null;
		String[] whereArgs = null;

		if(!TextUtils.isEmpty(query)) {
			where = COLUMN_USERNAME + " LIKE ?";
			whereArgs = new String[]{"%" + query + "%"};
		}

		if(context != null) {
			return context.getContentResolver().query(OSRSDatabase.ACCOUNTS_CONTENT_URI, ACCOUNTS_PROJECTION, where, whereArgs, COLUMN_TIME_USED + " DESC");
		}

		return null;
	}

	/****
	 * GRAND EXCHANGE
	 */

	public static Item createGrandExchangeItemFromCursor(final Cursor cursor) {
		final Item item = new Item();
		item.id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID));
		item.name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
		item.description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_DESCRIPTION));
		item.iconLarge = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_IMAGE));
		item.widgetId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WIDGET_ID));
		item.members = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_MEMBERS)) == 1;
		return item;
	}

	public void addGrandExchangeItem(final Context context, Item item) {
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

	public ArrayList<Item> getGrandExchangeItems(final Context context) {
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

	public void setGrandExchangeWidgetIdToItem(final Context context, String itemId, final String widgetId) {
		ContentValues values = new ContentValues();

		if(context != null) {
			values.put(COLUMN_WIDGET_ID, "");
			context.getContentResolver().update(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values, COLUMN_WIDGET_ID + "=?", new String[]{widgetId});

			values.put(COLUMN_WIDGET_ID, widgetId);
			context.getContentResolver().update(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values, COLUMN_ITEM_ID + "=?", new String[]{itemId});
		}

		Logger.add(TAG, ": setGrandExchangeWidgetIdToItem:");
	}


	public void setGrandExchangeStarred(final Context context, String itemId, final boolean isStarred, final long price) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_IS_STARRED, isStarred ? 1 : 0);
		values.put(COLUMN_PRICE_WANTED, price);

		if(context != null) {
			context.getContentResolver().update(OSRSDatabase.GRAND_EXCHANGE_CONTENT_URI, values, COLUMN_ITEM_ID + "=?", new String[]{itemId});
		}
	}

	private boolean doesGrandExchangeItemExist(Context context, String itemId) {
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

	public Item getGrandExchangeByWidgetId(Context context, int appWidgetId) {
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

	public String getQueryCache(final Context context, final String query) {
		final String[] projection = new String[]{COLUMN_OUTPUT};
		final String where = COLUMN_QUERY + "=?";
		final String[] whereArgs = new String[]{query};

		String output = null;

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.QUERY_CACHE_CONTENT_URI, projection, where, whereArgs,
					null);
			if (cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						output = cursor.getString(cursor.getColumnIndex(COLUMN_OUTPUT));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					cursor.close();
				}
			}
		}

		return output;
	}

	public boolean isQueryInCache(final Context context, final String query) {
		final String[] projection = new String[]{"COUNT(*)"};
		final String where = COLUMN_QUERY + "=?";
		final String[] whereArgs = new String[]{String.valueOf(query)};

		int count = 0;

		if(context != null) {
			final Cursor cursor = context.getContentResolver().query(OSRSDatabase.QUERY_CACHE_CONTENT_URI, projection, where, whereArgs,
					null);
			if (cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						count = cursor.getInt(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					cursor.close();
				}
			}
		}

		return count > 0;
	}

	public void insertOutputToQueryCache(final Context context, String query, final String output) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_OUTPUT, output);

		if(context != null) {
			if (isQueryInCache(context, query)) {
				context.getContentResolver().update(OSRSDatabase.QUERY_CACHE_CONTENT_URI, values, COLUMN_QUERY + "=?", new String[]{query});
			} else {
				values.put(COLUMN_QUERY, query);
				context.getContentResolver().insert(OSRSDatabase.QUERY_CACHE_CONTENT_URI, values);
			}
		}
	}
}
