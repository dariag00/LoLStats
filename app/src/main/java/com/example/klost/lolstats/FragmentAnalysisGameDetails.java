package com.example.klost.lolstats;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FragmentAnalysisGameDetails extends Fragment {

    private Match match;
    private Summoner summoner;
    private LineChart chart;

    private static final String SAVED_SUMMONER_KEY = "summoner";
    private static final String SAVED_MATCH_KEY = "match";

    public FragmentAnalysisGameDetails() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        match = (Match) this.getArguments().getSerializable(SAVED_MATCH_KEY);
        summoner = (Summoner) this.getArguments().getSerializable(SAVED_SUMMONER_KEY);

        Player summonerPlayer = match.getPlayer(summoner);
        Map<Long, Integer> player1 = match.getParticipantCs(summonerPlayer.getParticipantId());
        Map<Long, Integer> player2 = match.getParticipantCs(5);

        chart = view.findViewById(R.id.cs_chart);
        LineData data = getData(player1, player2);
        setupChart(chart, data);
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
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // set data
        chart.setData(data);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(2500);
    }

    private LineData getData(Map<Long,Integer> player1, Map<Long,Integer> player2) {

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();

        /*for (int i = 0; i < 10; i++) {
            values.add(new Entry(i, val));
            values2.add(new Entry(i,val2));
        }*/

        Iterator<Map.Entry<Long, Integer>> iterator = player1.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            Log.d("FRAME", "Aqui llega: " + entry.getKey());
            int minute = (int) (entry.getKey()/60000);
            if(minute != 0){
                float csMin = (float) entry.getValue()/minute;
                Log.d("Analysis", "Meto " + csMin + " en el minuto " + minute);
                values.add(new Entry(minute, csMin));
            }else{
                Log.d("Analysis", "Entro en 0");
                values.add(new Entry(minute, entry.getValue()));
            }
        }

        iterator = player2.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            int minute = (int) (entry.getKey()/60000);
            if(minute != 0){
                float csMin = (float) entry.getValue()/minute;
                Log.d("Analysis", "Meto 2 " + csMin + " en el minuto " + minute);
                values2.add(new Entry(minute, csMin));
            }else{
                values2.add(new Entry(minute, entry.getValue()));
            }
        }
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        LineDataSet set2 = new LineDataSet(values2, "DataSet 2");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

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
