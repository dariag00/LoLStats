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
import com.example.klost.lolstats.utilities.ReadTaskToken;
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
        ReadTaskToken token = new ReadTaskToken(championsUrl, "champion");
        championsTextTask.execute(token);

        //Obtencion de los datos estaticos de hechizos de invocador
        URL summonerSpellsUrl = NetworkUtils.buildUrl("summoner", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + summonerSpellsUrl.toString());

        ReadTextTask spellsTextTask = new ReadTextTask(this);
        token = new ReadTaskToken(summonerSpellsUrl, "summoner");
        spellsTextTask.execute(token);

        //Obtencion de los datos estaticos de las runas
        URL runesUrl = NetworkUtils.buildUrl("runesReforged", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + runesUrl.toString());

        ReadTextTask runesTextTask = new ReadTextTask(this);
        token = new ReadTaskToken(runesUrl, "runes");
        runesTextTask.execute(token);

        //Obtencion de los datos estaticos de los items
        URL itemsUrl = NetworkUtils.buildUrl("item", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + itemsUrl.toString());

        ReadTextTask itemsTextTask = new ReadTextTask(this);
        token = new ReadTaskToken(itemsUrl, "item");
        itemsTextTask.execute(token);

        //Obtención de datos estaticos de runas menores
        URL smallRunesUrl = NetworkUtils.buildUrl("", NetworkUtils.GET_CDRAGON_PERKS);
        Log.d(LOG_TAG, "URL: " + smallRunesUrl.toString());

        ReadTextTask smallRunesTask = new ReadTextTask(this);
        token = new ReadTaskToken(smallRunesUrl, "smallRunes");
        smallRunesTask.execute(token);
    }

    @Override
    public void onTaskCompleted(ReadTaskToken token) {

        String dataToken = token.getToken();
        Log.d(LOG_TAG, "DATA TYPE: " + dataToken);

        tasksCompleted++;
        Log.d(LOG_TAG, "Tasks Completed: " + tasksCompleted);
        if(tasksCompleted>=5){
            Intent intent = new Intent(this, InitialActivity.class);
            startActivity(intent);
        }
        try {
            //TODO problema con las runes
            if(dataToken != null) {
                switch (dataToken) {
                    case "summoner":
                        summonerSpellList = JsonUtils.getSpellListFromJSON(token.getResponse());
                        StaticData.setSpellList(summonerSpellList);
                        break;
                    case "champion":
                        championList = JsonUtils.getChampionListFromJSON(token.getResponse());
                        StaticData.setChampionList(championList);
                        break;
                    case "item":
                        itemList = JsonUtils.getItemListFromJSON(token.getResponse());
                        StaticData.setItemList(itemList);
                        break;
                    case "runes":
                        runeList = JsonUtils.getRuneListFromJSON(token.getResponse());
                        StaticData.setRuneList(runeList);
                        break;
                    case "smallRunes":
                        runeList = JsonUtils.addSmallRunes(runeList, token.getResponse());
                        StaticData.setRuneList(runeList);
                        break;
                }
            }else{
                //TODO fix this
                Log.d(LOG_TAG, "dataType es null");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class ReadTextTask extends AsyncTask<ReadTaskToken, Void, ReadTaskToken> {

        OnTaskCompleted listener;

        ReadTextTask(OnTaskCompleted listener){
            this.listener=listener;
        }

        @Override
        protected ReadTaskToken doInBackground(ReadTaskToken... tokens) {
            String str = null;
            String[] array = new String[2];
            try {
                // Read all the text returned by the server
                Log.d("Start","LA URL ES: " + tokens[0].toString());
                URL url = tokens[0].getUrl();
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                Log.d("Start", "Paso");
                str = in.readLine();
                Log.d("Start", "Paso");
                in.close();
                Log.d("Start", "Paso");
            }
            catch (IOException e) {
                Log.d("Start", "Se ha producido una excepcion " + e.getMessage());
            }
            tokens[0].setResponse(str);
            return tokens[0];
        }

        @Override
        protected void onPostExecute(ReadTaskToken resultToken) {

            Log.d("ReadTextTask", "resultado " + resultToken.getResponse());
            listener.onTaskCompleted(resultToken);

        }

    }
}
