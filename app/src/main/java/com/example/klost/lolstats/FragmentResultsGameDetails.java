package com.example.klost.lolstats;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.Team;

public class FragmentResultsGameDetails extends Fragment implements MatchDetailAdapter.MatchAdapterOnClickHandler{

    private RecyclerView recyclerViewBlueTeam;
    private RecyclerView recyclerViewRedTeam;
    private MatchDetailAdapter matchDetailAdapterBlueTeam;
    private MatchDetailAdapter matchDetailAdapterRedTeam;

    private Match match;
    private Summoner summoner;

    private static final String SAVED_SUMMONER_KEY = "summoner";
    private static final String SAVED_MATCH_KEY = "match";


    public FragmentResultsGameDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results_game_details, container, false);

        match = (Match) this.getArguments().getSerializable(SAVED_MATCH_KEY);
        summoner = (Summoner) this.getArguments().getSerializable(SAVED_SUMMONER_KEY);

        recyclerViewBlueTeam = view.findViewById(R.id.listview_blue_team);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        recyclerViewBlueTeam.setLayoutManager(layoutManager);
        recyclerViewBlueTeam.setHasFixedSize(true);

        matchDetailAdapterBlueTeam = new MatchDetailAdapter(this);
        matchDetailAdapterBlueTeam.setData(match.getBlueTeam().getPlayers(), summoner, match);

        recyclerViewBlueTeam.setAdapter(matchDetailAdapterBlueTeam);

        matchDetailAdapterRedTeam = new MatchDetailAdapter(this);
        matchDetailAdapterRedTeam.setData(match.getRedTeam().getPlayers(), summoner, match);

        recyclerViewRedTeam = view.findViewById(R.id.listview_red_team);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        recyclerViewRedTeam.setLayoutManager(layoutManager2);
        recyclerViewRedTeam.setHasFixedSize(true);

        recyclerViewRedTeam.setAdapter(matchDetailAdapterRedTeam);

        setBlueTeamData(view);
        setRedTeamData(view);

        return  view;
    }

    private void setBlueTeamData(View view){

        TextView result =  view.findViewById(R.id.tv_blue_team);

        Team blueTeam = match.getBlueTeam();
        if(blueTeam.isWon()){
            result.setText("VICTORY");
        }else{
            result.setText("DEFEAT");
        }

        int dragonsKilled = blueTeam.getDragonKills();
        TextView dragonsKilledView = view.findViewById(R.id.tv_dragons_blue);
        dragonsKilledView.setText(String.valueOf(dragonsKilled));
        int baronKills = blueTeam.getBaronKills();
        TextView baronsKilledView =view.findViewById(R.id.tv_barons_blue);
        baronsKilledView.setText(String.valueOf(baronKills));
        int towersDestroyed = blueTeam.getTowerKills();
        TextView towersDestroyedView = view.findViewById(R.id.tv_towers_blue);
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

        TextView totalKillsView = view.findViewById(R.id.tv_kills_blue);
        totalKillsView.setText(String.valueOf(kills));
        TextView totalAssistsView = view.findViewById(R.id.tv_assists_blue);
        totalAssistsView.setText(String.valueOf(assists));
        TextView totalDeathsView = view.findViewById(R.id.tv_deaths_blue);
        totalDeathsView.setText(String.valueOf(deaths));

        TextView totalGoldEarned = view.findViewById(R.id.tv_gold_blue);
        totalGoldEarned.setText(String.valueOf(gold));

    }

    private void setRedTeamData(View view){
        TextView result = view.findViewById(R.id.tv_red_team);

        Team redTeam = match.getRedTeam();
        if(redTeam.isWon()){
            result.setText("VICTORY");
        }else{
            result.setText("DEFEAT");
        }

        int dragonsKilled = redTeam.getDragonKills();
        TextView dragonsKilledView = view.findViewById(R.id.tv_dragons_red);
        dragonsKilledView.setText(String.valueOf(dragonsKilled));
        int baronKills = redTeam.getBaronKills();
        TextView baronsKilledView = view.findViewById(R.id.tv_barons_red);
        baronsKilledView.setText(String.valueOf(baronKills));
        int towersDestroyed = redTeam.getTowerKills();
        TextView towersDestroyedView = view.findViewById(R.id.tv_towers_red);
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

        TextView totalKillsView = view.findViewById(R.id.tv_kills_red);
        totalKillsView.setText(String.valueOf(kills));
        TextView totalAssistsView = view.findViewById(R.id.tv_assists_red);
        totalAssistsView.setText(String.valueOf(assists));
        TextView totalDeathsView = view.findViewById(R.id.tv_deaths_red);
        totalDeathsView.setText(String.valueOf(deaths));

        TextView totalGoldEarned = view.findViewById(R.id.tv_gold_red);
        totalGoldEarned.setText(String.valueOf(gold));
    }


    @Override
    public void onClick(String playerPosition) {
        Context context = getContext();
        Log.d("Prueba", "PULSADO Y LLAMADO2");
        Toast.makeText(context, "Clicked Player: " + playerPosition, Toast.LENGTH_SHORT)
                .show();
    }

}
