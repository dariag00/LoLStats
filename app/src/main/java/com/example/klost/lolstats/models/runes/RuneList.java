package com.example.klost.lolstats.models.runes;

import java.util.ArrayList;
import java.util.List;

public class RuneList {

    List<RunePath> runePathList;

    public RuneList(){
        runePathList = new ArrayList<>();
    }

    public void addRunePath(RunePath runePath){
        runePathList.add(runePath);
    }

    public RunePath getRunePath(RunePath runePath){
        for(RunePath r: runePathList){
            if(r.equals(runePath))
                return r;
        }
        return null;
    }

    public RunePath getRunePathById(int id){
        for(RunePath r: runePathList){
            if(r.getId() == id)
                return r;
        }
        return null;

    }

    public Rune getRune(Rune rune){
        for(RunePath r: runePathList){
            if(r.contains(rune))
                return r.getRune(rune);
        }
        return null;
    }

    public Rune getRuneById(int id){

        for(RunePath r: runePathList){
            if(r.containsId(id))
                return r.getRuneById(id);
        }
        return null;

    }


}
