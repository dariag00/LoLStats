package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChampionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
