package com.example.klost.lolstats;

public class Player {

    //TODO falta implementar estadisticas runas y timeline
    int participantId;
    int teamId; //TODO realmente necesario?
    int spell2Id;
    String highestAchievedSeasoNTier;
    int spell1Id;
    int championId;
    Summoner summoner;//TODO implementar
    int summonerId;
    int accountId;
    long visionScore;
    int assists;
    int deaths;
    int kills;
    long totalDamageDealtToChampions;

    public Player(){

    }

    public Player(int participantId){
        this.participantId = participantId;
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

    public int getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(int summonerId) {
        this.summonerId = summonerId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append("Participant Id: ");
        builder.append(String.valueOf(participantId));
        builder.append("\n");

        builder.append("Account Id: ");
        builder.append(String.valueOf(accountId));
        builder.append("\n");

        return builder.toString();
    }
}
