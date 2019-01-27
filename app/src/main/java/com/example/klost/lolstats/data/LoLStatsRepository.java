package com.example.klost.lolstats.data;

import androidx.lifecycle.LiveData;

import com.example.klost.lolstats.AppExecutors;
import com.example.klost.lolstats.data.database.MatchStatsDao;
import com.example.klost.lolstats.data.database.SummonerDao;

public class LoLStatsRepository {

    private static final String LOG_TAG = LoLStatsRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static LoLStatsRepository instance;
    private final SummonerDao summonerDao;
    private final MatchStatsDao matchStatsDao;
    private boolean initialized = false;
    private final AppExecutors executors;

    public LoLStatsRepository(SummonerDao summonerDao, MatchStatsDao matchStatsDao, AppExecutors executors){
        this.summonerDao = summonerDao;
        this.matchStatsDao = matchStatsDao;
        this.executors = executors;
    }

    /*public synchronized static LoLStatsRepository getInstance(){

    }

    /**
     *    Database related operations
     **/
    /*public LiveData<List<SummonerEntry>> getSavedSummoners(){

    }

    public LiveData<SummonerEntry> getSummonerById(){

    }*/

}
