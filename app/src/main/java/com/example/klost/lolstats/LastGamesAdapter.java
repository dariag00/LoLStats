package com.example.klost.lolstats;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.models.champions.ChampionStatsList;
import com.example.klost.lolstats.utilities.LoLStatsUtils;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LastGamesAdapter extends RecyclerView.Adapter<LastGamesAdapter.LastGamesAdapterViewHolder> {

    private ChampionStatsList championStatsList;
    private final Context context;
    private final ItemClickListener itemClickListener;

    public LastGamesAdapter(Context context ,ItemClickListener listener){
        this.itemClickListener = listener;
        this.context = context;
    }

    public interface ItemClickListener{
        void onItemClickListener(String championName, long championId);
    }

    @NonNull
    @Override
    public LastGamesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.last_played_item, parent, false);
        return new LastGamesAdapter.LastGamesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastGamesAdapterViewHolder holder, int position) {
        ChampionStats championStats = championStatsList.getChampionStatsList().get(position);
        Champion champion = championStats.getChampion();
        Log.d("Last Game", "Champion " + champion.getName());
        champion.loadImageFromDDragon(holder.championImageView);
        double kda = LoLStatsUtils.calculateKDA(championStats.getMeanKills(), championStats.getMeanAssists(), championStats.getMeanDeaths());
        LoLStatsUtils.setKdaAndTextColorInView(holder.kdaView, kda, context);
        holder.csView.setText(String.format(Locale.ENGLISH, "%.1f", championStats.getMeanTotalCs()).concat(" cs"));
        String gamesPlayed = championStats.getNumberOfGamesPlayed() + "G";
        holder.numOfGamesPlayedView.setText(gamesPlayed);
        float winRate = championStats.getNumberOfGamesWon()/(float)championStats.getNumberOfGamesPlayed();
        ViewGroup.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, winRate);
        holder.progressDoneView.setLayoutParams(params);
        params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1-winRate);
        holder.progressToDoView.setLayoutParams(params);

        if(winRate >0.1){
            holder.progressDoneView.setText(String.format(Locale.ENGLISH, "%.0f", winRate*100).concat("%"));
        }else{
            holder.progressToDoView.setText(String.format(Locale.ENGLISH, "%.0f", winRate*100).concat("%"));
        }

    }

    @Override
    public int getItemCount() {

        if(championStatsList != null)
            return championStatsList.getChampionStatsList().size();

        return 0;
    }

    public void setData(ChampionStatsList championStatsList){
        this.championStatsList = championStatsList;
        notifyDataSetChanged();
    }

    class LastGamesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView championImageView;
        TextView kdaView, csView, numOfGamesPlayedView, progressDoneView, progressToDoView;

        LastGamesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            championImageView = itemView.findViewById(R.id.iv_champion_image);
            kdaView = itemView.findViewById(R.id.tv_champion_kda);
            csView = itemView.findViewById(R.id.tv_champion_cs);
            progressDoneView = itemView.findViewById(R.id.progress_done);
            progressToDoView = itemView.findViewById(R.id.progress_todo);
            numOfGamesPlayedView = itemView.findViewById(R.id.tv_champion_games_played);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String championName = championStatsList.getChampionStatsList().get(getAdapterPosition()).getChampion().getName();
            long championId = championStatsList.getChampionStatsList().get(getAdapterPosition()).getChampion().getChampionId();
            itemClickListener.onItemClickListener(championName, championId);
        }
    }

}
