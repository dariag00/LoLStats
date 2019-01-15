package com.example.klost.lolstats;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;

public class GameDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Intent previousIntent = getIntent();
        Summoner summoner = (Summoner) previousIntent.getSerializableExtra("summonerObject");
        Match match = (Match) previousIntent.getSerializableExtra("matchObject");

        viewPager =  findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), match, summoner);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
