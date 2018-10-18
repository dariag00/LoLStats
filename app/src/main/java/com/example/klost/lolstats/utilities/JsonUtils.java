package com.example.klost.lolstats.utilities;

import android.content.Context;

import com.example.klost.lolstats.Summoner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class JsonUtils {

    public static Summoner getSimpleRiotAPIStringsFromJson(Context context, String requestJsonStr)
            throws JSONException {

        String OWM_MESSAGE_CODE = "cod";


        JSONObject requestJson = new JSONObject(requestJsonStr);

        /* Comprobación y busqueda de errores */
        //TODO poner esto acorde a la RIOT API
        if (requestJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = requestJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
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
