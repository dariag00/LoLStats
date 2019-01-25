package com.example.klost.lolstats.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ContentProvider extends android.content.ContentProvider {

    public static final int USERS = 100;
    public static final int USER_WITH_ID = 101;

    public static final int MATCHES = 200;
    public static final int MATCH_WITH_ID = 201;

    private DbHelper dbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_USERS, USERS);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_USERS + "/#", USER_WITH_ID);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_MATCHES_STATS, MATCHES);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_MATCHES_STATS + "/#", MATCH_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DbHelper(context);
        return true;
    }

    @androidx.annotation.Nullable
    @Nullable
    @Override
    public Cursor query(@androidx.annotation.NonNull @NonNull Uri uri, @androidx.annotation.Nullable @Nullable String[] projection, @androidx.annotation.Nullable @Nullable String selection, @androidx.annotation.Nullable @Nullable String[] selectionArgs, @androidx.annotation.Nullable @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case USERS:
                retCursor = db.query(DbContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MATCHES:
                retCursor = db.query(DbContract.MatchStatsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @androidx.annotation.Nullable
    @Nullable
    @Override
    public String getType(@androidx.annotation.NonNull @NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @androidx.annotation.Nullable
    @Nullable
    @Override
    public Uri insert(@androidx.annotation.NonNull @NonNull Uri uri, @androidx.annotation.Nullable @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case USERS:
                long usersId = db.insert(DbContract.UserEntry.TABLE_NAME, null, values);
                if(usersId>0){
                    returnUri = ContentUris.withAppendedId(DbContract.UserEntry.CONTENT_URI_USERS, usersId);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case MATCHES:
                long matchesId = db.insert(DbContract.MatchStatsEntry.TABLE_NAME, null, values);
                if(matchesId>0){
                    returnUri = ContentUris.withAppendedId(DbContract.MatchStatsEntry.CONTENT_URI_CHAMPIONS, matchesId);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@androidx.annotation.NonNull @NonNull Uri uri, @androidx.annotation.Nullable @Nullable String selection, @androidx.annotation.Nullable @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int tasksDeleted;
        String id;

        switch (match){
            case USER_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(DbContract.UserEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;

            case MATCH_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(DbContract.MatchStatsEntry.TABLE_NAME, "matchid=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(tasksDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@androidx.annotation.NonNull @NonNull Uri uri, @androidx.annotation.Nullable @Nullable ContentValues values, @androidx.annotation.Nullable @Nullable String selection, @androidx.annotation.Nullable @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
