package com.example.klost.lolstats;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.runes.Rune;
import com.example.klost.lolstats.models.runes.RunePath;
import com.example.klost.lolstats.models.summoners.SummonerSpell;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.example.klost.lolstats.utilities.StaticData;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveGameAdapter extends RecyclerView.Adapter<LiveGameAdapter.LiveGameAdapterViewHolder>{

    private final ItemClickListener itemClickListener;
    private final Context context;
    private Match match;
    private Summoner summoner;
    private List<Player> players;

    public LiveGameAdapter(Context context, ItemClickListener listener){
        this.itemClickListener = listener;
        this.context = context;
    }

    public interface ItemClickListener {
        void onItemClickListener();
    }

    @NonNull
    @Override
    public LiveGameAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_game_item, parent, false);
        return new LiveGameAdapter.LiveGameAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveGameAdapterViewHolder holder, int position) {

        Player player = players.get(position);
        Summoner currentSummoner = player.getSummoner();
        //Miramos si el player es nuestro summoner
        Log.d("DetailAdapter", "SUMM: " + summoner.getEncryptedAccountId() +" otro id: " + summoner.getEncryptedSummonerId() + " y comp: " + player.getSummoner().getEncryptedSummonerId());
        if(summoner.getEncryptedSummonerId().equals(currentSummoner.getEncryptedSummonerId())){
            System.out.println("entroas");
            holder.playerContainer.setBackgroundColor(Color.parseColor("#d6ffe7"));
        }

        holder.summonerNameView.setText(player.getSummoner().getSummonerName());
       // holder.summonerLevelView.setText(String.valueOf(player.getSummoner().getSummonerLevel()));


        //Setteo de las imagenes iniciales
        Champion champion = player.getChampion();
        Log.d("LOG", "ChampionID: " + champion.getChampionId() + " y champ " + champion);
        champion.loadImageFromDDragon(holder.championView);
        holder.championNameView.setText(champion.getName());

        SummonerSpell firstSummonerSpell = StaticData.getSpellList().getSpellById(player.getSpell1Id());

        if(firstSummonerSpell != null)
            firstSummonerSpell.loadImageFromDDragon(holder.mainSummonerView);
        SummonerSpell secondSummonerSpell = StaticData.getSpellList().getSpellById(player.getSpell2Id());
        if(secondSummonerSpell != null)
            secondSummonerSpell.loadImageFromDDragon(holder.secondaySummonerView);

        Rune mainRune = StaticData.getRuneList().getRuneById(player.getRune0());
        mainRune.loadImageFromDDragon(holder.keystoneView);
        RunePath secondaryRune = StaticData.getRuneList().getRunePathById(player.getRuneSecondaryStyle());
        secondaryRune.loadImageFromDDragon(holder.secondaryPathView);
        LeaguePosition leaguePosition = null;
        if(player.getSummoner().getPositionList() != null)
            leaguePosition = player.getSummoner().getPositionList().getHighestRankingPosition();
        if(leaguePosition == null){
            holder.rankIconView.setImageResource(R.drawable.unranked);
            holder.summonerRankView.setText("Unranked");
        }else {
            leaguePosition.setLeagueMiniIconOnImageView(holder.rankIconView);
            holder.summonerRankView.setText(leaguePosition.getRankAndTier());
            double winRate = leaguePosition.getWins() / (double) (leaguePosition.getWins() + leaguePosition.getLosses()) * 100;
            holder.winRateView.setText("WR: " + String.format(Locale.ENGLISH, "%.1f", winRate).concat("%"));
        }

    }

    public void setData(Summoner summoner, Match match, List<Player> players) {
        this.summoner = summoner;
        this.match = match;
        this.players = players;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(match != null){
            return match.getBlueTeam().getPlayers().size();
        }
        return 0;
    }

    public class LiveGameAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final CircleImageView championView;
        final ImageView mainSummonerView, secondaySummonerView, keystoneView, secondaryPathView, rankIconView;
        final TextView summonerNameView, championNameView, summonerRankView, winRateView;
        final LinearLayout playerContainer;

        LiveGameAdapterViewHolder(View view){
            super(view);
            championView = view.findViewById(R.id.iv_player_champion);
            mainSummonerView = view.findViewById(R.id.iv_player_first_summoner);
            secondaySummonerView = view.findViewById(R.id.iv_player_second_summoner);
            keystoneView = view.findViewById(R.id.iv_player_keystone);
            secondaryPathView = view.findViewById(R.id.iv_player_secondary_rune);
            summonerNameView = view.findViewById(R.id.tv_summoner_name);
            championNameView = view.findViewById(R.id.tv_champion_name);
            //summonerLevelView = view.findViewById(R.id.tv_summoner_level);
            summonerRankView = view.findViewById(R.id.tv_rank);
            winRateView = view.findViewById(R.id.tv_win_rate);
            rankIconView = view.findViewById(R.id.iv_rank_icon);
            playerContainer = view.findViewById(R.id.item_container);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            itemClickListener.onItemClickListener();
        }
    }

}
