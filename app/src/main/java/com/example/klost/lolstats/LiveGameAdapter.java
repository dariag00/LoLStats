package com.example.klost.lolstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.models.matches.Match;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LiveGameAdapter extends RecyclerView.Adapter<LiveGameAdapter.LiveGameAdapterViewHolder>{

    private final ItemClickListener itemClickListener;
    private final Context context;
    private Match match;

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
        final TextView summonerNameView, championNameView, summonerLevelView, summonerRankView, winRateView;

        LiveGameAdapterViewHolder(View view){
            super(view);
            championView = view.findViewById(R.id.iv_player_champion);
            mainSummonerView = view.findViewById(R.id.iv_first_summoner_spell);
            secondaySummonerView = view.findViewById(R.id.iv_second_summoner_spell);
            keystoneView = view.findViewById(R.id.iv_player_keystone);
            secondaryPathView = view.findViewById(R.id.iv_secondary_rune_path);
            summonerNameView = view.findViewById(R.id.tv_summoner_name);
            championNameView = view.findViewById(R.id.tv_champion_name);
            summonerLevelView = view.findViewById(R.id.tv_summoner_level);
            summonerRankView = view.findViewById(R.id.tv_rank);
            winRateView = view.findViewById(R.id.tv_win_rate);
            rankIconView = view.findViewById(R.id.iv_rank_icon);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            itemClickListener.onItemClickListener();
        }
    }

}
