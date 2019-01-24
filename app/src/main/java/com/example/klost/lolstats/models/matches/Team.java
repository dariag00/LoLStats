package com.example.klost.lolstats.models.matches;

import android.util.Log;

import com.example.klost.lolstats.models.Summoner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {

    //TODO falta implentación de modos diferentes a Summoner Rift y los baneos
    //TODO sacar los datos del jugador que ha buscado este match

    private int teamId; //100 for blue side, 200 for red side
    private boolean firstDragon; //Flag indicating whether or not the team scored the first Dragon kill
    private boolean firstInhibitor; //Flag indicating whether or not the team destroyed the first inhibitor
    private int baronKills; //Number of times the team killed Baron
    private boolean firstRiftHerald; //Flag indicating whether or not the team scored the first Rift Herald kill
    private boolean firstBaron; //Flag indicating whether or not the team scored the first Baron kill
    private int riftHeraldKills; //Number of times the team killed Rift Herald
    private boolean firstBlood; //Flag indicating whether or not the team scored the first blood
    private boolean firstTower; //Flag indicating whether or not the team destroyed the first tower
    private int inhibitorKills; //Number of inhibitors the team destroyed
    private int towerKills; //Number of towers the team destroyed
    private String win; //Legal Values Fail, Win
    private int dragonKills; //Number of times the team killed Dragon
    private List<Player> players;

    public Team(){
        players = new ArrayList<>();
    }

    public Team(int teamId){
        this.teamId = teamId;
        players = new ArrayList<>();
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public boolean isFirstDragon() {
        return firstDragon;
    }

    public void setFirstDragon(boolean firstDragon) {
        this.firstDragon = firstDragon;
    }

    public boolean isFirstInhibitor() {
        return firstInhibitor;
    }

    public void setFirstInhibitor(boolean firstInhibitor) {
        this.firstInhibitor = firstInhibitor;
    }

    public int getBaronKills() {
        return baronKills;
    }

    public void setBaronKills(int baronKills) {
        this.baronKills = baronKills;
    }

    public boolean isFirstRiftHerald() {
        return firstRiftHerald;
    }

    public void setFirstRiftHerald(boolean firstRiftHerald) {
        this.firstRiftHerald = firstRiftHerald;
    }

    public boolean isFirstBaron() {
        return firstBaron;
    }

    public void setFirstBaron(boolean firstBaron) {
        this.firstBaron = firstBaron;
    }

    public int getRiftHeraldKills() {
        return riftHeraldKills;
    }

    public void setRiftHeraldKills(int riftHeraldKills) {
        this.riftHeraldKills = riftHeraldKills;
    }

    public boolean isFirstBlood() {
        return firstBlood;
    }

    public void setFirstBlood(boolean firstBlood) {
        this.firstBlood = firstBlood;
    }

    public boolean isFirstTower() {
        return firstTower;
    }

    public void setFirstTower(boolean firstTower) {
        this.firstTower = firstTower;
    }

    public int getInhibitorKills() {
        return inhibitorKills;
    }

    public void setInhibitorKills(int inhibitorKills) {
        this.inhibitorKills = inhibitorKills;
    }

    public int getTowerKills() {
        return towerKills;
    }

    public void setTowerKills(int towerKills) {
        this.towerKills = towerKills;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public int getDragonKills() {
        return dragonKills;
    }

    public void setDragonKills(int dragonKills) {
        this.dragonKills = dragonKills;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
        //TODO comprobación de errores y existencia
        players.add(player);
    }

    public Player getPlayer(Summoner summoner){

        for(Player player:players) {
            if (player.getSummoner().getEncryptedSummonerId().equals(summoner.getEncryptedSummonerId())){
                Log.d("Team", "getPlayer: se ha encontrado al Player" + player.toString() + summoner.getEncryptedAccountId());
                return player;
            }
        }
        //TODO cambiar cuadno sea null
        Log.d("Team", "getPlayer: Se ha devuelto null al summoner " + summoner.toString());
        return null;
    }

    public boolean containsSummoner(Summoner summoner){

        for(Player player : players){
            Log.d("TEAM", "IDS: " + player.getSummoner().getEncryptedAccountId() + " " + summoner.getEncryptedAccountId());
            if(summoner.getEncryptedAccountId().equals(player.getSummoner().getEncryptedAccountId())){
                return true;
            }
        }

        return false;
    }

    public boolean containsPlayer(Player player){
        for(Player pl:players){
            if(player.equals(player))
                return true;
        }

        return false;
    }

    public boolean isWon(){
        return win.equals("Win");
    }
}
