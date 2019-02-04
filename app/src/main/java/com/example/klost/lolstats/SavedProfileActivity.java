package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchTimeline;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.common.util.concurrent.RateLimiter;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SavedProfileActivity extends AppCompatActivity{

    private static final String LOG_TAG = SavedProfileActivity.class.getSimpleName();

    public static final String EXTRA_STATS_ITEM = "championStats";
    private static final String LIFECYCLE_CALLBACKS_ENTRY_KEY = "callbacks";

    private static SummonerEntry summoner;
    private static LoLStatsRepository repository;
    private static String accountId;
    private static List<Long> provisionalList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SavedProfileViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_profile);

        Intent previousIntent = getIntent();
        int entryId  = previousIntent.getIntExtra(InitialActivity.EXTRA_ENTRY_ID, -1);
        Log.d(LOG_TAG, "Entry: " + entryId);
        viewPager =  findViewById(R.id.viewPager);
        viewPagerAdapter = new SavedProfileViewPagerAdapter(getSupportFragmentManager(), entryId);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        repository = LoLStatsRepository.getInstance(getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId, 0);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        final SavedProfileActivity context = this;
        viewModel.getEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> matchStatsEntries) {

                provisionalList = new ArrayList<>();
                for(MatchStatsEntry entry:matchStatsEntries){
                    provisionalList.add(entry.getMatchId());
                }
            }
        });
        viewModel.getSummonerEntryLiveData().observe(this, new Observer<SummonerEntry>() {
            @Override
            public void onChanged(SummonerEntry summonerEntry) {
                Log.d(LOG_TAG, "Cambios en el summonerEntry");
                summoner = summonerEntry;
                accountId = summoner.getAccoundId();
                new SavedProfileActivity.FetchSavedSummonerRankedMatchList(context).execute();
            }
        });
    }


    public class FetchSavedSummonerRankedMatchList extends AsyncTask<Void, Void, Match[]>{

        WeakReference<SavedProfileActivity> weakActivity;

        public FetchSavedSummonerRankedMatchList(SavedProfileActivity context){
            this.weakActivity = new WeakReference<>(context);
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
                        //Log.d(LOG_TAG,"Final stamp: " + 1548284400000L + " actual: " + match.getGameCreation().getTime());
                        if((match.getGameCreation().getTime() >= 1548284400000L) || match.getSeason() == 13){
                            //Log.d(LOG_TAG, "Entro");
                            if(provisionalList != null){
                                if(!provisionalList.contains(match.getGameId())){
                                    //Log.d(LOG_TAG, "Añado un game con id: " + match.getGameId());
                                    matchListToProcess.add(match);
                                }
                            }else{
                                //Log.d(LOG_TAG, "Es null asi que añado todo");
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

            SavedProfileActivity activity = weakActivity.get();

            if(matchList != null) {
                for (int i = 0; i < matchList.length; i++) {
                    Match match = matchList[i];
                    if(match != null) {
                        Log.d(LOG_TAG, match.toString());
                        MatchStatsEntry entry = matchToEntry(match);
                        repository.addMatchStatsEntry(entry);
                    }else{
                        Toast.makeText(activity, "A problem ocurred while processing a match", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }else{
                Toast.makeText(activity, "A problem ocurred", Toast.LENGTH_SHORT)
                        .show();
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
        entry.setGoldPercent(LoLStatsUtils.getGoldPercentOfGivenPlayer(match.getTeamOfGivenPlayer(player).getPlayers(), player));
        entry.setVictory(match.hasGivenPlayerWon(player));
        Log.d(LOG_TAG, "ROLE: " + player.getRole() + " mId " +match.getGameId() + " ch " + player.getChampion().getName());
        entry.setRole(player.getRole());
        entry.setPlayedChampion(player.getChampion());
        entry.setPlayer(player);

        Map<Long, Integer> goldDifferenceOverTime = match.getGoldDifferentOfLanersOverTime(sum);

        Iterator<Map.Entry<Long, Integer>> iterator = goldDifferenceOverTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> mapEntry = iterator.next();
            int minute = (int) (mapEntry.getKey()/60000);
            //Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
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

            if(Math.round(minute) == 10) {
                Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
                entry.setCsDiffAt10(mapEntry.getValue());
            }
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
            //Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
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
            //Log.d(LOG_TAG, "Rounded: " + Math.round(minute) + " " + mapEntry.getValue());
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
