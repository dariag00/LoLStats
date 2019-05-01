package com.example.klost.lolstats.models.runes;

import java.util.ArrayList;
import java.util.List;

public class RuneList {

    private List<RunePath> runePathList;
    private List<Rune> auxiliaryRunes;

    public RuneList(){
        runePathList = new ArrayList<>();
        auxiliaryRunes = new ArrayList<>();
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

        for(Rune rune: auxiliaryRunes)
            if(rune.getId() == id)
                return rune;

        return null;

    }

    public List<Rune> getAuxiliaryRunes(){
        return this.auxiliaryRunes;
    }

    public void setAuxiliaryRunes(List<Rune> auxiliaryRunes){
        this.auxiliaryRunes = auxiliaryRunes;
    }

    public boolean contains(int runeId){
        boolean contained = false;
        for(RunePath path : runePathList){
            if(path.containsId(runeId))
                contained = true;
        }

        return contained;
    }


    public void addAuxiliaryRune(Rune rune){
        this.auxiliaryRunes.add(rune);
    }

}
