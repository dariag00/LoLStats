package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.utilities.StaticData;

import java.util.List;

public class ChampionDetailsActivity extends AppCompatActivity {

    private final String SAVED_ENTRY = "SAVED_ENTRY_ID";
    private final String SAVED_CHAMP = "CHAMPION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        Intent previousIntent = getIntent();
        int entryId = previousIntent.getIntExtra(SAVED_ENTRY, -1);
        int championId = previousIntent.getIntExtra(SAVED_CHAMP, -1);

        final Champion champion = StaticData.getChampionList().getChampionById(championId);

        LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId, championId);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        viewModel.getChampionEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> entries) {
                ImageView splashArt = findViewById(R.id.iv_splash_art);
                champion.loadSplashArtFromDDragon(splashArt);

                ImageView profileIcon = findViewById(R.id.iv_profile_icon);
                TextView summonerName = findViewById(R.id.tv_summoner_name);

                champion.loadImageFromDDragon(profileIcon);
                summonerName.setText(champion.getName());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
