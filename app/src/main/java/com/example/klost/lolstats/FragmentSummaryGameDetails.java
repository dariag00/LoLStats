package com.example.klost.lolstats;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.runes.Rune;
import com.example.klost.lolstats.models.runes.RuneList;
import com.example.klost.lolstats.models.runes.RunePath;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.example.klost.lolstats.utilities.StaticData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentSummaryGameDetails  extends Fragment{


    private Player currentPlayer;
    private Match match;

    private static final String SAVED_MATCH_KEY = "match";
    private static final String SAVED_PLAYER_KEY = "player";

    private LineChart goldChart;

    public FragmentSummaryGameDetails(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_game_details, container, false);

        Bundle args = getArguments();
        if (args == null) {
            Toast.makeText(getActivity(), "arguments is null " , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "text " + args , Toast.LENGTH_LONG).show();
        }

        currentPlayer = (Player) this.getArguments().getSerializable(SAVED_PLAYER_KEY);
        match = (Match) this.getArguments().getSerializable(SAVED_MATCH_KEY);

        TextView gameResultView = view.findViewById(R.id.tv_game_result);
        if(match.hasGivenPlayerWon(currentPlayer))
            gameResultView.setText("VICTORY");
        else
            gameResultView.setText("DEFEAT");

        TextView gameLengthView = view.findViewById(R.id.tv_game_length);
        gameLengthView.setText(match.getGameDurationInMinutesAndSeconds());
        TextView gameDateView = view.findViewById(R.id.tv_game_date);
        Date date = match.getGameCreation();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dateString = String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year).substring(2,4);
        gameDateView.setText(dateString);
        TextView gameTypeView = view.findViewById(R.id.tv_game_type);
        gameTypeView.setText(LoLStatsUtils.getQueueName(match.getQueue()));

        CircleImageView championImageView = view.findViewById(R.id.iv_champion_played);

        Champion championPlayed = currentPlayer.getChampion();
        System.out.println(currentPlayer.getChampionId());
        championPlayed.loadImageFromDDragon(championImageView);

        TextView killsView = view.findViewById(R.id.tv_kills);
        TextView deathsView = view.findViewById(R.id.tv_deaths);
        TextView assistsView = view.findViewById(R.id.tv_assists);
        TextView kdaView = view.findViewById(R.id.tv_kda);

        int numberOfKills = currentPlayer.getKills();
        int numberOfDeaths = currentPlayer.getDeaths();
        int numberOfAssists = currentPlayer.getAssists();

        killsView.setText(String.valueOf(numberOfKills));
        deathsView.setText(String.valueOf(numberOfDeaths));
        assistsView.setText(String.valueOf(numberOfAssists));
        double kda = LoLStatsUtils.calculateKDA(numberOfKills, numberOfAssists, numberOfDeaths);
        LoLStatsUtils.setKdaAndTextColorInView(kdaView, kda, view.getContext());

        TextView damagePercentView = view.findViewById(R.id.tv_damage_percent);
        TextView totalDamageView = view.findViewById(R.id.tv_damage_dealt);
        TextView totalCsView = view.findViewById(R.id.tv_total_cs);
        TextView csPerMinView = view.findViewById(R.id.tv_cs_min);
        TextView totalGoldView = view.findViewById(R.id.tv_total_gold);
        TextView goldPercentView = view.findViewById(R.id.tv_gold_percent);

        double damagePercent = LoLStatsUtils.getDamagePercentOfGivenPlayer(match.getTeamOfGivenPlayer(currentPlayer).getPlayers(), currentPlayer);
        String damagePercentString = "DMG%: " + String.format(Locale.ENGLISH, "%.1f", damagePercent) + "%";
        damagePercentView.setText(damagePercentString);

        String damageDealt = "DD: " + currentPlayer.getTotalDamageDealtToChampions();
        totalDamageView.setText(damageDealt);
        int cs = currentPlayer.getTotalMinionsKilled() + currentPlayer.getNeutralMinionsKilled();
        String totalCs = "CS: " + cs;
        totalCsView.setText(totalCs);

        long gameDuration = match.getGameDuration();
        long gameDurationInMinutes = gameDuration / 60;
        double totalCsPerMin = (double) cs / (double) gameDurationInMinutes;
        String csPerMinString = "CS/MIN: " + String.format(Locale.ENGLISH, "%.1f", totalCsPerMin);
        csPerMinView.setText(csPerMinString);

        String totalGold = "GOLD: " + currentPlayer.getGoldEarned();
        totalGoldView.setText(totalGold);

        String goldPercentString = "G%: " + String.format(Locale.ENGLISH, "%.1f", LoLStatsUtils.getGoldPercentOfGivenPlayer(match.getTeamOfGivenPlayer(currentPlayer).getPlayers(), currentPlayer)) + "%";
        goldPercentView.setText(goldPercentString);

        ImageView itemView1 = view.findViewById(R.id.iv_item1);
        ImageView itemView2 = view.findViewById(R.id.iv_item2);
        ImageView itemView3 = view.findViewById(R.id.iv_item3);
        ImageView itemView4 = view.findViewById(R.id.iv_item4);
        ImageView itemView5 = view.findViewById(R.id.iv_item5);
        ImageView itemView6 = view.findViewById(R.id.iv_item6);
        CircleImageView trinketView = view.findViewById(R.id.iv_trinket);

        ItemList itemList = StaticData.getItemList();
        Item firstItem = itemList.getItemById(currentPlayer.getItem0());
        Item secondItem = itemList.getItemById(currentPlayer.getItem1());
        Item thirdItem = itemList.getItemById(currentPlayer.getItem2());
        Item fourthItem = itemList.getItemById(currentPlayer.getItem3());
        Item fifthItem = itemList.getItemById(currentPlayer.getItem4());
        Item sixthItem = itemList.getItemById(currentPlayer.getItem5());
        Item trinket = itemList.getItemById(currentPlayer.getItem6());

        if (firstItem != null)
            firstItem.loadImageFromDDragon(itemView1);
        if (secondItem != null)
            secondItem.loadImageFromDDragon(itemView2);
        if (thirdItem != null)
            thirdItem.loadImageFromDDragon(itemView3);
        if (fourthItem != null)
            fourthItem.loadImageFromDDragon(itemView4);
        if (fifthItem != null)
            fifthItem.loadImageFromDDragon(itemView5);
        if (sixthItem != null)
            sixthItem.loadImageFromDDragon(itemView6);
        if (trinket != null)
            trinket.loadImageFromDDragon(trinketView);

        CircleImageView mainPathIconView = view.findViewById(R.id.iv_rune_path);
        CircleImageView mainPathKeystoneIconView = view.findViewById(R.id.iv_rune1);
        CircleImageView mainPathRune2IconView = view.findViewById(R.id.iv_rune2);
        CircleImageView mainPathRune3IconView = view.findViewById(R.id.iv_rune3);
        CircleImageView mainPathRune4IconView = view.findViewById(R.id.iv_rune4);

        TextView mainPathNameView = view.findViewById(R.id.tv_rune_path_name);
        TextView mainPathKeystoneNameView = view.findViewById(R.id.tv_rune1_name);
        TextView mainPathRune2NameView = view.findViewById(R.id.tv_rune2_name);
        TextView mainPathRune3NameView = view.findViewById(R.id.tv_rune3_name);
        TextView mainPathRune4NameView = view.findViewById(R.id.tv_rune4_name);

        RuneList runeList = StaticData.getRuneList();
        RunePath mainPath = runeList.getRunePathById(currentPlayer.getRunePrimaryStyle());
        mainPath.loadImageFromDDragon(mainPathIconView);
        mainPathNameView.setText(mainPath.getName());

        Rune keystone = runeList.getRuneById(currentPlayer.getRune0());
        keystone.loadImageFromDDragon(mainPathKeystoneIconView);
        mainPathKeystoneNameView.setText(keystone.getName());

        Rune rune2 = runeList.getRuneById(currentPlayer.getRune1());
        rune2.loadImageFromDDragon(mainPathRune2IconView);
        mainPathRune2NameView.setText(rune2.getName());

        Rune rune3 = runeList.getRuneById(currentPlayer.getRune2());
        rune3.loadImageFromDDragon(mainPathRune3IconView);
        mainPathRune3NameView.setText(rune3.getName());

        Rune rune4 = runeList.getRuneById(currentPlayer.getRune3());
        rune4.loadImageFromDDragon(mainPathRune4IconView);
        mainPathRune4NameView.setText(rune4.getName());

        CircleImageView secondaryPathIconView = view.findViewById(R.id.iv_rune_path2);
        CircleImageView secondaryPathRune1IconView = view.findViewById(R.id.iv_rune2_1);
        CircleImageView secondaryPathRune2IconView = view.findViewById(R.id.iv_rune2_2);

        TextView secondaryPathNameView = view.findViewById(R.id.tv_rune_path2_name);
        TextView secondaryPathRune1NameView = view.findViewById(R.id.tv_rune2_1_name);
        TextView secondaryPathRune2NameView = view.findViewById(R.id.tv_rune2_2_name);


        RunePath secondaryPath = runeList.getRunePathById(currentPlayer.getRuneSecondaryStyle());
        secondaryPath.loadImageFromDDragon(secondaryPathIconView);
        secondaryPathNameView.setText(secondaryPath.getName());

        Rune rune5 = runeList.getRuneById(currentPlayer.getRune5());
        rune5.loadImageFromDDragon(secondaryPathRune1IconView);
        secondaryPathRune1NameView.setText(rune5.getName());

        Rune rune6 = runeList.getRuneById(currentPlayer.getRune4());
        rune6.loadImageFromDDragon(secondaryPathRune2IconView);
        secondaryPathRune2NameView.setText(rune6.getName());

        goldChart = view.findViewById(R.id.gold_chart);
        Log.d("XD", "TimeLine: " + match.getMatchTimeline().toString());
        Map<Long, Integer> goldMap = match.getGoldDifferenceOverTime();
        LineData data = getData(goldMap);
        setupChart(goldChart, data);


        CircleImageView perkRuneImage0 = view.findViewById(R.id.cv_rune_stat1);
        CircleImageView perkRuneImage1 = view.findViewById(R.id.cv_rune_stat2);
        CircleImageView perkRuneImage2 = view.findViewById(R.id.cv_rune_stat3);
        CircleImageView perkRuneImage3 = view.findViewById(R.id.cv_rune_stat4);
        CircleImageView perkRuneImage4 = view.findViewById(R.id.cv_rune_stat5);
        CircleImageView perkRuneImage5 = view.findViewById(R.id.cv_rune_stat6);
        CircleImageView perkRuneImage6 = view.findViewById(R.id.cv_rune_stat7);
        CircleImageView perkRuneImage7 = view.findViewById(R.id.cv_rune_stat8);
        CircleImageView perkRuneImage8 = view.findViewById(R.id.cv_rune_stat9);

        Rune perk1 = StaticData.getRuneList().getRuneById(currentPlayer.getStatRune0());
        Rune perk2 = StaticData.getRuneList().getRuneById(currentPlayer.getStatRune1());
        Rune perk3 = StaticData.getRuneList().getRuneById(currentPlayer.getStatRune2());
        Log.d("SUMM", "LLego aqui " + NetworkUtils.buildUrl(perk1.getIconPath(), NetworkUtils.GET_CDRAGON_PERK_IMAGE));
        perk1.loadImageFromCDragon(perkRuneImage0);
        perk1.loadImageFromCDragon(perkRuneImage1);
        perk1.loadImageFromCDragon(perkRuneImage2);

        perk2.loadImageFromCDragon(perkRuneImage3);
        perk2.loadImageFromCDragon(perkRuneImage4);
        perk2.loadImageFromCDragon(perkRuneImage5);

        perk3.loadImageFromCDragon(perkRuneImage7);
        perk3.loadImageFromCDragon(perkRuneImage8);
        perk3.loadImageFromCDragon(perkRuneImage6);

        return view;
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
    }

    private LineData getData(Map<Long,Integer> goldDifference) {

        ArrayList<Entry> values = new ArrayList<>();

        Iterator<Map.Entry<Long, Integer>> iterator = goldDifference.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            Log.d("FRAME", "Aqui llega: " + entry.getKey());
            int minute = (int) (entry.getKey()/60000);
            Log.d("Analysis", "Meto " + entry.getValue() + " en el minuto " + minute);
            values.add(new Entry(minute, entry.getValue()));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "Gold Difference");
        set1.setFillAlpha(Color.RED);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(2.5f);
        set1.setDrawValues(false);
        set1.setDrawCircleHole(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(set1);

        return new LineData(sets);
    }

}
