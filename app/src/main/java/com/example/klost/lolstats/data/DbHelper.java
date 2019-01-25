package com.example.klost.lolstats.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.klost.lolstats.data.DbContract.UserEntry;
import com.example.klost.lolstats.data.DbContract.MatchStatsEntry;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lolStats.db";

    private static final int VERSION = 1;

    private static final String SQL_CREATE_USER_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + "INTEGER PRIMARY KEY, " +
                    UserEntry.COLUMN_NAME + "TEXT NOT NULL, " +
                    UserEntry.COLUMN_ACCOUNT_ID + "INTEGER NOT NULL, " +
                    UserEntry.COLUMN_SUMMONER_ID + "INTEGER NOT NULL, " +
                    UserEntry.COLUMN_ICON_ID + "INTEGER NOT NULL);";

    private static final String SQL_CREATE_CHAMPION_STAT_ENTRIES =
            "CREATE TABLE " + DbContract.MatchStatsEntry.TABLE_NAME + " (" +
                    MatchStatsEntry.COLUMN_MATCH_ID + "INTEGER PRIMARY KEY, " +
                    MatchStatsEntry.COLUMN_CHAMPION_ID + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_VICTORY + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_KILLS + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_DEATHS + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_ASSISTS + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_TOTAL_CS + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_CSMINAT10 + "REAL, " +
                    MatchStatsEntry.COLUMN_CSMINAT15 + "REAL, " +
                    MatchStatsEntry.COLUMN_CSMINAT20 + "REAL, " +
                    MatchStatsEntry.COLUMN_CSDIFAT10 + "REAL, " +
                    MatchStatsEntry.COLUMN_CSDIFAT15 + "REAL, " +
                    MatchStatsEntry.COLUMN_CSDIFAT20 + "REAL, " +
                    MatchStatsEntry.COLUMN_TOTAL_GOLD + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_GOLDDIFAT10 + "INTEGER, " +
                    MatchStatsEntry.COLUMN_GOLDDIFAT15 + "INTEGER, " +
                    MatchStatsEntry.COLUMN_GOLDDIFAT20 + "INTEGER, " +
                    MatchStatsEntry.COLUMN_TOTAL_DAMAGE + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_DAMAGE_PERCENT + "INTEGER, " +
                    MatchStatsEntry.COLUMN_GAME_DURATION + "INTEGER NOT NULL, " +
                    MatchStatsEntry.COLUMN_VISION_SCORE + "INTEGER);";


    private static final String SQL_DELETE_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_MATCHES_ENTRIES =
            "DROP TABLE IF EXISTS " + MatchStatsEntry.TABLE_NAME;


    DbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_ENTRIES);
        db.execSQL(SQL_CREATE_CHAMPION_STAT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_ENTRIES);
        db.execSQL(SQL_DELETE_MATCHES_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
