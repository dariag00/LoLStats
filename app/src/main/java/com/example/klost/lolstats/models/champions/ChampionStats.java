package com.example.klost.lolstats.models.champions;

import android.util.Log;

import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.utilities.StaticData;

public class ChampionStats{

    private Champion champion;
    private Summoner summoner;
    private int numberOfGamesPlayed;
    private int numberOfGamesWon;
    private int totalKills;
    private int totalDeaths;
    private int totalAssists;
    private long totalCs;
    private long totalGold;
    private long totalDamage;
    private double meanCsAt10;
    private double meanCsAt15;
    private double meanCsAt20;
    private double meanCsDiffAt10;
    private double meanCsDiffAt15;
    private double meanCsDiffAt20;
    private double meanTotalCs;
    private double meanGoldAt10;
    private double meanGoldAt15;
    private double meanGoldAt20;
    private double meanGoldDiff10;
    private double meanGoldDiff15;
    private double meanGoldDiff20;
    private double meanTotalGold;
    private double damagePercent;
    private double meanTotalDamage;
    private double meanKills;
    private double meanDeaths;
    private double meanAssists;
    private double visionScore;
    private long timePlaying;
    private long timesPlayedTop;
    private long timesPlayedJungle;
    private long timesPlayedMid;
    private long timesPlayedAdc;
    private long timesPlayedSupp;
    private double goldPercent;
    private double meanDpm;

    public ChampionStats(MatchStatsEntry entry) {
        this.champion = entry.getPlayedChampion();
        //TODO summoner
        this.numberOfGamesPlayed = 1;
        if(entry.isVictory()){
            this.numberOfGamesWon = 1;
        }
        this.totalKills = entry.getKills();
        this.totalDeaths = entry.getDeaths();
        this.totalAssists = entry.getAssists();
        this.totalCs = entry.getTotalCs();
        this.totalGold = entry.getTotalGold();
        this.totalDamage = entry.getTotalDamage();
        this.meanCsAt10 = entry.getCsAt10();
        this.meanCsAt15 = entry.getCsAt15();
        this.meanCsAt20 = entry.getCsAt20();
        this.meanCsDiffAt10 = entry.getCsDiffAt10();
        this.meanCsDiffAt15 = entry.getCsDiffAt15();
        this.meanCsDiffAt20 = entry.getCsDiffAt20();
        this.meanGoldAt10 = entry.getGoldAt10();
        this.meanGoldAt15 = entry.getGoldAt15();
        this.meanGoldAt20 = entry.getGoldAt20();
        this.meanGoldDiff10 = entry.getGoldDiff10();
        this.meanGoldDiff15 = entry.getGoldDiff15();
        this.meanGoldDiff20 = entry.getGoldDiff20();
        this.meanTotalGold = entry.getTotalGold();
        this.meanTotalCs = entry.getTotalCs();
        this.damagePercent = entry.getDamagePercent();
        this.meanTotalDamage = entry.getTotalDamage();
        this.visionScore = entry.getVisionScore();
        this.timePlaying = entry.getDuration();
        this.meanKills = entry.getKills();
        this.meanAssists = entry.getAssists();
        this.meanDeaths = entry.getDeaths();
        this.goldPercent = entry.getGoldPercent();
    }


    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
        this.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public int getNumberOfGamesWon() {
        return numberOfGamesWon;
    }

    public void setNumberOfGamesWon(int numberOfGamesWon) {
        this.numberOfGamesWon = numberOfGamesWon;
    }

    public double getGoldPercent() {
        return goldPercent;
    }

    public void setGoldPercent(double goldPercent) {
        this.goldPercent = goldPercent;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getTotalAssists() {
        return totalAssists;
    }

    public void setTotalAssists(int totalAssists) {
        this.totalAssists = totalAssists;
    }

    public double getMeanCsAt10() {
        return meanCsAt10;
    }

    public void setMeanCsAt10(double meanCsAt10) {
        this.meanCsAt10 = meanCsAt10;
    }

    public double getMeanCsAt15() {
        return meanCsAt15;
    }

    public void setMeanCsAt15(double meanCsAt15) {
        this.meanCsAt15 = meanCsAt15;
    }

    public double getMeanCsAt20() {
        return meanCsAt20;
    }

    public void setMeanCsAt20(double meanCsAt20) {
        this.meanCsAt20 = meanCsAt20;
    }

    public double getMeanCsDiffAt10() {
        return meanCsDiffAt10;
    }

    public void setMeanCsDiffAt10(double meanCsDiffAt10) {
        this.meanCsDiffAt10 = meanCsDiffAt10;
    }

    public double getMeanCsDiffAt15() {
        return meanCsDiffAt15;
    }

    public void setMeanCsDiffAt15(double meanCsDiffAt15) {
        this.meanCsDiffAt15 = meanCsDiffAt15;
    }

    public double getMeanCsDiffAt20() {
        return meanCsDiffAt20;
    }

    public void setMeanCsDiffAt20(double meanCsDiffAt20) {
        this.meanCsDiffAt20 = meanCsDiffAt20;
    }

    public double getMeanTotalCs() {
        return meanTotalCs;
    }

    public void setMeanTotalCs(double meanTotalCs) {
        this.meanTotalCs = meanTotalCs;
    }

    public double getMeanGoldAt10() {
        return meanGoldAt10;
    }

    public void setMeanGoldAt10(double meanGoldAt10) {
        this.meanGoldAt10 = meanGoldAt10;
    }

    public double getMeanGoldAt15() {
        return meanGoldAt15;
    }

    public void setMeanGoldAt15(double meanGoldAt15) {
        this.meanGoldAt15 = meanGoldAt15;
    }

    public double getMeanGoldAt20() {
        return meanGoldAt20;
    }

    public void setMeanGoldAt20(double meanGoldAt20) {
        this.meanGoldAt20 = meanGoldAt20;
    }

    public double getMeanGoldDiff10() {
        return meanGoldDiff10;
    }

    public void setMeanGoldDiff10(double meanGoldDiff10) {
        this.meanGoldDiff10 = meanGoldDiff10;
    }

    public double getMeanGoldDiff15() {
        return meanGoldDiff15;
    }

    public void setMeanGoldDiff15(double meanGoldDiff15) {
        this.meanGoldDiff15 = meanGoldDiff15;
    }

    public double getMeanGoldDiff20() {
        return meanGoldDiff20;
    }

    public void setMeanGoldDiff20(double meanGoldDiff20) {
        this.meanGoldDiff20 = meanGoldDiff20;
    }

    public double getMeanTotalGold() {
        return meanTotalGold;
    }

    public void setMeanTotalGold(double meanTotalGold) {
        this.meanTotalGold = meanTotalGold;
    }

    public double getDamagePercent() {
        return damagePercent;
    }

    public void setDamagePercent(double damagePercent) {
        this.damagePercent = damagePercent;
    }

    public double getMeanTotalDamage() {
        return meanTotalDamage;
    }

    public void setMeanTotalDamage(double meanTotalDamage) {
        this.meanTotalDamage = meanTotalDamage;
    }

    public double getVisionScore() {
        return visionScore;
    }

    public void setVisionScore(double visionScore) {
        this.visionScore = visionScore;
    }

    public long getTotalCs() {
        return totalCs;
    }

    public void setTotalCs(long totalCs) {
        this.totalCs = totalCs;
    }

    public long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(long totalGold) {
        this.totalGold = totalGold;
    }

    public long getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(long totalDamage) {
        this.totalDamage = totalDamage;
    }

    public double getMeanKills() {
        return meanKills;
    }

    public void setMeanKills(double meanKills) {
        this.meanKills = meanKills;
    }

    public double getMeanDeaths() {
        return meanDeaths;
    }

    public void setMeanDeaths(double meanDeaths) {
        this.meanDeaths = meanDeaths;
    }

    public double getMeanAssists() {
        return meanAssists;
    }

    public void setMeanAssists(double meanAssists) {
        this.meanAssists = meanAssists;
    }

    public long getTimePlaying() {
        return timePlaying;
    }

    public void setTimePlaying(long timePlaying) {
        this.timePlaying = timePlaying;
    }

    public double calculateCsPerMin(){
        double minutes = timePlaying / 60.0;
        return (double) totalCs / minutes;
    }

    public long getTimesPlayedTop() {
        return timesPlayedTop;
    }

    public void setTimesPlayedTop(long timesPlayedTop) {
        this.timesPlayedTop = timesPlayedTop;
    }

    public long getTimesPlayedJungle() {
        return timesPlayedJungle;
    }

    public void setTimesPlayedJungle(long timesPlayedJungle) {
        this.timesPlayedJungle = timesPlayedJungle;
    }

    public long getTimesPlayedMid() {
        return timesPlayedMid;
    }

    public void setTimesPlayedMid(long timesPlayedMid) {
        this.timesPlayedMid = timesPlayedMid;
    }

    public long getTimesPlayedAdc() {
        return timesPlayedAdc;
    }

    public void setTimesPlayedAdc(long timesPlayedAdc) {
        this.timesPlayedAdc = timesPlayedAdc;
    }

    public long getTimesPlayedSupp() {
        return timesPlayedSupp;
    }

    public void setTimesPlayedSupp(long timesPlayedSupp) {
        this.timesPlayedSupp = timesPlayedSupp;
    }

    public double getMeanDpm() {
        return meanDpm;
    }

    public void setMeanDpm(double meanDpm) {
        this.meanDpm = meanDpm;
    }

    public void addNewStat(MatchStatsEntry newStat){
        if(newStat.getPlayedChampion().equals(this.champion)){
            this.numberOfGamesPlayed++;
            if(newStat.isVictory()){
                this.numberOfGamesWon++;
            }

            this.totalKills = totalKills + newStat.getKills();
            this.totalDeaths = totalDeaths + newStat.getDeaths();
            this.totalAssists = totalAssists + newStat.getAssists();
            this.totalGold = this.totalGold + newStat.getTotalGold();
            this.totalCs = this.totalCs + newStat.getTotalCs();
            this.totalDamage = this.totalDamage + newStat.getTotalDamage();
            this.timePlaying = this.timePlaying + newStat.getDuration();
            this.meanTotalGold = (double) this.totalGold / (double) this.numberOfGamesPlayed;
            this.meanTotalCs = (double) this.totalCs / (double) this.numberOfGamesPlayed;
            this.meanTotalDamage = (double) this.totalDamage / (double) this.numberOfGamesPlayed;
            this.visionScore =  ((this.visionScore * (this.numberOfGamesPlayed - 1)) + newStat.getVisionScore()) / (double) this.numberOfGamesPlayed;

            this.meanCsAt10 =  ((this.meanCsAt10 * (this.numberOfGamesPlayed - 1)) + newStat.getCsAt10()) / (double) this.numberOfGamesPlayed;
            this.meanCsAt15 =  ((this.meanCsAt15 * (this.numberOfGamesPlayed - 1)) + newStat.getCsAt15()) / (double) this.numberOfGamesPlayed;
            this.meanCsAt20 =  ((this.meanCsAt20 * (this.numberOfGamesPlayed - 1)) + newStat.getCsAt20()) / (double) this.numberOfGamesPlayed;

            this.meanGoldAt10 =  ((this.meanGoldAt10 * (this.numberOfGamesPlayed - 1)) + newStat.getGoldAt10()) / (double) this.numberOfGamesPlayed;
            this.meanGoldAt15 =  ((this.meanGoldAt15 * (this.numberOfGamesPlayed - 1)) + newStat.getGoldAt15()) / (double) this.numberOfGamesPlayed;
            this.meanGoldAt20 =  ((this.meanGoldAt20 * (this.numberOfGamesPlayed - 1)) + newStat.getGoldAt20()) / (double) this.numberOfGamesPlayed;

            this.meanGoldDiff10 =  ((this.meanGoldDiff10 * (this.numberOfGamesPlayed - 1)) + newStat.getGoldDiff10()) / (double) this.numberOfGamesPlayed;
            this.meanGoldDiff15 =  ((this.meanGoldDiff15 * (this.numberOfGamesPlayed - 1)) + newStat.getGoldDiff15()) / (double) this.numberOfGamesPlayed;
            this.meanGoldDiff20 =  ((this.meanGoldDiff20 * (this.numberOfGamesPlayed - 1)) + newStat.getGoldDiff20()) / (double) this.numberOfGamesPlayed;

            this.meanCsDiffAt10 =  ((this.meanCsDiffAt10 * (this.numberOfGamesPlayed - 1)) + newStat.getCsDiffAt10()) / (double) this.numberOfGamesPlayed;
            this.meanCsDiffAt15 =  ((this.meanCsDiffAt15 * (this.numberOfGamesPlayed - 1)) + newStat.getCsDiffAt15()) / (double) this.numberOfGamesPlayed;
            this.meanCsDiffAt20 =  ((this.meanCsDiffAt20 * (this.numberOfGamesPlayed - 1)) + newStat.getCsDiffAt20()) / (double) this.numberOfGamesPlayed;
            Log.d("Test", "Antes DMG: " + this.damagePercent);
            this.damagePercent =  ((this.damagePercent * (this.numberOfGamesPlayed - 1)) + newStat.getDamagePercent()) / (double) this.numberOfGamesPlayed;
            Log.d("Test", "DMG: " + this.damagePercent + " " + newStat.getDamagePercent());
            this.goldPercent =  ((this.goldPercent * (this.numberOfGamesPlayed - 1)) + newStat.getGoldPercent()) / (double) this.numberOfGamesPlayed;

            this.meanKills =  ((this.meanKills * (this.numberOfGamesPlayed - 1)) + newStat.getKills()) / (double) this.numberOfGamesPlayed;
            this.meanDeaths =  ((this.meanDeaths * (this.numberOfGamesPlayed - 1)) + newStat.getDeaths()) / (double) this.numberOfGamesPlayed;
            this.meanAssists =  ((this.meanAssists * (this.numberOfGamesPlayed - 1)) + newStat.getAssists()) / (double) this.numberOfGamesPlayed;

            this.meanDpm =  ((this.meanDpm * (this.numberOfGamesPlayed - 1)) + newStat.getDpm()) / (double) this.numberOfGamesPlayed;

        }
    }
}
