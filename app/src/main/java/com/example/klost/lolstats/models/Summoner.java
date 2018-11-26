package com.example.klost.lolstats.models;

import com.example.klost.lolstats.models.matches.MatchList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Summoner {

    long accountId;
    long summonerId;
    long summonerLevel;
    String summonerName;
    Date revisionDate;
    int profileIconId;
    MatchList matchList;


    public Summoner(){

    }

    public Summoner(int accountId, int summonerId){
        this.accountId = accountId;
        this.summonerId = summonerId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = new Date(revisionDate);
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public MatchList getMatchList() {
        return matchList;
    }

    public void setMatchList(MatchList matchList) {
        this.matchList = matchList;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Account ID: ");
        builder.append(this.accountId);
        builder.append("\n");

        builder.append("Summoner Name: ");
        builder.append(this.summonerName);
        builder.append("\n");

        builder.append("Summoner Id: ");
        builder.append(this.summonerId);
        builder.append("\n");

        builder.append("Summoner Level: ");
        builder.append(this.summonerLevel);
        builder.append("\n");

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(this.revisionDate);

        builder.append("Revision Date: ");
        builder.append(date);
        builder.append("\n");

        builder.append("Icon Id: ");
        builder.append(this.profileIconId);
        builder.append("\n");

        return builder.toString();
    }

}
