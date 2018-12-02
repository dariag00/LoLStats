package com.example.klost.lolstats.models.summoners;

import java.util.ArrayList;
import java.util.List;

public class SummonerSpellList {

    private List<SummonerSpell> spells;

    public SummonerSpellList(){
        spells = new ArrayList<>();
    }

    public void addSpell(SummonerSpell summonerSpell){
        spells.add(summonerSpell);
    }

    public SummonerSpell getSpell(SummonerSpell summonerSpell){
        for(SummonerSpell s:spells){
            if(s.equals(summonerSpell))
                return s;
        }
        return null;
    }

    public SummonerSpell getSpellById(int key){

        for(SummonerSpell s:spells){
            if(s.getKey() == key)
                return s;
        }
        return null;

    }

}
