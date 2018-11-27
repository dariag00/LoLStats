package com.example.klost.lolstats;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private static RecyclerView recyclerView;
    private static RiotAdapter riotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxEditText =  findViewById(R.id.et_search_box);
        urlDisplayTextView =  findViewById(R.id.tv_url_display);
        recyclerView = findViewById(R.id.recyclerview_matches);

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


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        riotAdapter = new RiotAdapter();
        recyclerView.setAdapter(riotAdapter);

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
            //Seteamos la cantidad de requests que pueden salir por segundo.
            throttler = throttler.create(0.7);
            //throttler2 = throttler2.create(0.1);
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
                    matchListSearchResults = NetworkUtils.getResponseFromHttpUrl(matchListURL, throttler);
                    matchList = JsonUtils.getMatchListFromJSON(matchListSearchResults);
                    summoner.setMatchList(matchList);

                    List<Match> matchListToProcess = matchList.getMatches();
                    Log.d(LOG_TAG, " MatchList Total Games: " + matchList.getMatches().size());
                    for(int i = 0;i<matchList.getMatches().size(); i++){
                        Match match = matchListToProcess.get(i);
                        URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH);
                        if(getMatchURL != null){
                            matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL, throttler);
                            JsonUtils.getMatchFromJSON(matchSearchResults, match);//TODO revisar esto
                        }else{
                            return null;
                        }
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

                MatchList matchList = summoner.getMatchList();
                List<Match> matches = matchList.getMatches();
                int contador = 0;
                Match[] processedMatches = new Match[matchList.getMatches().size()];

                for(Match m : matches){
                    if(m.isProcessed()){
                        processedMatches[contador] = m;
                        contador++;
                    }
                    if(contador>=matchList.getMatches().size()){
                        break;
                    }

                    Log.d(LOG_TAG, "Meto un match: " + contador);
                }

                Log.d(LOG_TAG, "Contador: " + contador);

                //TODO procesar N matches

                riotAdapter.setData(processedMatches, summoner, championList, runeList, summonerSpellList, itemList);


            }else{
                showErrorMessage(activity);
            }

        }

        private void showData(MainActivity activity){
            TextView errorMessageDisplay = activity.findViewById(R.id.tv_error_message);


            errorMessageDisplay.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage(MainActivity activity){
            TextView errorMessageDisplay = activity.findViewById(R.id.tv_error_message);

            recyclerView.setVisibility(View.INVISIBLE);
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
