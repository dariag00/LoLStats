package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class LiveGameActivity extends AppCompatActivity {

    ImageView blueBan1, blueBan2, blueBan3, redBan1, redBan2, redBan3;
    TextView meanRankBlue, meanRankRed, meanWinRateBlue, meanWinRateRed;
    RecyclerView bluePlayersRecyclerView, redPlayersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_game);
        populateViews();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        bluePlayersRecyclerView.setLayoutManager(layoutManager);
        bluePlayersRecyclerView.setHasFixedSize(true);

        redPlayersRecyclerView.setLayoutManager(layoutManager);
        redPlayersRecyclerView.setHasFixedSize(true);
    }

    private void populateViews(){
        blueBan1 = findViewById(R.id.iv_ban1_blue);
        blueBan2 = findViewById(R.id.iv_ban2_blue);
        blueBan3 = findViewById(R.id.iv_ban3_blue);
        redBan1 = findViewById(R.id.iv_ban1_red);
        redBan2 = findViewById(R.id.iv_ban2_red);
        redBan3 = findViewById(R.id.iv_ban3_red);
        meanRankBlue = findViewById(R.id.tv_mean_division_blue);
        meanRankRed = findViewById(R.id.tv_mean_division_red);
        meanWinRateBlue = findViewById(R.id.tv_mean_win_rate_blue);
        meanWinRateRed = findViewById(R.id.tv_mean_win_rate_red);
        bluePlayersRecyclerView = findViewById(R.id.listview_blue_team);
        redPlayersRecyclerView = findViewById(R.id.listview_red_team);
    }
}
