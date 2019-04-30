package com.example.klost.lolstats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.klost.lolstats.activities.ChampionDetailsActivity;
import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.models.champions.ChampionStatsList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentFavProfileChampionsStats extends Fragment implements ChampionStatsAdapter.ItemClickListener{

    private RecyclerView recyclerView;
    private ChampionStatsAdapter adapter;
    private static final String LOG_TAG = FragmentFavProfileChampionsStats.class.getSimpleName();
    private final String SAVED_ENTRY = "SAVED_ENTRY_ID";
    private final String SAVED_CHAMP = "CHAMPION_ID";
    private int entryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fav_profile_champions, container, false);

        entryId = this.getArguments().getInt(SAVED_ENTRY);
        Log.d(LOG_TAG, "ENTRY ID: " + entryId);
        recyclerView = view.findViewById(R.id.champions_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new ChampionStatsAdapter(this.getContext(), this);
        recyclerView.setAdapter(adapter);

        LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getActivity().getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId, 0);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> matchStatsEntries) {

                ChampionStatsList list = generateChampionStats(matchStatsEntries);
                List<ChampionStats> championStatsList = list.getChampionStatsList();
                //Ordenamos la lista por numero de partidos jugados
                Collections.sort(championStatsList, new Comparator<ChampionStats>() {
                    @Override
                    public int compare(ChampionStats o1, ChampionStats o2) {
                        if (o1.getNumberOfGamesPlayed() < o2.getNumberOfGamesPlayed()){
                            return 1;
                        }else if(o1.getNumberOfGamesPlayed() > o2.getNumberOfGamesPlayed()) {
                            return -1;
                        }else{
                            if(o1.getNumberOfGamesWon() < o2.getNumberOfGamesWon())
                                return 1;
                            if(o1.getNumberOfGamesWon() > o2.getNumberOfGamesWon())
                                return -1;
                            return 0;
                        }
                    }
                });
                adapter.setEntries(championStatsList);

            }
        });
        return view;
    }

    public ChampionStatsList generateChampionStats(List<MatchStatsEntry> entries){

        ChampionStatsList championStatsList = new ChampionStatsList();

        for(MatchStatsEntry statsEntry : entries){
            Champion champion = statsEntry.getPlayedChampion();
            Log.d(LOG_TAG, "CsDif: " + statsEntry.getCsDiffAt10());
            if(championStatsList.containsChampion(champion)){
                Log.d(LOG_TAG, "Entro en stats de: " + champion.getName());
                ChampionStats currentStats = championStatsList.getChampionStats(champion);
                currentStats.addNewStat(statsEntry);
            }else{
                Log.d(LOG_TAG, "Nueva stats de: " + champion.getName());
                ChampionStats newChampionStats = new ChampionStats(statsEntry);
                championStatsList.addChampionStats(newChampionStats);
            }
        }

        return championStatsList;
    }

    @Override
    public void onItemClickListener(ChampionStats championStats) {
        Context context = this.getContext();
        Toast.makeText(context, "Clicked " + championStats.getChampion().getName(), Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this.getActivity(), ChampionDetailsActivity.class);
        intent.putExtra(SAVED_ENTRY, entryId);
        intent.putExtra(SAVED_CHAMP, championStats.getChampion().getChampionId());
        startActivity(intent);
    }
}
