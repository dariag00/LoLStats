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
    final static String RIOT_API_KEY = "RGAPI-32fbdbda-15e0-4a1b-9a4b-00a8f5787577";

    final static String RIOT_BASE_URL = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name";

    final static String PARAM_KEY = "api_key";

    final static String RIOT_BASE = "https://euw1.api.riotgames.com";

    final static String RIOT_GET_MATCHLIST = "/lol/match/v3/matchlists/by-account";

    final static String RIOT_GET_MATCH = "/lol/match/v3/matches";

    final static String RIOT_GET_SUMMONER = "/lol/summoner/v3/summoners/by-name";

    public final static int GET_SUMMONER = 0;

    public final static int GET_MATCHLIST = 1;

    public final static int GET_MATCH = 2;

    public final static int GET_MATCH_TIMELINE = 3;
    
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
                return null;
            default:
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
