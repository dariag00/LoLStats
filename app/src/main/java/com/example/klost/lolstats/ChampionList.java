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
            if(champion.equals(ch)){
                return ch;
            }
        }
        return null;
    }

    public Champion getChampionById(int id){
        for(Champion ch : champions){
            if(ch.getChampionId() == id){
                return ch;
            }
        }
        return null;
    }
}
