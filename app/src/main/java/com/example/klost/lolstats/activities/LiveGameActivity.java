package com.example.klost.lolstats.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.LiveGameAdapter;
import com.example.klost.lolstats.MatchDetailAdapter;
import com.example.klost.lolstats.R;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.Team;
import com.example.klost.lolstats.models.matches.livegame.Ban;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class LiveGameActivity extends AppCompatActivity implements LiveGameAdapter.ItemClickListener {

    ImageView blueBan1, blueBan2, blueBan3, blueBan4, blueBan5, redBan1, redBan2, redBan3, redBan4, redBan5;
    TextView meanRankBlue, meanRankRed, meanWinRateBlue, meanWinRateRed;
    RecyclerView bluePlayersRecyclerView, redPlayersRecyclerView;

    public static final String EXTRA_LIVE_MATCH = "com.example.klost.lolstats.LIVE_MATCH";
    public static final String EXTRA_SUMMONER_NAME = "com.example.klost.lolstats.SUMMONER_NAME";

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

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        layoutManager2.setReverseLayout(false);

        redPlayersRecyclerView.setLayoutManager(layoutManager2);
        redPlayersRecyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        Match match = (Match) intent.getSerializableExtra(EXTRA_LIVE_MATCH);
        Summoner summoner = (Summoner) intent.getSerializableExtra(EXTRA_SUMMONER_NAME);
        Log.d("LOG", match.getBlueTeam().toString());
        Log.d("LOG", match.getRedTeam().toString());
        double blueMeanWinrate = calculateMeanWinrate(match.getBlueTeam().getPlayers());
        double redMeanWinrate = calculateMeanWinrate(match.getRedTeam().getPlayers());
        meanWinRateBlue.setText(String.format(Locale.ENGLISH, "%.1f", blueMeanWinrate).concat("%"));
        meanWinRateRed.setText(String.format(Locale.ENGLISH, "%.1f", redMeanWinrate).concat("%"));

        LiveGameAdapter blueAdapter = new LiveGameAdapter(this, this);
        blueAdapter.setData(summoner, match, match.getBlueTeam().getPlayers());
        bluePlayersRecyclerView.setAdapter(blueAdapter);

        LiveGameAdapter redAdapter = new LiveGameAdapter(this, this);
        redAdapter.setData(summoner, match, match.getRedTeam().getPlayers());
        redPlayersRecyclerView.setAdapter(redAdapter);

        double meanRankBlueValue = LoLStatsUtils.getAverageDivisionOfTeam(match.getBlueTeam().getPlayers());
        meanRankBlue.setText(LoLStatsUtils.getStringValueOfDivision((int) meanRankBlueValue));

        double meanRankRedValue= LoLStatsUtils.getAverageDivisionOfTeam(match.getRedTeam().getPlayers());
        meanRankRed.setText(LoLStatsUtils.getStringValueOfDivision((int) meanRankRedValue));

        setBans(match);
    }

    private void setBans(Match match){
        List<Ban> blueTeamBans = match.getBlueTeam().getBannedChampions();
        List<Ban> redTeamBans = match.getRedTeam().getBannedChampions();

        Ban bBan1 = blueTeamBans.get(0);
        bBan1.getChampion().loadImageFromDDragon(blueBan1);
        Ban bBan2 = blueTeamBans.get(1);
        bBan2.getChampion().loadImageFromDDragon(blueBan2);
        Ban bBan3 = blueTeamBans.get(2);
        bBan3.getChampion().loadImageFromDDragon(blueBan3);
        Ban bBan4 = blueTeamBans.get(3);
        bBan4.getChampion().loadImageFromDDragon(blueBan4);
        Ban bBan5 = blueTeamBans.get(4);
        bBan5.getChampion().loadImageFromDDragon(blueBan5);

        Ban rBan1 = redTeamBans.get(0);
        rBan1.getChampion().loadImageFromDDragon(redBan1);
        Ban rBan2 = redTeamBans.get(1);
        rBan2.getChampion().loadImageFromDDragon(redBan2);
        Ban rBan3 = redTeamBans.get(2);
        rBan3.getChampion().loadImageFromDDragon(redBan3);
        Ban rBan4 = redTeamBans.get(3);
        rBan4.getChampion().loadImageFromDDragon(redBan4);
        Ban rBan5 = redTeamBans.get(4);
        rBan5.getChampion().loadImageFromDDragon(redBan5);
    }

    private double calculateMeanWinrate(List<Player> playerList){
        double totalWinrate = 0;
        double winrate;
        int nPlayers = playerList.size();
        for(Player player: playerList){
            LeaguePositionList list = player.getSummoner().getPositionList();
            if(list != null && list.getHighestRankingPosition() != null)
                winrate = list.getHighestRankingPosition().getWins() / (double) (list.getHighestRankingPosition().getWins() + list.getHighestRankingPosition().getLosses());
            else {
                nPlayers--;
                winrate = 0;
            }
            totalWinrate += winrate;
        }
        return (totalWinrate / nPlayers ) * 100;
    }

    private void populateViews(){
        blueBan1 = findViewById(R.id.iv_ban1_blue);
        blueBan2 = findViewById(R.id.iv_ban2_blue);
        blueBan3 = findViewById(R.id.iv_ban3_blue);
        blueBan4 = findViewById(R.id.iv_ban4_blue);
        blueBan5 = findViewById(R.id.iv_ban5_blue);
        redBan1 = findViewById(R.id.iv_ban1_red);
        redBan2 = findViewById(R.id.iv_ban2_red);
        redBan3 = findViewById(R.id.iv_ban3_red);
        redBan4 = findViewById(R.id.iv_ban4_red);
        redBan5 = findViewById(R.id.iv_ban5_red);
        meanRankBlue = findViewById(R.id.tv_mean_division_blue);
        meanRankRed = findViewById(R.id.tv_mean_division_red);
        meanWinRateBlue = findViewById(R.id.tv_mean_win_rate_blue);
        meanWinRateRed = findViewById(R.id.tv_mean_win_rate_red);
        bluePlayersRecyclerView = findViewById(R.id.listview_blue_team);
        redPlayersRecyclerView = findViewById(R.id.listview_red_team);
    }

    @Override
    public void onItemClickListener() {

    }
}
