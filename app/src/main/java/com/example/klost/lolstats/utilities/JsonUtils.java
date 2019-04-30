package com.example.klost.lolstats.utilities;

import android.util.Log;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchEvent;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchFrame;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchTimeline;
import com.example.klost.lolstats.models.matches.matchtimeline.ParticipantFrame;
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

    private final static String LOG_TAG = "JsonUtils";

    public static Summoner getSummonerFromJSON(String requestJsonStr)
            throws JSONException {

        JSONObject requestJSON = new JSONObject(requestJsonStr);

        String summonerName = requestJSON.getString("name");
        long level  = requestJSON.getLong("summonerLevel");
        long revisionDate = requestJSON.getLong("revisionDate");
        String encryptedSummonerId = requestJSON.getString("id");
        String encryptedAccountId = requestJSON.getString("accountId");
        int profileIconId = requestJSON.getInt("profileIconId");
        String puuid = requestJSON.getString("puuid");

        Summoner summoner = new Summoner();
        summoner.setEncryptedAccountId(encryptedAccountId);
        summoner.setEncryptedSummonerId(encryptedSummonerId);
        summoner.setProfileIconId(profileIconId);
        summoner.setRevisionDate(revisionDate);
        summoner.setSummonerName(summonerName);
        summoner.setSummonerLevel(level);
        summoner.setPuuid(puuid);

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
            long timeStamp = matchJSON.getLong("timestamp");

            match.setLane(lane);
            match.setGameId(gameId);
            match.setChampionId(champion);
            match.setPlatformId(platformId);
            match.setQueue(queue);
            match.setRole(role);
            match.setSeason(season);
            match.setGameCreation(timeStamp);
            Log.d(LOG_TAG, "Lane: " + lane + " rol " + role);

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
            //TODO no value for summonerId

            String accountId = playerJSON.getString("currentAccountId");
            String summonerId;

            //En caso de que sea 0 se trata de un bot
            if(playerJSON.has("summonerId"))
                summonerId = playerJSON.getString("summonerId");
            else
                summonerId = "0";

            String summonerName = playerJSON.getString("summonerName");

            Summoner summoner = new Summoner(accountId, summonerId);
            summoner.setSummonerName(summonerName);

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
            //TODO añadir bans
            JSONObject teamJSON = teamsJSON.getJSONObject(i);

            //Si el id es 100 el equipo jugó en el lado azul, si es 200, en el rojo
            int teamId = teamJSON.getInt("teamId");
            String win = teamJSON.getString("win");
            int dragonsKilled = teamJSON.getInt("dragonKills");
            int towersDestroyed = teamJSON.getInt("towerKills");
            Log.d("TOWERS DESTROYED", "T:" + towersDestroyed);
            int baronsKilled = teamJSON.getInt("baronKills");
            Log.d(LOG_TAG, "Team: " + teamId + " game result: " + win);

            if(teamId == 100){
                blueTeam.setWin(win);
                blueTeam.setBaronKills(baronsKilled);
                blueTeam.setDragonKills(dragonsKilled);
                blueTeam.setTowerKills(towersDestroyed);
            }else if(teamId == 200){
                redTeam.setWin(win);
                redTeam.setBaronKills(baronsKilled);
                redTeam.setDragonKills(dragonsKilled);
                redTeam.setTowerKills(towersDestroyed);
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

            int champLevel = statsJSON.getInt("champLevel");

            int totalMinionsKilled = statsJSON.getInt("totalMinionsKilled");
            int neutralMinionsKilled = statsJSON.getInt("neutralMinionsKilled");

            int goldEarned = statsJSON.getInt("goldEarned");
            int goldSpent = statsJSON.getInt("goldSpent");

            long visionScore = statsJSON.getLong("visionScore");
            int largestMultiKill = statsJSON.getInt("largestMultiKill");
            int wardsKilled = 0;
            if(statsJSON.has("wardsKilled")){
                wardsKilled = statsJSON.getInt("wardsKilled");
            }
            int largestKillingSpree = statsJSON.getInt("largestKillingSpree");
            int quadrakills = statsJSON.getInt("quadraKills");
            int wardsPlaced = 0;
            if(statsJSON.has("wardsPlaced"))
                wardsPlaced = statsJSON.getInt("wardsPlaced");

            int pentakills = statsJSON.getInt("pentaKills");
            int visionWardsBought = statsJSON.getInt("visionWardsBoughtInGame");

            JSONObject timelineJSON = participantJSON.getJSONObject("timeline");
            String lane = timelineJSON.getString("lane");
            String role = timelineJSON.getString("role");

            for(Player player : playerList){
                if(player.getParticipantId() == participantId){

                    player.setSpell1Id(spell1Id);
                    player.setSpell2Id(spell2Id);
                    //TODO eliminar
                    player.setChampionId(championId);
                    Champion playedChampion = StaticData.getChampionList().getChampionById(championId);
                    player.setChampion(playedChampion);
                    player.setTeamId(teamId);

                    player.setKills(kills);
                    player.setDeaths(deaths);
                    player.setAssists(assists);

                    player.setVisionScore(visionScore);
                    player.setLargestMultiKill(largestMultiKill);
                    player.setWardsPlaced(wardsPlaced);
                    player.setPentaKills(pentakills);
                    player.setQuadraKills(quadrakills);
                    player.setVisionWardsBought(visionWardsBought);
                    player.setWardsKilled(wardsKilled);
                    player.setLargestKillingSpree(largestKillingSpree);

                    Log.d(LOG_TAG, "Role: " + role);
                    if(role.equals("NONE") || role.equals("SOLO") ){
                        Log.d(LOG_TAG, "Entro con " + role);
                        player.setRole(lane);
                    }else{
                        player.setRole(role);
                    }
                    Log.d(LOG_TAG, "El rol es: " + player.getRole() + " " +  match.getGameId());

                    player.setLane(lane);

                    player.setChampionLevel(champLevel);
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
                    player.setItem1(item1);
                    player.setItem2(item2);
                    player.setItem3(item3);
                    player.setItem4(item4);
                    player.setItem5(item5);
                    player.setItem6(item6);

                    player.setTotalMinionsKilled(totalMinionsKilled);
                    player.setNeutralMinionsKilled(neutralMinionsKilled);

                    player.setGoldEarned(goldEarned);
                    player.setGoldSpent(goldSpent);

                    //Determinamos el equipo del jugador
                    if(teamId == blueTeam.getTeamId()){
                        if(role.equals("DUO") && blueTeam.hasCarryPlayer()){
                            Player carryPlayer = blueTeam.getCarryPlayer();
                            if(carryPlayer.getTotalMinionsKilled() < player.getTotalMinionsKilled()){
                                player.setRole("DUO_CARRY");
                                carryPlayer.setRole("DUO_SUPPORT");
                            }else{
                                player.setRole("DUO_SUPPORT");
                            }
                        }else if(role.equals("DUO")){
                            player.setRole("DUO_CARRY");
                        }
                        blueTeam.addPlayer(player);
                    }else if(teamId == redTeam.getTeamId()){
                        if(role.equals("DUO") && redTeam.hasCarryPlayer()){
                            Player carryPlayer = redTeam.getCarryPlayer();
                            if(carryPlayer.getTotalMinionsKilled() < player.getTotalMinionsKilled()){
                                player.setRole("DUO_CARRY");
                                carryPlayer.setRole("DUO_SUPPORT");
                            }else{
                                player.setRole("DUO_SUPPORT");
                            }
                        }else if(role.equals("DUO")){
                            player.setRole("DUO_CARRY");
                        }
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
        Log.d(LOG_TAG, "Entro con: " + requestJsonStr);
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

    public static LeaguePositionList getLeaguePositionListFromJSON(String requestStrJson) throws JSONException {

        JSONArray jsonArray = new JSONArray(requestStrJson);
        LeaguePositionList list = new LeaguePositionList();

        for(int i=0; i<jsonArray.length(); i++){

            JSONObject positionJSON = jsonArray.getJSONObject(i);

            String queueType = positionJSON.getString("queueType");
            int wins = positionJSON.getInt("wins");
            int losses = positionJSON.getInt("losses");
            String rank = positionJSON.getString("rank");
            String tier = positionJSON.getString("tier");
            int leaguePoints = positionJSON.getInt("leaguePoints");
      //      boolean hotStreak = positionJSON.getBoolean("hotStreak");

            LeaguePosition position = new LeaguePosition();

            position.setLeaguePoints(leaguePoints);
            position.setTier(tier);
            position.setWins(wins);
            position.setLosses(losses);
            position.setRank(rank);
            position.setQueueType(queueType);
          //  position.setHotstreak(hotStreak);

            list.addLeaguePosition(position);
        }

        return list;

    }

    public static MatchTimeline getMatchTimeLine(String requestJsonStr) throws JSONException {

        JSONObject jsonObject = new JSONObject(requestJsonStr);

        ArrayList<MatchFrame> matchFrames = new ArrayList<>();
        long frameInterval = jsonObject.getLong("frameInterval");

        JSONArray framesJSON = jsonObject.getJSONArray("frames");
        //Recorremos la lista de frames
        for(int i = 0; i<framesJSON.length(); i++){

            JSONObject frameJSON = framesJSON.getJSONObject(i);
            long timestamp = frameJSON.getLong("timestamp");
            JSONObject participantsJSON = frameJSON.getJSONObject("participantFrames");
            Iterator x = participantsJSON.keys();
            JSONArray participantsArray = new JSONArray();

            while (x.hasNext()){
                String key = (String) x.next();
                participantsArray.put(participantsJSON.get(key));
            }

            ArrayList<ParticipantFrame> participantsFrames = new ArrayList<>();
            //Recorremos la lista de participant frames
            for(int j = 0; j<participantsArray.length(); j++){
                JSONObject participantJSON = participantsArray.getJSONObject(j);
                ParticipantFrame participantFrame = new ParticipantFrame();

                int totalGold = participantJSON.getInt("totalGold");
                if(participantJSON.has("teamScore")) {
                    int teamScore = participantJSON.getInt("teamScore");
                    participantFrame.setTeamScore(teamScore);
                }
                int participantId = participantJSON.getInt("participantId");
                int level = participantJSON.getInt("level");
                int currentGold = participantJSON.getInt("currentGold");
                int minionsKilled = participantJSON.getInt("minionsKilled");
                int jungleMinionsKilled = participantJSON.getInt("jungleMinionsKilled");
                int xp = participantJSON.getInt("xp");
                participantFrame.setTotalGold(totalGold);
                participantFrame.setParticipantId(participantId);
                participantFrame.setLevel(level);
                participantFrame.setCurrentGold(currentGold);
                participantFrame.setMinionsKilled(minionsKilled);
                participantFrame.setJungleMinionsKilled(jungleMinionsKilled);
                participantFrame.setXp(xp);

                participantsFrames.add(participantFrame);
            }

            ArrayList<MatchEvent> matchEvents = new ArrayList<>();
            //TODO añadir eventos

            MatchFrame frame = new MatchFrame(timestamp, matchEvents, participantsFrames);
            matchFrames.add(frame);
        }

        MatchTimeline matchTimeline = new MatchTimeline(matchFrames, frameInterval);

        return matchTimeline;
    }

    //TODO testear
    public static Match getLiveGameFromJson(String requestJsonStr) throws JSONException {

        JSONObject jsonObject = new JSONObject(requestJsonStr);
        long gameId = jsonObject.getLong("gameId");
        long gameStartTime = jsonObject.getLong("gameStartTime");
        String gameMode = jsonObject.getString("gameMode");
        int mapId = jsonObject.getInt("mapId");
        String gameType = jsonObject.getString("gameType");
        int gameQueue = jsonObject.getInt("gameQueueConfigId");

        JSONArray participantsArray = jsonObject.getJSONArray("participants");
        Match match = new Match();
        match.setGameId(gameId);
        match.setGameCreation(gameStartTime);
        match.setQueue(gameQueue);
        match.setGameType(gameType);

        Team blueTeam = new Team(100);
        Team redTeam = new Team(200);

        for(int i = 0; i<participantsArray.length(); i++){
            JSONObject participantJson = participantsArray.getJSONObject(i);
            int profileIconId = participantJson.getInt("profileIconId");
            int championId = participantJson.getInt("championId");
            String summonerName = participantJson.getString("summonerName");
            boolean bot = participantJson.getBoolean("bot");
            int spell1Id = participantJson.getInt("spell1Id");
            int spell2Id = participantJson.getInt("spell2Id");
            int teamId = participantJson.getInt("teamId");
            String summonerId = participantJson.getString("summonerId");

            Champion champion = new Champion(championId);
            Summoner summoner = new Summoner();
            summoner.setEncryptedSummonerId(summonerId);
            summoner.setProfileIconId(profileIconId);
            summoner.setSummonerName(summonerName);
            Player player = new Player();
            player.setChampion(champion);
            player.setSummoner(summoner);
            player.setSpell1Id(spell1Id);
            player.setSpell2Id(spell2Id);
            //TODO añadir is bot a player
            if(teamId == 100)
                blueTeam.addPlayer(player);
            else if(teamId == 200)
                redTeam.addPlayer(player);
        }

        match.setBlueTeam(blueTeam);
        match.setRedTeam(redTeam);

        return match;

    }





}
