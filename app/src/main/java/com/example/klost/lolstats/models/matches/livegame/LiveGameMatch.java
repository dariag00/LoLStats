package com.example.klost.lolstats.models.matches.livegame;

import com.example.klost.lolstats.models.matches.Match;

import java.util.List;

public class LiveGameMatch extends Match{

    private long gameStartTime;
    private List<Ban> bannedChampions;
    private long gameLength;

    public LiveGameMatch(){

    }



}
