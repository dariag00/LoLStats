package com.example.klost.lolstats.models.matches.matchtimeline;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MatchTimeline implements Serializable{

    private ArrayList<MatchFrame> matchFrames;
    private long frameInterval;

    public MatchTimeline(ArrayList<MatchFrame> matchFrames, long frameInterval){
        this.matchFrames = matchFrames;
        this.frameInterval = frameInterval;
    }

    public Map<Long, Integer> getParticipantCs(int participantId){

        Map<Long, Integer> map = new LinkedHashMap<>();
        for(MatchFrame frame:matchFrames){
            int cs = frame.getParticipantCsFrames(participantId);
            Log.d("FRAMES", "Frame: " + frame.getTimestamp() + " cs " + cs);
            map.put(frame.getTimestamp(), cs);
        }

        return map;
    }
}
