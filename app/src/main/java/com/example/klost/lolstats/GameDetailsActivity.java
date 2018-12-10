package com.example.klost.lolstats;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;

public class GameDetailsActivity extends AppCompatActivity implements MatchDetailAdapter.MatchAdapterOnClickHandler {

    private RecyclerView recyclerViewBlueTeam;
    private RecyclerView recyclerViewRedTeam;
    private MatchDetailAdapter matchDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        recyclerViewBlueTeam = findViewById(R.id.listview_blue_team);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        recyclerViewBlueTeam.setLayoutManager(layoutManager);
        recyclerViewBlueTeam.setHasFixedSize(true);

        matchDetailAdapter = new MatchDetailAdapter(this);

        String[] mockData = new String[5];

        matchDetailAdapter.setData(mockData);


        recyclerViewBlueTeam.setAdapter(matchDetailAdapter);

        recyclerViewRedTeam = findViewById(R.id.listview_red_team);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        recyclerViewRedTeam.setLayoutManager(layoutManager2);
        recyclerViewRedTeam.setHasFixedSize(true);

        recyclerViewRedTeam.setAdapter(matchDetailAdapter);;
    }


    @Override
    public void onClick(String playerPosition) {
        Context context = this;
        Log.d("Prueba", "PULSADO Y LLAMADO2");
        Toast.makeText(context, "Clicked Player: " + playerPosition, Toast.LENGTH_SHORT)
                .show();
    }
}
