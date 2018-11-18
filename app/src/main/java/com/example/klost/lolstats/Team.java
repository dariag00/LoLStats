package com.example.klost.lolstats;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Team {

    //TODO falta implentación de modos diferentes a Summoner Rift y los baneos
    //TODO sacar los datos del jugador que ha buscado este match

    int teamId; //100 for blue side, 200 for red side
    boolean firstDragon; //Flag indicating whether or not the team scored the first Dragon kill
    boolean firstInhibitor; //Flag indicating whether or not the team destroyed the first inhibitor
    int baronKills; //Number of times the team killed Baron
    boolean firstRiftHerald; //Flag indicating whether or not the team scored the first Rift Herald kill
    boolean firstBaron; //Flag indicating whether or not the team scored the first Baron kill
    int riftHeraldKills; //Number of times the team killed Rift Herald
    boolean firstBlood; //Flag indicating whether or not the team scored the first blood
    boolean firstTower; //Flag indicating whether or not the team destroyed the first tower
    int inhibitorKills; //Number of inhibitors the team destroyed
    int towerKills; //Number of towers the team destroyed
    String win; //Legal Values Fail, Win
    int dragonKills; //Number of times the team killed Dragon
    List<Player> players;

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

        for(Player player:players){
            if(player.getSummoner().getSummonerId() == summoner.summonerId)
                return player;
        }
        //TODO cambiar cuadno sea null
        return null;
    }

    public boolean containsSummoner(Summoner summoner){

        for(Player player : players){
            Log.d("TEAM", "IDS: " + player.getSummoner().getAccountId() + " " + summoner.getAccountId());
            if(summoner.getAccountId() == player.getSummoner().getAccountId()){
                return true;
            }
        }

        return false;
    }

    public boolean isWon(){
        return win.equals("Win");
    }
}
