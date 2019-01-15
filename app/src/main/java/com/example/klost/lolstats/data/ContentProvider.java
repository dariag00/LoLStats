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

    private UserDbHelper userDbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(UserContract.AUTHORITY, UserContract.PATH_USERS, USERS);
        uriMatcher.addURI(UserContract.AUTHORITY, UserContract.PATH_USERS + "/#", USER_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        userDbHelper = new UserDbHelper(context);
        return true;
    }

    @androidx.annotation.Nullable
    @Nullable
    @Override
    public Cursor query(@androidx.annotation.NonNull @NonNull Uri uri, @androidx.annotation.Nullable @Nullable String[] projection, @androidx.annotation.Nullable @Nullable String selection, @androidx.annotation.Nullable @Nullable String[] selectionArgs, @androidx.annotation.Nullable @Nullable String sortOrder) {
        final SQLiteDatabase db = userDbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case USERS:
                retCursor = db.query(UserContract.UserEntry.TABLE_NAME,
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
        final SQLiteDatabase db = userDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case USERS:
                long id = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(UserContract.UserEntry.CONTENT_URI, id);
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

        final SQLiteDatabase db = userDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int tasksDeleted;

        switch (match){
            case USER_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(UserContract.UserEntry.TABLE_NAME, "_id=?", new String[]{id});
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
