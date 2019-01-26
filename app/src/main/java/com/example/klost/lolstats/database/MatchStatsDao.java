package com.example.klost.lolstats.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MatchStatsDao {

    @Query("SELECT * FROM match_stats")
    List<MatchStatsEntry> loadAllMatches();

    @Insert
    void insertMatchStats(MatchStatsEntry matchStatsEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMatchStats(MatchStatsEntry matchStatsEntry);

    @Delete
    void deleteMatchStats(MatchStatsEntry matchStatsEntry);
}
