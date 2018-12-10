package com.example.klost.lolstats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.Team;

import org.w3c.dom.Text;

public class GameDetailsActivity extends AppCompatActivity implements MatchDetailAdapter.MatchAdapterOnClickHandler {

    private RecyclerView recyclerViewBlueTeam;
    private RecyclerView recyclerViewRedTeam;
    private MatchDetailAdapter matchDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Intent previousIntent = getIntent();
        Summoner summoner = (Summoner) previousIntent.getSerializableExtra("summonerObject");
        Match match = (Match) previousIntent.getSerializableExtra("matchObject");

        recyclerViewBlueTeam = findViewById(R.id.listview_blue_team);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        recyclerViewBlueTeam.setLayoutManager(layoutManager);
        recyclerViewBlueTeam.setHasFixedSize(true);

        matchDetailAdapter = new MatchDetailAdapter(this);

        String[] mockData = new String[5];

        //TODO crear 2 adapters
        matchDetailAdapter.setData(match.getBlueTeam().getPlayers(), summoner);


        recyclerViewBlueTeam.setAdapter(matchDetailAdapter);

        recyclerViewRedTeam = findViewById(R.id.listview_red_team);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        recyclerViewRedTeam.setLayoutManager(layoutManager2);
        recyclerViewRedTeam.setHasFixedSize(true);

        recyclerViewRedTeam.setAdapter(matchDetailAdapter);

        setBlueTeamData(match);
        setRedTeamData(match);

    }

    public void setBlueTeamData(Match match){

        TextView result = findViewById(R.id.tv_blue_team);

        Team blueTeam = match.getBlueTeam();
        if(blueTeam.isWon()){
            result.setText("VICTORY");
        }else{
            result.setText("DEFEAT");
        }

        int dragonsKilled = blueTeam.getDragonKills();
        TextView dragonsKilledView = findViewById(R.id.tv_dragons_blue);
        dragonsKilledView.setText(String.valueOf(dragonsKilled));
        int baronKills = blueTeam.getBaronKills();
        TextView baronsKilledView = findViewById(R.id.tv_barons_blue);
        baronsKilledView.setText(String.valueOf(baronKills));
        int towersDestroyed = blueTeam.getTowerKills();
        TextView towersDestroyedView = findViewById(R.id.tv_towers_blue);
        towersDestroyedView.setText(String.valueOf(towersDestroyed));

        //Calculate total kills assists and deaths
        int kills = 0;
        int assists = 0;
        int deaths = 0;
        int gold = 0;

        for(Player player : blueTeam.getPlayers()){
            kills = kills + player.getKills();
            assists = assists + player.getAssists();
            deaths = deaths + player.getDeaths();
            gold = gold + player.getGoldEarned();
        }

        TextView totalKillsView = findViewById(R.id.tv_kills_blue);
        totalKillsView.setText(String.valueOf(kills));
        TextView totalAssistsView = findViewById(R.id.tv_assists_blue);
        totalAssistsView.setText(String.valueOf(assists));
        TextView totalDeathsView = findViewById(R.id.tv_deaths_blue);
        totalDeathsView.setText(String.valueOf(deaths));

        TextView totalGoldEarned = findViewById(R.id.tv_gold_blue);
        totalGoldEarned.setText(String.valueOf(gold));

    }

    public void setRedTeamData(Match match){
        TextView result = findViewById(R.id.tv_red_team);

        Team redTeam = match.getRedTeam();
        if(redTeam.isWon()){
            result.setText("VICTORY");
        }else{
            result.setText("DEFEAT");
        }

        int dragonsKilled = redTeam.getDragonKills();
        TextView dragonsKilledView = findViewById(R.id.tv_dragons_red);
        dragonsKilledView.setText(String.valueOf(dragonsKilled));
        int baronKills = redTeam.getBaronKills();
        TextView baronsKilledView = findViewById(R.id.tv_barons_red);
        baronsKilledView.setText(String.valueOf(baronKills));
        int towersDestroyed = redTeam.getTowerKills();
        TextView towersDestroyedView = findViewById(R.id.tv_towers_red);
        towersDestroyedView.setText(String.valueOf(towersDestroyed));

        //Calculate total kills assists and deaths
        int kills = 0;
        int assists = 0;
        int deaths = 0;
        int gold = 0;

        for(Player player : redTeam.getPlayers()){
            kills = kills + player.getKills();
            assists = assists + player.getAssists();
            deaths = deaths + player.getDeaths();
            gold = gold + player.getGoldEarned();
        }

        TextView totalKillsView = findViewById(R.id.tv_kills_red);
        totalKillsView.setText(String.valueOf(kills));
        TextView totalAssistsView = findViewById(R.id.tv_assists_red);
        totalAssistsView.setText(String.valueOf(assists));
        TextView totalDeathsView = findViewById(R.id.tv_deaths_red);
        totalDeathsView.setText(String.valueOf(deaths));

        TextView totalGoldEarned = findViewById(R.id.tv_gold_red);
        totalGoldEarned.setText(String.valueOf(gold));
    }


    @Override
    public void onClick(String playerPosition) {
        Context context = this;
        Log.d("Prueba", "PULSADO Y LLAMADO2");
        Toast.makeText(context, "Clicked Player: " + playerPosition, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
