package com.example.klost.lolstats.utilities;

import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.Summoner;

public class FetchSummonerBean {

    private Summoner summoner;
    private SummonerEntry summonerEntry;

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public SummonerEntry getSummonerEntry() {
        return summonerEntry;
    }

    public void setSummonerEntry(SummonerEntry summonerEntry) {
        this.summonerEntry = summonerEntry;
    }
}
