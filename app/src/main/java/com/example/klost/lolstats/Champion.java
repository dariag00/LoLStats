package com.example.klost.lolstats;

import android.graphics.drawable.Drawable;

import com.example.klost.lolstats.utilities.NetworkUtils;

import java.io.InputStream;
import java.net.URL;

public class Champion {

    int championId;
    String name;
    String title;
    String imageFileName;
    //TODO añadir stats y + info del campeón
    //TODO implementar equals

    public Champion(int championId){
        this.championId = championId;
    }

    public Champion(int championId, String name){
        this.championId = championId;
        this.name = name;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    //TODO añadir todo lo que salga de internet en un hilo nuevo
    public Drawable LoadImageFromDDragon() {
        URL url = NetworkUtils.buildUrl(this.imageFileName, NetworkUtils.GET_DDRAGON_CHAMPION_IMAGE);
        try {
            InputStream is = (InputStream) url.getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
