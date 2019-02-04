package com.example.klost.lolstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStatsList;
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
import java.util.List;
import java.util.Locale;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class ChampionDetailsActivity extends AppCompatActivity {

    private final String SAVED_ENTRY = "SAVED_ENTRY_ID";
    private final String SAVED_CHAMP = "CHAMPION_ID";

    private ChampionStatsList list;
    private static PieChart pieChart;
    private RadarChart radarChart;
    private LineChart csChart;
    private static final String LOG_TAG = ChampionDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        pieChart = findViewById(R.id.pie_chart);
        radarChart = findViewById(R.id.radar_chart);
        csChart = findViewById(R.id.cs_chart);

        Intent previousIntent = getIntent();
        int entryId = previousIntent.getIntExtra(SAVED_ENTRY, -1);
        int championId = previousIntent.getIntExtra(SAVED_CHAMP, -1);

        final Champion champion = StaticData.getChampionList().getChampionById(championId);

        LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId, championId);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        final Context context = this;
        viewModel.getChampionEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> entries) {
                ImageView splashArt = findViewById(R.id.iv_splash_art);
                champion.loadSplashArtFromDDragon(splashArt);

                ImageView profileIcon = findViewById(R.id.iv_profile_icon);
                TextView summonerName = findViewById(R.id.tv_summoner_name);

                champion.loadImageFromDDragon(profileIcon);
                summonerName.setText(champion.getName());

                list = LoLStatsUtils.generateChampionStats(entries);
                TextView totalCs = findViewById(R.id.tv_total_cs);
                TextView csPerMin = findViewById(R.id.tv_cs_min);

                String totalCsString = "Cs: " + String.format(Locale.ENGLISH, "%.1f", list.getMeanCsData());
                String csMinString = "(" + String.format(Locale.ENGLISH, "%.1f", list.getCsPerMin()) + "/min)";

                totalCs.setText(totalCsString);
                csPerMin.setText(csMinString);

                TextView winRate = findViewById(R.id.tv_win_rate);
                TextView wins = findViewById(R.id.tv_losses);
                TextView losses = findViewById(R.id.tv_wins);
                wins.setText(String.valueOf(list.getNumberOfGamesWon()));
                losses.setText(String.valueOf(list.getNumberOfGamesPlayed() - list.getNumberOfGamesWon()));
                double wRate = (list.getNumberOfGamesWon()/(double) list.getNumberOfGamesPlayed()) * 100;
                String wRateString = "(" + String.format(Locale.ENGLISH, "%.1f", wRate) + "%)";
                winRate.setText(wRateString);

                TextView topPlayRate = findViewById(R.id.tv_role_played1);
                TextView topWinRate = findViewById(R.id.tv_role_win_rate1);
                TextView jglePlayRate = findViewById(R.id.tv_role_played2);
                TextView jgleWinRate = findViewById(R.id.tv_role_win_rate2);
                TextView midPlayRate = findViewById(R.id.tv_role_played3);
                TextView midWinRate = findViewById(R.id.tv_role_win_rate3);
                TextView adcPlayRate = findViewById(R.id.tv_role_played4);
                TextView adcWinRate = findViewById(R.id.tv_role_win_rate4);
                TextView suppPlayRate = findViewById(R.id.tv_role_played5);
                TextView suppWinRate = findViewById(R.id.tv_role_win_rate5);

                double[][] roleData = LoLStatsUtils.getWinRateAndPlayRateOfRoles(entries);
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

                TextView killsView = findViewById(R.id.tv_global_kills);
                TextView deathsView = findViewById(R.id.tv_global_deaths);
                TextView assistsView = findViewById(R.id.tv_global_assists);
                TextView kdaView = findViewById(R.id.tv_global_kda);

                killsView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanKills()));
                deathsView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanDeaths()));
                assistsView.setText(String.format(Locale.ENGLISH, "%.1f", list.getMeanAssists()));
                double kda = LoLStatsUtils.calculateKDA(list.getMeanKills(), list.getMeanAssists(), list.getMeanDeaths());
                LoLStatsUtils.setKdaAndTextColorInView(kdaView, kda, context);

                TextView statView1 = findViewById(R.id.tv_best_stat1);
                TextView dateView1 = findViewById(R.id.tv_best_date1);
                ImageView championView1 = findViewById(R.id.iv_best_champ1);

                MatchStatsEntry csEntry = LoLStatsUtils.getBestCsMinMatch(entries);
                double csMin = csEntry.getTotalCs()/(csEntry.getDuration()/60.0);
                statView1.setText(String.format(Locale.ENGLISH, "%.1f", csMin));
                dateView1.setText(LoLStatsUtils.formatDate(csEntry.getGameDate()));
                csEntry.getPlayedChampion().loadImageFromDDragon(championView1);

                TextView statView2 = findViewById(R.id.tv_best_stat2);
                TextView dateView2 = findViewById(R.id.tv_best_date2);
                ImageView championView2 = findViewById(R.id.iv_best_champ2);

                MatchStatsEntry kdaEntry = LoLStatsUtils.getBestKdaMatch(entries);
                LoLStatsUtils.setKdaAndTextColorInView(statView2, LoLStatsUtils.calculateKDA(kdaEntry.getKills(), kdaEntry.getAssists(), kdaEntry.getDeaths()), context);
                dateView2.setText(LoLStatsUtils.formatDate(csEntry.getGameDate()));
                kdaEntry.getPlayedChampion().loadImageFromDDragon(championView2);

                TextView statView3 = findViewById(R.id.tv_best_stat3);
                TextView dateView3 = findViewById(R.id.tv_best_date3);
                ImageView championView3 = findViewById(R.id.iv_best_champ3);

                MatchStatsEntry dmgEntry = LoLStatsUtils.getBestDmgPercentMatch(entries);
                statView3.setText(String.format(Locale.ENGLISH, "%.1f", dmgEntry.getDamagePercent()));
                dateView3.setText(LoLStatsUtils.formatDate(dmgEntry.getGameDate()));
                dmgEntry.getPlayedChampion().loadImageFromDDragon(championView3);

                TextView statView4 = findViewById(R.id.tv_best_stat4);
                TextView dateView4 = findViewById(R.id.tv_best_date4);
                ImageView championView4 = findViewById(R.id.iv_best_champ4);

                MatchStatsEntry visionEntry = LoLStatsUtils.getBestDmgPercentMatch(entries);
                statView4.setText(String.valueOf(visionEntry.getVisionScore()));
                dateView4.setText(LoLStatsUtils.formatDate(visionEntry.getGameDate()));
                dmgEntry.getPlayedChampion().loadImageFromDDragon(championView4);

                TextView aheadCs = findViewById(R.id.tv_ahead_cs);
                double csPerc = LoLStatsUtils.getPercentageOfGamesCsAheadAt15(entries);
                aheadCs.setText(String.format(Locale.ENGLISH, "%.1f", csPerc).concat("%"));

                TextView aheadGold = findViewById(R.id.tv_ahead_gold);
                double goldPerc = LoLStatsUtils.getPercentageOfGamesGoldAheadAt15(entries);
                aheadGold.setText(String.format(Locale.ENGLISH, "%.1f", goldPerc).concat("%"));

                TextView csDiff10 =findViewById(R.id.tv_cs_diff_10);
                TextView csDiff15 =findViewById(R.id.tv_cs_diff_15);
                TextView csDiff20 =findViewById(R.id.tv_cs_diff_20);

                LoLStatsUtils.setDifferenceData(csDiff10, list.getMeanCsDiffAt10(), context);
                LoLStatsUtils.setDifferenceData(csDiff15, list.getMeanCsDiffAt15(), context);
                LoLStatsUtils.setDifferenceData(csDiff20, list.getMeanCsDiffAt20(), context);

                TextView goldDiff10 = findViewById(R.id.tv_gold_diff_10);
                TextView goldDiff15 = findViewById(R.id.tv_gold_diff_15);
                TextView goldDiff20 = findViewById(R.id.tv_gold_diff_20);

                LoLStatsUtils.setDifferenceData(goldDiff10, list.getMeanGoldDiffAt10(), context);
                LoLStatsUtils.setDifferenceData(goldDiff15, list.getMeanGoldDiffAt15(), context);
                LoLStatsUtils.setDifferenceData(goldDiff20, list.getMeanGoldDiffAt20(), context);

                TextView totalDamage = findViewById(R.id.tv_total_damage);
                TextView totalGold = findViewById(R.id.tv_total_gold);
                TextView goldPerMin = findViewById(R.id.tv_gold_min);
                TextView damagePercent = findViewById(R.id.tv_dmg_percent);

                String totalGoldString = "Gold: " + LoLStatsUtils.formatDoubleValue(list.getMeanGoldData());
                String totalDamageString = "Damage: " + LoLStatsUtils.formatDoubleValue(list.getMeanDamageData());
                String goldMinString = "G/min: " + String.format(Locale.ENGLISH, "%.1f", list.getGoldPerMin());
                String dmgPercentString = "DMG%: " + String.format(Locale.ENGLISH, "%.1f", list.getMeanDamagePercentData());

                totalDamage.setText(totalDamageString);
                totalGold.setText(totalGoldString);
                goldPerMin.setText(goldMinString);
                damagePercent.setText(dmgPercentString);

                setChart(list.getNumberOfGamesWon(), list.getNumberOfGamesPlayed()-list.getNumberOfGamesWon());
                setRadarChart();

                LineData data = getData(entries);
                setupChart(csChart, data);

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();  // finish() here.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


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
        set1.setColor(ContextCompat.getColor(this, R.color.midKdaColor));
        set1.setFillColor(ContextCompat.getColor(this, R.color.lightPurple));
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

}
