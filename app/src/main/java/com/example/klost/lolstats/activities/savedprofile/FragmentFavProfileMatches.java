package com.example.klost.lolstats.activities.savedprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.klost.lolstats.AppExecutors;
import com.example.klost.lolstats.MatchEntryListAdapter;
import com.example.klost.lolstats.R;
import com.example.klost.lolstats.SummonerProfileViewModel;
import com.example.klost.lolstats.SummonerProfileViewModelFactory;
import com.example.klost.lolstats.activities.GameDetailsActivity;
import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentFavProfileMatches extends Fragment implements MatchEntryListAdapter.ItemClickListener {

    private static final String LOG_TAG = FragmentFavProfileMatches.class.getSimpleName();
    private final String SAVED_ENTRY = "SAVED_ENTRY_ID";
    private RecyclerView recyclerView;
    private MatchEntryListAdapter adapter;
    private int summonerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_profile_matches, container, false);

        final int entryId = this.getArguments().getInt(SAVED_ENTRY);
        Log.d(LOG_TAG, "ENTRY ID: " + entryId);
        recyclerView = view.findViewById(R.id.recyclerview_matches);
        adapter = new MatchEntryListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getActivity().getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId, 0);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> entries) {
                Log.d(LOG_TAG, "Setteo el data " + entries.size());
                if(entries.size() >=1)
                    Log.d(LOG_TAG, "DATA: " + entries.get(0).getMatchId());
                adapter.setData(entries);
                Log.d(LOG_TAG, "COUNT: " + adapter.getItemCount());
            }
        });

        viewModel.getSummonerEntryLiveData().observe(this, new Observer<SummonerEntry>() {
            @Override
            public void onChanged(SummonerEntry entry) {
                Log.d(LOG_TAG, "Entro en Summoner");
                summonerId = entry.getId();
            }
        });

        return view;
    }

    @Override
    public void onItemClickListener(long matchId) {
        Context context = this.getContext();
        Toast.makeText(context, "Clicked " + matchId, Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this.getActivity(), GameDetailsActivity.class);
        intent.putExtra("MatchStatsEntryId", matchId);
        intent.putExtra("summonerEntry", summonerId);
        startActivity(intent);
    }

}
