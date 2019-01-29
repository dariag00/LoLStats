package com.example.klost.lolstats.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.klost.lolstats.AppExecutors;
import com.example.klost.lolstats.data.database.AppDatabase;
import com.example.klost.lolstats.data.database.MatchStatsDao;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerDao;
import com.example.klost.lolstats.data.database.SummonerEntry;

import java.util.List;

public class LoLStatsRepository {

    private static final String LOG_TAG = LoLStatsRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static LoLStatsRepository instance;
    private final SummonerDao summonerDao;
    private final MatchStatsDao matchStatsDao;
    private boolean initialized = false;
    private final AppExecutors executors;

    public LoLStatsRepository(Application application, AppExecutors executors){
        AppDatabase appDatabase = AppDatabase.getInstance(application);

        this.summonerDao = appDatabase.summonerDao();
        this.matchStatsDao = appDatabase.matchStatsDao();
        this.executors = executors;
    }

    public synchronized static LoLStatsRepository getInstance(Application application, AppExecutors executors){
        Log.d(LOG_TAG, "Getting the repository");
        if(instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Made new repository");
                instance = new LoLStatsRepository(application, executors);
            }
        }
        return instance;
    }

    /**
     *    Database related operations
     **/
    public LiveData<List<SummonerEntry>> getSavedSummoners(){
        return summonerDao.loadAllSummoners();
    }

    public LiveData<SummonerEntry> getSummonerById(int id){
        return summonerDao.loadSummonerById(id);
    }

    public void addSummonerEntry(final SummonerEntry entry){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                summonerDao.insertSummoner(entry);
            }
        });
    }

    public void addMatchStatsEntry(final MatchStatsEntry entry){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                matchStatsDao.insertMatchStats(entry);
            }
        });
    }

    public LiveData<List<MatchStatsEntry>> getMatchesStats(){
        return matchStatsDao.loadAllMatches();
    }

    public LiveData<List<MatchStatsEntry>> getMatchesFromSummoner(int id){
        return matchStatsDao.loadMatchesForSummoner(id);
    }

}
