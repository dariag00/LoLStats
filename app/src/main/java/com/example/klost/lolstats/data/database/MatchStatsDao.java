package com.example.klost.lolstats.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MatchStatsDao {

    @Query("SELECT * FROM match_stats")
    LiveData<List<MatchStatsEntry>> loadAllMatches();

    @Query("SELECT * FROM match_stats WHERE summonerId=:id")
    LiveData<List<MatchStatsEntry>> loadMatchesForSummoner(final int id);

    @Query("SELECT * FROM match_stats WHERE championId=:id")
    LiveData<List<MatchStatsEntry>> loadMatchesForChampion(final int id);

    @Insert
    void insertMatchStats(MatchStatsEntry matchStatsEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMatchStats(MatchStatsEntry matchStatsEntry);

    @Delete
    void deleteMatchStats(MatchStatsEntry matchStatsEntry);
}
