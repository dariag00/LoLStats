package com.example.klost.lolstats.data.database;

import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SummonerDao {

    @Query("SELECT * FROM favourite_summoner")
    LiveData<List<SummonerEntry>> loadAllSummoners();

    @Insert
    void insertSummoner(SummonerEntry summonerEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSummoner(SummonerEntry summonerEntry);

    @Delete
    void deleteSummoner(SummonerEntry summonerEntry);



}
