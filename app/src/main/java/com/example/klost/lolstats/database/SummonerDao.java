package com.example.klost.lolstats.database;

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
    List<SummonerEntry> loadAllSummoners();

    @Insert
    void insertMatchStats(SummonerEntry summonerEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMatchStats(SummonerEntry summonerEntry);

    @Delete
    void deleteMatchStats(SummonerEntry summonerEntry);

}
