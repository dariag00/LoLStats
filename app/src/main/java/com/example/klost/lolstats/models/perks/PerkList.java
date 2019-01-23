package com.example.klost.lolstats.models.perks;


import java.util.ArrayList;
import java.util.List;

public class PerkList {

    private List<Perk> perkList;

    public PerkList(){
        perkList = new ArrayList<>();
    }

    public Perk getPerkById(int id){
        for(Perk perk : perkList){
            if(perk.getId() == id){
                return perk;
            }
        }
        return null;
    }

}
