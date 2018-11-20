package com.example.klost.lolstats;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private EditText searchBoxEditText;

    private TextView searchResultsTextView;

    private TextView errorMessageDisplay;

    private TextView urlDisplayTextView;

    private String summonerName;

    private static ChampionList championList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxEditText =  findViewById(R.id.et_search_box);
        urlDisplayTextView =  findViewById(R.id.tv_url_display);
        errorMessageDisplay = findViewById(R.id.tv_error_message);
        searchResultsTextView = findViewById(R.id.tv_riot_search_resutls_json);

        URL championUrl = NetworkUtils.buildUrl("champion",NetworkUtils.GET_DDRAGON_DATA);
        Log.d("MainActivity", "URL: " + championUrl.toString());

        new ReadTextTask().execute(championUrl);


        Button searchButton =  findViewById(R.id.bt_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBoxEditText.getText().toString();
                makeRiotSearchQuery(searchQuery);
            }
        });


        Intent previousIntent = getIntent();
        summonerName = previousIntent.getStringExtra(InitialActivity.EXTRA_SUMMONER_NAME);
        makeRiotSearchQuery(summonerName);

    }

    private void makeRiotSearchQuery(String searchQuery){
        URL riotSearchUrl = NetworkUtils.buildUrl(searchQuery, NetworkUtils.GET_SUMMONER);
        URL prueba = NetworkUtils.buildUrl("123456", NetworkUtils.GET_MATCHLIST);
        urlDisplayTextView.setText(prueba.toString()+ "\n" + riotSearchUrl);
        new RiotQueryTask(this).execute(riotSearchUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.action_refresh){
            makeRiotSearchQuery(summonerName);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class RiotQueryTask extends AsyncTask<URL, Void, Summoner> {

        private WeakReference<MainActivity> activityReference;

        RiotQueryTask(MainActivity context){
            //TODO estudiar el impacto que tiene esto en la memoria y si no usar SoftReference
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityReference.get();
            ProgressBar loadingIndicator = activity.findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            TextView searchResultsTextView =  activity.findViewById(R.id.tv_riot_search_resutls_json);
        }

        @Override
        protected Summoner doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String summonerSearchResults;
            String matchListSearchResults;
            String matchSearchResults;
            Summoner summoner = null;
            MatchList matchList;
            try {
                summonerSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
                Log.d("MainActivity", "summonerSearchResults: " + summonerSearchResults);

                if(summonerSearchResults.charAt(0) != '{'){
                    //Entonces significa que la respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error.
                    Log.e(LOG_TAG, "Error: La respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error");
                    //TODO conseguir diferenciar los errores
                    return null;
                }

                //TODO Multiple async task
                summoner = JsonUtils.getSummonerFromJSON(summonerSearchResults);

                URL matchListURL = NetworkUtils.buildUrl(String.valueOf(summoner.getAccountId()), NetworkUtils.GET_MATCHLIST);
                if(matchListURL != null){
                    matchListSearchResults = NetworkUtils.getResponseFromHttpUrl(matchListURL);
                    matchList = JsonUtils.getMatchListFromJSON(matchListSearchResults);
                    summoner.setMatchList(matchList);

                    List<Match> matchListToProcess = matchList.getMatches();
                    Match match = matchListToProcess.get(0);
                    URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH);
                    if(getMatchURL != null){
                        matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL);
                        JsonUtils.getMatchFromJSON(matchSearchResults, match);//TODO revisar esto
                    }else{
                        return null;
                    }

                }else{
                    return null;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            if(summoner != null)
                return summoner;
            else
                return null;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {

            MainActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;
            ProgressBar loadingIndicator = activity.findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.INVISIBLE);
            if(summoner != null){
                showData(activity);

                TextView searchResultsTextView = activity.findViewById(R.id.tv_riot_search_resutls_json);
                LinearLayout matchLayout = activity.findViewById(R.id.ly_match);

                TextView killsTextView = activity.findViewById(R.id.tv_kills);
                TextView deathsTextView = activity.findViewById(R.id.tv_deaths);
                TextView assistsTextView = activity.findViewById(R.id.tv_assists);

                CircleImageView championImageView = activity.findViewById(R.id.iv_champion_icon);

                searchResultsTextView.setText(summoner.toString());
                MatchList matchList = summoner.getMatchList();
                List<Match> matches = matchList.getMatches();
                Match match = matches.get(0);
                if(match.isProcessed()){
                    boolean gameWon = match.hasGivenSummonerWon(summoner);

                    if(gameWon) {
                        matchLayout.setBackgroundColor(Color.parseColor("#4286f4"));
                    }else {
                        matchLayout.setBackgroundColor(Color.parseColor("#f44d41"));
                    }

                    Player player = match.getPlayer(summoner);
                    int championId = player.getChampionId();
                    //TODO hacer para que no halla problemas con la asyncronia
                    Champion champion = championList.getChampionById(championId);
                    champion.loadImageFromDDragon(championImageView);
                    killsTextView.setText(String.valueOf(player.getKills()));
                    deathsTextView.setText(String.valueOf(player.getDeaths()));
                    assistsTextView.setText(String.valueOf(player.getAssists()));
                }else{
                    killsTextView.setText("ERROR");
                }
            }else{
                showErrorMessage(activity);
            }

        }

        private void showData(MainActivity activity){
            TextView errorMessageDisplay = activity.findViewById(R.id.tv_error_message);
            TextView searchResultsTextView = activity.findViewById(R.id.tv_riot_search_resutls_json);

            errorMessageDisplay.setVisibility(View.INVISIBLE);
            searchResultsTextView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage(MainActivity activity){
            TextView errorMessageDisplay = activity.findViewById(R.id.tv_error_message);
            TextView searchResultsTextView = activity.findViewById(R.id.tv_riot_search_resutls_json);

            searchResultsTextView.setVisibility(View.INVISIBLE);
            errorMessageDisplay.setVisibility(View.VISIBLE);
        }


    }


    private class ReadTextTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String str = null;
            try {
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(urls[0].openStream()));
                str = in.readLine();
                in.close();
            }
            catch (IOException e) {
                // ** do something here **
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("ReadTextTask", "resultado" + result);

            try {
                 championList = JsonUtils.getChampionListFromJSON(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
