package com.example.klost.lolstats.data.database;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.matches.Player;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "match_stats", foreignKeys = @ForeignKey(entity = SummonerEntry.class,
        parentColumns = "id",
        childColumns = "summonerId",
        onDelete = CASCADE), indices = {@Index("summonerId")})
public class MatchStatsEntry {

    @PrimaryKey
    private long matchId;
    private int summonerId;
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
    private double damagePercent;
    private double goldPercent;
    private long totalDamage;
    private long visionScore;
    private Date gameDate;
    private int seasonId;
    private String role;
    private double dpm;
    @Embedded
    private Champion playedChampion;
    @Embedded
    Player player;

    public MatchStatsEntry(long matchId) {
        this.matchId = matchId;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public int getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(int summonerId) {
        this.summonerId = summonerId;
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

    public double getDamagePercent() {
        return damagePercent;
    }

    public void setDamagePercent(double damagePercent) {
        this.damagePercent = damagePercent;
    }

    public long getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(long totalDamage) {
        this.totalDamage = totalDamage;
    }

    public long getVisionScore() {
        return visionScore;
    }

    public void setVisionScore(long visionScore) {
        this.visionScore = visionScore;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public double getGoldPercent() {
        return goldPercent;
    }

    public void setGoldPercent(double goldPercent) {
        this.goldPercent = goldPercent;
    }

    public Champion getPlayedChampion() {
        return playedChampion;
    }

    public void setPlayedChampion(Champion playedChampion) {
        this.playedChampion = playedChampion;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Player getPlayer() {
        return player;
    }

    public double getDpm() {
        return dpm;
    }

    public void setDpm(double dpm) {
        this.dpm = dpm;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getGameDurationInMinutesAndSeconds(){

        long uptime = this.duration;

        long minutes = TimeUnit.SECONDS
                .toMinutes(uptime);
        uptime -= TimeUnit.MINUTES.toSeconds(minutes);

        return String.valueOf(minutes) + "m " + String.valueOf(uptime) + "s";
    }
}
