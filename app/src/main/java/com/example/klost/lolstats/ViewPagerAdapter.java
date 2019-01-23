package com.example.klost.lolstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Match match;
    private Summoner summoner;
    private Player currentPlayer;

    private static final String SAVED_SUMMONER_KEY = "summoner";
    private static final String SAVED_MATCH_KEY = "match";
    private static final String SAVED_PLAYER_KEY = "player";

    private static final String LOG_TAG = "ViewPagerAdapter";

    public ViewPagerAdapter(FragmentManager fm, Match match, Summoner summoner, Player player) {
        super(fm);
        this.match = match;
        this.summoner = summoner;
        this.currentPlayer = player;
    }

    @Override
    public Fragment getItem(int i) {

        Bundle bundle = new Bundle();

        switch (i) {
            case 0:
                FragmentResultsGameDetails resultsGameDetails = new FragmentResultsGameDetails();
                bundle.putSerializable(SAVED_MATCH_KEY, match);
                bundle.putSerializable(SAVED_SUMMONER_KEY, summoner);
                resultsGameDetails.setArguments(bundle);
                return resultsGameDetails;
            case 1:
                FragmentSummaryGameDetails summaryGameDetails = new FragmentSummaryGameDetails();
                bundle.putSerializable(SAVED_PLAYER_KEY, currentPlayer);
                Log.d(LOG_TAG, "Player: " + currentPlayer);
                Log.d(LOG_TAG, "Bungle" + bundle.toString());
                summaryGameDetails.setArguments(bundle);
                Log.d(LOG_TAG, "Bungle" + summaryGameDetails.getArguments().toString());
                return summaryGameDetails;
            case 2:
                FragmentAnalysisGameDetails fragmentAnalysis = new FragmentAnalysisGameDetails();
                bundle.putSerializable(SAVED_MATCH_KEY, match);
                bundle.putSerializable(SAVED_SUMMONER_KEY, summoner);
                fragmentAnalysis.setArguments(bundle);
                return fragmentAnalysis;
            /*case 2:
                 summaryGameDetails = new FragmentSummaryGameDetails();
                summaryGameDetails.setArguments(bundle);
                return summaryGameDetails;*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = "Game Summary";
                break;
            case 1:
                title = "Game Stats";
                break;
            case 2:
                title = "Analysis";
                break;
        }

        return title;
    }

}
