package com.example.klost.lolstats;

import java.util.ArrayList;
import java.util.List;

public class ChampionList {

    List<Champion> champions;
    String version;

    public ChampionList(){
        champions = new ArrayList<>();
    }

    public void addChampion(Champion champion){
        //TODO comprobar existencia
        champions.add(champion);
    }

    public Champion getChampion(Champion champion){
        for(Champion ch : champions){
            if(ch.getChampionId() == champion.getChampionId()){
                return ch;
            }
        }
        return null;
    }
}
