package com.example.klost.lolstats.utilities;

import android.net.Uri;
import android.util.Log;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class NetworkUtils {

    /*
        https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/xd?%3F=api_key&api_key=RGAPI-18427979-8b46-4850-8c12-402852c546f2
        https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/RiotSchmick?api_key=<key>
    */

    //KEY DE LA API - CAMBIAR CADA 24H HASTA TENER MODELO DE PRODUCCIÓN
    private final static String RIOT_API_KEY = "RGAPI-31327657-e148-49de-b97e-86097db1a766";

    private final static String PARAM_KEY = "api_key";

    private final static String RIOT_BASE_URL = "https://euw1.api.riotgames.com";

    private final static String RIOT_GET_MATCHLIST = "/lol/match/v4/matchlists/by-account";

    private final static String RIOT_GET_MATCH = "/lol/match/v4/matches";

    private final static String RIOT_GET_MATCH_TIMELINE = "/lol/match/v4/timelines/by-match";

    private final static String RIOT_GET_SUMMONER = "/lol/summoner/v4/summoners/by-name";

    private final static String RIOT_GET_LEAGUES_POSITIONS = "/lol/league/v4/positions/by-summoner";

    private final static String RIOT_GET_LIVE_GAME = "/lol/spectator/v4/active-games/by-summoner";

    private final static String DDRAGON_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/"; //URL utilizada para sacar datos estaticos

    private final static String CDRAGON_BASE_URL = "http://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/";

    //TODO sacarlo de la url: https://ddragon.leagueoflegends.com/api/versions.json
    private final static String DDRAGON_VERSION = "9.12.1";

    private final static String DDRAGON_GET_CHAMPION_IMAGE = "/img/champion/";

    private final static String DDRAGON_GET_SPELL_IMAGE = "/img/spell/";

    private final static String DDRAGON_GET_RUNE_IMAGE = "img/";

    private final static String DDRAGON_GET_DATA = "/data/en_US/";

    private final static String DDRAGON_GET_ITEM_IMAGE = "/img/item/";

    private final static String DDRAGON_GET_PROFILE_ICON = "/img/profileicon/";

    private final static String DDRAGON_GET_SPLASH_ART = "splash/";

    public final static String CDRAGON_GET_PERKS = "global/default/v1/perks.json";

    public final static String CDRAGON_GET_IMAGES_BASE = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/";

    public final static int GET_SUMMONER = 0;

    public final static int GET_MATCHLIST = 1;

    public final static int GET_MATCH = 2;

    public final static int GET_MATCH_TIMELINE = 3;

    public final static int GET_LEAGUES_POSITIONS = 4;

    public final static int GET_DDRAGON_CHAMPION_IMAGE = 5;

    public final static int GET_DDRAGON_PROFILE_ICON = 6;

    public final static int GET_DDRAGON_SUMMONER_SPELL_ICON = 7;

    public final static int GET_DDRAGON_DATA = 8;

    public final static int GET_DDRAGON_RUNE_IMAGE = 9;

    public final static int GET_DDRAGON_ITEM_IMAGE = 10;

    public final static int GET_CDRAGON_PERKS = 11;

    public final static int GET_RANKED_MATCHLIST = 12;

    public final static int GET_DDRAGON_SPLASH_ART = 13;

    public final static int GET_CDRAGON_PERK_IMAGE = 14;

    public final static int GET_LIVE_GAME = 15;

    final static String QUEUE_PARAM = "queue";
    final static String SEASON_PARAM = "season";

    //TODO documentar los metodos
    //TODO buscar si path = X + Y es realmente la solucion
    public static URL buildUrl(String riotSearchQuery, int requestType){

        Uri builtUri;
        String path;
        switch(requestType){
            case GET_SUMMONER:
                path = RIOT_BASE_URL + RIOT_GET_SUMMONER;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_MATCHLIST:
                path = RIOT_BASE_URL + RIOT_GET_MATCHLIST;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_MATCH:
                path = RIOT_BASE_URL +RIOT_GET_MATCH;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_MATCH_TIMELINE:
                path = RIOT_BASE_URL + RIOT_GET_MATCH_TIMELINE;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_LEAGUES_POSITIONS:
                path = RIOT_BASE_URL + RIOT_GET_LEAGUES_POSITIONS;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_DDRAGON_DATA:
                path = DDRAGON_BASE_URL + DDRAGON_VERSION + DDRAGON_GET_DATA;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery + ".json")
                        .build();
                break;

            case GET_DDRAGON_CHAMPION_IMAGE:
                path = DDRAGON_BASE_URL + DDRAGON_VERSION + DDRAGON_GET_CHAMPION_IMAGE;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .build();
                break;

            case GET_DDRAGON_ITEM_IMAGE:
                path = DDRAGON_BASE_URL + DDRAGON_VERSION + DDRAGON_GET_ITEM_IMAGE;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .build();
                break;

            case GET_DDRAGON_PROFILE_ICON:
                path = DDRAGON_BASE_URL + DDRAGON_VERSION + DDRAGON_GET_PROFILE_ICON;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery + ".png")
                        .build();

                break;

            case GET_DDRAGON_SUMMONER_SPELL_ICON:
                path = DDRAGON_BASE_URL + DDRAGON_VERSION + DDRAGON_GET_SPELL_IMAGE;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .build();
                break;

            case GET_DDRAGON_RUNE_IMAGE:
                path = DDRAGON_BASE_URL + DDRAGON_GET_RUNE_IMAGE + riotSearchQuery;
                builtUri = Uri.parse(path).buildUpon().build();
                break;
            case GET_CDRAGON_PERKS:
                path = CDRAGON_BASE_URL + CDRAGON_GET_PERKS;
                builtUri = Uri.parse(path).buildUpon().build();
                break;

            case GET_RANKED_MATCHLIST:
                path = RIOT_BASE_URL + RIOT_GET_MATCHLIST;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .appendQueryParameter(QUEUE_PARAM, "420")
                        .appendQueryParameter(SEASON_PARAM, "13")
                        .build();
                break;
            case GET_DDRAGON_SPLASH_ART:
                path = DDRAGON_BASE_URL + DDRAGON_GET_CHAMPION_IMAGE.substring(1) + DDRAGON_GET_SPLASH_ART;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .build();
                break;

            case GET_LIVE_GAME:
                path = RIOT_BASE_URL + RIOT_GET_LIVE_GAME;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_CDRAGON_PERK_IMAGE:
                path = CDRAGON_GET_IMAGES_BASE;
                riotSearchQuery = riotSearchQuery.substring(riotSearchQuery.indexOf("v1/")).toLowerCase();
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .build();
                break;
            default:
                //Not supported yet
                return null;

        }

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v("NetworkUtils", url.toString());
        return url;
    }

    public static String getResponseFromHttpUrl(URL url, RateLimiter throttle) throws IOException {

        throttle.acquire();
        Log.d("RateLimiter", "Mensaje enviado a las: " + new Date() + " " + url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            int responseCode = urlConnection.getResponseCode();
            Log.d("NetworkUtils", "Response code: " + responseCode);

            String responseCodeErrorString;

            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    break;

                case HttpURLConnection.HTTP_BAD_REQUEST:
                    responseCodeErrorString = "Error 404: BadRequest";
                    return responseCodeErrorString;

                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    responseCodeErrorString = "Error 401: Unauthorized";
                    return responseCodeErrorString;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    responseCodeErrorString = "Error 403: Forbidden";
                    return responseCodeErrorString;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    responseCodeErrorString = "Error 404: Not Found";
                    return responseCodeErrorString;

                case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
                    responseCodeErrorString = "Error 415: Unsupported Media Type";
                    return responseCodeErrorString;

                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    responseCodeErrorString = "Error 500: Internal Server Error";
                    return responseCodeErrorString;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    responseCodeErrorString = "Error 503: Service Unavailable";
                    return responseCodeErrorString;

                default:
                    responseCodeErrorString = "Error XXX: Unexpected error";
                    return responseCodeErrorString;

            }

            String xMethodRateLimitCount = urlConnection.getHeaderField("X-Method-Rate-Limit-Count");
            String xAppRateLimitCount = urlConnection.getHeaderField("X-App-Rate-Limit-Count");
            String xAppRateLimit = urlConnection.getHeaderField("X-App-Rate-Limit");
            String xMethodRateLimit = urlConnection.getHeaderField("X-Method-Rate-Limit");
            String date = urlConnection.getHeaderField("Date");

            Log.d("RateLimiter", "Riots date: " + date);
            Log.d("RateLimiter", "Method Rate Limit Count: " + xMethodRateLimitCount + " para URL " + url.toString() + " y siendo el limite: " + xMethodRateLimit);
            Log.d("RateLimiter", "App Rate Limit Count: " +  xAppRateLimitCount + " y siendo el limite: " + xAppRateLimit);

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
