package com.example.klost.lolstats.models.leagueposition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeaguePositionList implements Serializable {

    private List<LeaguePosition> list;

    public LeaguePositionList(){
        list = new ArrayList<>();
    }

    public void addLeaguePosition(LeaguePosition position){
        list.add(position);
    }

    public LeaguePosition getRankedSoloPosition(){
        for(LeaguePosition position : list){
            if(position.getQueueType().equals("RANKED_SOLO_5x5"))
                return position;
        }
        return null;
    }

    public LeaguePosition getRankedFlexPosition(){

        for(LeaguePosition position : list){
            if(position.getQueueType().equals("RANKED_FLEX_SR"))
                return position;
        }

        return null;

    }

    public LeaguePosition getRankedFlexTTPosition(){

        for(LeaguePosition position : list){
            if(position.getQueueType().equals("RANKED_FLEX_TT"))
                return position;
        }

        return null;

    }

    public LeaguePosition getHighestRankingPosition(){

        LeaguePosition highestPosition = null;

        for(LeaguePosition position : list){
            if(highestPosition == null)
                highestPosition = position;
            else if(position.compareTo(highestPosition) == 1 )
                highestPosition = position;
        }

        return highestPosition;
    }

}
