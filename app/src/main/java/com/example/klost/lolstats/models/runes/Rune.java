package com.example.klost.lolstats.models.runes;

import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class Rune {

    private int id;
    private String key;
    private String iconPath;
    private String name;
    private String shortDesc;
    private String longDesc;

    public Rune(){

    }

    public Rune(int id){
        this.id = id;
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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }


    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(this.iconPath, NetworkUtils.GET_DDRAGON_RUNE_IMAGE);
        Log.d("Rune", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
    }

    public void loadImageFromCDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(this.iconPath, NetworkUtils.GET_CDRAGON_PERK_IMAGE);
        Log.d("Perk Rune", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
    }

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("ID:");
        builder.append(this.id);
        builder.append(" + ");

        builder.append("Name:");
        builder.append(this.name);
        builder.append("\n");

        return builder.toString();
    }
}
