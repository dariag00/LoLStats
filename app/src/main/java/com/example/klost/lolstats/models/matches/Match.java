package com.example.klost.lolstats.models.matches;

import android.util.Log;

import com.example.klost.lolstats.models.Summoner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Match {

    //TODO añadir estadisticas del JSON de GET_MATCH
    //TODO poner en private
    //TODO pasar el role a Player

    String lane;
    long gameId;
    int championId;
    String platformId;
    int queue;
    String role;
    int season;
    Team blueTeam;
    Team redTeam;
    //Valor que indica si se ha realizado una consulta sobre ese game para sacar mas informacion
    boolean isProcessed;

    long gameDuration;
    Date gameCreation;
    String gameType;


    public Match(){
        isProcessed = false;
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


    public boolean isProcessed(){
        return isProcessed;
    }

    public void setAsProcessed(){
        isProcessed = true;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public void setBlueTeam(Team blueTeam) {
        this.blueTeam = blueTeam;
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public void setRedTeam(Team redTeam) {
        this.redTeam = redTeam;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public Date getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(long gameCreation) {
        Date date = new Date(gameCreation);
        this.gameCreation = date;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Team getTeamOfGivenSummoner(Summoner summoner){

        if(blueTeam.containsSummoner(summoner)){
            return blueTeam;
        }else if(redTeam.containsSummoner(summoner)){
            return redTeam;
        }
        //TODO caso que sea null
        return null;
    }

    public boolean hasGivenSummonerWon(Summoner summoner){
        return getTeamOfGivenSummoner(summoner).isWon();
    }
    //TODO revisar metodo
    public Player getPlayer(Summoner summoner){
        Team team = getTeamOfGivenSummoner(summoner);
        return team.getPlayer(summoner);
    }

    public String getGameDurationInMinutesAndSeconds(){

        long uptime = this.gameDuration;

        Log.d("MATCH", "upTime " + String.valueOf(uptime));
        long minutes = TimeUnit.SECONDS
                .toMinutes(uptime);
        uptime -= TimeUnit.MINUTES.toSeconds(minutes);
        Log.d("MATCH", "upTime " + String.valueOf(uptime) + " " + " mins " + String.valueOf(minutes));

        return String.valueOf(minutes) + "m " + String.valueOf(uptime) + "s";
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
       /* DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(this.date);

        builder.append("Date: ");
        builder.append(date);
        builder.append("\n");*/

        builder.append("com.example.klost.lolstats.models.champions.Champion: ");
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
