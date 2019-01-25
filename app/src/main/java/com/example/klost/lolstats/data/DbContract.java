package com.example.klost.lolstats.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {

    public static final String AUTHORITY = "com.klosote.klst.lolstats";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_USERS = "users";

    public static final String PATH_MATCHES_STATS = "matches";

    public static final class MatchStatsEntry implements BaseColumns{
        public static final Uri CONTENT_URI_CHAMPIONS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MATCHES_STATS).build();

        public static final String TABLE_NAME = "matches_stats";

        public static final String COLUMN_CHAMPION_ID = "championid";

        public static final String COLUMN_MATCH_ID = "matchid";

        public static final String COLUMN_KILLS = "kills";

        public static final String COLUMN_DEATHS = "deaths";

        public static final String COLUMN_ASSISTS = "assists";

        public static final String COLUMN_VICTORY = "victory";

        public static final String COLUMN_CSMINAT10 = "csminat10";

        public static final String COLUMN_CSMINAT15 = "csminat15";

        public static final String COLUMN_CSMINAT20 = "csminat20";

        public static final String COLUMN_CSDIFAT10 = "csdifat10";

        public static final String COLUMN_CSDIFAT15 = "csdifat15";

        public static final String COLUMN_CSDIFAT20 = "csdifat20";

        public static final String COLUMN_TOTAL_CS = "totalcs";

        public static final String COLUMN_GAME_DURATION = "duration";

        public static final String COLUMN_GOLDDIFAT10 = "goldat10";

        public static final String COLUMN_GOLDDIFAT15 = "goldat15";

        public static final String COLUMN_GOLDDIFAT20 = "goldat20";

        public static final String COLUMN_TOTAL_GOLD = "totalgold";

        public static final String COLUMN_DAMAGE_PERCENT = "dmgpercent";

        public static final String COLUMN_TOTAL_DAMAGE = "totaldmg";

        public static final String COLUMN_VISION_SCORE = "visionscore";

    }

    public static final class UserEntry implements BaseColumns{

        public static final Uri CONTENT_URI_USERS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_SUMMONER_ID = "summonerId";

        public static final String COLUMN_ACCOUNT_ID = "accountId";

        public static final String COLUMN_ICON_ID = "icon";
    }
}
