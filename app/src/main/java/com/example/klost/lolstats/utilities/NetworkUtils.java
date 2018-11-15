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
    final static String RIOT_API_KEY = "RGAPI-efc09ede-554f-4de3-b863-ac3951d5125e";

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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
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
