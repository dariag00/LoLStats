package com.example.klost.lolstats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Match {

    private String lane;
    private long gameId;
    private int championId;
    private String platformId;
    private Date date;
    private int queue;
    private String role;
    private int season;

    public Match(){

    }

    public int getChampionId() {
        return championId;
    }

    public long getGameId() {
        return gameId;
    }

    public int getQueue() {
        return queue;
    }

    public int getSeason() {
        return season;
    }

    public Date getDate() {
        return date;
    }

    public String getLane() {
        return lane;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getRole() {
        return role;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setDate(long timestamp) {
        this.date = new Date(timestamp);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Match: ");
        builder.append(this.gameId);
        builder.append("\n");

        builder.append("Queue: ");
        builder.append(this.queue);
        builder.append("\n");

        //TODO Poner hace cuanto tiempo
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(this.date);

        builder.append("Date: ");
        builder.append(date);
        builder.append("\n");

        builder.append("Champion: ");
        builder.append(this.championId);
        builder.append("\n");

        builder.append("Lane: ");
        builder.append(this.lane);
        builder.append(" and Role: ");
        builder.append(this.role);
        builder.append("\n");

        builder.append("Season: ");
        builder.append(this.season);
        builder.append("\n");

        return builder.toString();
    }

}
