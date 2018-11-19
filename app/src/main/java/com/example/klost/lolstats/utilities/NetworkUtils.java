package com.example.klost.lolstats.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /*
        https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/xd?%3F=api_key&api_key=RGAPI-18427979-8b46-4850-8c12-402852c546f2
        https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/RiotSchmick?api_key=<key>
    */

    //KEY DE LA API - CAMBIAR CADA 24H HASTA TENER MODELO DE PRODUCCIÓN
    private final static String RIOT_API_KEY = "RGAPI-fffd173e-32cb-49f8-b7e0-7f1c6585950a";

    private final static String RIOT_BASE_URL = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name";

    private final static String PARAM_KEY = "api_key";

    private final static String RIOT_BASE = "https://euw1.api.riotgames.com";

    private final static String RIOT_GET_MATCHLIST = "/lol/match/v3/matchlists/by-account";

    private final static String RIOT_GET_MATCH = "/lol/match/v3/matches";

    private final static String RIOT_GET_SUMMONER = "/lol/summoner/v3/summoners/by-name";

    private final static String DDRAGON_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/"; //URL utilizada para sacar datos estaticos

    private final static String DDRAGON_VERSION = "6.24.1";

    private final static String DDRAGON_IMAGE_FORMAT = ".png";

    private final static String DDRAGON_GET_CHAMPION_IMAGE = "/img/champion/";

    private final static String DDRAGON_GET_DATA = "/data/en_US/";


    public final static int GET_SUMMONER = 0;

    public final static int GET_MATCHLIST = 1;

    public final static int GET_MATCH = 2;

    public final static int GET_MATCH_TIMELINE = 3;

    public final static int GET_DDRAGON_CHAMPION_IMAGE = 4;

    public final static int GET_DDRAGON_ITEM = 5;

    public final static int GET_DDRAGON_PROFILE_ICON = 6;

    public final static int GET_DDRAGON_SUMMONER_SPELL_ICON = 7;

    public final static int GET_DDRAGON_DATA = 8;

    //TODO documentar los metodos
    //TODO buscar si path = X + Y es realmente la solucion
    public static URL buildUrl(String riotSearchQuery, int requestType){

        Uri builtUri;
        String path;
        switch(requestType){
            case GET_SUMMONER:
                path = RIOT_BASE + RIOT_GET_SUMMONER;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_MATCHLIST:
                path = RIOT_BASE + RIOT_GET_MATCHLIST;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_MATCH:
                path = RIOT_BASE +RIOT_GET_MATCH;
                builtUri = Uri.parse(path).buildUpon()
                        .appendPath(riotSearchQuery)
                        .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                        .build();
                break;

            case GET_MATCH_TIMELINE:
                //Not supported yet
                return null;

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

            case GET_DDRAGON_ITEM:
                //Not supported yet
                return null;

            case GET_DDRAGON_PROFILE_ICON:
                //Not supported yet
                return null;

            case GET_DDRAGON_SUMMONER_SPELL_ICON:
                //Not supported yet
                return null;

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

    //TODO procesar los limites de llamadas ubicados en el header
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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

            Log.d("NetworkUtils", "Method Rate Limit: " + xMethodRateLimitCount + " para URL " + url.toString());
            Log.d("NetworkUtils", "App Rate Limit: " +  xAppRateLimitCount);

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
