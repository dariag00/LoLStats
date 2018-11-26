package com.example.klost.lolstats.models.champions;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class Champion {

    int championId;
    String name;
    String title;
    String imageFileName;
    //TODO añadir stats y + info del campeón
    //TODO implementar equals
    Bitmap image;

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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        Champion champion = (Champion) obj;
        return champion.getChampionId() == this.getChampionId();
    }


    //TODO comprobar que picasso utilice un hilo nuevo
    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(this.imageFileName, NetworkUtils.GET_DDRAGON_CHAMPION_IMAGE);
        Log.d("Champion", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
    }

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("Id: ");
        builder.append(this.championId);
        builder.append(" + ");

        builder.append("Name: ");
        builder.append(this.name);
        builder.append("\n");

        return builder.toString();
    }
}