package com.example.klost.lolstats.models.champions;

import java.util.ArrayList;
import java.util.List;

public class ChampionStatsList {

    private List<ChampionStats> championStatsList;

    public ChampionStatsList(){
        championStatsList = new ArrayList<>();
    }

    public void addChampionStats(ChampionStats stats){
        if(!championStatsList.contains(stats.getChampion())){
            championStatsList.add(stats);
        }
    }

    public ChampionStats getChampionStats(Champion champion){
        for(ChampionStats championStats : championStatsList){
            if(championStats.getChampion().equals(champion))
                return championStats;
        }
        return null;
    }

    public boolean containsChampion(Champion champion){
        for(ChampionStats stats : championStatsList){
            if(stats.getChampion().equals(champion)){
                return true;
            }
        }
        return false;
    }

    public int getNumberOfGamesPlayed(){
        int totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalGames;
    }

    public int getNumberOfGamesWon(){
        int totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalGames = totalGames + stats.getNumberOfGamesWon();
        }
        return totalGames;
    }

    public double getMeanKills(){
        long totalKills = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalKills = totalKills + stats.getTotalKills();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalKills/totalGames;
    }

    public double getMeanDeaths(){
        long totalDeaths = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalDeaths = totalDeaths + stats.getTotalDeaths();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalDeaths/totalGames;
    }

    public double getMeanAssists(){
        long totalAssists = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalAssists = totalAssists + stats.getTotalAssists();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalAssists/totalGames;
    }

    public double getMeanDamageData(){
        long totalDamage = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalDamage = totalDamage + stats.getTotalDamage();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalDamage/totalGames;
    }

    public double getMeanCsData(){
        long totalCs = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalCs = totalCs + stats.getTotalCs();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalCs/totalGames;
    }

    public double getMeanVisionScore(){
        double totalScore = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalScore = totalScore + stats.getVisionScore();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalScore/totalGames;
    }

    public double getMeanGoldData(){
        long totalGold = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            totalGold = totalGold + stats.getTotalGold();
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalGold/totalGames;
    }

    public double getMeanDamagePercentData(){
        double totalPercent = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            double percent = stats.getDamagePercent() * stats.getNumberOfGamesPlayed();
            totalPercent = totalPercent + percent;
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalPercent/totalGames;
    }

    public double getMeanGoldPercentData(){
        double totalPercent = 0;
        double totalGames = 0;
        for(ChampionStats stats : championStatsList){
            double percent = stats.getGoldPercent() * stats.getNumberOfGamesPlayed();
            totalPercent = totalPercent + percent;
            totalGames = totalGames + stats.getNumberOfGamesPlayed();
        }
        return totalPercent/totalGames;
    }

    public double getGoldPerMin(){
        long totalGold = 0;
        double totalTimePlayed = 0;
        for(ChampionStats stats : championStatsList){
            totalGold = totalGold + stats.getTotalGold();
            totalTimePlayed = totalTimePlayed + stats.getTimePlaying();
        }
        totalTimePlayed = totalTimePlayed/60;
        return totalGold/totalTimePlayed;
    }

    public double getCsPerMin(){
        long totalCs = 0;
        double totalTimePlayed = 0;
        for(ChampionStats stats : championStatsList){
            totalCs = totalCs + stats.getTotalCs();
            totalTimePlayed = totalTimePlayed + stats.getTimePlaying();
        }
        totalTimePlayed = totalTimePlayed/60;
        return totalCs/totalTimePlayed;
    }

    public List<ChampionStats> getChampionStatsList() {
        return championStatsList;
    }
}
