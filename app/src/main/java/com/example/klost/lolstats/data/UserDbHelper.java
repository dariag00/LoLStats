package com.example.klost.lolstats.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.klost.lolstats.data.UserContract.UserEntry;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lolStats.db";

    private static final int VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + "INTEGER PRIMARY KEY, " +
                    UserEntry.COLUMN_NAME + "TEXT NOT NULL, " +
                    UserEntry.COLUMN_ACCOUNT_ID + "INTEGER NOT NULL, " +
                    UserEntry.COLUMN_SUMMONER_ID + "INTEGER NOT NULL, " +
                    UserEntry.COLUMN_ICON_ID + "INTEGER NOT NULL);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


    UserDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
