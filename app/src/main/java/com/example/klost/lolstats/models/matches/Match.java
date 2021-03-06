package com.example.klost.lolstats.models.matches;

import android.util.Log;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchFrame;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchTimeline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Match implements Serializable {

    //TODO añadir estadisticas del JSON de GET_MATCH
    //TODO pasar el role a Player
    //TODO quitar lane de aqui
    private String lane;
    private long gameId;
    //TODO quitar championId de aqui
    private int championId;
    private String platformId;
    private int queue;
    //TODO quitar role de aqui
    private String role;
    private int season;
    private Team blueTeam;
    private Team redTeam;
    //Valor que indica si se ha realizado una consulta sobre ese game para sacar mas informacion
    private boolean isProcessed;

    private long gameDuration;
    private Date gameCreation;
    private String gameType;
    private MatchTimeline matchTimeline;


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
        this.gameCreation =  new Date(gameCreation);
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public MatchTimeline getMatchTimeline() {
        return matchTimeline;
    }

    public void setMatchTimeline(MatchTimeline matchTimeline) {
        this.matchTimeline = matchTimeline;
    }

    public Team getTeamOfGivenSummoner(Summoner summoner){

        if(blueTeam.containsSummoner(summoner)){
            return blueTeam;
        }else if(redTeam.containsSummoner(summoner)){
            return redTeam;
        }
        //TODO caso que sea null
        Log.d("Match", "getTeam: Se ha devuelto null al summoner " + summoner.toString());
        return null;
    }

    public Team getTeamOfGivenPlayer(Player player){
        if(blueTeam.containsPlayer(player)){
            return blueTeam;
        }else if(redTeam.containsPlayer(player)){
            return redTeam;
        }
        return null;
    }

    public Team getOppositeTeamOfGivenPlayer(Player player){
        if(!blueTeam.containsPlayer(player)){
            Log.d("LOG", "Entro en blue");
            return blueTeam;
        }else if(!redTeam.containsPlayer(player)){
            Log.d("LOG", "Entro en red");
            return redTeam;
        }
        Log.d("LOG", "Entro en null");
        return null;
    }

    public boolean hasGivenSummonerWon(Summoner summoner){
        return getTeamOfGivenSummoner(summoner).isWon();
    }

    public boolean hasGivenPlayerWon(Player player){
        return getTeamOfGivenPlayer(player).isWon();
    }

    public Player getPlayer(Summoner summoner){
        Team team = getTeamOfGivenSummoner(summoner);
        return team.getPlayer(summoner);
    }

    public String getGameDurationInMinutesAndSeconds(){

        long uptime = this.gameDuration;

        long minutes = TimeUnit.SECONDS
                .toMinutes(uptime);
        uptime -= TimeUnit.MINUTES.toSeconds(minutes);

        return String.valueOf(minutes) + "m " + String.valueOf(uptime) + "s";
    }

    public Map<Long, Integer> getParticipantCs(int participantId){
        return matchTimeline.getParticipantCs(participantId);
    }

    public Map<Long, Integer> getParticipantGold(int participantId){
        return matchTimeline.getParticipantGold(participantId);
    }

    public Map<Long, Integer> getBlueTeamGoldOverTime(){
        int[] participantIds = getTeamPlayerIds(blueTeam);
        return matchTimeline.getTeamGoldFrames(participantIds);
    }

    public Map<Long, Integer> getRedTeamGoldOverTime(){
        int[] participantIds = getTeamPlayerIds(redTeam);
        return matchTimeline.getTeamGoldFrames(participantIds);
    }

    public Map<Long, Integer> getGoldDifferenceOverTime(){
        int[] participantIdsRedTeam = getTeamPlayerIds(redTeam);
        int[] participantIdsBlueTeam = getTeamPlayerIds(blueTeam);
        return matchTimeline.getGoldDifferenceFrames(participantIdsBlueTeam, participantIdsRedTeam );
    }

    public Map<Long, Integer> getCsDifferenceOfLanersOverTime(Summoner summoner){
        Player currentPlayer = getPlayer(summoner);
        Player oppositePlayer = getOppositePlayerBasedOnRole(currentPlayer);
        if(oppositePlayer != null)
            return matchTimeline.getCsDifferenceBetween2Players(currentPlayer.getParticipantId(), oppositePlayer.getParticipantId());
        return null;
    }

    public Map<Long, Integer> getGoldDifferenceOfLanersOverTime(Summoner summoner){
        Player currentPlayer = getPlayer(summoner);
        Player oppositePlayer = getOppositePlayerBasedOnRole(currentPlayer);
        Log.d("LOG", "PLAYER1: " + currentPlayer.getParticipantId());
        if(oppositePlayer != null) {
            Log.d("LOG", "PLAYER2: " + oppositePlayer.getParticipantId());
            return matchTimeline.getGoldDifferenceBetween2Players(currentPlayer.getParticipantId(), oppositePlayer.getParticipantId());
        }

        return null;
    }

    private int[] getTeamPlayerIds(Team team){

        List<Player> players = team.getPlayers();
        int[] participantIds = new int[players.size()];

        for(int i = 0; i<5; i++){
            participantIds[i] = players.get(i).getParticipantId();
        }

        return participantIds;

    }

    public Player getOppositePlayerBasedOnRole(Player player){
        Team oppositeTeam = getOppositeTeamOfGivenPlayer(player);
        Log.d("LOG", "Opposite Team " + oppositeTeam.toString());
        return oppositeTeam.getPlayerByRole(player.getRole());
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Match: ");
        builder.append(this.gameId);
        builder.append("\n");

        builder.append("Queue: ");
        builder.append(this.queue);
        builder.append("\n");

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
