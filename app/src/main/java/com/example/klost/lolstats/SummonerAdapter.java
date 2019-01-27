package com.example.klost.lolstats;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.klost.lolstats.data.database.SummonerEntry;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SummonerAdapter extends RecyclerView.Adapter<SummonerAdapter.SummonerViewHolder> {

    private List<SummonerEntry> summonerEntries;
    private Context context;
    final private ItemClickListener itemClickListener;

    public SummonerAdapter(Context context, ItemClickListener listener){
        this.context = context;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public SummonerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_summoner_item, viewGroup, false);
        return new SummonerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummonerViewHolder summonerViewHolder, int position) {
        SummonerEntry summonerEntry = summonerEntries.get(position);
        String summonerName = summonerEntry.getSummonerName();
        long summonerLevel = summonerEntry.getSummonerLevel();
        String rank = summonerEntry.getRank();
        String tier = summonerEntry.getTier();
        int leaguePoints = summonerEntry.getLeaguePoints();
        int winRate = summonerEntry.getWinRate();
        //TODO icono de perfil y de division
        summonerViewHolder.summonerNameView.setText(summonerName);

        String summonerLevelString = "Level " + summonerLevel;
        summonerViewHolder.summonerLevelView.setText(summonerLevelString);

        String rankString = rank + " " + tier;
        summonerViewHolder.rankView.setText(rankString);

        String winRateString = String.valueOf(winRate) + "%";
        summonerViewHolder.winRateView.setText(winRateString);

        summonerViewHolder.leaguePointsView.setText(String.valueOf(leaguePoints));

        summonerEntry.loadImageFromDDragon(summonerViewHolder.profileIconView);
    }

    @Override
    public int getItemCount() {
        if(summonerEntries == null){
            return 0;
        }
        return summonerEntries.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(String accountId);
    }

    public void setSummonerEntries(List<SummonerEntry> summonerEntries){
        this.summonerEntries = summonerEntries;
        notifyDataSetChanged();
    }

    public List<SummonerEntry> getSummonerEntries(){
        return summonerEntries;
    }

    class SummonerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView profileIconView;
        TextView summonerNameView;
        TextView summonerLevelView;
        CircleImageView rankIconView;
        TextView rankView;
        TextView winRateView;
        TextView leaguePointsView;

        public SummonerViewHolder(View itemView){
            super(itemView);

            profileIconView = itemView.findViewById(R.id.iv_summoner_icon);
            summonerNameView = itemView.findViewById(R.id.tv_summoner_name);
            summonerLevelView = itemView.findViewById(R.id.tv_summoner_level);
            rankIconView = itemView.findViewById(R.id.iv_summoner_division);
            rankView = itemView.findViewById(R.id.tv_summoner_rank);
            winRateView = itemView.findViewById(R.id.tv_summoner_winrate);
            leaguePointsView = itemView.findViewById(R.id.tv_summoner_league_points);
        }

        //TODO implementar
        @Override
        public void onClick(View v) {
            String elementId = summonerEntries.get(getAdapterPosition()).getAccoundId();
            itemClickListener.onItemClickListener(elementId);
        }
    }
}
