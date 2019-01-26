package com.example.klost.lolstats.database;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "match_stats")
public class MatchStatsEntry {

    @PrimaryKey
    private long matchId;
    private long championId;
    private int kills;
    private int deaths;
    private int assists;
    private boolean victory;
    private int csAt10;
    private int csAt15;
    private int csAt20;
    private int csDiffAt10;
    private int csDiffAt15;
    private int csDiffAt20;
    private int totalCs;
    private long duration;
    private int goldAt10;
    private int goldAt15;
    private int goldAt20;
    private int goldDiff10;
    private int goldDiff15;
    private int goldDiff20;
    private int totalGold;
    private int damagePercent;
    private int totalDamage;
    private int visionScore;
    private Date gameDate;

    public MatchStatsEntry(long matchId, long championId, int kills, int deaths, int assists, boolean victory, int csAt10, int csAt15, int csAt20, int csDiffAt10, int csDiffAt15, int csDiffAt20, int totalCs, long duration, int goldAt10, int goldAt15, int goldAt20, int goldDiff10, int goldDiff15, int goldDiff20, int totalGold, int damagePercent, int totalDamage, int visionScore, Date date) {
        this.matchId = matchId;
        this.championId = championId;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.victory = victory;
        this.csAt10 = csAt10;
        this.csAt15 = csAt15;
        this.csAt20 = csAt20;
        this.csDiffAt10 = csDiffAt10;
        this.csDiffAt15 = csDiffAt15;
        this.csDiffAt20 = csDiffAt20;
        this.totalCs = totalCs;
        this.duration = duration;
        this.goldAt10 = goldAt10;
        this.goldAt15 = goldAt15;
        this.goldAt20 = goldAt20;
        this.goldDiff10 = goldDiff10;
        this.goldDiff15 = goldDiff15;
        this.goldDiff20 = goldDiff20;
        this.totalGold = totalGold;
        this.damagePercent = damagePercent;
        this.totalDamage = totalDamage;
        this.visionScore = visionScore;
        this.gameDate = date;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public long getChampionId() {
        return championId;
    }

    public void setChampionId(long championId) {
        this.championId = championId;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public int getCsAt10() {
        return csAt10;
    }

    public void setCsAt10(int csAt10) {
        this.csAt10 = csAt10;
    }

    public int getCsAt15() {
        return csAt15;
    }

    public void setCsAt15(int csAt15) {
        this.csAt15 = csAt15;
    }

    public int getCsAt20() {
        return csAt20;
    }

    public void setCsAt20(int csAt20) {
        this.csAt20 = csAt20;
    }

    public int getCsDiffAt10() {
        return csDiffAt10;
    }

    public void setCsDiffAt10(int csDiffAt10) {
        this.csDiffAt10 = csDiffAt10;
    }

    public int getCsDiffAt15() {
        return csDiffAt15;
    }

    public void setCsDiffAt15(int csDiffAt15) {
        this.csDiffAt15 = csDiffAt15;
    }

    public int getCsDiffAt20() {
        return csDiffAt20;
    }

    public void setCsDiffAt20(int csDiffAt20) {
        this.csDiffAt20 = csDiffAt20;
    }

    public int getTotalCs() {
        return totalCs;
    }

    public void setTotalCs(int totalCs) {
        this.totalCs = totalCs;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getGoldAt10() {
        return goldAt10;
    }

    public void setGoldAt10(int goldAt10) {
        this.goldAt10 = goldAt10;
    }

    public int getGoldAt15() {
        return goldAt15;
    }

    public void setGoldAt15(int goldAt15) {
        this.goldAt15 = goldAt15;
    }

    public int getGoldAt20() {
        return goldAt20;
    }

    public void setGoldAt20(int goldAt20) {
        this.goldAt20 = goldAt20;
    }

    public int getGoldDiff10() {
        return goldDiff10;
    }

    public void setGoldDiff10(int goldDiff10) {
        this.goldDiff10 = goldDiff10;
    }

    public int getGoldDiff15() {
        return goldDiff15;
    }

    public void setGoldDiff15(int goldDiff15) {
        this.goldDiff15 = goldDiff15;
    }

    public int getGoldDiff20() {
        return goldDiff20;
    }

    public void setGoldDiff20(int goldDiff20) {
        this.goldDiff20 = goldDiff20;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public int getDamagePercent() {
        return damagePercent;
    }

    public void setDamagePercent(int damagePercent) {
        this.damagePercent = damagePercent;
    }

    public int getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(int totalDamage) {
        this.totalDamage = totalDamage;
    }

    public int getVisionScore() {
        return visionScore;
    }

    public void setVisionScore(int visionScore) {
        this.visionScore = visionScore;
    }
}
