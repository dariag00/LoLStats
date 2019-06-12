package com.example.klost.lolstats;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.models.champions.ChampionStatsList;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.StaticData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class FragmentFavProfileSummary extends Fragment implements LastGamesAdapter.ItemClickListener{

    private static final String LOG_TAG = FragmentFavProfileSummary.class.getSimpleName();
    private final String SAVED_ENTRY = "SAVED_ENTRY_ID";
    ChampionStatsList list;
    private static PieChart pieChart;
    private RadarChart radarChart;
    private LineChart csChart;
    private RecyclerView lastGamesRecyclerView;
    private LastGamesAdapter adapter;
    private LinearLayout alertLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fav_profile_summary, container, false);

        final int entryId = this.getArguments().getInt(SAVED_ENTRY);
        LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getActivity().getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId, 0);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        pieChart = view.findViewById(R.id.pie_chart);
        radarChart = view.findViewById(R.id.radar_chart);
        csChart = view.findViewById(R.id.cs_chart);


        lastGamesRecyclerView = view.findViewById(R.id.last_games_recyclerView);

        lastGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new LastGamesAdapter(this.getContext(), this);
        lastGamesRecyclerView.setAdapter(adapter);

        alertLayout = view.findViewById(R.id.ly_no_data);

        viewModel.getEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> matchStatsEntries) {
                if (matchStatsEntries.size() > 0) {
                    list = LoLStatsUtils.generateChampionStats(matchStatsEntries);
                    List<ChampionStats> championStatsList = list.getChampionStatsList();
                    //Ordenamos la lista por numero de partidos jugados
                    Collections.sort(championStatsList, new Comparator<ChampionStats>() {
                        @Override
                        public int compare(ChampionStats o1, ChampionStats o2) {
                            if (o1.getNumberOfGamesPlayed() < o2.getNumberOfGamesPlayed()) {
                                return 1;
                            } else if (o1.getNumberOfGamesPlayed() > o2.getNumberOfGamesPlayed()) {
                                return -1;
                            } else {
                                if (o1.getNumberOfGamesWon() < o2.getNumberOfGamesWon())
                                    return 1;
                                if (o1.getNumberOfGamesWon() > o2.getNumberOfGamesWon())
                                    return -1;
                                return 0;
                            }
                        }
                    });

                    List<MatchStatsEntry> lastEntries = LoLStatsUtils.getLast7DaysMatches(matchStatsEntries);
                    ChampionStatsList lastGamesPlayedList = LoLStatsUtils.generateChampionStats(lastEntries);
                    Log.d(LOG_TAG, "Sizee: " + lastGamesPlayedList.getChampionStatsList().size());
                    Log.d(LOG_TAG, "Sizee: 2" + adapter.getItemCount());
                    if(lastGamesPlayedList.getChampionStatsList().size()>0){
                        alertLayout.setVisibility(View.GONE);
                        adapter.setData(lastGamesPlayedList);
                        Log.d(LOG_TAG, "Sizee: " + adapter.getItemCount());
                    }else{
                        alertLayout.setVisibility(View.VISIBLE);
                    }


                    ImageView splashArt = view.findViewById(R.id.iv_splash_art);
                    Champion champion = list.getChampionStatsList().get(0).getChampion();
                    champion.loadSplashArtFromDDragon(splashArt);
                    //TODO funcion
                    TextView totalCs = view.findViewById(R.id.tv_total_cs);
                    TextView totalDamage = view.findViewById(R.id.tv_total_damage);
                    TextView totalGold = view.findViewById(R.id.tv_total_gold);
                    TextView csPerMin = view.findViewById(R.id.tv_cs_min);
                    TextView goldPerMin = view.findViewById(R.id.tv_gold_min);
                    TextView damagePercent = view.findViewById(R.id.tv_dmg_percent);
                    TextView goldPercent = view.findViewById(R.id.tv_gold_percent);
                    TextView killPercent = view.findViewById(R.id.tv_kill_percent);

                    String totalCsString = "Cs: " + String.format(Locale.ENGLISH, "%.1f", list.getMeanCsData());
                    String totalGoldString = "Gold: " + LoLStatsUtils.formatDoubleValue(list.getMeanGoldData());
                    String totalDamageString = "Damage: " + LoLStatsUtils.formatDoubleValue(list.getMeanDamageData());
                    //String totalDamageString = "Damage: " + LString.format(Locale.ENGLISH, "%.1f", list.getMeanDamageData());
                    String csMinString = "Cs/min: " + String.format(Locale.ENGLISH, "%.1f", list.getCsPerMin());
                    String goldMinString = "G/min: " + String.format(Locale.ENGLISH, "%.1f", list.getGoldPerMin());
                    String dmgPercentString = "DMG%: " + String.format(Locale.ENGLISH, "%.1f", list.getMeanDamagePercentData());
                    String goldPercentString = "G%: " + String.format(Locale.ENGLISH, "%.1f", list.getMeanGoldPercentData());
                    String killPercentString = "K%: " + String.format(Locale.ENGLISH, "%.1f", list.getMeanGoldPercentData() + 4.3);

                    totalCs.setText(totalCsString);
                    totalDamage.setText(totalDamageString);
                    totalGold.setText(totalGoldString);
                    csPerMin.setText(csMinString);
                    goldPerMin.setText(goldMinString);
                    damagePercent.setText(dmgPercentString);
                    goldPercent.setText(goldPercentString);
                    killPercent.setText(killPercentString);

                    TextView topPlayRate = view.findViewById(R.id.tv_role_played1);
                    TextView topWinRate = view.findViewById(R.id.tv_role_win_rate1);
                    TextView jglePlayRate = view.findViewById(R.id.tv_role_played2);
                    TextView jgleWinRate = view.findViewById(R.id.tv_role_win_rate2);
                    TextView midPlayRate = view.findViewById(R.id.tv_role_played3);
                    TextView midWinRate = view.findViewById(R.id.tv_role_win_rate3);
                    TextView adcPlayRate = view.findViewById(R.id.tv_role_played4);
                    TextView adcWinRate = view.findViewById(R.id.tv_role_win_rate4);
                    TextView suppPlayRate = view.findViewById(R.id.tv_role_played5);
                    TextView suppWinRate = view.findViewById(R.id.tv_role_win_rate5);

                    double[][] roleData = LoLStatsUtils.getWinRateAndPlayRateOfRoles(matchStatsEntries);
                    topPlayRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[0][0]).concat("%"));
                    topWinRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[0][1]).concat("%"));
                    jglePlayRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[1][0]).concat("%"));
                    jgleWinRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[1][1]).concat("%"));
                    midPlayRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[2][0]).concat("%"));
                    midWinRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[2][1]).concat("%"));
                    adcPlayRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[3][0]).concat("%"));
                    adcWinRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[3][1]).concat("%"));
                    suppPlayRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[4][0]).concat("%"));
                    suppWinRate.setText(String.format(Locale.ENGLISH, "%.1f",  roleData[4][1]).concat("%"));

                    TextView killsView = view.findViewById(R.id.tv_global_kills);
                    TextView deathsView = view.findViewById(R.id.tv_global_deaths);
                    TextView assistsView = view.findViewById(R.id.tv_global_assists);
                    TextView kdaView = view.findViewById(R.id.tv_global_kda);

                    killsView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanKills()));
                    deathsView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanDeaths()));
                    assistsView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanAssists()));
                    double kda = LoLStatsUtils.calculateKDA(list.getMeanKills(), list.getMeanAssists(), list.getMeanDeaths());
                    LoLStatsUtils.setKdaAndTextColorInView(kdaView, kda, view.getContext());

                    setChart(list.getNumberOfGamesWon(), list.getNumberOfGamesPlayed()-list.getNumberOfGamesWon());
                    setRadarChart();

                    TextView statView1 = view.findViewById(R.id.tv_best_stat1);
                    TextView dateView1 = view.findViewById(R.id.tv_best_date1);
                    ImageView championView1 = view.findViewById(R.id.iv_best_champ1);

                    MatchStatsEntry csEntry = LoLStatsUtils.getBestCsMinMatch(matchStatsEntries);
                    double csMin = csEntry.getTotalCs()/(csEntry.getDuration()/60.0);
                    statView1.setText(String.format(Locale.ENGLISH, "%.1f", csMin));
                    dateView1.setText(LoLStatsUtils.formatDate(csEntry.getGameDate()));
                    csEntry.getPlayedChampion().loadImageFromDDragon(championView1);

                    TextView statView2 = view.findViewById(R.id.tv_best_stat2);
                    TextView dateView2 = view.findViewById(R.id.tv_best_date2);
                    ImageView championView2 = view.findViewById(R.id.iv_best_champ2);

                    MatchStatsEntry kdaEntry = LoLStatsUtils.getBestKdaMatch(matchStatsEntries);
                    LoLStatsUtils.setKdaAndTextColorInView(statView2, LoLStatsUtils.calculateKDA(kdaEntry.getKills(), kdaEntry.getAssists(), kdaEntry.getDeaths()), view.getContext());
                    dateView2.setText(LoLStatsUtils.formatDate(kdaEntry.getGameDate()));
                    kdaEntry.getPlayedChampion().loadImageFromDDragon(championView2);

                    TextView statView3 = view.findViewById(R.id.tv_best_stat3);
                    TextView dateView3 = view.findViewById(R.id.tv_best_date3);
                    ImageView championView3 = view.findViewById(R.id.iv_best_champ3);

                    MatchStatsEntry dmgEntry = LoLStatsUtils.getBestDmgPercentMatch(matchStatsEntries);
                    statView3.setText(String.format(Locale.ENGLISH, "%.1f", dmgEntry.getDamagePercent()));
                    dateView3.setText(LoLStatsUtils.formatDate(dmgEntry.getGameDate()));
                    dmgEntry.getPlayedChampion().loadImageFromDDragon(championView3);

                    TextView statView4 = view.findViewById(R.id.tv_best_stat4);
                    TextView dateView4 = view.findViewById(R.id.tv_best_date4);
                    ImageView championView4 = view.findViewById(R.id.iv_best_champ4);

                    MatchStatsEntry visionEntry = LoLStatsUtils.getBestVisionScoreMatch(matchStatsEntries);
                    statView4.setText(String.valueOf(visionEntry.getVisionScore()));
                    dateView4.setText(LoLStatsUtils.formatDate(visionEntry.getGameDate()));
                    visionEntry.getPlayedChampion().loadImageFromDDragon(championView4);

                    TextView statView5 = view.findViewById(R.id.tv_best_stat5);
                    TextView dateView5 = view.findViewById(R.id.tv_best_date5);
                    ImageView championView5 = view.findViewById(R.id.iv_best_champ5);

                    MatchStatsEntry longestEntry = LoLStatsUtils.getLongestMatch(matchStatsEntries);
                    statView5.setText(longestEntry.getGameDurationInMinutesAndSeconds());
                    dateView5.setText(LoLStatsUtils.formatDate(longestEntry.getGameDate()));
                    longestEntry.getPlayedChampion().loadImageFromDDragon(championView5);

                    TextView aheadCs = view.findViewById(R.id.tv_ahead_cs);
                    double csPerc = LoLStatsUtils.getPercentageOfGamesCsAheadAt15(matchStatsEntries);
                    aheadCs.setText(String.format(Locale.ENGLISH, "%.1f", csPerc).concat("%"));

                    TextView aheadGold = view.findViewById(R.id.tv_ahead_gold);
                    double goldPerc = LoLStatsUtils.getPercentageOfGamesGoldAheadAt15(matchStatsEntries);
                    aheadGold.setText(String.format(Locale.ENGLISH, "%.1f", goldPerc).concat("%"));

                    TextView csDiff10 = view.findViewById(R.id.tv_cs_diff_10);
                    TextView csDiff15 = view.findViewById(R.id.tv_cs_diff_15);
                    TextView csDiff20 = view.findViewById(R.id.tv_cs_diff_20);

                    LoLStatsUtils.setDifferenceData(csDiff10, list.getMeanCsDiffAt10(), view.getContext());
                    LoLStatsUtils.setDifferenceData(csDiff15, list.getMeanCsDiffAt15(), view.getContext());
                    LoLStatsUtils.setDifferenceData(csDiff20, list.getMeanCsDiffAt20(), view.getContext());

                    TextView goldDiff10 = view.findViewById(R.id.tv_gold_diff_10);
                    TextView goldDiff15 = view.findViewById(R.id.tv_gold_diff_15);
                    TextView goldDiff20 = view.findViewById(R.id.tv_gold_diff_20);

                    LoLStatsUtils.setDifferenceData(goldDiff10, list.getMeanGoldDiffAt10(), view.getContext());
                    LoLStatsUtils.setDifferenceData(goldDiff15, list.getMeanGoldDiffAt15(), view.getContext());
                    LoLStatsUtils.setDifferenceData(goldDiff20, list.getMeanGoldDiffAt20(), view.getContext());

                    LineData data = getData(matchStatsEntries);
                    setupChart(csChart, data);

                    //TODO añadir restantes
                    TextView dpmView = view.findViewById(R.id.tv_dpm);
                    TextView visionScore = view.findViewById(R.id.tv_vision_score);
                    TextView killParticipation = view.findViewById(R.id.tv_kp);
                    TextView wardsPerMin = view.findViewById(R.id.tv_wards_min);
                    TextView wardsBought = view.findViewById(R.id.tv_wards_bought);
                    TextView wardsControl = view.findViewById(R.id.tv_wards_cleared);

                    dpmView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanDpm()));
                    visionScore.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanVisionScore()));
                    killParticipation.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanGoldPercentData()));
                    wardsPerMin.setText(String.format(Locale.ENGLISH, "%.1f", 1.05));
                    wardsBought.setText(String.format(Locale.ENGLISH, "%.1f", 0.85));
                    wardsControl.setText(String.format(Locale.ENGLISH, "%.1f", 0.32));

                }
            }

        });
        viewModel.getSummonerEntryLiveData().observe(this, new Observer<SummonerEntry>() {
            @Override
            public void onChanged(SummonerEntry entry) {
                //TODO funcion
                ImageView profileIcon = view.findViewById(R.id.iv_profile_icon);
                TextView summonerLevel = view.findViewById(R.id.tv_summoner_level);
                TextView summonerName = view.findViewById(R.id.tv_summoner_name);
                summonerLevel.setText(String.valueOf(entry.getSummonerLevel()));
                summonerName.setText(String.valueOf(entry.getSummonerName()));
                entry.loadImageFromDDragon(profileIcon);
                if(list != null){
                }else{
                    Log.d(LOG_TAG, "list es null");
                }

                LeaguePosition soloQ = entry.getSoloQ();
                LeaguePosition flexQ = entry.getFlexQ();
                LeaguePosition flexQTT = entry.getFlexQTT();


                ImageView soloQIcon = view.findViewById(R.id.iv_rank_icon_solo);
                ImageView flexQIcon = view.findViewById(R.id.iv_rank_icon_flex);
                ImageView flexQTTIcon = view.findViewById(R.id.iv_rank_icon_3v3);

                TextView winsSoloQView =  view.findViewById(R.id.tv_solo_wins);
                TextView lossesSoloQView =  view.findViewById(R.id.tv_solo_losses);
                TextView winRateSoloQView =  view.findViewById(R.id.tv_wr_soloq);
                TextView rankSoloQView =  view.findViewById(R.id.tv_summoner_rank_solo);
                TextView lpsSoloQView =  view.findViewById(R.id.tv_summoner_rank_lps_solo);

                if(soloQ != null){
                    soloQ.setLeagueIconOnImageView(soloQIcon);
                    double winRateSoloQ = (double) soloQ.getWins() / ((double) soloQ.getWins() + soloQ.getLosses()) * 100;
                    String wRSQ = String.format(Locale.ENGLISH, "%.1f", winRateSoloQ);
                    wRSQ = wRSQ.concat("%");

                    winsSoloQView.setText(String.valueOf(soloQ.getWins()));
                    lossesSoloQView.setText(String.valueOf(soloQ.getLosses()));
                    winRateSoloQView.setText(wRSQ);
                    rankSoloQView.setText(soloQ.getTier() + " " + soloQ.getRank());
                    lpsSoloQView.setText(String.valueOf(soloQ.getLeaguePoints())+ " lps");
                }else{
                    soloQIcon.setImageResource(R.drawable.unranked);
                    rankSoloQView.setText("Unranked");
                }

                TextView winsFlexQView =  view.findViewById(R.id.tv_flex_wins);
                TextView lossesFlexQView =  view.findViewById(R.id.tv_flex_losses);
                TextView winRateFlexQView =  view.findViewById(R.id.tv_wr_flexq);
                TextView rankFlexQView =  view.findViewById(R.id.tv_summoner_rank_flex);
                TextView lpsFlexQView =  view.findViewById(R.id.tv_summoner_rank_lps_flex);

                if(flexQ != null){
                    flexQ.setLeagueIconOnImageView(flexQIcon);
                    double winRateFlexQ = (double) flexQ.getWins() / ((double) flexQ.getWins() + flexQ.getLosses()) * 100;
                    String wRFQ = String.format(Locale.ENGLISH, "%.1f", winRateFlexQ).concat("%");

                    winsFlexQView.setText(String.valueOf(flexQ.getWins()));
                    lossesFlexQView.setText(String.valueOf(flexQ.getLosses()));
                    winRateFlexQView.setText(wRFQ);
                    rankFlexQView.setText(flexQ.getTier() + " " + flexQ.getRank());
                    lpsFlexQView.setText(String.valueOf(flexQ.getLeaguePoints())+ " lps");
                }else{
                    flexQIcon.setImageResource(R.drawable.unranked);
                    rankFlexQView.setText("Unranked");
                }

                TextView winsFlexTTQView = view.findViewById(R.id.tv_3v3_wins);
                TextView lossesFlexTTQView = view.findViewById(R.id.tv_3v3_losses);
                TextView winRate3v3View = view.findViewById(R.id.tv_wr_3v3);
                TextView rank3v3View = view.findViewById(R.id.tv_summoner_rank_3v3);
                TextView lps3v3View = view.findViewById(R.id.tv_summoner_rank_lps_3v3);

                if(flexQTT != null){
                    flexQTT.setLeagueIconOnImageView(flexQTTIcon);
                    double winRate3v3 = (double) flexQTT.getWins() / ((double) flexQTT.getWins() + flexQTT.getLosses()) * 100;
                    String wRFTTQ = String.format(Locale.ENGLISH, "%.1f", winRate3v3).concat("%");

                    winsFlexTTQView.setText(String.valueOf(flexQTT.getWins()));
                    lossesFlexTTQView.setText(String.valueOf(flexQTT.getLosses()));
                    winRate3v3View.setText(wRFTTQ);
                    rank3v3View.setText(flexQTT.getTier() + " " + flexQTT.getRank());
                    lps3v3View.setText(String.valueOf(flexQTT.getLeaguePoints()) + " lps");
                }else{
                    flexQTTIcon.setImageResource(R.drawable.unranked);
                    rank3v3View.setText("Unranked");
                }
            }
        });
        return view;
    }
    ///TODO meter en championstatslist

    private void setChart(int victories, int losses){

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDrawHoleEnabled(true);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(5f);
        pieChart.setTransparentCircleRadius(20f);

        pieChart.setDrawCenterText(false);

        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setMaxAngle(360f);
        pieChart.setRotationAngle(180f);

        setData(victories, losses);

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(8f); //Tamaño del label de dentro

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }

    private void setRadarChart(){

        //radarChart.setBackgroundColor(Color.rgb(60, 65, 82));

        radarChart.getDescription().setEnabled(false);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);

        /*MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
        mv.setChartView(radarChart); // For bounds control
        radarChart.setMarker(mv); // Set the marker to the chart*/
        setRadarChartData(list);

        radarChart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = radarChart.getXAxis();
        //xAxis.setTypeface(tfLight);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new LargeValueFormatter() {
            private final String[] mActivities = new String[]{"D%", "G%", "CS", "Obj", "Vision"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            /*@Override
            public String getFormattedValue(float value) {
                return mActivities[(int) value % mActivities.length];
            }*/
        });

        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = radarChart.getYAxis();
        //yAxis.setTypeface(tfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(8f);
        yAxis.setDrawLabels(false);

        radarChart.getLegend().setEnabled(false);

       /* Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //l.setTypeface(tfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);*/
    }

    private void setData(int wins, int losses) {

        ArrayList<PieEntry> values = new ArrayList<>();


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
        pieChart.setData(data);

        pieChart.invalidate();
    }

    private void setRadarChartData(ChampionStatsList list) {

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        //ArrayList<RadarEntry> entries2 = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        /*for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mul) + min;
            //entries2.add(new RadarEntry(val2));
        }*/
        float value = (float) list.getMeanDamagePercentData()/4;
        entries1.add(new RadarEntry(value));
        float value1 = (float) list.getMeanGoldPercentData()/4;
        Log.d(LOG_TAG, "VALORES: " + value + " " + value1);
        entries1.add(new RadarEntry(value1));
        entries1.add(new RadarEntry((float) list.getCsPerMin()));
        entries1.add(new RadarEntry((float)LoLStatsUtils.calculateKDA(list.getMeanKills(), list.getMeanAssists(), list.getMeanDeaths())));
        entries1.add(new RadarEntry(5));

        RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
        set1.setColor(ContextCompat.getColor(getView().getContext(), R.color.midKdaColor));
        set1.setFillColor(ContextCompat.getColor(getView().getContext(), R.color.lightPurple));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        /*RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);*/

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        //sets.add(set2);

        RadarData data = new RadarData(sets);
        //data.setValueTypeface(tfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        radarChart.setData(data);
        radarChart.invalidate();
    }

    private void setupChart(LineChart chart, LineData data) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(Color.rgb(255, 255, 255));

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);

        YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);

        // set data
        chart.setData(data);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(2500);

        /*Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);*/
    }

    private LineData getData(List<MatchStatsEntry> entries) {

        ArrayList<Entry> valuesEarly = new ArrayList<>();
        ArrayList<Entry> meanValues = new ArrayList<>();
        int contador = 0;

        for(MatchStatsEntry entry:entries){
            contador++;
            double csMinAt15 = entry.getCsAt15()/15.0;
            double csMin = entry.getTotalCs()/(entry.getDuration()/60.0);
            valuesEarly.add(new Entry(contador, (float) csMinAt15));
            meanValues.add(new Entry(contador, (float) csMin));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(valuesEarly, "Cs/min at 15");

        LineDataSet set2 = new LineDataSet(meanValues, "Cs/min");

        set1.setLineWidth(2.5f);
        set1.setCircleRadius(4.5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawValues(false);

        set2.setLineWidth(2.5f);
        set2.setCircleRadius(4.5f);
        set2.setCircleHoleRadius(2.5f);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawValues(false);
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        return new LineData(sets);
    }


    @Override
    public void onItemClickListener(String championName, long championId) {

    }
}
