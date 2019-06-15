package com.example.klost.lolstats.utilities;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;

public class LiveGameBean {
    private Summoner summoner;
    private Match match;

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
