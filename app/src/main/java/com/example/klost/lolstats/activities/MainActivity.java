package com.example.klost.lolstats.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klost.lolstats.GameDetailsActivity;
import com.example.klost.lolstats.MainViewModel;
import com.example.klost.lolstats.MainViewModelFactory;
import com.example.klost.lolstats.R;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.example.klost.lolstats.utilities.StaticData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class MainActivity extends AppCompatActivity implements RiotAdapter.RiotAdapterOnClickHandler {

    //TODO comprobar conexión a internet
    //TODO crear clase que ejecute todas las requests de datos estaticos

    private static final String LOG_TAG = "MainActivity";

    private String summonerName;

    private static RecyclerView recyclerView;
    private static LinearLayout mainLayout;
    private static RiotAdapter riotAdapter;
    private static PieChart chart;
    private static TextView summonerNameView;
    private ProgressBar progressBar;
    private int MATCHLIST_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview_matches);
        mainLayout = findViewById(R.id.main_layout);

        Intent previousIntent = getIntent();
        summonerName = previousIntent.getStringExtra(InitialActivity.EXTRA_SUMMONER_NAME);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        riotAdapter = new RiotAdapter(this);
        recyclerView.setAdapter(riotAdapter);

        summonerNameView = findViewById(R.id.tv_summoner_name);
        summonerNameView.setText(summonerName);

        progressBar = findViewById(R.id.pb_loading_indicator);
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.INVISIBLE);

        setupViewModel();
    }

    private void setupViewModel(){
        URL riotSearchUrl = NetworkUtils.buildUrl(summonerName, NetworkUtils.GET_SUMMONER);
        MainViewModelFactory factory = new MainViewModelFactory(riotSearchUrl);
        final MainViewModel viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        viewModel.getData().observe(this, new Observer<Summoner>() {
            @Override
            public void onChanged(Summoner summoner) {
                progressBar.setVisibility(View.INVISIBLE);
                setDataOnView(summoner);
            }
        });
    }

    private void setDataOnView(Summoner summoner){
        if(summoner != null){
            //Setteamos los datos del perfil
            setProfileData(summoner);
            showData();

            MatchList matchList = summoner.getMatchList();
            List<Match> matches = matchList.getMatches();
            int contador = 0;
            Match[] processedMatches = new Match[MATCHLIST_SIZE];

            for(Match m : matches){
                if(m.isProcessed()){
                    processedMatches[contador] = m;
                    contador++;
                }
                if(contador>=MATCHLIST_SIZE){
                    break;
                }
            }

            setChart(matches, summoner);

            Log.d(LOG_TAG, "Numero Total de Partidos Analizados: " + contador);
            //TODO fix this
            riotAdapter.setData(processedMatches, summoner, StaticData.getChampionList(), StaticData.getRuneList(), StaticData.getSpellList(), StaticData.getItemList());

            if(matches.size()>3){
                String[] roles = LoLStatsUtils.get3MostPlayedRoles(matches, summoner);
                setMostPlayedRoles(roles, matches, summoner);
            }

            //Setteamos los datos globales de resultados
            TextView killsView = findViewById(R.id.tv_global_kills);
            String kills = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageKillsOfLast20Games(matchList.getMatches(), summoner));
            killsView.setText(kills);

            TextView deathsView = findViewById(R.id.tv_global_deaths);
            String deaths = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageDeathsOfLast20Games(matchList.getMatches(), summoner));
            deathsView.setText(deaths);

            TextView assistsView = findViewById(R.id.tv_global_assists);
            String assists = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageAssistsOfLast20Games(matchList.getMatches(), summoner));
            assistsView.setText(assists);

            TextView kdaView = findViewById(R.id.tv_global_kda);
            String kda = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageKDAOfLast20Games(matchList.getMatches(), summoner));
            kdaView.setText(kda);

            //Setteo de los mejores champions
            Champion[] top3Champions = LoLStatsUtils.get3MostPlayedChampion(matches, summoner, StaticData.getChampionList());
            if(top3Champions[0] != null) {
                int[] stats1 = LoLStatsUtils.getChampionStats(matches, summoner, top3Champions[0]);
                setBestChampionData(stats1, 1, top3Champions[0]);
            }
            if(top3Champions[1] != null) {
                int[] stats2 = LoLStatsUtils.getChampionStats(matches, summoner, top3Champions[1]);
                setBestChampionData(stats2, 2, top3Champions[1]);
            }
            if(top3Champions[2] != null) {
                int[] stats3 = LoLStatsUtils.getChampionStats(matches, summoner, top3Champions[2]);
                setBestChampionData(stats3, 3, top3Champions[2]);
            }

        }else{
            showErrorMessage();
        }
    }

    private void setChart(List<Match> matches, Summoner summoner){
        chart = findViewById(R.id.pie_chart);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        chart.setDrawHoleEnabled(true);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(5f);
        chart.setTransparentCircleRadius(20f);

        chart.setDrawCenterText(false);

        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        chart.setMaxAngle(360f);
        chart.setRotationAngle(180f);

        int wins = LoLStatsUtils.getWinsOfLast20Games(matches, summoner);

        setData(wins);

        chart.animateY(1400, Easing.EaseInOutQuad);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(8f); //Tamaño del label de dentro

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
    }

    private void setProfileData(Summoner summoner){
        ImageView profileIcon = findViewById(R.id.iv_profile_icon);
        summoner.loadImageFromDDragon(profileIcon);

        TextView summonerLevelView = findViewById(R.id.tv_summoner_level);
        summonerLevelView.setText(String.valueOf(summoner.getSummonerLevel()));

        ImageView soloQIcon = findViewById(R.id.iv_rank_icon_solo);
        ImageView flexQIcon = findViewById(R.id.iv_rank_icon_flex);
        ImageView flexQTTIcon = findViewById(R.id.iv_rank_icon_3v3);

        LeaguePositionList list = summoner.getPositionList();
        LeaguePosition soloQ = list.getRankedSoloPosition();
        LeaguePosition flexQ = list.getRankedFlexPosition();
        LeaguePosition flexTTQ = list.getRankedFlexTTPosition();

        if(soloQ == null)
            soloQIcon.setImageResource(R.drawable.unranked);
        else
            soloQ.setLeagueIconOnImageView(soloQIcon);

        if(flexQ == null)
            flexQIcon.setImageResource(R.drawable.unranked);
        else
            flexQ.setLeagueIconOnImageView(flexQIcon);

        if(flexTTQ == null)
            flexQTTIcon.setImageResource(R.drawable.unranked);
        else
            flexTTQ.setLeagueIconOnImageView(flexQTTIcon);

        TextView winsSoloQView = findViewById(R.id.tv_solo_wins);
        TextView lossesSoloQView = findViewById(R.id.tv_solo_losses);
        TextView winRateSoloQView = findViewById(R.id.tv_wr_soloq);
        TextView rankSoloQView = findViewById(R.id.tv_summoner_rank_solo);
        TextView lpsSoloQView = findViewById(R.id.tv_summoner_rank_lps_solo);
        if(soloQ != null) {
            double winRateSoloQ = (double) soloQ.getWins() / ((double) soloQ.getWins() + soloQ.getLosses()) * 100;
            String wRSQ = String.format(Locale.ENGLISH, "%.1f", winRateSoloQ);
            wRSQ = wRSQ.concat("%");

            winsSoloQView.setText(String.valueOf(soloQ.getWins()));
            lossesSoloQView.setText(String.valueOf(soloQ.getLosses()));
            winRateSoloQView.setText(wRSQ);
            rankSoloQView.setText(soloQ.getTier() + " " + soloQ.getRank());
            lpsSoloQView.setText(String.valueOf(soloQ.getLeaguePoints())+ " lps");
        }else{
            winsSoloQView.setText("Unranked");
        }

        //Setteamos los valores de FlexQ
        TextView winsFlexQView = findViewById(R.id.tv_flex_wins);
        TextView lossesFlexQView = findViewById(R.id.tv_flex_losses);
        TextView winRateFlexQView = findViewById(R.id.tv_wr_flexq);
        TextView rankFlexQView = findViewById(R.id.tv_summoner_rank_flex);
        TextView lpsFlexQView = findViewById(R.id.tv_summoner_rank_lps_flex);

        if(flexQ != null){
            double winRateFlexQ = (double) flexQ.getWins() / ((double) flexQ.getWins() + flexQ.getLosses()) * 100;
            String wRFQ = String.format(Locale.ENGLISH, "%.1f", winRateFlexQ).concat("%");

            winsFlexQView.setText(String.valueOf(flexQ.getWins()));
            lossesFlexQView.setText(String.valueOf(flexQ.getLosses()));
            winRateFlexQView.setText(wRFQ);
            rankFlexQView.setText(flexQ.getTier() + " " + flexQ.getRank());
            lpsFlexQView.setText(String.valueOf(flexQ.getLeaguePoints())+ " lps");
        }else{
            winRateFlexQView.setText("Unranked");
        }


        //Setteamos los valores de 3v3
        TextView winsFlexTTQView = findViewById(R.id.tv_3v3_wins);
        TextView lossesFlexTTQView = findViewById(R.id.tv_3v3_losses);
        TextView winRate3v3View = findViewById(R.id.tv_wr_3v3);
        TextView rank3v3View = findViewById(R.id.tv_summoner_rank_3v3);
        TextView lps3v3View = findViewById(R.id.tv_summoner_rank_lps_3v3);

        if(flexTTQ != null){
            double winRate3v3 = (double) flexTTQ.getWins() / ((double) flexTTQ.getWins() + flexTTQ.getLosses()) * 100;
            String wRFTTQ = String.format(Locale.ENGLISH, "%.1f", winRate3v3).concat("%");

            winsFlexTTQView.setText(String.valueOf(flexTTQ.getWins()));
            lossesFlexTTQView.setText(String.valueOf(flexTTQ.getLosses()));
            winRate3v3View.setText(wRFTTQ);
            rank3v3View.setText(flexTTQ.getTier() + " " + flexTTQ.getRank());
            lps3v3View.setText(String.valueOf(flexTTQ.getLeaguePoints()) + " lps");
        }else{
            winsFlexTTQView.setText("Unranked");
        }
    }

    /*private void makeRiotSearchQuery(String searchQuery){
        URL riotSearchUrl = NetworkUtils.buildUrl(searchQuery, NetworkUtils.GET_SUMMONER);
        new RiotQueryTask(this).execute(riotSearchUrl);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.action_refresh){
            setupViewModel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Match clickedMatch, Summoner givenSummoner) {
        Context context = this;
        Toast.makeText(context, "Clicked " + clickedMatch.getGameId() + " summ name " + givenSummoner.getSummonerName(), Toast.LENGTH_SHORT)
                .show();

        Intent intent = new Intent(this, GameDetailsActivity.class);
        intent.putExtra("matchObject", clickedMatch);
        intent.putExtra("summonerObject", givenSummoner);
        startActivity(intent);
    }

    private void showData(){
        TextView errorMessageDisplay = findViewById(R.id.tv_error_message);
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        TextView errorMessageDisplay = findViewById(R.id.tv_error_message);

        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.INVISIBLE);
    }

    private void setMostPlayedRoles(String[] roles, List<Match> listOfGames, Summoner summoner){

        TextView winRateView1 = findViewById(R.id.tv_role_win_rate1);
        TextView playRateView1 = findViewById(R.id.tv_role_played1);
        TextView roleNameView1 = findViewById(R.id.tv_role_name_1);

        TextView winRateView2 = findViewById(R.id.tv_role_win_rate2);
        TextView playRateView2 = findViewById(R.id.tv_role_played2);
        TextView roleNameView2 = findViewById(R.id.tv_role_name_2);

        TextView winRateView3 = findViewById(R.id.tv_role_win_rate3);
        TextView playRateView3 = findViewById(R.id.tv_role_played3);
        TextView roleNameView3 = findViewById(R.id.tv_role_name_3);

        ImageView roleIcon1 = findViewById(R.id.iv_role_1);
        ImageView roleIcon2 = findViewById(R.id.iv_role_2);
        ImageView roleIcon3 = findViewById(R.id.iv_role_3);

        LoLStatsUtils.setRoleImage(roleIcon1, roles[0]);
        LoLStatsUtils.setRoleImage(roleIcon2, roles[1]);
        LoLStatsUtils.setRoleImage(roleIcon3, roles[2]);

        roleNameView1.setText(roles[0]);
        roleNameView2.setText(roles[1]);
        roleNameView3.setText(roles[2]);

        double[] stats1 = LoLStatsUtils.getRoleStats(listOfGames, summoner, roles[0]);

        Log.d(LOG_TAG, "Played: " + stats1[0] + " Won: " + stats1[1]);

        String winRate1 = String.format(Locale.ENGLISH, "%.1f", stats1[1]).concat("%");
        String playRate1 = String.valueOf((int) stats1[0]).concat("%");
        winRateView1.setText(winRate1);
        playRateView1.setText(playRate1);

        double[] stats2 = LoLStatsUtils.getRoleStats(listOfGames, summoner, roles[1]);

        String winRate2 = String.format(Locale.ENGLISH, "%.1f", stats2[1]).concat("%");
        String playRate2 = String.valueOf((int) stats2[0]).concat("%");
        winRateView2.setText(winRate2);
        playRateView2.setText(playRate2);

        double[] stats3 = LoLStatsUtils.getRoleStats(listOfGames, summoner, roles[2]);

        String winRate3 = String.format(Locale.ENGLISH, "%.1f", stats3[1]).concat("%");
        String playRate3 =String.valueOf((int) stats3[0]).concat("%");
        winRateView3.setText(winRate3);
        playRateView3.setText(playRate3);

    }

    //TODO meter en stats el championid
    private void setBestChampionData(int[] stats, int championRank, Champion champion){

        ImageView championView;
        TextView championCs;
        TextView kdaChampionView;
        TextView gamesPlayedView;
        TextView gamesWonView;

        if(championRank == 1){
            championView = findViewById(R.id.iv_champion1_image);
            championCs = findViewById(R.id.tv_champion1_cs);
            kdaChampionView = findViewById(R.id.tv_champion1_kda);
            gamesPlayedView = findViewById(R.id.tv_champion1_games_played);
            gamesWonView = findViewById(R.id.tv_champion1_win_rate);
        }else if(championRank == 2){
            championView = findViewById(R.id.iv_champion2_image);
            championCs = findViewById(R.id.tv_champion2_cs);
            kdaChampionView = findViewById(R.id.tv_champion2_kda);
            gamesPlayedView = findViewById(R.id.tv_champion2_games_played);
            gamesWonView = findViewById(R.id.tv_champion2_win_rate);
        }else{
            championView = findViewById(R.id.iv_champion3_image);
            championCs = findViewById(R.id.tv_champion3_cs);
            kdaChampionView = findViewById(R.id.tv_champion3_kda);
            gamesPlayedView = findViewById(R.id.tv_champion3_games_played);
            gamesWonView = findViewById(R.id.tv_champion3_win_rate);
        }

        champion.loadImageFromDDragon(championView);

        int numberOfKills = stats[0];
        int numberOfDeaths = stats[1];
        int numberOfAssists = stats[2];
        int totalMinionsKilled = stats[3];
        int gamesPlayed = stats[4];
        int gamesWon = stats[5];
        Log.d(LOG_TAG, "Games won: " + gamesWon);

        double averageMinionsKilled = (double) totalMinionsKilled / (double) gamesPlayed;
        double averageKills = (double) numberOfKills / (double) gamesPlayed;
        double averageDeaths = (double) numberOfDeaths / (double) gamesPlayed;
        double averageAssists = (double) numberOfAssists / (double) gamesPlayed;

        championCs.setText(String.valueOf(averageMinionsKilled));

        double kdaChampion = LoLStatsUtils.calculateKDA(averageKills, averageAssists, averageDeaths);
        LoLStatsUtils.setKdaAndTextColorInView(kdaChampionView, kdaChampion, this);

        double winRate = ((double) gamesWon / (double) gamesPlayed) *100;
        Log.d(LOG_TAG, "WR: " + winRate + " dividiendo " + gamesWon + " entre "+ gamesPlayed);

        gamesPlayedView.setText("Games: " + String.valueOf(gamesPlayed));
        gamesWonView.setText(String.valueOf((int) winRate).concat("%"));

    }

    private void setData(int wins) {

        ArrayList<PieEntry> values = new ArrayList<>();

        int losses = 20- wins;

        values.add(new PieEntry(wins, "Victories"));
        values.add(new PieEntry(losses, "Defeats"));

        PieDataSet dataSet = new PieDataSet(values, "Win/Losses of last Games");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        int[] colors = {
                rgb("#4286f4"), rgb("#f44d41")};
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(7f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.invalidate();
    }
    /*
         private SpannableString generateCenterSpannableText() {

            SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by klosote");
            s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length(), 0);
            return s;
        }
     */


}
