package com.example.klost.lolstats.models.runes;

import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RunePath {

    int id;
    String key;
    String iconPath;
    String name;
    List<Rune> firstSlot;
    List<Rune> secondSlot;
    List<Rune> thirdSlot;
    List<Rune> fourthSlot;
    List<Rune> allRunesList;

    public RunePath(){
        firstSlot = new ArrayList<>();
        secondSlot = new ArrayList<>();
        thirdSlot = new ArrayList<>();
        fourthSlot = new ArrayList<>();
        allRunesList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rune> getFirstSlot() {
        return firstSlot;
    }

    public List<Rune> getSecondSlot() {
        return secondSlot;
    }

    public List<Rune> getThirdSlot() {
        return thirdSlot;
    }

    public List<Rune> getFourthSlot() {
        return fourthSlot;
    }

    //TODO comprobar existencia

    public void addToSlot(Rune rune, int slot){

        allRunesList.add(rune);

        if(slot == 0){
            firstSlot.add(rune);
        }else if(slot == 1){
            secondSlot.add(rune);
        }else if(slot == 2){
            thirdSlot.add(rune);
        }else if(slot == 3){
            fourthSlot.add(rune);
        }
    }

    public Rune getRune(Rune rune){
        for(Rune r: allRunesList){
            if(r.equals(rune))
                return r;
        }
        return null;
    }

    public Rune getRuneById(int id){

        for(Rune r: allRunesList){
            if(r.id == id)
                return r;
        }
        return null;

    }

    //TODO implementar equals

    public boolean contains(Rune rune){

        for(Rune r: allRunesList){
            if(r.equals(rune))
                return true;
        }

        return false;

    }

    public boolean containsId(int id){

        for(Rune r: allRunesList){
            if(r.id == id)
                return true;
        }

        return false;

    }

    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(this.iconPath, NetworkUtils.GET_DDRAGON_RUNE_IMAGE);
        Log.d("SummonerSpell", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
    }



    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("Id:");
        builder.append(this.id);
        builder.append(" + ");

        builder.append("Name:");
        builder.append(this.name);
        builder.append("\n");

        return builder.toString();

    }
}
