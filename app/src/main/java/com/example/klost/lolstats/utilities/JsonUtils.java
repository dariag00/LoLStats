package com.example.klost.lolstats.utilities;

import android.util.Log;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.runes.Rune;
import com.example.klost.lolstats.models.runes.RuneList;
import com.example.klost.lolstats.models.runes.RunePath;
import com.example.klost.lolstats.models.summoners.SummonerSpell;
import com.example.klost.lolstats.models.summoners.SummonerSpellList;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Team;

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

        //TODO incluir fecha
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
            int queue = matchJSON.getInt("queue");
            String role = matchJSON.getString("role");
            int season = matchJSON.getInt("season");

            match.setLane(lane);
            match.setGameId(gameId);
            match.setChampionId(champion);
            match.setPlatformId(platformId);
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

        String gameType = requestJSON.getString("gameType");
        long gameDuration = requestJSON.getLong("gameDuration");
        long gameCreationDate = requestJSON.getLong("gameCreation");

        Log.d(LOG_TAG, "Game Duration" + String.valueOf(gameDuration));

        match.setGameType(gameType);
        match.setGameDuration(gameDuration);
        match.setGameCreation(gameCreationDate);


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

            int item0 = statsJSON.getInt("item0");
            int item1 = statsJSON.getInt("item1");
            int item2 = statsJSON.getInt("item2");
            int item3 = statsJSON.getInt("item3");
            int item4 = statsJSON.getInt("item4");
            int item5 = statsJSON.getInt("item5");
            int item6 = statsJSON.getInt("item6");

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

                    //TODO modificar para que haya una lista de runas e items en cada player
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

                    player.setItem0(item0);
                    Log.d("ERRORR", "Seteo " + item0);
                    player.setItem1(item1);
                    player.setItem2(item2);
                    player.setItem3(item3);
                    player.setItem4(item4);
                    player.setItem5(item5);
                    player.setItem6(item6);


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
        //TODO yype mismatch con rune
        JSONObject requestJSON = new JSONObject(requestJsonStr);

        if(requestJSON.has("type"))
            return requestJSON.getString("type");
        else
            return null;
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

    public static ItemList getItemListFromJSON(String requestJsonStr) throws JSONException {

        ItemList itemList = new ItemList();

        JSONObject requestJSON = new JSONObject(requestJsonStr);

        JSONObject dataJSON = requestJSON.getJSONObject("data");

        int firstItem = 1001;
        int lastItem = 4403;

        //AL riot no proporcionar el id del item dentro del objeto tenemos que revisar el rango de items completo y mirar si existe ese item para procesarlo
        for(int i=firstItem; i<lastItem+1; i++){

            if(dataJSON.has(String.valueOf(i))){
                //Hemos encontrado un item valido
                JSONObject itemJSON =  dataJSON.getJSONObject(String.valueOf(i));

                String itemName = itemJSON.getString("name");
                String itemShortDesc = itemJSON.getString("plaintext");
                String itemDescription = itemJSON.getString("description");

                JSONObject goldJSON = itemJSON.getJSONObject("gold");

                int itemBaseCost = goldJSON.getInt("base");
                int itemTotalCost = goldJSON.getInt("total");
                int itemSellCost = goldJSON.getInt("sell");
                boolean isItemPurchasable = goldJSON.getBoolean("purchasable");

                JSONObject imageJSON = itemJSON.getJSONObject("image");

                String itemImagePath = imageJSON.getString("full");

                Item item = new Item(i);
                item.setName(itemName);
                item.setPlainText(itemShortDesc);
                item.setDescription(itemDescription);
                item.setBaseCost(itemBaseCost);
                item.setTotalCost(itemTotalCost);
                item.setSellCost(itemSellCost);
                item.setPurchasable(isItemPurchasable);
                item.setImagePath(itemImagePath);

                itemList.addItem(item);
                Log.d(LOG_TAG, "Añado el item: " + item.toString() + " con id " + i);
            }
        }

        return itemList;
    }





}
