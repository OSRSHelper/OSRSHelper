package com.infonuascape.osrshelper.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by marc_ on 2018-01-15.
 */

public class OSRSContentProvider extends ContentProvider {
    private static final String TAG = "OSRSContentProvider";

    private OSRSDatabase osrsDatabase;

    private static final int ACCOUNTS_URI = 1;
    private static final int WIDGETS_URI = 2;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(OSRSDatabase.AUTHORITY, OSRSDatabase.ACCOUNTS_TABLE, ACCOUNTS_URI);
        sUriMatcher.addURI(OSRSDatabase.AUTHORITY, OSRSDatabase.WIDGETS_TABLE, WIDGETS_URI);
    }

    @Override
    public boolean onCreate() {
        osrsDatabase = new OSRSDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs,
                        final String sortOrder) {
        final SQLiteDatabase db = osrsDatabase.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ACCOUNTS_URI:
                return db.query(OSRSDatabase.TABLE_USERNAMES, projection, selection, selectionArgs, OSRSDatabase.COLUMN_USERNAME, null, sortOrder);
            case WIDGETS_URI:
                return db.query(OSRSDatabase.TABLE_WIDGET, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("No matching URI");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ACCOUNTS_URI:
                return OSRSDatabase.TABLE_USERNAMES;
            case WIDGETS_URI:
                return OSRSDatabase.TABLE_WIDGET;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = osrsDatabase.getWritableDatabase();
        long rowId = 0;
        Uri newUri = null;
        switch (sUriMatcher.match(uri)) {
            case ACCOUNTS_URI:
                rowId = db.insert(OSRSDatabase.TABLE_USERNAMES, null, contentValues);
                newUri = ContentUris.withAppendedId(OSRSDatabase.ACCOUNTS_CONTENT_URI, rowId);
                break;
            case WIDGETS_URI:
                rowId = db.insert(OSRSDatabase.TABLE_WIDGET, null, contentValues);
                newUri = ContentUris.withAppendedId(OSRSDatabase.WIDGETS_CONTENT_URI, rowId);
                break;
            default:
                throw new IllegalArgumentException("No matching URI");
        }

        if (rowId > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return newUri;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase db = osrsDatabase.getWritableDatabase();

        int affectedRows = 0;

        switch (sUriMatcher.match(uri)) {
            case ACCOUNTS_URI:
                affectedRows = db.delete(OSRSDatabase.TABLE_USERNAMES, selection, selectionArgs);
                break;
            case WIDGETS_URI:
                affectedRows = db.delete(OSRSDatabase.TABLE_WIDGET, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("No matching URI");
        }

        if (affectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        final SQLiteDatabase db = osrsDatabase.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ACCOUNTS_URI:
                count = db.update(OSRSDatabase.TABLE_USERNAMES, contentValues, selection, selectionArgs);
                break;
            case WIDGETS_URI:
                count = db.update(OSRSDatabase.TABLE_WIDGET, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
