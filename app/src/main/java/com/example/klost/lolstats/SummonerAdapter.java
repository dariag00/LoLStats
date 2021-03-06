package com.example.klost.lolstats;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.klost.lolstats.data.database.SummonerEntry;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.utilities.LoLStatsUtils;

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

        summonerViewHolder.summonerNameView.setText(summonerName);
        summonerViewHolder.summonerLevelView.setText(String.valueOf(summonerLevel));
        Log.d("Test", "AQUI: " + summonerEntry.getProfileIconId());
        summonerEntry.loadImageFromDDragon(summonerViewHolder.profileIconView);

        if(summonerEntry.getSoloQ() == null){
            showNoRankedData(summonerViewHolder);
            summonerViewHolder.rankView.setText("Unranked");
            summonerViewHolder.rankIconView.setImageResource(R.drawable.unranked);
        }else{
            showRankedData(summonerViewHolder);
            int leaguePoints = summonerEntry.getSoloQ().getLeaguePoints();
            double winRate = (double) summonerEntry.getSoloQ().getWins() / ((double) summonerEntry.getSoloQ().getWins() + summonerEntry.getSoloQ().getLosses()) * 100;
            String rankString = summonerEntry.getSoloQ().getTier() + " " + summonerEntry.getSoloQ().getRank();
            summonerViewHolder.rankView.setText(rankString);
            String winRateString = String.valueOf(Math.round(winRate)) + "%";
            summonerViewHolder.winRateView.setText(winRateString);
            summonerViewHolder.leaguePointsView.setText(String.valueOf(leaguePoints).concat(" lps"));
            //TODO fix
            LeaguePosition leaguePosition = new LeaguePosition();
            leaguePosition.setTier(summonerEntry.getSoloQ().getTier());
            leaguePosition.setLeagueIconOnImageView(summonerViewHolder.rankIconView);
        }
    }

    @Override
    public int getItemCount() {
        if(summonerEntries == null){
            return 0;
        }
        return summonerEntries.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int id, String accountId);
    }

    public void setSummonerEntries(List<SummonerEntry> summonerEntries){
        this.summonerEntries = summonerEntries;
        notifyDataSetChanged();
    }

    private void showRankedData(SummonerViewHolder summonerViewHolder){
        summonerViewHolder.noRankedDataView.setVisibility(View.INVISIBLE);
        summonerViewHolder.rankedDataContainer.setVisibility(View.VISIBLE);
    }

    private void showNoRankedData(SummonerViewHolder summonerViewHolder) {
        summonerViewHolder.noRankedDataView.setVisibility(View.VISIBLE);
        summonerViewHolder.rankedDataContainer.setVisibility(View.INVISIBLE);
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
        LinearLayout rankedDataContainer;
        TextView noRankedDataView;

        SummonerViewHolder(View itemView){
            super(itemView);

            profileIconView = itemView.findViewById(R.id.iv_summoner_icon);
            summonerNameView = itemView.findViewById(R.id.tv_summoner_name);
            summonerLevelView = itemView.findViewById(R.id.tv_summoner_level);
            rankIconView = itemView.findViewById(R.id.iv_summoner_division);
            rankView = itemView.findViewById(R.id.tv_summoner_rank);
            winRateView = itemView.findViewById(R.id.tv_summoner_winrate);
            leaguePointsView = itemView.findViewById(R.id.tv_summoner_league_points);
            rankedDataContainer = itemView.findViewById(R.id.ranked_data_container);
            noRankedDataView = itemView.findViewById(R.id.tv_ranked_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = summonerEntries.get(getAdapterPosition()).getId();
            String accountId = summonerEntries.get(getAdapterPosition()).getAccoundId();
            itemClickListener.onItemClickListener(elementId, accountId);
        }
    }
}
