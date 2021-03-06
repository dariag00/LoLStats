package com.example.klost.lolstats.models.matches.matchtimeline;

import android.util.Log;

import com.example.klost.lolstats.models.matches.Team;

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
            int cs = frame.getParticipantCsFrame(participantId);
            Log.d("FRAMES", "Frame: " + frame.getTimestamp() + " cs " + cs);
            map.put(frame.getTimestamp(), cs);
        }

        return map;
    }

    public Map<Long, Integer> getParticipantGold(int participantId){

        Map<Long, Integer> map = new LinkedHashMap<>();
        for(MatchFrame frame:matchFrames){
            int gold = frame.getParticipantGoldFrame(participantId);
            Log.d("FRAMES", "Frame: " + frame.getTimestamp() + " gold " + gold);
            map.put(frame.getTimestamp(), gold);
        }

        return map;
    }

    public Map<Long, Integer> getTeamGoldFrames(int[] participantIds){
        Map<Long, Integer> map = new LinkedHashMap<>();
        int totalGoldThisFrame = 0;
        for(MatchFrame frame:matchFrames){
           for(int i = 0; i<participantIds.length; i++){
               totalGoldThisFrame = totalGoldThisFrame + frame.getParticipantGoldFrame(participantIds[i]);
           }
            map.put(frame.getTimestamp(), totalGoldThisFrame);
        }
        return map;
    }

    public Map<Long, Integer> getGoldDifferenceBetween2Players(int participantId1, int participantId2){

        Map<Long, Integer> map = new LinkedHashMap<>();
        int goldDifference;
        for(MatchFrame frame: matchFrames){
            goldDifference = frame.getParticipantGoldFrame(participantId1) - frame.getParticipantGoldFrame(participantId2);
            map.put(frame.getTimestamp(), goldDifference);
        }

        return map;

    }

    public Map<Long, Integer> getCsDifferenceBetween2Players(int participantId1, int participantId2){

        Map<Long, Integer> map = new LinkedHashMap<>();
        int csDifference;
        for(MatchFrame frame: matchFrames){
            csDifference = frame.getParticipantCsFrame(participantId1) - frame.getParticipantCsFrame(participantId2);
            map.put(frame.getTimestamp(), csDifference);
        }

        return map;

    }

    public Map<Long, Integer> getGoldDifferenceFrames(int[] participantIdsBlueTeam, int[] participantIdsRedTeam){
        Map<Long, Integer> map = new LinkedHashMap<>();
        int totalGoldBlueTeamThisFrame = 0;
        int totalGoldRedTeamThisFrame = 0;
        for(MatchFrame frame:matchFrames){
            for(int i = 0; i<participantIdsBlueTeam.length; i++){
                totalGoldBlueTeamThisFrame = totalGoldBlueTeamThisFrame + frame.getParticipantGoldFrame(participantIdsBlueTeam[i]);
            }
            for(int i = 0; i<participantIdsRedTeam.length; i++){
                totalGoldRedTeamThisFrame = totalGoldRedTeamThisFrame + frame.getParticipantGoldFrame(participantIdsRedTeam[i]);
            }
            Log.d("LOG", "Blue Gold: " + totalGoldBlueTeamThisFrame + " red gold: " + totalGoldRedTeamThisFrame + " en el frame: " + frame.getTimestamp());
            map.put(frame.getTimestamp(), totalGoldBlueTeamThisFrame-totalGoldRedTeamThisFrame);
        }
        return map;
    }
}
