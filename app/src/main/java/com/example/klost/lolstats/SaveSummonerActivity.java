package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.AppDatabase;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class SaveSummonerActivity extends AppCompatActivity {

    EditText editText;

    private static LoLStatsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_summoner);
        editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
        repository = LoLStatsRepository.getInstance(this.getApplication(),  AppExecutors.getInstance());
    }

    public void onSaveButtonClicked() {
        String summoner = editText.getText().toString();
        new SummonerQueryTask().execute(summoner);
        finish();
    }

    public static class SummonerQueryTask extends AsyncTask<String, Void, Summoner>{


        @Override
        protected Summoner doInBackground(String... strings) {
            RateLimiter throttler = RateLimiter.create(0.7);
            String summonerName = strings[0];
            Summoner summoner = null;
            String summonerSearchResults;
            String leaguePositionList;
            URL riotSearchUrl = NetworkUtils.buildUrl(summonerName, NetworkUtils.GET_SUMMONER);
            try {
                summonerSearchResults = NetworkUtils.getResponseFromHttpUrl(riotSearchUrl, throttler);
                summoner = JsonUtils.getSummonerFromJSON(summonerSearchResults);
                URL leaguesURL = NetworkUtils.buildUrl(String.valueOf(summoner.getEncryptedSummonerId()), NetworkUtils.GET_LEAGUES_POSITIONS);
                if(leaguesURL != null){
                    String leaguesSearchResult = NetworkUtils.getResponseFromHttpUrl(leaguesURL, throttler);
                    LeaguePositionList positionList = JsonUtils.getLeaguePositionListFromJSON(leaguesSearchResult);
                    summoner.setPositionList(positionList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return summoner;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {

            final SummonerEntry entry = new SummonerEntry(summoner.getPuuid(), summoner.getEncryptedAccountId(), summoner.getEncryptedSummonerId());
            entry.setProfileIconId(summoner.getProfileIconId());
            entry.setSummonerLevel(summoner.getSummonerLevel());
            entry.setSummonerName(summoner.getSummonerName());

            LeaguePositionList list = summoner.getPositionList();
            //TODO metodo que devuelva tu rank mas alto
            LeaguePosition soloQ = list.getRankedSoloPosition();

            if(soloQ != null) {
                entry.setDivision(soloQ.getRank());
                entry.setRank(soloQ.getRank());
                entry.setTier(soloQ.getTier());
                entry.setTotalRankedGames(soloQ.getWins() + soloQ.getLosses());
                entry.setWinRate(52);
                entry.setGamesWon(soloQ.getWins());
                entry.setLeaguePoints(soloQ.getLeaguePoints());
            }

            repository.addSummonerEntry(entry);
        }
    }
}
