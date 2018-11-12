package com.example.klost.lolstats.utilities;

import android.content.Context;
import android.util.Log;

import com.example.klost.lolstats.Summoner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class JsonUtils {

    public static Summoner getSimpleRiotAPIStringsFromJson(Context context, String requestJsonStr)
            throws JSONException {

        String RIOT_MESSAGE_CODE = "status_code";


        JSONObject requestJson = new JSONObject(requestJsonStr);

        /* Comprobación y busqueda de errores */
        //TODO poner esto acorde a la RIOT API
        if (requestJson.has(RIOT_MESSAGE_CODE)) {
            int errorCode = requestJson.getInt(RIOT_MESSAGE_CODE);
            Log.v("ERRORCODE", String.valueOf(errorCode));
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    Log.e("JSONUTILS", "Error 404: BadRequest");
                    return null;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    Log.e("JSONUTILS", "Error 401: Unauthorized");
                    return null;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    Log.e("JSONUTILS", "Error 403: Forbidden");
                    return null;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    Log.e("JSONUTILS", "Error 404: Not Found");

                case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
                    Log.e("JSONUTILS", "Error 415: Unsupported Media Type");
                    return null;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    Log.e("JSONUTILS", "Error 500: Internal Server Error");
                    return null;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    Log.e("JSONUTILS", "Error 503: Service Unavailable");
                    return null;
                default:
                    Log.e("JSONUTILS", "Error XXX: Unexpected error");
                    return null;

            }
        }


        String summonerName = requestJson.getString("name");
        int level  = requestJson.getInt("summonerLevel");
        long revisionDate = requestJson.getLong("revisionDate");
        int id = requestJson.getInt("id");
        int accountId = requestJson.getInt("accountId");
        //TODO Faltaria el Icon ID

        Summoner summoner = new Summoner(accountId);
        summoner.setId(id);
        summoner.setRevisionDate(revisionDate);
        summoner.setSummonerName(summonerName);
        summoner.setSummonerLevel(level);

        return summoner;
    }

}
