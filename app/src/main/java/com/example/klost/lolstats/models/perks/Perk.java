package com.example.klost.lolstats.models.perks;

import com.example.klost.lolstats.models.items.Item;

public class Perk {

    int id;
    String name;
    String tooltip;
    String shortDesc;
    String longDesc;
    String iconPath;

    public Perk() {
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

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public boolean equals(Object obj) {
        Perk perk = (Perk) obj;
        return perk.getId() == this.getId();
    }

}
