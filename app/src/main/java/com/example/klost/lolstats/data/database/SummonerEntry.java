package com.example.klost.lolstats.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_summoner")
public class SummonerEntry {

    @PrimaryKey
    private long puuid;
    private String summonerName;
    private long accoundId;
    private long summonerId;
    private int profileIconId;
    private int summonerLevel;
    private String division;
    private String rank;
    private String tier;
    private int totalRankedGames;
    private int gamesWon;
    private int leaguePoints;
    private int winRate;

    public SummonerEntry(long puuid, long accoundId, long summonerId) {
        this.puuid = puuid;
        this.accoundId = accoundId;
        this.summonerId = summonerId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getTotalRankedGames() {
        return totalRankedGames;
    }

    public void setTotalRankedGames(int totalRankedGames) {
        this.totalRankedGames = totalRankedGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public int getWinRate() {
        return winRate;
    }

    public void setWinRate(int winRate) {
        this.winRate = winRate;
    }

    public long getPuuid() {
        return puuid;
    }

    public void setPuuid(long puuid) {
        this.puuid = puuid;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public long getAccoundId() {
        return accoundId;
    }

    public void setAccoundId(long accoundId) {
        this.accoundId = accoundId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
