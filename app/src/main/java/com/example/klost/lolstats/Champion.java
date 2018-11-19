package com.example.klost.lolstats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

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

    /*//TODO añadir todo lo que salga de internet en un hilo nuevo
    public void loadImageFromDDragon() {
        URL url = NetworkUtils.buildUrl(this.imageFileName, NetworkUtils.GET_DDRAGON_CHAMPION_IMAGE);
        Log.d("Champion", "URL: " + url.toString());
        new DownloadImageTask().execute(url);
    }*/

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("Id: ");
        builder.append(this.championId);
        builder.append("\n");

        builder.append("Name: ");
        builder.append(this.name);
        builder.append("\n");

        return builder.toString();
    }


    /*private class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            Bitmap mIcon11 = null;
            try {
                //TODO fix this
                InputStream in = new java.net.URL(url.toString()).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Champion", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d("Champion", result.toString());
            setImage(result);
        }
    }*/

}
