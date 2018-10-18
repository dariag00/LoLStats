package com.example.klost.lolstats;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Summoner {

    int id;
    int summonerId;
    int summonerLevel;
    String summonerName;
    Date revisionDate;

    public Summoner(){

    }

    public Summoner(int summonerId){
        this.summonerId = summonerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(int summonerId) {
        this.summonerId = summonerId;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {

        Date date = new Date(revisionDate);
        this.revisionDate = date;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Summoner Name: ");
        builder.append(this.summonerName);
        builder.append("\n");

        builder.append("Summoner Id: ");
        builder.append(this.summonerId);
        builder.append("\n");

        builder.append("Summoner Level: ");
        builder.append(this.summonerLevel);
        builder.append("\n");

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(this.revisionDate);

        builder.append("Revision Date: ");
        builder.append(date);
        builder.append("\n");

        return builder.toString();
    }

}
