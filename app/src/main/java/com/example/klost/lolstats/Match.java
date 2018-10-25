package com.example.klost.lolstats;

import java.util.Date;

public class Match {

    private String lane;
    private int gameId;
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

    public int getGameId() {
        return gameId;
    }

    public int getQueue() {
        return queue;
    }

    public int getSeason() {
        return season;
    }

    public Date getTimestamp() {
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

    public void setGameId(int gameId) {
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

    public void setTimestamp(long timestamp) {
        this.date = new Date(timestamp);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        return builder.toString();
    }

}
