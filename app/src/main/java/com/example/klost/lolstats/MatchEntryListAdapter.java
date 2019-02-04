package com.example.klost.lolstats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.klost.lolstats.data.database.MatchStatsEntry;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.champions.ChampionStats;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.runes.Rune;
import com.example.klost.lolstats.models.runes.RuneList;
import com.example.klost.lolstats.models.runes.RunePath;
import com.example.klost.lolstats.models.summoners.SummonerSpell;
import com.example.klost.lolstats.models.summoners.SummonerSpellList;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.StaticData;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MatchEntryListAdapter  extends RecyclerView.Adapter<MatchEntryListAdapter.MatchEntryListAdapterViewHolder>{

    private List<MatchStatsEntry> matches;
    final private ItemClickListener itemClickListener;

    public MatchEntryListAdapter(ItemClickListener listener){
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClickListener(long matchId);
    }

    @NonNull
    @Override
    public MatchEntryListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =  parent.getContext();
        int layoutIdForListItem = R.layout.match_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MatchEntryListAdapter.MatchEntryListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchEntryListAdapterViewHolder holder, int position) {

        MatchStatsEntry match = matches.get(position);
        Player player = match.getPlayer();
        Log.d("Adapter", "Entro en " + match.getPlayedChampion().getName());

        ItemList itemList = StaticData.getItemList();
        SummonerSpellList summonerSpellList = StaticData.getSpellList();
        RuneList runeList = StaticData.getRuneList();

        if(match != null) {
            boolean gameWon = match.isVictory();
            if (gameWon) {
                holder.gameResultIndicatorView.setBackgroundResource(R.color.victoryColor);
            } else {
                holder.gameResultIndicatorView.setBackgroundResource(R.color.defeatColor);
            }


            //Seteo de la imagen del campeon jugado
            Champion champion = match.getPlayedChampion();
            champion.loadImageFromDDragon(holder.championImageView);

            //Seteo de los hechizos de invocador jugados
            SummonerSpell firstSummonerSpell = summonerSpellList.getSpellById(player.getSpell1Id());
            firstSummonerSpell.loadImageFromDDragon(holder.firstSummonerSpellIconView);

            SummonerSpell secondSummonerSpell = summonerSpellList.getSpellById(player.getSpell2Id());
            secondSummonerSpell.loadImageFromDDragon(holder.secondSummonerSpellIconView);

            //Seteo de la keystone y de la rama de runas secundaria jugadas
            Rune mainRune = runeList.getRuneById(player.getRune0());
            mainRune.loadImageFromDDragon(holder.mainRuneView);

            RunePath secondaryRune = runeList.getRunePathById(player.getRuneSecondaryStyle());
            secondaryRune.loadImageFromDDragon(holder.secondaryRuneView);

            //Seteo del resultado del jugador
            holder.killsTextView.setText(String.valueOf(match.getKills()));
            holder.deathsTextView.setText(String.valueOf(match.getDeaths()));
            holder.assistsTextView.setText(String.valueOf(match.getAssists()));

            //Seteo de las estadisticas
            int totalCs = match.getTotalCs();
            long totalDamageToChampions = match.getTotalDamage();

            long gameDuration = match.getDuration();
            long gameDurationInMinutes = gameDuration / 60;

            double totalDamagePerMin = (double) totalDamageToChampions / (double) gameDurationInMinutes;
            //TODO bug con los junglas
            double totalCsPerMin = (double) totalCs / (double) gameDurationInMinutes;

            holder.csPerMinView.setText(String.format(Locale.ENGLISH, "%.2f", totalCsPerMin));
            holder.dmgPerMinView.setText(String.format(Locale.ENGLISH, "%.2f", totalDamagePerMin));

            double kda = LoLStatsUtils.calculateKDA(match.getKills(), match.getAssists(), match.getDeaths());
            LoLStatsUtils.setKdaAndTextColorInView(holder.kdaTextView, kda, holder.itemView.getContext());

            //Seteo de los items jugados
            Item firstItem = itemList.getItemById(player.getItem0());
            Item secondItem = itemList.getItemById(player.getItem1());
            Item thirdItem = itemList.getItemById(player.getItem2());
            Item fourthItem = itemList.getItemById(player.getItem3());
            Item fifthItem = itemList.getItemById(player.getItem4());
            Item sixthItem = itemList.getItemById(player.getItem5());
            Item seventhItem = itemList.getItemById(player.getItem6());

            if (firstItem != null)
                firstItem.loadImageFromDDragon(holder.firstItemView);
            if (secondItem != null)
                secondItem.loadImageFromDDragon(holder.secondItemView);
            if (thirdItem != null)
                thirdItem.loadImageFromDDragon(holder.thirdItemView);
            if (fourthItem != null)
                fourthItem.loadImageFromDDragon(holder.fourthItemView);
            if (fifthItem != null)
                fifthItem.loadImageFromDDragon(holder.fifthItemView);
            if (sixthItem != null)
                sixthItem.loadImageFromDDragon(holder.sixthItemView);
            if (seventhItem != null)
                seventhItem.loadImageFromDDragon(holder.trinketItemView);

            //Seteo del resto de datos
            //holder.gameDurationTextView.setText(match.getGameDurationInMinutesAndSeconds());
            holder.gameDateView.setText(LoLStatsUtils.getDaysAgo(match.getGameDate()));
            holder.queueTypeView.setText(LoLStatsUtils.getQueueName(420));
        }
    }

    public void setData(List<MatchStatsEntry> entries){
        this.matches = entries;
    }

    @Override
    public int getItemCount() {
        if(matches != null){
            return matches.size();
        }
        return 0;
    }


    public class MatchEntryListAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        final LinearLayout matchLinearLayout;

        final CircleImageView championImageView;

        final ImageView firstSummonerSpellIconView;
        final ImageView secondSummonerSpellIconView;

        final ImageView mainRuneView;
        final ImageView secondaryRuneView;

        final TextView killsTextView;
        final TextView deathsTextView;
        final TextView assistsTextView;
        final TextView kdaTextView;

        final ImageView firstItemView;
        final ImageView secondItemView;
        final ImageView thirdItemView;
        final ImageView fourthItemView;
        final ImageView fifthItemView;
        final ImageView sixthItemView;
        final ImageView trinketItemView;

        final TextView gameDateView;
        final TextView queueTypeView;
        final TextView gameDurationTextView;

        final View gameResultIndicatorView;

        final TextView csPerMinView;
        final TextView dmgPerMinView;

        public MatchEntryListAdapterViewHolder(@NonNull View view) {
            super(view);

            matchLinearLayout = view.findViewById(R.id.ly_match_container);

            championImageView = view.findViewById(R.id.iv_champion_image);

            firstSummonerSpellIconView = view.findViewById(R.id.iv_first_summoner_spell);
            secondSummonerSpellIconView = view.findViewById(R.id.iv_second_summoner_spell);

            mainRuneView = view.findViewById(R.id.iv_rune_keystone);
            secondaryRuneView = view.findViewById(R.id.iv_secondary_rune_path);

            killsTextView = view.findViewById(R.id.tv_kills);
            deathsTextView = view.findViewById(R.id.tv_deaths);
            assistsTextView = view.findViewById(R.id.tv_assists);
            kdaTextView = view.findViewById(R.id.tv_kda);

            firstItemView = view.findViewById(R.id.iv_item0);
            secondItemView = view.findViewById(R.id.iv_item1);
            thirdItemView = view.findViewById(R.id.iv_item2);
            fourthItemView = view.findViewById(R.id.iv_item3);
            fifthItemView = view.findViewById(R.id.iv_item4);
            sixthItemView = view.findViewById(R.id.iv_item5);
            trinketItemView = view.findViewById(R.id.iv_trinket);

            gameDateView = view.findViewById(R.id.tv_game_time_ago);
            queueTypeView = view.findViewById(R.id.tv_game_type);
            gameDurationTextView = view.findViewById(R.id.tv_game_duration);

            gameResultIndicatorView = view.findViewById(R.id.view_match_result_indicator);

            csPerMinView = view.findViewById(R.id.tv_match_primary_stat);
            dmgPerMinView = view.findViewById(R.id.tv_match_secondary_stat);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            long gameId = matches.get(adapterPosition).getMatchId();
            itemClickListener.onItemClickListener(gameId);
        }
    }
}
