package com.example.klost.lolstats.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.klost.lolstats.AppExecutors;
import com.example.klost.lolstats.BuildConfig;
import com.example.klost.lolstats.CustomSpinnerAdapter;
import com.example.klost.lolstats.InitialViewModel;
import com.example.klost.lolstats.MainActivity.MainActivity;
import com.example.klost.lolstats.OnLiveGameTaskCompleted;
import com.example.klost.lolstats.R;
import com.example.klost.lolstats.SummonerAdapter;
import com.example.klost.lolstats.TestActivity;
import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.AppDatabase;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialActivity extends AppCompatActivity implements SummonerAdapter.ItemClickListener, OnLiveGameTaskCompleted{


    public static final String EXTRA_SUMMONER_NAME = "com.example.klost.lolstats.SUMMONER_NAME";
    public static final String EXTRA_LIVE_MATCH = "com.example.klost.lolstats.LIVE_MATCH";

    private EditText summonerNameView;
    private RecyclerView recyclerView;
    private CardView noDataView;
    private SummonerAdapter adapter;
    public static final String EXTRA_ENTRY_ID = "com.example.klost.lolstats.ENTRY_ID";

    private static final String LOG_TAG = InitialActivity.class.getSimpleName();
    private String[] spinnerTitles;
    private static LoLStatsRepository repository;
    private boolean updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        summonerNameView = findViewById(R.id.et_summoner_name);
        Button searchButton = findViewById(R.id.bt_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSearch();
            }
        });

        Button liveButton = findViewById(R.id.bt_live_game);
        liveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLiveGameSearch();
            }
        });

        /*Button testButton = findViewById(R.id.bt_test);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest();
            }
        });*/

        repository = LoLStatsRepository.getInstance(this.getApplication(), AppExecutors.getInstance());

        noDataView = findViewById(R.id.no_data_card_view);

        spinnerTitles = new String[]{"EUW", "EUNE", "NA", "KR", "OCE", "BR", "RU", "JP"};
        int flags[] = {R.drawable.eu_flag, R.drawable.eune_flag, R.drawable.na_flag, R.drawable.korea_flag, R.drawable.oceania_flag, R.drawable.brazil_flag, R.drawable.russia_flag, R.drawable.japan_flag};
        //int flags[] = {R.drawable.bronze_mini, R.drawable.bronze_mini, R.drawable.bronze_mini, R.drawable.bronze_mini, R.drawable.bronze_mini, R.drawable.bronze_mini, R.drawable.bronze_mini};
        Spinner regionSpinner = findViewById(R.id.region_spinner);
        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(this, spinnerTitles, flags);
        regionSpinner.setAdapter(customAdapter);


        recyclerView = findViewById(R.id.recyclerViewSummoners);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SummonerAdapter(this, this);
        recyclerView.setAdapter(adapter);

        updated = false;
        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(InitialActivity.this, SaveSummonerActivity.class);
                startActivity(addTaskIntent);
            }
        });

        final InitialActivity context = this;

        InitialViewModel viewModel = ViewModelProviders.of(this).get(InitialViewModel.class);
        viewModel.getSummoners().observe(this, new Observer<List<SummonerEntry>>() {
            @Override
            public void onChanged(List<SummonerEntry> summonerEntries) {
                Log.d(LOG_TAG, "Updating list of tasks from LiveData in ViewModel");

                if (!summonerEntries.isEmpty()){

                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.GONE);

                    if (!updated) {
                        updated = true;
                        for (SummonerEntry entry : summonerEntries) {
                            new SummonerQueryTask(context).execute(entry.getSummonerName());
                        }
                    }
                    adapter.setSummonerEntries(summonerEntries);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    noDataView.setVisibility(View.VISIBLE);
                }
            }
        });

        final AppDatabase database = AppDatabase.getInstance(getApplicationContext());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<SummonerEntry> tasks = adapter.getSummonerEntries();
                        database.summonerDao().deleteSummoner(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        noDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(InitialActivity.this, SaveSummonerActivity.class);
                startActivity(addTaskIntent);
            }
        });

    }

    @Override
    public void onTaskCompleted(Match result) {
        if(result == null){
            summonerNameView.setError("El usuario no está jugando ahorita mismo");
        }else{
            Intent intent = new Intent(this, LiveGameActivity.class);
            intent.putExtra(EXTRA_SUMMONER_NAME, result);
            startActivity(intent);
        }
    }

    private void attemptLiveGameSearch(){
        String summonerName = summonerNameView.getText().toString();
        if(isValid(summonerName)) {
            LiveGameTaks task = new LiveGameTaks(this);
            URL liveGameUrl = NetworkUtils.buildUrl(summonerName, NetworkUtils.GET_LIVE_GAME);
            task.execute(liveGameUrl);
        }
    }

    private void toTest(){
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }


    private void toMatchDetail(){
        Intent intent = new Intent(this, GameDetailsActivity.class);
        startActivity(intent);
    }

    private boolean isValid(String summonerName){
        summonerNameView.setError(null);

        if (BuildConfig.DEBUG) {
            Log.d("IA.summonerName", summonerName);
        }

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(summonerName)){
            if (BuildConfig.DEBUG) {
                Log.d("IA.summonerName", "Se ha introducido un summoner Name vacio");
            }
            summonerNameView.setError(getString(R.string.error_empty_summoner_name));
            focusView=  summonerNameView;
            cancel = true;
        } else if(!isSummonerNameValid(summonerName)){
            if (BuildConfig.DEBUG) {
                Log.d("IA.summonerName", "Se ha introducido un summoner Name incorrecto " + summonerName);
            }
            summonerNameView.setError(getString(R.string.error_invalid_summoner_name));
            focusView=  summonerNameView;
            cancel = true;
        }

        if(cancel){
            //Se ha producido un error y se ha de cancelar el proceso
            focusView.requestFocus();
            return false;
        }

        return true;

    }

    private void attemptSearch(){
        String summonerName = summonerNameView.getText().toString();
        if(isValid(summonerName)) {
            //Creamos un intent que iniciará la activity MainActivity y le pasamos el Summoner Name que hemos procesado
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_SUMMONER_NAME, summonerName);
            startActivity(intent);
        }
    }

    private boolean isSummonerNameValid(String summonerName){
        Pattern pattern = Pattern.compile("^[\\p{L} 0-9_.]+$");
        Matcher matcher = pattern.matcher(summonerName);
        return matcher.find();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }


    @Override
    public void onItemClickListener(int id, String accountId) {
        Context context = this;
        Toast.makeText(context, "Clicked " + accountId + " " + accountId, Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, SavedProfileActivity.class);
        intent.putExtra(EXTRA_ENTRY_ID, id);
        startActivity(intent);
    }

    public static class SummonerQueryTask extends AsyncTask<String, Void, Summoner> {

        WeakReference<InitialActivity> weakActivity;

        public SummonerQueryTask(InitialActivity context){
            this.weakActivity = new WeakReference<>(context);
        }


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
            Log.d(LOG_TAG, "EMTRO");
            if(summoner != null) {
                Log.d(LOG_TAG, "Entro 2");
                final SummonerEntry entry = new SummonerEntry(summoner.getPuuid(), summoner.getEncryptedAccountId(), summoner.getEncryptedSummonerId());
                entry.setProfileIconId(summoner.getProfileIconId());
                Log.d(LOG_TAG, "ID: " + summoner.getProfileIconId());
                entry.setSummonerLevel(summoner.getSummonerLevel());
                entry.setSummonerName(summoner.getSummonerName());

                LeaguePositionList list = summoner.getPositionList();
                LeaguePosition soloQ = list.getRankedSoloPosition();
                LeaguePosition flexQ = list.getRankedFlexPosition();
                LeaguePosition flexQTT = list.getRankedFlexTTPosition();

                if (soloQ != null) {
                    entry.setSoloQ(soloQ);
                }
                if (flexQ != null) {
                    entry.setFlexQ(flexQ);
                }
                if (flexQTT != null) {
                    entry.setFlexQTT(flexQTT);
                }
                entry.setId(23);
                repository.updateSummonerEntry(entry);
            }else{
                InitialActivity activity = weakActivity.get();
                Toast.makeText(activity, "A problem ocurred", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private static class LiveGameTaks extends AsyncTask<URL, Void, Match> {

        OnLiveGameTaskCompleted listener;

        LiveGameTaks(OnLiveGameTaskCompleted listener){
            this.listener=listener;
        }

        @Override
        protected Match doInBackground(URL... urls) {
            String liveGameResults;
            RateLimiter throttler = RateLimiter.create(0.7);
            Match match = null;

            try {
                liveGameResults = NetworkUtils.getResponseFromHttpUrl(urls[0] , throttler);
                match = JsonUtils.getLiveGameFromJson(liveGameResults);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return match;
        }

        @Override
        protected void onPostExecute(Match result) {
            listener.onTaskCompleted(result);
        }

    }
}
