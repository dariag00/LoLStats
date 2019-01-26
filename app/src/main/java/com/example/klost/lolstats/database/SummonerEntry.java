package com.example.klost.lolstats.database;

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
    private long summonerLevel;
    private String division;
    private int rank;
    private int totalRankedGames;
    private int gamesWon;

    public SummonerEntry(long puuid, String summonerName, long accoundId, long summonerId, int profileIconId, long summonerLevel) {
        this.puuid = puuid;
        this.summonerName = summonerName;
        this.accoundId = accoundId;
        this.summonerId = summonerId;
        this.profileIconId = profileIconId;
        this.summonerLevel = summonerLevel;
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

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
