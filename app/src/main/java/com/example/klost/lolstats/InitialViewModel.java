package com.example.klost.lolstats;

import android.app.Application;
import android.util.Log;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.SummonerEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class InitialViewModel extends AndroidViewModel {

    private static final String LOG_TAG = InitialViewModel.class.getSimpleName();

    private LiveData<List<SummonerEntry>> summoners;
    private LoLStatsRepository repository;

    public InitialViewModel(Application application){
        super(application);
        //TODO fix this
        AppExecutors executors = AppExecutors.getInstance();
        repository = LoLStatsRepository.getInstance(application, executors);
        Log.d(LOG_TAG, "Actively retrieving the summoners from the DataBase");
        summoners = repository.getSavedSummoners();
    }

    public LiveData<List<SummonerEntry>> getSummoners(){
        return summoners;
    }
}
