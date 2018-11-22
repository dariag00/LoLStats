package com.example.klost.lolstats.utilities;

import android.util.Log;

import com.example.klost.lolstats.Champion;
import com.example.klost.lolstats.ChampionList;
import com.example.klost.lolstats.Match;
import com.example.klost.lolstats.MatchList;
import com.example.klost.lolstats.Player;
import com.example.klost.lolstats.Runes.Rune;
import com.example.klost.lolstats.Runes.RuneList;
import com.example.klost.lolstats.Runes.RunePath;
import com.example.klost.lolstats.SummonerSpell;
import com.example.klost.lolstats.SummonerSpellList;
import com.example.klost.lolstats.Summoner;
import com.example.klost.lolstats.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {

    final static String LOG_TAG = "JsonUtils";

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

    public static Match getMatchFromJSON(String requestJsonStr, Match match) throws JSONException {
        //TODO revisar si es mejor no pasar match


        JSONObject requestJSON = new JSONObject(requestJsonStr);

        JSONArray participantIdentitiesJSON = requestJSON.getJSONArray("participantIdentities");

        List<Player> playerList = new ArrayList<>();
        //Procesado de los datos de las identidades de los jugadores
        Log.d(LOG_TAG, String.valueOf(participantIdentitiesJSON.length()));
        for(int i = 0; i<participantIdentitiesJSON.length(); i++){

            JSONObject participantIdentityJSON = participantIdentitiesJSON.getJSONObject(i);

            int participantId = participantIdentityJSON.getInt("participantId");
            JSONObject playerJSON = participantIdentityJSON.getJSONObject("player");
            //TODO implementar Summoner info

            int accountId = playerJSON.getInt("accountId");
            int summonerId = playerJSON.getInt("summonerId");
            Summoner summoner = new Summoner(accountId, summonerId);

            Player player = new Player(participantId);
            player.setSummoner(summoner);
            playerList.add(player);
            Log.d(LOG_TAG, "Añado un player a la lista con acc id " + accountId);
        }


        JSONArray teamsJSON = requestJSON.getJSONArray("teams");

        //Procesado de los datos del team
        Team blueTeam = new Team();
        blueTeam.setTeamId(100);
        Team redTeam= new Team();
        redTeam.setTeamId(200);
        for(int i=0; i<teamsJSON.length(); i++){
            //TODO procesar el resto de datos del Team
            JSONObject teamJSON = teamsJSON.getJSONObject(i);

            //Si el id es 100 el equipo jugó en el lado azul, si es 200, en el rojo
            int teamId = teamJSON.getInt("teamId");
            String win = teamJSON.getString("win");
            Log.d(LOG_TAG, "Team: " + teamId + " game result: " + win);

            if(teamId == 100){
                blueTeam.setWin(win);
            }else if(teamId == 200){
                redTeam.setWin(win);
            }
        }

        JSONArray participantsJSON = requestJSON.getJSONArray("participants");

        for(int i=0; i<participantsJSON.length(); i++){
            JSONObject participantJSON = participantsJSON.getJSONObject(i);
            //TODO procesar el resto de datos del Participant
            int spell1Id = participantJSON.getInt("spell1Id");
            int spell2Id = participantJSON.getInt("spell2Id");
            int teamId = participantJSON.getInt("teamId");
            int championId = participantJSON.getInt("championId");
            int participantId = participantJSON.getInt("participantId");

            //Array que contiene las estadísticas de un jugador en la partida
            JSONObject statsJSON = participantJSON.getJSONObject("stats");

            long visionScore = statsJSON.getLong("visionScore");

            int kills = statsJSON.getInt("kills");
            int deaths = statsJSON.getInt("deaths");
            int assists = statsJSON.getInt("assists");

            long totalDamageDealtToChampions = statsJSON.getLong("totalDamageDealtToChampions");

            //Rune Data
            int runePrimaryStyle = statsJSON.getInt("perkPrimaryStyle");
            int runeSecondaryStyle = statsJSON.getInt("perkSubStyle");

            int rune0 = statsJSON.getInt("perk0");
            int rune1 = statsJSON.getInt("perk1");
            int rune2 = statsJSON.getInt("perk2");
            int rune3 = statsJSON.getInt("perk3");

            int rune4 = statsJSON.getInt("perk4");
            int rune5 = statsJSON.getInt("perk5");


            for(Player player : playerList){
                if(player.getParticipantId() == participantId){

                    player.setSpell1Id(spell1Id);
                    player.setSpell2Id(spell2Id);
                    player.setChampionId(championId);
                    player.setTeamId(teamId);

                    player.setKills(kills);
                    Log.d(LOG_TAG, "PLayer kda: " + kills + " "+deaths+ " " + assists);
                    player.setDeaths(deaths);
                    player.setAssists(assists);

                    //player.setVisionScore(visionScore);
                    player.setTotalDamageDealtToChampions(totalDamageDealtToChampions);

                    player.setRune0(rune0);
                    player.setRune1(rune1);
                    player.setRune2(rune2);
                    player.setRune3(rune3);
                    player.setRune4(rune4);
                    player.setRune5(rune5);

                    player.setRunePrimaryStyle(runePrimaryStyle);
                    player.setRuneSecondaryStyle(runeSecondaryStyle);


                    Log.d(LOG_TAG, "ID DEL TEAM; " + String.valueOf(teamId));
                    //Determinamos el equipo del jugador
                    if(teamId == blueTeam.getTeamId()){
                        Log.d(LOG_TAG, "AÑADO UN PLAYER al equipo azul" + player.toString());
                        blueTeam.addPlayer(player);
                    }else if(teamId == redTeam.getTeamId()){
                        Log.d(LOG_TAG, "AÑADO UN PLAYER al equipo rojo" + player.toString());
                        redTeam.addPlayer(player);
                    }else{
                        Log.d(LOG_TAG, "EL objeto es nulo");
                    }
                }
            }

        }

        match.setBlueTeam(blueTeam);
        match.setRedTeam(redTeam);
        match.setAsProcessed();

        return match;
    }

    public static String getDataTypeFromJSON(String requestJsonStr) throws JSONException {

        JSONObject requestJSON = new JSONObject(requestJsonStr);

        return requestJSON.getString("type");
    }

    public static ChampionList getChampionListFromJSON(String requestJsonStr) throws JSONException {

        ChampionList championList = new ChampionList();

        JSONObject requestJSON = new JSONObject(requestJsonStr);

        JSONObject data = requestJSON.getJSONObject("data");
        //Transformo el Json object data en un json array
        //JSONArray dataArray = new JSONArray(data.toString());

        Iterator x = data.keys();
        JSONArray dataArray = new JSONArray();

        while (x.hasNext()){
            String key = (String) x.next();
            dataArray.put(data.get(key));
        }


        for(int i=0; i<dataArray.length(); i++){
            JSONObject championJSON = dataArray.getJSONObject(i);
            String id = championJSON.getString("id");
            int key = championJSON.getInt("key"); //Nombre del Campeón
            String title = championJSON.getString("title");

            JSONObject imageJSON = championJSON.getJSONObject("image");
            String imageFileName = imageJSON.getString("full");

            Champion champion = new Champion(key, id);
            champion.setTitle(title);
            champion.setImageFileName(imageFileName);

            //TODO parsear tags, estadisticas y el resto de información

            championList.addChampion(champion);
            Log.d(LOG_TAG, "Añado: " + champion.toString());
        }

        return championList;
    }

    public static SummonerSpellList getSpellListFromJSON(String requestJsonStr) throws JSONException {

        SummonerSpellList summonerSpellList = new SummonerSpellList();

        JSONObject requestJSON = new JSONObject(requestJsonStr);

        JSONObject data = requestJSON.getJSONObject("data");

        Iterator x = data.keys();
        JSONArray dataArray = new JSONArray();

        while (x.hasNext()){
            String key = (String) x.next();
            dataArray.put(data.get(key));
        }

        for(int i=0; i<dataArray.length(); i++) {

            JSONObject spellJSON = dataArray.getJSONObject(i);

            String id = spellJSON.getString("id");
            String name = spellJSON.getString("name");
            String description = spellJSON.getString("description");
            int key = spellJSON.getInt("key");

            JSONObject imageJSON = spellJSON.getJSONObject("image");
            String imageFileName = imageJSON.getString("full");

            SummonerSpell spell = new SummonerSpell(key);
            spell.setId(id);
            spell.setName(name);
            spell.setDescription(description);
            spell.setImageFileName(imageFileName);

            summonerSpellList.addSpell(spell);

            Log.d(LOG_TAG, "Añado: " + spell.toString());
        }

        return summonerSpellList;
    }

    public static RuneList getRuneListFromJSON(String requestJsonStr) throws JSONException {

        JSONArray requestArray = new JSONArray(requestJsonStr);
        RuneList runeList = new RuneList();

        //Cada objeto del array corresponde a un tipo de runa concreto
        for (int i = 0; i<requestArray.length(); i++){

            JSONObject runePathJSON = requestArray.getJSONObject(i);
            String runePathKey = runePathJSON.getString("key");
            String iconPath = runePathJSON.getString("icon");
            String pathName = runePathJSON.getString("name");
            int pathId = runePathJSON.getInt("id");

            RunePath runePath = new RunePath();
            runePath.setId(pathId);
            runePath.setName(pathName);
            runePath.setIconPath(iconPath);
            runePath.setKey(runePathKey);

            //Cada Path de Runas tiene 4 slots donde poner runas
            JSONArray slotsArray = runePathJSON.getJSONArray("slots");

            Log.d(LOG_TAG, "Añado el path:" + runePath.toString());

            for(int j = 0; j<slotsArray.length(); j++){

                JSONObject slots = slotsArray.getJSONObject(j);
                JSONArray runesArray = slots.getJSONArray("runes");

                Log.d(LOG_TAG, "Entro al slot: " + j);

                for(int k = 0; k<runesArray.length(); k++){

                    JSONObject runeJSON = runesArray.getJSONObject(k);
                    int runeId = runeJSON.getInt("id");
                    String runeKey = runeJSON.getString("key");
                    String runeIconPath = runeJSON.getString("icon");
                    String runeName = runeJSON.getString("name");
                    String runeShortDesc = runeJSON.getString("shortDesc");
                    String runeLongDesc = runeJSON.getString("longDesc");

                    Rune rune  = new Rune(runeId);
                    rune.setIconPath(runeIconPath);
                    rune.setKey(runeKey);
                    rune.setName(runeName);
                    rune.setShortDesc(runeShortDesc);
                    rune.setLongDesc(runeLongDesc);

                    runePath.addToSlot(rune, k);

                    Log.d(LOG_TAG, "Añado en el slot: " + k + " la runa: " + rune.toString() + " y ya son: " + k + " runas");
                }

            }

            runeList.addRunePath(runePath);

        }

        return runeList;

    }





}
