package com.example.klost.lolstats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.klost.lolstats.data.database.AppDatabase;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialActivity extends AppCompatActivity implements SummonerAdapter.ItemClickListener {


    public static final String EXTRA_SUMMONER_NAME = "com.example.klost.lolstats.SUMMONER_NAME";

    private EditText summonerNameView;
    private RecyclerView recyclerView;
    private SummonerAdapter adapter;
    public static final String EXTRA_ENTRY_ID = "com.example.klost.lolstats.ENTRY_ID";

    private static final String LOG_TAG = InitialActivity.class.getSimpleName();
    private String[] spinnerTitles;

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

        Button testButton = findViewById(R.id.bt_test);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest();
            }
        });

        /*Button testButton2 = findViewById(R.id.bt_test2);

        testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest2();
            }
        });*/

        Button testButton3 = findViewById(R.id.bt_test3);

        testButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMatchDetail();
            }
        });

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


        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(InitialActivity.this, SaveSummonerActivity.class);
                startActivity(addTaskIntent);
            }
        });

        InitialViewModel viewModel = ViewModelProviders.of(this).get(InitialViewModel.class);
        viewModel.getSummoners().observe(this, new Observer<List<SummonerEntry>>() {
            @Override
            public void onChanged(List<SummonerEntry> summonerEntries) {
                Log.d(LOG_TAG, "Updating list of tasks from LiveData in ViewModel");
                adapter.setSummonerEntries(summonerEntries);
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

    }

    private void toTest(){
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    private void toTest2(){
        Intent intent = new Intent(this, TestMainLayoutActivity.class);
        startActivity(intent);
    }

    private void toMatchDetail(){
        Intent intent = new Intent(this, GameDetailsActivity.class);
        startActivity(intent);
    }

    private void attemptSearch(){

        summonerNameView.setError(null);

        String summonerName = summonerNameView.getText().toString();
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
        }else{
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
        URL riotSearchUrl = NetworkUtils.buildUrl("ScJfpvZkwqgnXiyoQ52hFS5F0iLpaMuHvus_vJf79mDObQ", NetworkUtils.GET_RANKED_MATCHLIST);
        Log.d(LOG_TAG, "URL: " + riotSearchUrl.toString());
        Toast.makeText(context, "Clicked " + accountId + " " + accountId, Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, SavedProfileActivity.class);
        intent.putExtra(EXTRA_ENTRY_ID, id);
        startActivity(intent);
    }
}
