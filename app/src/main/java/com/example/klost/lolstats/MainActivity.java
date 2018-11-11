package com.example.klost.lolstats;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText searchBoxEditText;

    private TextView searchResultsTextView;

    private TextView errorMessageDisplay;

    private TextView urlDisplayTextView;

    private ProgressBar loadingIndicator;

    private String summonerName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxEditText =  findViewById(R.id.et_search_box);
        searchResultsTextView =  findViewById(R.id.tv_riot_search_resutls_json);
        errorMessageDisplay =  findViewById(R.id.tv_error_message);
        urlDisplayTextView =  findViewById(R.id.tv_url_display);
        loadingIndicator =  findViewById(R.id.pb_loading_indicator);

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
        URL riotSearchUrl = NetworkUtils.buildUrl(searchQuery);
        urlDisplayTextView.setText(riotSearchUrl.toString());
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

    public class RiotQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String riotSearchResults = null;
            Summoner summoner = null;
            try {
                riotSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);

                summoner = JsonUtils.getSimpleRiotAPIStringsFromJson(MainActivity.this, riotSearchResults);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return summoner.toString();
        }

        @Override
        protected void onPostExecute(String riotSearchResults) {

            loadingIndicator.setVisibility(View.INVISIBLE);
            if(riotSearchResults != null && !riotSearchResults.equals("")){
                showData();
                searchResultsTextView.setText(riotSearchResults);
            }else{
                showErrorMessage();
            }

        }
    }

}
