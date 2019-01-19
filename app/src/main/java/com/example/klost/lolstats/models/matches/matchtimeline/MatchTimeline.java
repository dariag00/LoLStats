package com.example.klost.lolstats.models.matches.matchtimeline;

import java.util.ArrayList;

public class MatchTimeline {

    private ArrayList<MatchFrame> matchFrames;
    private long frameInterval;

    public MatchTimeline(ArrayList<MatchFrame> matchFrames, long frameInterval){
        this.matchFrames = matchFrames;
        this.frameInterval = frameInterval;
    }
}
