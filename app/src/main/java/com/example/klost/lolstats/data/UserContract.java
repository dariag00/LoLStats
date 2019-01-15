package com.example.klost.lolstats.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserContract {

    public static final String AUTHORITY = "com.klosote.klst.lolstats";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_USERS = "videogames";

    public static final class UserEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_SUMMONER_ID = "summonerId";

        public static final String COLUMN_ACCOUNT_ID = "accountId";

        public static final String COLUMN_ICON_ID = "icon";
    }
}
