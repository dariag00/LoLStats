package com.example.klost.lolstats;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;

public class GameDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private static final String LOG_TAG = "GameDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Intent previousIntent = getIntent();
        Summoner summoner = (Summoner) previousIntent.getSerializableExtra("summonerObject");
        Match match = (Match) previousIntent.getSerializableExtra("matchObject");

        viewPager =  findViewById(R.id.viewPager);
        Player player = match.getPlayer(summoner);
        Log.d(LOG_TAG, "Player: " + player.toString());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), match, summoner, player);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
