package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.models.champions.ChampionStatsList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchTimeline;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.example.klost.lolstats.utilities.StaticData;
import com.google.common.util.concurrent.RateLimiter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SavedProfileActivity extends AppCompatActivity implements ChampionStatsAdapter.ItemClickListener{

    private RecyclerView recyclerView;
    private ChampionStatsAdapter adapter;
    private static String accountId;
    private static List<Long> provisionalList;
    private static final String LOG_TAG = SavedProfileActivity.class.getSimpleName();
    private static LoLStatsRepository repository;
    private static SummonerEntry summoner;

    public static final String EXTRA_STATS_ITEM = "championStats";
    private static final String LIFECYCLE_CALLBACKS_ENTRY_KEY = "callbacks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent previousIntent = getIntent();
        //TODO null
        int entryId  = previousIntent.getIntExtra(InitialActivity.EXTRA_ENTRY_ID, -1);

        repository = LoLStatsRepository.getInstance(getApplication(), AppExecutors.getInstance());

        Log.d(LOG_TAG, "Account Id: " + entryId);
        //accountId = "ScJfpvZkwqgnXiyoQ52hFS5F0iLpaMuHvus_vJf79mDObQ";
        setContentView(R.layout.activity_saved_profile);
        recyclerView = findViewById(R.id.champions_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChampionStatsAdapter(this, this);
        recyclerView.setAdapter(adapter);

        LoLStatsRepository repository = LoLStatsRepository.getInstance(getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> matchStatsEntries) {
                //TODO llamar a un metodo que analice todo y cree una List de ChampionStats
                provisionalList = new ArrayList<>();
                for(MatchStatsEntry entry:matchStatsEntries){
                    provisionalList.add(entry.getMatchId());
                }
                ChampionStatsList list = generateChampionStats(matchStatsEntries);
                List<ChampionStats> championStatsList = list.getChampionStatsList();
                //Ordenamos la lista por numero de partidos jugados
                Collections.sort(championStatsList, new Comparator<ChampionStats>() {
                    @Override
                    public int compare(ChampionStats o1, ChampionStats o2) {
                        if (o1.getNumberOfGamesPlayed() < o2.getNumberOfGamesPlayed()){
                            return 1;
                        }else if(o1.getNumberOfGamesPlayed() > o2.getNumberOfGamesPlayed()) {
                            return -1;
                        }else{
                            if(o1.getNumberOfGamesWon() < o2.getNumberOfGamesWon())
                                return 1;
                            if(o1.getNumberOfGamesWon() > o2.getNumberOfGamesWon())
                                return -1;
                            return 0;
                        }
                    }
                });
                adapter.setEntries(championStatsList);
            }
        });

        viewModel.getSummonerEntryLiveData().observe(this, new Observer<SummonerEntry>() {
            @Override
            public void onChanged(SummonerEntry summonerEntry) {
                Log.d(LOG_TAG, "Cambios en el summonerEntry");
                summoner = summonerEntry;
                accountId = summoner.getAccoundId();
                new FetchSavedSummonerRankedMatchList().execute();
            }
        });

    }

    public ChampionStatsList generateChampionStats(List<MatchStatsEntry> entries){

        ChampionStatsList championStatsList = new ChampionStatsList();

        for(MatchStatsEntry statsEntry : entries){
            Champion champion = StaticData.getChampionList().getChampionById(statsEntry.getChampionId());
            if(championStatsList.containsChampion(champion)){
                Log.d(LOG_TAG, "Entro en stats de: " + champion.getName());
                ChampionStats currentStats = championStatsList.getChampionStats(champion);
                currentStats.addNewStat(statsEntry);
            }else{
                Log.d(LOG_TAG, "Nueva stats de: " + champion.getName());
                ChampionStats newChampionStats = new ChampionStats(statsEntry);
                championStatsList.addChampionStats(newChampionStats);
            }
        }

        return championStatsList;
    }

    @Override
    public void onItemClickListener(ChampionStats championStats) {
        Context context = this;
        Toast.makeText(context, "Clicked " + championStats.getChampion().getName(), Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, ChampionDetailActivity.class);
        //intent.putExtra(EXTRA_STATS_ITEM, championStats);
        //sstartActivity(intent);
    }

    public class FetchSavedSummonerRankedMatchList extends AsyncTask<Void, Void, Match[]>{

        public FetchSavedSummonerRankedMatchList(){
        }

        @Override
        protected Match[] doInBackground(Void... voids) {
            String matchListSearchResults;
            String matchSearchResults;
            String matchTimelineSearchResults;
            MatchList matchList;
            RateLimiter throttler = RateLimiter.create(0.7);
            URL matchListURL = NetworkUtils.buildUrl(accountId, NetworkUtils.GET_RANKED_MATCHLIST);
            Match[] matches = null;
            try {
                if(matchListURL != null){
                    Log.d(LOG_TAG, "URL: " + matchListURL.toString());
                    matchListSearchResults = NetworkUtils.getResponseFromHttpUrl(matchListURL, throttler);
                    matchList = JsonUtils.getMatchListFromJSON(matchListSearchResults);

                    List<Match> matchListToProcess = new ArrayList<>();
                    //Log.d(LOG_TAG, matchList.toString());
                    for(Match match : matchList.getMatches()){
                        Log.d(LOG_TAG,"Final stamp: " + 1548284400000L + " actual: " + match.getGameCreation().getTime());
                        if((match.getGameCreation().getTime() >= 1548284400000L) || match.getSeason() == 13){
                            Log.d(LOG_TAG, "Entro");
                            if(provisionalList != null){
                                if(!provisionalList.contains(match.getGameId())){
                                    Log.d(LOG_TAG, "Añado un game con id: " + match.getGameId());
                                    matchListToProcess.add(match);
                                }
                            }else{
                                Log.d(LOG_TAG, "Es null asi que añado todo");
                                matchListToProcess.add(match);
                            }
                        }
                    }

                    Log.d(LOG_TAG, " MatchList Total Games: " + matchListToProcess.size());
                    matches = new Match[matchListToProcess.size()];
                    for(int i = 0;i<matchListToProcess.size(); i++){
                        Match match = matchListToProcess.get(i);
                        URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH);
                        if(getMatchURL != null){
                            matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL, throttler);
                            JsonUtils.getMatchFromJSON(matchSearchResults, match);

                            URL getMatchTimelineURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH_TIMELINE);
                            if(getMatchTimelineURL != null){
                                matchTimelineSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchTimelineURL, throttler);
                                MatchTimeline matchTimeline = JsonUtils.getMatchTimeLine(matchTimelineSearchResults);
                                match.setMatchTimeline(matchTimeline);
                                Log.d(LOG_TAG, "Procesado con exito");
                                matches[i] = match;
                            }
                        }else{
                            Log.d(LOG_TAG, "Devuelvo null en getMatchURL");
                            return null;
                        }
                    }

                }else{
                    return null;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return matches;
        }

        @Override
        protected void onPostExecute(Match[] matchList) {
            //TODO meter en la base de datos
            for(int i = 0; i<matchList.length; i++){
                Match match = matchList[i];
                Log.d(LOG_TAG, match.toString());
                MatchStatsEntry entry = matchToEntry(match);
                repository.addMatchStatsEntry(entry);
            }
        }
    }

    public MatchStatsEntry matchToEntry(Match match){
        MatchStatsEntry entry = new MatchStatsEntry(match.getGameId());

        Summoner sum = new Summoner(summoner);
        Player player = match.getPlayer(sum);
        //TODO crear constructor con match
        entry.setKills(player.getKills());
        entry.setAssists(player.getAssists());
        entry.setDeaths(player.getDeaths());
        entry.setChampionId(player.getChampionId());
        entry.setDuration(match.getGameDuration());
        entry.setGameDate(match.getGameCreation());
        //TODO temporal
        entry.setSeasonId(13);
        entry.setSummonerId(summoner.getId());
        entry.setTotalCs(player.getTotalMinionsKilled() + player.getNeutralMinionsKilled());
        entry.setTotalDamage(player.getTotalDamageDealtToChampions());
        entry.setVisionScore(player.getVisionScore());
        entry.setTotalGold(player.getGoldEarned());
        entry.setDamagePercent(LoLStatsUtils.getDamagePercentOfGivenPlayer(match.getTeamOfGivenPlayer(player).getPlayers(), player));
        entry.setVictory(match.hasGivenPlayerWon(player));

        Map<Long, Integer> goldDifferenceOverTime = match.getGoldDifferentOfLanersOverTime(sum);

        Iterator<Map.Entry<Long, Integer>> iterator = goldDifferenceOverTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> mapEntry = iterator.next();
            int minute = (int) (mapEntry.getKey()/60000);
            Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
            if(Math.round(minute) == 10)
                entry.setGoldDiff10(mapEntry.getValue());
            if(Math.round(minute) == 15)
                entry.setGoldDiff15(mapEntry.getValue());
            if(Math.round(minute) == 20)
                entry.setGoldDiff20(mapEntry.getValue());
        }

        Map<Long, Integer> csDifferenceOverTime = match.getCsDifferentOfLanersOverTime(sum);

        iterator = csDifferenceOverTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> mapEntry = iterator.next();
            int minute = (int) (mapEntry.getKey()/60000);
            Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
            if(Math.round(minute) == 10)
                entry.setCsDiffAt10(mapEntry.getValue());
            if(Math.round(minute) == 15)
                entry.setCsDiffAt15(mapEntry.getValue());
            if(Math.round(minute) == 20)
                entry.setCsDiffAt20(mapEntry.getValue());
        }

        Map<Long, Integer> getCsOverTime = match.getParticipantCs(player.getParticipantId());

        iterator = getCsOverTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> mapEntry = iterator.next();
            int minute = (int) (mapEntry.getKey()/60000);
            Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
            if(Math.round(minute) == 10)
                entry.setCsAt10(mapEntry.getValue());
            if(Math.round(minute) == 15)
                entry.setCsAt15(mapEntry.getValue());
            if(Math.round(minute) == 20)
                entry.setCsAt20(mapEntry.getValue());
        }

        Map<Long, Integer> getGoldOverTime = match.getParticipantGold(player.getParticipantId());

        iterator = getGoldOverTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> mapEntry = iterator.next();
            int minute = (int) (mapEntry.getKey()/60000);
            Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
            if(Math.round(minute) == 10)
                entry.setGoldAt10(mapEntry.getValue());
            if(Math.round(minute) == 15)
                entry.setGoldAt15(mapEntry.getValue());
            if(Math.round(minute) == 20)
                entry.setGoldAt20(mapEntry.getValue());
        }


        return entry;
    }

}
