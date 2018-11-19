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
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private EditText searchBoxEditText;

    private TextView searchResultsTextView;

    private TextView errorMessageDisplay;

    private TextView urlDisplayTextView;

    private ProgressBar loadingIndicator;

    private String summonerName;

    private LinearLayout matchLayout;

    private TextView killsTextView;
    private TextView assistsTextView;
    private TextView deathsTextView;

    private ChampionList championList;
    private de.hdodenhof.circleimageview.CircleImageView championImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxEditText =  findViewById(R.id.et_search_box);
        searchResultsTextView =  findViewById(R.id.tv_riot_search_resutls_json);
        errorMessageDisplay =  findViewById(R.id.tv_error_message);
        urlDisplayTextView =  findViewById(R.id.tv_url_display);
        loadingIndicator =  findViewById(R.id.pb_loading_indicator);

        matchLayout = findViewById(R.id.ly_match);

        killsTextView = findViewById(R.id.tv_kills);
        deathsTextView = findViewById(R.id.tv_deaths);
        assistsTextView = findViewById(R.id.tv_assists);

        championImageView = findViewById(R.id.iv_champion_icon);

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
        new RiotQueryTask().execute(riotSearchUrl);
    }

    private void showErrorMessage(){
        searchResultsTextView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showData(){
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        searchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showMatchData(Player player){
        killsTextView.setText(player.getKills());
        deathsTextView.setText(player.getDeaths());
        assistsTextView.setText(player.getAssists());
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

    public class RiotQueryTask extends AsyncTask<URL, Void, Summoner> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Summoner doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String summonerSearchResults;
            String matchlistSearchResults = null;
            String matchSearchResults = null;
            Summoner summoner = null;
            MatchList matchList = null;
            try {
                summonerSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
                Log.d("MainActivity", "summonerSearchResults: " + summonerSearchResults);

                if(summonerSearchResults.charAt(0) != '{'){
                    //Entonces significa que la respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error.
                    return null;
                }

                //TODO Multiple async task
                summoner = JsonUtils.getSummonerFromJSON(summonerSearchResults);

                URL matchlistURL = NetworkUtils.buildUrl(String.valueOf(summoner.getAccountId()), NetworkUtils.GET_MATCHLIST);
                matchlistSearchResults = NetworkUtils.getResponseFromHttpUrl(matchlistURL);
                matchList = JsonUtils.getMatchListFromJSON(matchlistSearchResults);

                summoner.setMatchList(matchList);


                List<Match> matchListToProcess = matchList.getMatches();
                Match match = matchListToProcess.get(0);
                URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH);
                matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL);
                match = JsonUtils.getMatchFromJSON(matchSearchResults, match);//TODO revisar esto

                //TODO sacar los datos del match del summoner concreto
                //En funcion de si el summoner ha ganado la partida o no cambiamos el color de fondo para reflejarlo
                boolean gameWon = match.hasGivenSummonerWon(summoner);
                //TODO buena practica?
                Player player = match.getPlayer(summoner);



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

            loadingIndicator.setVisibility(View.INVISIBLE);
            if(summoner != null){
                showData();
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
                    killsTextView.setText(String.valueOf(player.getKills()));
                    deathsTextView.setText(String.valueOf(player.getDeaths()));
                    assistsTextView.setText(String.valueOf(player.getAssists()));

                    URL url = NetworkUtils.buildUrl(champion.getImageFileName(), NetworkUtils.GET_DDRAGON_CHAMPION_IMAGE);
                    Picasso.get().load(url.toString()).into(championImageView);
                }else{
                    killsTextView.setText("ERROR");
                }
            }else{
                showErrorMessage();
            }

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
