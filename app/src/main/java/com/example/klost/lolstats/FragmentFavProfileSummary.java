package com.example.klost.lolstats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.data.LoLStatsRepository;
import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.models.champions.ChampionStatsList;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.utilities.StaticData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FragmentFavProfileSummary extends Fragment {

    private static final String LOG_TAG = FragmentFavProfileSummary.class.getSimpleName();
    private final String SAVED_ENTRY = "SAVED_ENTRY_ID";
    ChampionStatsList list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fav_profile_summary, container, false);

        int entryId = this.getArguments().getInt(SAVED_ENTRY);
        LoLStatsRepository repository = LoLStatsRepository.getInstance(this.getActivity().getApplication(), AppExecutors.getInstance());
        SummonerProfileViewModelFactory factory = new SummonerProfileViewModelFactory(repository, entryId);
        final SummonerProfileViewModel viewModel = ViewModelProviders.of(this, factory).get(SummonerProfileViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MatchStatsEntry>>() {
            @Override
            public void onChanged(List<MatchStatsEntry> matchStatsEntries) {
                if (matchStatsEntries.size() > 0) {
                    list = generateChampionStats(matchStatsEntries);
                    List<ChampionStats> championStatsList = list.getChampionStatsList();
                    //Ordenamos la lista por numero de partidos jugados
                    Collections.sort(championStatsList, new Comparator<ChampionStats>() {
                        @Override
                        public int compare(ChampionStats o1, ChampionStats o2) {
                            if (o1.getNumberOfGamesPlayed() < o2.getNumberOfGamesPlayed()) {
                                return 1;
                            } else if (o1.getNumberOfGamesPlayed() > o2.getNumberOfGamesPlayed()) {
                                return -1;
                            } else {
                                if (o1.getNumberOfGamesWon() < o2.getNumberOfGamesWon())
                                    return 1;
                                if (o1.getNumberOfGamesWon() > o2.getNumberOfGamesWon())
                                    return -1;
                                return 0;
                            }
                        }
                    });
                    ImageView splashArt = view.findViewById(R.id.iv_splash_art);
                    Champion champion = list.getChampionStatsList().get(0).getChampion();
                    champion.loadSplashArtFromDDragon(splashArt);
                }
            }

        });
        viewModel.getSummonerEntryLiveData().observe(this, new Observer<SummonerEntry>() {
            @Override
            public void onChanged(SummonerEntry entry) {
                ImageView profileIcon = view.findViewById(R.id.iv_profile_icon);
                TextView summonerLevel = view.findViewById(R.id.tv_summoner_level);
                TextView summonerName = view.findViewById(R.id.tv_summoner_name);
                summonerLevel.setText(String.valueOf(entry.getSummonerLevel()));
                summonerName.setText(String.valueOf(entry.getSummonerName()));
                entry.loadImageFromDDragon(profileIcon);
                if(list != null){
                }else{
                    Log.d(LOG_TAG, "list es null");
                }

                LeaguePosition soloQ = entry.getSoloQ();
                LeaguePosition flexQ = entry.getFlexQ();
                LeaguePosition flexQTT = entry.getFlexQTT();


                ImageView soloQIcon = view.findViewById(R.id.iv_rank_icon_solo);
                ImageView flexQIcon = view.findViewById(R.id.iv_rank_icon_flex);
                ImageView flexQTTIcon = view.findViewById(R.id.iv_rank_icon_3v3);

                TextView winsSoloQView =  view.findViewById(R.id.tv_solo_wins);
                TextView lossesSoloQView =  view.findViewById(R.id.tv_solo_losses);
                TextView winRateSoloQView =  view.findViewById(R.id.tv_wr_soloq);
                TextView rankSoloQView =  view.findViewById(R.id.tv_summoner_rank_solo);
                TextView lpsSoloQView =  view.findViewById(R.id.tv_summoner_rank_lps_solo);

                if(soloQ != null){
                    soloQ.setLeagueIconOnImageView(soloQIcon);
                    double winRateSoloQ = (double) soloQ.getWins() / ((double) soloQ.getWins() + soloQ.getLosses()) * 100;
                    String wRSQ = String.format(Locale.ENGLISH, "%.1f", winRateSoloQ);
                    wRSQ = wRSQ.concat("%");

                    winsSoloQView.setText(String.valueOf(soloQ.getWins()));
                    lossesSoloQView.setText(String.valueOf(soloQ.getLosses()));
                    winRateSoloQView.setText(wRSQ);
                    rankSoloQView.setText(soloQ.getTier() + " " + soloQ.getRank());
                    lpsSoloQView.setText(String.valueOf(soloQ.getLeaguePoints())+ " lps");
                }else{
                    soloQIcon.setImageResource(R.drawable.unranked);
                    rankSoloQView.setText("Unranked");
                }

                TextView winsFlexQView =  view.findViewById(R.id.tv_flex_wins);
                TextView lossesFlexQView =  view.findViewById(R.id.tv_flex_losses);
                TextView winRateFlexQView =  view.findViewById(R.id.tv_wr_flexq);
                TextView rankFlexQView =  view.findViewById(R.id.tv_summoner_rank_flex);
                TextView lpsFlexQView =  view.findViewById(R.id.tv_summoner_rank_lps_flex);

                if(flexQ != null){
                    flexQ.setLeagueIconOnImageView(flexQIcon);
                    double winRateFlexQ = (double) flexQ.getWins() / ((double) flexQ.getWins() + flexQ.getLosses()) * 100;
                    String wRFQ = String.format(Locale.ENGLISH, "%.1f", winRateFlexQ).concat("%");

                    winsFlexQView.setText(String.valueOf(flexQ.getWins()));
                    lossesFlexQView.setText(String.valueOf(flexQ.getLosses()));
                    winRateFlexQView.setText(wRFQ);
                    rankFlexQView.setText(flexQ.getTier() + " " + flexQ.getRank());
                    lpsFlexQView.setText(String.valueOf(flexQ.getLeaguePoints())+ " lps");
                }else{
                    flexQIcon.setImageResource(R.drawable.unranked);
                    rankFlexQView.setText("Unranked");
                }

                TextView winsFlexTTQView = view.findViewById(R.id.tv_3v3_wins);
                TextView lossesFlexTTQView = view.findViewById(R.id.tv_3v3_losses);
                TextView winRate3v3View = view.findViewById(R.id.tv_wr_3v3);
                TextView rank3v3View = view.findViewById(R.id.tv_summoner_rank_3v3);
                TextView lps3v3View = view.findViewById(R.id.tv_summoner_rank_lps_3v3);

                if(flexQTT != null){
                    flexQTT.setLeagueIconOnImageView(flexQTTIcon);
                    double winRate3v3 = (double) flexQTT.getWins() / ((double) flexQTT.getWins() + flexQTT.getLosses()) * 100;
                    String wRFTTQ = String.format(Locale.ENGLISH, "%.1f", winRate3v3).concat("%");

                    winsFlexTTQView.setText(String.valueOf(flexQTT.getWins()));
                    lossesFlexTTQView.setText(String.valueOf(flexQTT.getLosses()));
                    winRate3v3View.setText(wRFTTQ);
                    rank3v3View.setText(flexQTT.getTier() + " " + flexQTT.getRank());
                    lps3v3View.setText(String.valueOf(flexQTT.getLeaguePoints()) + " lps");
                }else{
                    flexQTTIcon.setImageResource(R.drawable.unranked);
                    rank3v3View.setText("Unranked");
                }
            }
        });
        return view;
    }
    ///TODO meter en championstatslist
    public ChampionStatsList generateChampionStats(List<MatchStatsEntry> entries){

        ChampionStatsList championStatsList = new ChampionStatsList();

        for(MatchStatsEntry statsEntry : entries){
            Champion champion = StaticData.getChampionList().getChampionById(statsEntry.getChampionId());
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

}
