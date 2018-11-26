package com.example.klost.lolstats;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.klost.lolstats.models.Summoner;
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
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    //TODO comprobar conexión a internet
    //TODO crear clase que ejecute todas las requests de datos estaticos

    private static final String LOG_TAG = "MainActivity";

    private EditText searchBoxEditText;

    private TextView urlDisplayTextView;

    private String summonerName;

    private static ChampionList championList;
    private static SummonerSpellList summonerSpellList;
    private static RuneList runeList;
    private static ItemList itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxEditText =  findViewById(R.id.et_search_box);
        urlDisplayTextView =  findViewById(R.id.tv_url_display);

        //Obtencion de los datos estaticos de campeones
        URL championsUrl = NetworkUtils.buildUrl("champion",NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + championsUrl.toString());

        ReadTextTask championsTextTask = new ReadTextTask(this);
        championsTextTask.execute(championsUrl);

        //Obtencion de los datos estaticos de hechizos de invocador
        URL summonerSpellsUrl = NetworkUtils.buildUrl("summoner", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + summonerSpellsUrl.toString());

        ReadTextTask spellsTextTask = new ReadTextTask(this);
        spellsTextTask.execute(summonerSpellsUrl);

        //Obtencion de los datos estaticos de las runas
        URL runesUrl = NetworkUtils.buildUrl("runesReforged", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + runesUrl.toString());

        ReadTextTask runesTextTask = new ReadTextTask(this);
        runesTextTask.execute(runesUrl);

        //Obtencion de los datos estaticos de los items
        URL itemsUrl = NetworkUtils.buildUrl("item", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + itemsUrl.toString());

        ReadTextTask itemsTextTask = new ReadTextTask(this);
        itemsTextTask.execute(itemsUrl);


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

    @Override
    public void onTaskCompleted(String result, String dataType) {

        Log.d(LOG_TAG, "DATA TYPE: " + dataType);

        try {
            //TODO problema con las runes
            if(dataType != null) {
                switch (dataType) {
                    case "summoner":
                        summonerSpellList = JsonUtils.getSpellListFromJSON(result);
                        break;
                    case "champion":
                        championList = JsonUtils.getChampionListFromJSON(result);
                        break;
                    case "item":
                        itemList = JsonUtils.getItemListFromJSON(result);
                        break;
                }
            }else{
                //TODO fix this
                Log.d(LOG_TAG, "dataType es null");
                runeList = JsonUtils.getRuneListFromJSON(result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        private RateLimiter throttler;
        private RateLimiter throttler2;

        RiotQueryTask(MainActivity context){
            //TODO estudiar el impacto que tiene esto en la memoria y si no usar SoftReference
            activityReference = new WeakReference<>(context);
            throttler = throttler.create(1);
            throttler2 = throttler2.create(0.1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityReference.get();
            ProgressBar loadingIndicator = activity.findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
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
                summonerSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL, throttler);
                Log.d(LOG_TAG, "summonerSearchResults: " + summonerSearchResults);

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
                    matchListSearchResults = NetworkUtils.getResponseFromHttpUrl(matchListURL, throttler2);
                    matchList = JsonUtils.getMatchListFromJSON(matchListSearchResults);
                    summoner.setMatchList(matchList);

                    List<Match> matchListToProcess = matchList.getMatches();
                    Match match = matchListToProcess.get(0);
                    URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH);
                    if(getMatchURL != null){
                        matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL, throttler2);
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
                TextView kdaTextView = activity.findViewById(R.id.tv_kda);

                CircleImageView championImageView = activity.findViewById(R.id.iv_champion_icon);

                ImageView firstSummonerSpellIconView = activity.findViewById(R.id.iv_first_summoner);
                ImageView secondSummonerSpellIconView = activity.findViewById(R.id.iv_second_summoner);

                ImageView mainRuneView = activity.findViewById(R.id.iv_main_rune);
                ImageView secondaryRuneView = activity.findViewById(R.id.iv_secondary_rune);

                ImageView firstItemView = activity.findViewById(R.id.iv_item1);
                ImageView secondItemView = activity.findViewById(R.id.iv_item2);
                ImageView thirdItemView = activity.findViewById(R.id.iv_item3);
                ImageView fourthItemView = activity.findViewById(R.id.iv_item4);
                ImageView fifthItemView = activity.findViewById(R.id.iv_item5);
                ImageView sixthItemView = activity.findViewById(R.id.iv_item6);
                ImageView seventhItemView = activity.findViewById(R.id.iv_item7);

                TextView gameDateView = activity.findViewById(R.id.tv_date);
                TextView queueTypeView = activity.findViewById(R.id.tv_game_type);
                TextView gameDurationTextView = activity.findViewById(R.id.tv_game_duration);

                searchResultsTextView.setText(summoner.toString());
                MatchList matchList = summoner.getMatchList();
                List<Match> matches = matchList.getMatches();
                Match match = matches.get(0);

                if(match.isProcessed()){
                    //Comprobamos que el game ha sido analizado y mostramos en la UI la información extraida
                    boolean gameWon = match.hasGivenSummonerWon(summoner);

                    if(gameWon) {
                        matchLayout.setBackgroundColor(Color.parseColor("#4286f4"));
                    }else {
                        matchLayout.setBackgroundColor(Color.parseColor("#f44d41"));
                    }

                    Player player = match.getPlayer(summoner);
                    int championId = player.getChampionId();

                    //TODO revisar posibles problemas con asyncronia(Firebase?)
                    //Seteo de la imagen del campeon jugado
                    Champion champion = championList.getChampionById(championId);
                    champion.loadImageFromDDragon(championImageView);

                    //Seteo de los hechizos de invocador jugados
                    SummonerSpell firstSummonerSpell = summonerSpellList.getSpellById(player.getSpell1Id());
                    firstSummonerSpell.loadImageFromDDragon(firstSummonerSpellIconView);

                    SummonerSpell secondSummonerSpell = summonerSpellList.getSpellById(player.getSpell2Id());
                    secondSummonerSpell.loadImageFromDDragon(secondSummonerSpellIconView);

                    //Seteo de la keystone y de la rama de runas secundaria jugadas
                    Rune mainRune = runeList.getRuneById(player.getRune0());
                    mainRune.loadImageFromDDragon(mainRuneView);

                    RunePath secondaryRune = runeList.getRunePathById(player.getRuneSecondaryStyle());
                    Log.d(LOG_TAG, "Secondary Runepath:" + player.getRuneSecondaryStyle());
                    secondaryRune.loadImageFromDDragon(secondaryRuneView);

                    //Seteo del resultado del jugador
                    killsTextView.setText(String.valueOf(player.getKills()));
                    deathsTextView.setText(String.valueOf(player.getDeaths()));
                    assistsTextView.setText(String.valueOf(player.getAssists()));

                    double kda = LoLStatsUtils.calculateKDA(player.getKills(), player.getAssists(), player.getDeaths());
                    kdaTextView.setText(String.format(Locale.ENGLISH, "KDA: %.2f", kda));

                    //Seteo de los items jugados
                    Item firstItem = itemList.getItemById(player.getItem0());
                    Item secondItem = itemList.getItemById(player.getItem1());
                    Item thirdItem = itemList.getItemById(player.getItem2());
                    Item fourthItem = itemList.getItemById(player.getItem3());
                    Item fifthItem = itemList.getItemById(player.getItem4());
                    Item sixthItem = itemList.getItemById(player.getItem5());
                    Item seventhItem = itemList.getItemById(player.getItem6());

                    Log.d(LOG_TAG, "item" + firstItem.toString());

                    firstItem.loadImageFromDDragon(firstItemView);
                    secondItem.loadImageFromDDragon(secondItemView);
                    thirdItem.loadImageFromDDragon(thirdItemView);
                    fourthItem.loadImageFromDDragon(fourthItemView);
                    fifthItem.loadImageFromDDragon(fifthItemView);
                    sixthItem.loadImageFromDDragon(sixthItemView);
                    seventhItem.loadImageFromDDragon(seventhItemView);

                    //Seteo del resto de datos
                    gameDurationTextView.setText(match.getGameDurationInMinutesAndSeconds());
                    gameDateView.setText(LoLStatsUtils.getDaysAgo(match.getGameCreation()));
                    queueTypeView.setText(LoLStatsUtils.getQueueName(match.getQueue()));


                }else{
                    //TODO make alternative layout for error
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


    private static class ReadTextTask extends AsyncTask<URL, Void, String> {

        OnTaskCompleted listener;

        public ReadTextTask(OnTaskCompleted listener){
            this.listener=listener;
        }

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

            String dataType = null;

            try {
                dataType = JsonUtils.getDataTypeFromJSON(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listener.onTaskCompleted(result, dataType);

        }

    }

}
