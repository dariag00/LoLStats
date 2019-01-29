package com.example.klost.lolstats.models.champions;

import java.util.ArrayList;
import java.util.List;

public class ChampionStatsList {

    private List<ChampionStats> championStatsList;

    public ChampionStatsList(){
        championStatsList = new ArrayList<>();
    }

    public void addChampionStats(ChampionStats stats){
        if(!championStatsList.contains(stats.getChampion())){
            championStatsList.add(stats);
        }
    }

    public ChampionStats getChampionStats(Champion champion){
        for(ChampionStats championStats : championStatsList){
            if(championStats.getChampion().equals(champion))
                return championStats;
        }
        return null;
    }

    public boolean containsChampion(Champion champion){
        for(ChampionStats stats : championStatsList){
            if(stats.getChampion().equals(champion)){
                return true;
            }
        }
        return false;
    }

    public List<ChampionStats> getChampionStatsList() {
        return championStatsList;
    }
}
