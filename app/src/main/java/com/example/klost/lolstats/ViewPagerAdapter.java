package com.example.klost.lolstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Match match;
    private Summoner summoner;

    private static final String SAVED_SUMMONER_KEY = "summoner";
    private static final String SAVED_MATCH_KEY = "match";

    public ViewPagerAdapter(FragmentManager fm, Match match, Summoner summoner) {
        super(fm);
        this.match = match;
        this.summoner = summoner;
    }

    @Override
    public Fragment getItem(int i) {

        Bundle bundle = new Bundle();
        Log.d("Prueba", summoner.toString());
        bundle.putSerializable(SAVED_MATCH_KEY, match);
        bundle.putSerializable(SAVED_SUMMONER_KEY, summoner);

        switch (i) {
            case 0:
                FragmentResultsGameDetails fragmentResultsGameDetails = new FragmentResultsGameDetails();
                fragmentResultsGameDetails.setArguments(bundle);
                return fragmentResultsGameDetails;
            case 1:
                fragmentResultsGameDetails = new FragmentResultsGameDetails();
                fragmentResultsGameDetails.setArguments(bundle);
                return fragmentResultsGameDetails;
            case 2:
                fragmentResultsGameDetails = new FragmentResultsGameDetails();
                fragmentResultsGameDetails.setArguments(bundle);
                return fragmentResultsGameDetails;
            case 3:
                fragmentResultsGameDetails = new FragmentResultsGameDetails();
                fragmentResultsGameDetails.setArguments(bundle);
                return fragmentResultsGameDetails;
            case 4:
                fragmentResultsGameDetails = new FragmentResultsGameDetails();
                fragmentResultsGameDetails.setArguments(bundle);
                return fragmentResultsGameDetails;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
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
            case 3:
                title = "";
                break;
        }

        return title;
    }

}
