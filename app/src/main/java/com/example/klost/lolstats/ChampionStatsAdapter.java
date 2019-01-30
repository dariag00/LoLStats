package com.example.klost.lolstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.utilities.LoLStatsUtils;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChampionStatsAdapter  extends RecyclerView.Adapter<ChampionStatsAdapter.ChampionViewHolder>  {

    private Context context;
    private List<ChampionStats> list;
    final private ItemClickListener itemClickListener;

    public ChampionStatsAdapter(Context context, ItemClickListener listener){
        this.context = context;
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClickListener(ChampionStats championStats);
    }


    @NonNull
    @Override
    public ChampionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.champion_info_item, viewGroup, false);
        return new ChampionStatsAdapter.ChampionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChampionViewHolder holder, int position) {
        ChampionStats stats = list.get(position);
        Champion champion = stats.getChampion();
        champion.loadImageFromDDragon(holder.championIconView);
        holder.itemCountView.setText("#" + String.valueOf(position + 1));
        holder.winsView.setText(String.valueOf(stats.getNumberOfGamesWon()));
        holder.losesView.setText(String.valueOf(stats.getNumberOfGamesPlayed()-stats.getNumberOfGamesWon()));
        //holder.totalCsView.setText("CS: " + String.valueOf(stats.getTotalCs()));
        holder.totalCsView.setText("CS: " + String.format(Locale.ENGLISH, "%.1f", stats.getMeanTotalCs()));
        holder.killsView.setText(String.format(Locale.ENGLISH, "%.0f", stats.getMeanKills()));
        holder.deathsView.setText(String.format(Locale.ENGLISH, "%.0f",stats.getMeanDeaths()));
        holder.assistsView.setText(String.format(Locale.ENGLISH, "%.0f",stats.getMeanAssists()));
        //TODO meter en stats
        double winRate = ((double) stats.getNumberOfGamesWon() / (double) stats.getNumberOfGamesPlayed()) * 100;
        LoLStatsUtils.setWinRateAndTextColorInView(winRate, context, holder.winRateView);
        //holder.winRateView.setText(String.format(Locale.ENGLISH, "%.0f", winRate).concat("%"));

        double kda = LoLStatsUtils.calculateKDA(stats.getMeanKills(), stats.getMeanAssists(), stats.getMeanDeaths());
        LoLStatsUtils.setKdaAndTextColorInView(holder.kdaView, kda, context);

        holder.csMinView.setText(String.format(Locale.ENGLISH, "%.1f", stats.calculateCsPerMin()).concat("/min"));

    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public void setEntries(List<ChampionStats> championStats){
        this.list = championStats;
        notifyDataSetChanged();
    }

    class ChampionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView championIconView;
        TextView itemCountView;
        TextView winsView;
        TextView losesView;
        TextView winRateView;
        TextView kdaView;
        TextView killsView;
        TextView deathsView;
        TextView assistsView;
        TextView totalCsView;
        TextView csMinView;


        public ChampionViewHolder(@NonNull View itemView) {
            super(itemView);

            championIconView = itemView.findViewById(R.id.iv_champion_icon);
            itemCountView = itemView.findViewById(R.id.tv_list_number);
            winsView = itemView.findViewById(R.id.tv_wins);
            losesView = itemView.findViewById(R.id.tv_losses);
            winRateView = itemView.findViewById(R.id.tv_win_rate);
            kdaView = itemView.findViewById(R.id.tv_kda);
            killsView = itemView.findViewById(R.id.tv_kills);
            deathsView = itemView.findViewById(R.id.tv_deaths);
            assistsView = itemView.findViewById(R.id.tv_assists);
            totalCsView = itemView.findViewById(R.id.tv_total_cs);
            csMinView = itemView.findViewById(R.id.tv_cs_min);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ChampionStats stats = list.get(getAdapterPosition());
            itemClickListener.onItemClickListener(stats);
        }
    }
}
