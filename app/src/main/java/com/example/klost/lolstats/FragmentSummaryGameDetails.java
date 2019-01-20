package com.example.klost.lolstats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;

public class FragmentSummaryGameDetails  extends Fragment{


    private Match match;
    private Summoner summoner;

    private static final String SAVED_SUMMONER_KEY = "summoner";
    private static final String SAVED_MATCH_KEY = "match";

    public FragmentSummaryGameDetails(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_game_details, container, false);

       /* match = (Match) this.getArguments().getSerializable(SAVED_MATCH_KEY);
        summoner = (Summoner) this.getArguments().getSerializable(SAVED_SUMMONER_KEY);*/


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
