package com.example.klost.lolstats.models;

import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Summoner implements Serializable {

    private long accountId;
    private long summonerId;
    private long summonerLevel;
    private String summonerName;
    private Date revisionDate;
    private int profileIconId;

    private MatchList matchList;

    private LeaguePositionList positionList;


    public Summoner(){

    }

    public Summoner(long accountId, long summonerId){
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

    public LeaguePositionList getPositionList() {
        return positionList;
    }

    public void setPositionList(LeaguePositionList positionList) {
        this.positionList = positionList;
    }

    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(String.valueOf(this.profileIconId), NetworkUtils.GET_DDRAGON_PROFILE_ICON);
        Log.d("Icon Image", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
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

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
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
