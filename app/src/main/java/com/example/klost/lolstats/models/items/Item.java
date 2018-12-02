package com.example.klost.lolstats.models.items;

import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class Item {

    private int id;
    private String name;
    private String plainText;
    private String imagePath;
    private String description;

    private int totalCost;
    private int baseCost;
    private int sellCost;
    private boolean purchasable;
    //TODO añadir estadisticas y mejoras

    public Item(){

    }

    public Item(int itemId){
        this.id = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public int getSellCost() {
        return sellCost;
    }

    public void setSellCost(int sellCost) {
        this.sellCost = sellCost;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    @Override
    public boolean equals(Object obj) {
        Item item = (Item) obj;
        return item.getId() == this.getId();
    }

    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(this.imagePath, NetworkUtils.GET_DDRAGON_ITEM_IMAGE);
        Log.d("Item", "URL: " + url.toString());
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
