package com.example.klost.lolstats.models.summoners;

import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class SummonerSpell {

    private String id;
    private String name;
    private String description;
    private int key;
    private String imageFileName;

    public SummonerSpell(){

    }

    public SummonerSpell(int key){
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public boolean equals(Object obj) {
        SummonerSpell summonerSpell = (SummonerSpell) obj;
        return summonerSpell.getKey() == summonerSpell.getKey();
    }

    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(this.imageFileName, NetworkUtils.GET_DDRAGON_SUMMONER_SPELL_ICON);
        Log.d("SummonerSpell", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
    }

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("Key:");
        builder.append(this.key);
        builder.append(" + ");

        builder.append("Name:");
        builder.append(this.name);
        builder.append("\n");

        return builder.toString();
    }
}
