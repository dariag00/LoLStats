package com.example.klost.lolstats.utilities;

import android.content.Context;
import android.util.Log;

import com.example.klost.lolstats.Match;
import com.example.klost.lolstats.MatchList;
import com.example.klost.lolstats.Summoner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Summoner getSummonerFromJSON(String requestJsonStr)
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
        long level  = requestJson.getLong("summonerLevel");
        long revisionDate = requestJson.getLong("revisionDate");
        long summonerId = requestJson.getLong("id");
        long accountId = requestJson.getLong("accountId");
        int profileIconId = requestJson.getInt("profileIconId");

        Summoner summoner = new Summoner();
        summoner.setAccountId(accountId);
        summoner.setSummonerId(summonerId);
        summoner.setProfileIconId(profileIconId);
        summoner.setRevisionDate(revisionDate);
        summoner.setSummonerName(summonerName);
        summoner.setSummonerLevel(level);

        return summoner;
    }

    public static MatchList getMatchListFromJSON(String requestJsonStr) throws JSONException {

        String RIOT_MESSAGE_CODE = "status_code";


        JSONObject requestJson = new JSONObject(requestJsonStr);

        /* Comprobación y busqueda de errores */
        //TODO metodo que compruebe si ha habido problemas?
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

        JSONArray matchListJSON = requestJson.getJSONArray("matches");

        int startIndex = requestJson.getInt("startIndex");
        int endIndex = requestJson.getInt("endIndex");
        int totalGames = requestJson.getInt("totalGames");


        List<Match> matches = new ArrayList<>();

        for(int i = 0; i<matchListJSON.length(); i++){

            JSONObject matchJSON = matchListJSON.getJSONObject(i);

            Match match = new Match();

            String lane = matchJSON.getString("lane");
            long gameId = matchJSON.getLong("gameId");
            int champion = matchJSON.getInt("champion");
            String platformId = matchJSON.getString("platformId");
            long timestamp = matchJSON.getLong("timestamp");
            int queue = matchJSON.getInt("queue");
            String role = matchJSON.getString("role");
            int season = matchJSON.getInt("season");

            match.setLane(lane);
            match.setGameId(gameId);
            match.setChampionId(champion);
            match.setPlatformId(platformId);
            match.setDate(timestamp);
            match.setQueue(queue);
            match.setRole(role);
            match.setSeason(season);

            matches.add(match);
        }

        MatchList matchList = new MatchList(matches);
        matchList.setStartIndex(startIndex);
        matchList.setEndIndex(endIndex);
        matchList.setTotalGames(totalGames);

        return matchList;
    }




}
