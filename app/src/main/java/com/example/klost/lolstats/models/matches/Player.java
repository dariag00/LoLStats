package com.example.klost.lolstats.models.matches;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;

import java.io.Serializable;

import androidx.room.Ignore;

public class Player implements Serializable {

    private int participantId;
    private int teamId; //TODO realmente necesario?
    private int spell2Id;
    private String highestAchievedSeasoNTier;
    private int spell1Id;
    //TODO eliminar
    @Ignore
    private int championId;
    @Ignore
    private Champion champion;
    @Ignore
    private Summoner summoner;
    @Ignore//TODO implementar
    private long visionScore;
    @Ignore
    private int assists;
    @Ignore
    private int deaths;
    @Ignore
    private int kills;
    @Ignore
    private long totalDamageDealtToChampions;
    @Ignore
    private int totalMinionsKilled;
    @Ignore
    private int neutralMinionsKilled;
    @Ignore
    private int goldEarned;
    @Ignore
    private int goldSpent;
    @Ignore
    private int championLevel;

    private int visionWardsBought;
    @Ignore
    private int pentaKills;
    @Ignore
    private int quadraKills;
    @Ignore
    private int largestKillingSpree;

    private int wardsPlaced;
    @Ignore
    private int largestMultiKill;

    private int wardsKilled;
    @Ignore
    private String role;
    @Ignore
    private String lane;

    //TEMPORAL
    //TODO añadir RuneList
    private int runePrimaryStyle;
    private int runeSecondaryStyle;

    private int rune0;
    private int rune1;
    private int rune2;
    private int rune3;

    private int rune4;
    private int rune5;

    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;

    public Player(){

    }

    @Ignore
    public Player(int participantId){
        this.participantId = participantId;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public Champion getChampion() {
        return champion;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getSpell2Id() {
        return spell2Id;
    }

    public void setSpell2Id(int spell2Id) {
        this.spell2Id = spell2Id;
    }

    public String getHighestAchievedSeasoNTier() {
        return highestAchievedSeasoNTier;
    }

    public void setHighestAchievedSeasoNTier(String highestAchievedSeasoNTier) {
        this.highestAchievedSeasoNTier = highestAchievedSeasoNTier;
    }

    public int getSpell1Id() {
        return spell1Id;
    }

    public void setSpell1Id(int spell1Id) {
        this.spell1Id = spell1Id;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public long getVisionScore() {
        return visionScore;
    }

    public void setVisionScore(long visionScore) {
        this.visionScore = visionScore;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public long getTotalDamageDealtToChampions() {
        return totalDamageDealtToChampions;
    }

    public void setTotalDamageDealtToChampions(long totalDamageDealtToChampions) {
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
    }

    public int getGoldSpent() {
        return goldSpent;
    }

    public void setGoldSpent(int goldSpent) {
        this.goldSpent = goldSpent;
    }

    //TEMPORAL
    public int getRunePrimaryStyle() {
        return runePrimaryStyle;
    }

    public void setRunePrimaryStyle(int runePrimaryStyle) {
        this.runePrimaryStyle = runePrimaryStyle;
    }

    public int getRuneSecondaryStyle() {
        return runeSecondaryStyle;
    }

    public void setRuneSecondaryStyle(int runeSecondaryStyle) {
        this.runeSecondaryStyle = runeSecondaryStyle;
    }

    public int getVisionWardsBought() {
        return visionWardsBought;
    }

    public void setVisionWardsBought(int visionWardsBought) {
        this.visionWardsBought = visionWardsBought;
    }

    public int getPentaKills() {
        return pentaKills;
    }

    public void setPentaKills(int pentaKills) {
        this.pentaKills = pentaKills;
    }

    public int getQuadraKills() {
        return quadraKills;
    }

    public void setQuadraKills(int quadraKills) {
        this.quadraKills = quadraKills;
    }

    public int getLargestKillingSpree() {
        return largestKillingSpree;
    }

    public void setLargestKillingSpree(int largestKillingSpree) {
        this.largestKillingSpree = largestKillingSpree;
    }

    public int getWardsPlaced() {
        return wardsPlaced;
    }

    public void setWardsPlaced(int wardsPlaced) {
        this.wardsPlaced = wardsPlaced;
    }

    public int getLargestMultiKill() {
        return largestMultiKill;
    }

    public void setLargestMultiKill(int largestMultiKill) {
        this.largestMultiKill = largestMultiKill;
    }

    public int getWardsKilled() {
        return wardsKilled;
    }

    public void setWardsKilled(int wardsKilled) {
        this.wardsKilled = wardsKilled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public int getRune0() {
        return rune0;
    }

    public void setRune0(int rune0) {
        this.rune0 = rune0;
    }

    public int getRune1() {
        return rune1;
    }

    public void setRune1(int rune1) {
        this.rune1 = rune1;
    }

    public int getRune2() {
        return rune2;
    }

    public void setRune2(int rune2) {
        this.rune2 = rune2;
    }

    public int getRune3() {
        return rune3;
    }

    public void setRune3(int rune3) {
        this.rune3 = rune3;
    }

    public int getRune4() {
        return rune4;
    }

    public void setRune4(int rune4) {
        this.rune4 = rune4;
    }

    public int getRune5() {
        return rune5;
    }

    public void setRune5(int rune5) {
        this.rune5 = rune5;
    }

    public int getItem0() {
        return item0;
    }

    public void setItem0(int item0) {
        this.item0 = item0;
    }

    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public int getItem4() {
        return item4;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }

    public int getItem5() {
        return item5;
    }

    public void setItem5(int item5) {
        this.item5 = item5;
    }

    public int getItem6() {
        return item6;
    }

    public void setItem6(int item6) {
        this.item6 = item6;
    }

    public int getTotalMinionsKilled() {
        return totalMinionsKilled;
    }

    public void setTotalMinionsKilled(int totalMinionsKilled) {
        this.totalMinionsKilled = totalMinionsKilled;
    }

    public int getNeutralMinionsKilled() {
        return neutralMinionsKilled;
    }

    public void setNeutralMinionsKilled(int neutralMinionsKilled) {
        this.neutralMinionsKilled = neutralMinionsKilled;
    }

    public int getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.championLevel = championLevel;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(obj instanceof Player) {
            Player player = (Player) obj;
            return player.getParticipantId() == this.getParticipantId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("Participant Id: ");
        builder.append(String.valueOf(participantId));
        builder.append("\n");

        builder.append("Account Id: ");
        builder.append(String.valueOf(summoner.getEncryptedAccountId()));
        builder.append("\n");

        return builder.toString();
    }
}
