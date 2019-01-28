package com.example.klost.lolstats.models.leagueposition;

import android.os.Build;
import android.widget.ImageView;

import com.example.klost.lolstats.R;

import java.io.Serializable;

public class LeaguePosition implements Serializable, Comparable<LeaguePosition>{

    private String queueType;
    private boolean hotstreak;
    private int wins;
    private int losses;
    private String rank;
    private String tier;
    private int leaguePoints;

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public boolean isHotstreak() {
        return hotstreak;
    }

    public void setHotstreak(boolean hotstreak) {
        this.hotstreak = hotstreak;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public String getRankAndTier(){
        return tier + " " + rank;
    }

    public void setLeagueIconOnImageView(ImageView imageView){

        switch (tier){
            case "IRON":
                imageView.setImageResource(R.drawable.iron);
                break;
            case "BRONZE":
                imageView.setImageResource(R.drawable.bronze);
                break;
            case "SILVER":
                imageView.setImageResource(R.drawable.silver);
                break;
            case "GOLD":
                imageView.setImageResource(R.drawable.gold);
                break;
            case "PLATINUM":
                imageView.setImageResource(R.drawable.platinum);
                break;
            case "DIAMOND":
                imageView.setImageResource(R.drawable.diamond);
                break;
            case "MASTER":
                imageView.setImageResource(R.drawable.master);
                break;
            case "GRANDMASTER":
                imageView.setImageResource(R.drawable.grandmaster);
                break;
            case "CHALLENGER":
                imageView.setImageResource(R.drawable.challenger);
                break;
            default:
                imageView.setImageResource(R.drawable.unranked);

        }

    }

    public void setLeagueMiniIconOnImageView(ImageView imageView){

        switch (tier){
            case "IRON":
                imageView.setImageResource(R.drawable.iron_mini);
                break;
            case "BRONZE":
                imageView.setImageResource(R.drawable.bronze_mini);
                break;
            case "SILVER":
                imageView.setImageResource(R.drawable.silver_mini);
                break;
            case "GOLD":
                imageView.setImageResource(R.drawable.gold_mini);
                break;
            case "PLATINUM":
                imageView.setImageResource(R.drawable.platinum_mini);
                break;
            case "DIAMOND":
                imageView.setImageResource(R.drawable.diamond_mini);
                break;
            case "MASTER":
                imageView.setImageResource(R.drawable.master_mini);
                break;
            case "GRANDMASTER":
                imageView.setImageResource(R.drawable.grandmaster_mini);
                break;
            case "CHALLENGER":
                imageView.setImageResource(R.drawable.challenger_mini);
                break;
            default:
                imageView.setImageResource(R.drawable.unranked);

        }

    }

    private int getNumericTier(){
        switch(tier){
            case "IRON":
                return 1;
            case "BRONZE":
                return 2;
            case "SILVER":
                return 3;
            case "GOLD":
                return 4;
            case "PLATINUM":
                return 5;
            case "DIAMOND":
                return 6;
            case "MASTER":
                return 7;
            case "GRANDMASTER":
                return 8;
            case "CHALLENGER":
                return 9;
            default:
                return 0;
        }
    }

    private int getNumericRank(){
        switch(tier){
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            default:
                return -1;
        }
    }




    @Override
    public int compareTo(LeaguePosition o) {

            if(this.getNumericTier() > o.getNumericTier())
                return 1;
            else if(this.getNumericTier() == o.getNumericTier())
                if(this.getNumericRank() > o.getNumericRank())
                    return 1;
                else if(this.getNumericRank() == o.getNumericRank())
                    return 0;
                else
                    return -1;
            else
                return -1;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Type:");
        builder.append(this.queueType);
        builder.append("\n");

        return builder.toString();
    }
}
