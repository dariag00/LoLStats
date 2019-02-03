package com.example.klost.lolstats;

import android.content.Context;
import android.content.Intent;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchTimeline;
import com.example.klost.lolstats.models.summoners.SummonerSpell;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class GameDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private static final String LOG_TAG = "GameDetails";
    private long gameId;
    private int summonerId;
    private SummonerEntry summonerEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        viewPager =  findViewById(R.id.viewPager);
        tabLayout =  findViewById(R.id.tabs);

        final GameDetailsActivity context = this;
        Intent previousIntent = getIntent();
        if(previousIntent.hasExtra("MatchStatsEntryId")){
            gameId = previousIntent.getLongExtra("MatchStatsEntryId", -1);
            Log.d(LOG_TAG, "Game ID: " + gameId);
            summonerId = previousIntent.getIntExtra("summonerEntry", -1);

            LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getApplication(), AppExecutors.getInstance());
            SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, summonerId, 0);
            final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
            viewModel.getSummonerEntryLiveData().observe(this, new Observer<SummonerEntry>() {
                @Override
                public void onChanged(SummonerEntry entry) {
                    Log.d(LOG_TAG, "Entro en observer");
                    summonerEntry = entry;
                    new FetchMatch(context).execute();
                }
            });

        }else{
            Summoner summoner = (Summoner) previousIntent.getSerializableExtra("summonerObject");
            Match match = (Match) previousIntent.getSerializableExtra("matchObject");
            Player player = match.getPlayer(summoner);
            Log.d(LOG_TAG, "Player: " + player.toString());
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), match, summoner, player);
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class FetchMatch extends AsyncTask<Void, Void, Match> {

        WeakReference<GameDetailsActivity> weakActivity;

        public FetchMatch(GameDetailsActivity activity){
            this.weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected Match doInBackground(Void... voids) {
            String matchSearchResults = null;
            String matchTimelineSearchResults = null;
            RateLimiter throttler = RateLimiter.create(0.7);
            URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(gameId), NetworkUtils.GET_MATCH);
            Match match = null;
            if(getMatchURL != null) {
                try {
                    matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL, throttler);
                    Match testMatch = new Match();
                    match = JsonUtils.getMatchFromJSON(matchSearchResults, testMatch);
                    match.setGameId(gameId);
                    match.setGameType("420");
                    URL getMatchTimelineURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH_TIMELINE);
                    if(getMatchTimelineURL != null) {
                        matchTimelineSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchTimelineURL, throttler);
                        MatchTimeline matchTimeline = JsonUtils.getMatchTimeLine(matchTimelineSearchResults);
                        match.setMatchTimeline(matchTimeline);
                        Log.d(LOG_TAG, "TimeLine: " + match.getMatchTimeline().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return match;
        }

        @Override
        protected void onPostExecute(Match match) {

            GameDetailsActivity activity = weakActivity.get();

            if(match != null) {
                Summoner summoner = new Summoner(summonerEntry);
                Player player = match.getPlayer(summoner);
                Log.d(LOG_TAG, "XD_ " + match.getMatchTimeline());
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), match, summoner, player);
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }else{
                Toast.makeText(activity, "A problem ocurred", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
