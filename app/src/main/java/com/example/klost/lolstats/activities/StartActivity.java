package com.example.klost.lolstats.activities;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.klost.lolstats.OnTaskCompleted;
import com.example.klost.lolstats.R;
import com.example.klost.lolstats.activities.InitialActivity;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.runes.RuneList;
import com.example.klost.lolstats.models.summoners.SummonerSpellList;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.example.klost.lolstats.utilities.StaticData;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class StartActivity extends AppCompatActivity implements OnTaskCompleted {

    static ChampionList championList;
    static SummonerSpellList summonerSpellList;
    static RuneList runeList;
    static ItemList itemList;
    int tasksCompleted;
    String LOG_TAG = "Start Activity";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        progressBar = findViewById(R.id.pb_loading_indicator);

        tasksCompleted = 0;
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
    }

    @Override
    public void onTaskCompleted(String result, String dataType) {

        Log.d(LOG_TAG, "DATA TYPE: " + dataType);

        tasksCompleted++;
        Log.d(LOG_TAG, "Tasks Completed: " + tasksCompleted);
        if(tasksCompleted>=4){
            Intent intent = new Intent(this, InitialActivity.class);
            startActivity(intent);
        }
        try {
            //TODO problema con las runes
            if(dataType != null) {
                switch (dataType) {
                    case "summoner":
                        summonerSpellList = JsonUtils.getSpellListFromJSON(result);
                        StaticData.setSpellList(summonerSpellList);
                        break;
                    case "champion":
                        championList = JsonUtils.getChampionListFromJSON(result);
                        StaticData.setChampionList(championList);
                        break;
                    case "item":
                        itemList = JsonUtils.getItemListFromJSON(result);
                        StaticData.setItemList(itemList);
                        break;
                }
            }else{
                //TODO fix this
                Log.d(LOG_TAG, "dataType es null");
                runeList = JsonUtils.getRuneListFromJSON(result);
                StaticData.setRuneList(runeList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class ReadTextTask extends AsyncTask<URL, Void, String> {

        OnTaskCompleted listener;

        ReadTextTask(OnTaskCompleted listener){
            this.listener=listener;
        }

        @Override
        protected String doInBackground(URL... urls) {
            String str = null;
            try {
                // Read all the text returned by the server
                Log.d("Start","LA URL ES: " + urls[0].toString());
                BufferedReader in = new BufferedReader(new InputStreamReader(urls[0].openStream()));
                Log.d("Start", "Paso");
                str = in.readLine();
                Log.d("Start", "Paso");
                in.close();
                Log.d("Start", "Paso");
            }
            catch (IOException e) {
                Log.d("Start", "Se ha producido una excepcion " + e.getMessage());
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("ReadTextTask", "resultado " + result);

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
