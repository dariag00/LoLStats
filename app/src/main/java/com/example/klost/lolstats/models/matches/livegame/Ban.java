package com.example.klost.lolstats.models.matches.livegame;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.matches.Team;

public class Ban {
    private int pickTurn;
    private Champion champion;
    private Team team;

    public Ban(int pickTurn, Champion champion, Team team) {
        this.pickTurn = pickTurn;
        this.champion = champion;
        this.team = team;
    }

    public Ban(){

    }

    public int getPickTurn() {
        return pickTurn;
    }

    public void setPickTurn(int pickTurn) {
        this.pickTurn = pickTurn;
    }

    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
