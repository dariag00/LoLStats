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

        JSONObject requestJSON = new JSONObject(requestJsonStr);

        String summonerName = requestJSON.getString("name");
        long level  = requestJSON.getLong("summonerLevel");
        long revisionDate = requestJSON.getLong("revisionDate");
        long summonerId = requestJSON.getLong("id");
        long accountId = requestJSON.getLong("accountId");
        int profileIconId = requestJSON.getInt("profileIconId");

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


        JSONObject requestJSON  = new JSONObject(requestJsonStr);

        JSONArray matchListJSON = requestJSON.getJSONArray("matches");

        int startIndex = requestJSON.getInt("startIndex");
        int endIndex = requestJSON.getInt("endIndex");
        int totalGames = requestJSON.getInt("totalGames");


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
