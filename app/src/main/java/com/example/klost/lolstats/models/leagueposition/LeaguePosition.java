package com.example.klost.lolstats.models.leagueposition;

import android.widget.ImageView;

import com.example.klost.lolstats.R;

import java.io.Serializable;

public class LeaguePosition implements Serializable {

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

    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Type:");
        builder.append(this.queueType);
        builder.append("\n");

        return builder.toString();
    }
}
