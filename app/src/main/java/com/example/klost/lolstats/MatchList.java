package com.example.klost.lolstats;

import java.util.List;

public class MatchList {

    int totalGames;
    int startIndex;
    int endIndex;
    List<Match> matches;

    public MatchList(){}

    public MatchList(List<Match> matches) {
        this.matches = matches;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }


    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Total Games: ");
        builder.append(totalGames);
        builder.append("\n");

        for(int i=0; i<matches.size(); i++){
            builder.append(matches.get(i).toString());
            builder.append("\n");
        }

        return builder.toString();
    }
}
