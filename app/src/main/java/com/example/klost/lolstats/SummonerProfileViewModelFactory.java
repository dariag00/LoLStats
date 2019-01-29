package com.example.klost.lolstats;

import com.example.klost.lolstats.data.LoLStatsRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SummonerProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int summonerId;
    private final LoLStatsRepository repository;

    public SummonerProfileViewModelFactory(LoLStatsRepository repository, int summonerId){
        this.repository = repository;
        this.summonerId = summonerId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SummonerProfileViewModel(repository, summonerId);
    }

}
