package com.example.klost.lolstats.models.champions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChampionList {

    private List<Champion> champions;
    private String version;

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

    class SortByName implements Comparator<Champion> {
        @Override
        public int compare(Champion a, Champion b) {
            return a.getName().compareTo(b.getName());
        }
    }
}
