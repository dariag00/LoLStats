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
    final static String RIOT_API_KEY = "RGAPI-18427979-8b46-4850-8c12-402852c546f2";

    final static String RIOT_BASE_URL = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name";

    final static String PARAM_KEY = "api_key";

    public static URL buildUrl(String riotSearchQuery){
        Uri builtUri = Uri.parse(RIOT_BASE_URL).buildUpon()
                .appendPath(riotSearchQuery)
                .appendQueryParameter(PARAM_KEY, RIOT_API_KEY)
                .build();

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
