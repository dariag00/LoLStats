package com.example.klost.lolstats;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SummonerProfileViewModel extends ViewModel {

    private LiveData<List<MatchStatsEntry>> entries;
    private LiveData<SummonerEntry> summonerEntryLiveData;

    public SummonerProfileViewModel(LoLStatsRepository repository, int summonerId){
        entries = repository.getMatchesFromSummoner(summonerId);
        summonerEntryLiveData = repository.getSummonerById(summonerId);
    }

    public LiveData<List<MatchStatsEntry>> getEntries(){
        return entries;
    }

    public LiveData<SummonerEntry> getSummonerEntryLiveData(){
        return summonerEntryLiveData;
    }

}
