package com.example.klost.lolstats;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.runes.Rune;
import com.example.klost.lolstats.models.runes.RunePath;
import com.example.klost.lolstats.models.summoners.SummonerSpell;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.example.klost.lolstats.utilities.StaticData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchDetailAdapter extends RecyclerView.Adapter<MatchDetailAdapter.MatchDetailAdapterViewHolder>{

    private Summoner summoner;
    private List<Player> players;

    private final MatchDetailAdapter.MatchAdapterOnClickHandler mClickHandler;

    public interface MatchAdapterOnClickHandler{
        void onClick(String playerPosition);
    }

    MatchDetailAdapter(MatchAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class MatchDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final CircleImageView championView;
        final ImageView keystoneView;
        final ImageView secondaryRuneView;
        final ImageView firstSummonerView;
        final ImageView secondSummonerView;

        final TextView summonerNameView;
        final TextView divisionTextView;
        final ImageView divisionImageView;

        final TextView summonerLevelView;

        final TextView killsView;
        final TextView deathsView;
        final TextView assistsView;
        final TextView kdaView;

        final ImageView item1View;
        final ImageView item2View;
        final ImageView item3View;
        final ImageView item4View;
        final ImageView item5View;
        final ImageView item6View;
        final CircleImageView trinketView;

        final TextView goldView;
        final TextView damagePercentView;
        final TextView totalCsView;
        final TextView csPerMinView;

        MatchDetailAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            championView = view.findViewById(R.id.iv_player_champion);
            keystoneView = view.findViewById(R.id.iv_player_keystone);
            secondaryRuneView = view.findViewById(R.id.iv_player_secondary_rune);
            firstSummonerView = view.findViewById(R.id.iv_player_first_summoner);
            secondSummonerView = view.findViewById(R.id.iv_player_second_summoner);
            summonerNameView = view.findViewById(R.id.tv_summoner_name);
            divisionTextView = view.findViewById(R.id.tv_player_division);
            divisionImageView = view.findViewById(R.id.iv_player_division);
            summonerLevelView = view.findViewById(R.id.tv_summoner_level);
            killsView = view.findViewById(R.id.tv_player_kills);
            deathsView = view.findViewById(R.id.tv_player_deaths);
            assistsView = view.findViewById(R.id.tv_player_assists);
            kdaView = view.findViewById(R.id.tv_player_kda);
            item1View = view.findViewById(R.id.iv_item1);
            item2View = view.findViewById(R.id.iv_item2);
            item3View = view.findViewById(R.id.iv_item3);
            item4View = view.findViewById(R.id.iv_item4);
            item5View = view.findViewById(R.id.iv_item5);
            item6View = view.findViewById(R.id.iv_item6);
            trinketView = view.findViewById(R.id.iv_trinket);
            goldView = view.findViewById(R.id.tv_player_gold);
            damagePercentView = view.findViewById(R.id.player_damage_percent);
            totalCsView = view.findViewById(R.id.player_cs);
            csPerMinView = view.findViewById(R.id.player_cs_min);
        }

        @Override
        public void onClick(View v) {
            Log.d("Prueba", "PULSADO Y LLAMADO");
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }

    }

    @NonNull
    @Override
    public MatchDetailAdapter.MatchDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context =  viewGroup.getContext();
        int layoutIdForListItem = R.layout.details_match_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new MatchDetailAdapter.MatchDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchDetailAdapter.MatchDetailAdapterViewHolder matchDetailAdapterViewHolder, int position) {

        Player player = players.get(position);
        Summoner currentSummoner;
        //Miramos si el player es nuestro summoner
        Log.d("DetailAdapter", "SUMM: " + summoner.getAccountId() +" otro id: " + summoner.getSummonerId() + " y comp: " + player.getAccountId());
        if(summoner.getSummonerId() == player.getAccountId()){
            currentSummoner = summoner;
            matchDetailAdapterViewHolder.summonerNameView.setText(summoner.getSummonerName());
            matchDetailAdapterViewHolder.divisionTextView.setText(summoner.getPositionList().getRankedSoloPosition().getTier());
        }else{
            currentSummoner = summoner;
        }



        //Busqueda del summoner asociado

        //Setteo de los valores del summoner

        //Setteo de las imagenes iniciales
        int championId = player.getChampionId();
        Champion champion = StaticData.getChampionList().getChampionById(championId);
        champion.loadImageFromDDragon(matchDetailAdapterViewHolder.championView);

        SummonerSpell firstSummonerSpell = StaticData.getSpellList().getSpellById(player.getSpell1Id());
        firstSummonerSpell.loadImageFromDDragon(matchDetailAdapterViewHolder.firstSummonerView);
        SummonerSpell secondSummonerSpell = StaticData.getSpellList().getSpellById(player.getSpell2Id());
        secondSummonerSpell.loadImageFromDDragon(matchDetailAdapterViewHolder.secondSummonerView);

        Rune mainRune = StaticData.getRuneList().getRuneById(player.getRune0());
        mainRune.loadImageFromDDragon(matchDetailAdapterViewHolder.keystoneView);
        RunePath secondaryRune = StaticData.getRuneList().getRunePathById(player.getRuneSecondaryStyle());
        secondaryRune.loadImageFromDDragon(matchDetailAdapterViewHolder.secondaryRuneView);

        //Setteo del resultado
        matchDetailAdapterViewHolder.killsView.setText(String.valueOf(player.getKills()));
        matchDetailAdapterViewHolder.deathsView.setText(String.valueOf(player.getDeaths()));
        matchDetailAdapterViewHolder.assistsView.setText(String.valueOf(player.getAssists()));

        double kda = LoLStatsUtils.calculateKDA(player.getKills(), player.getAssists(), player.getDeaths());
        LoLStatsUtils.setKdaAndTextColorInView(matchDetailAdapterViewHolder.kdaView, kda);

        //Setteo de los items

        Item firstItem = StaticData.getItemList().getItemById(player.getItem0());
        Item secondItem = StaticData.getItemList().getItemById(player.getItem1());
        Item thirdItem = StaticData.getItemList().getItemById(player.getItem2());
        Item fourthItem = StaticData.getItemList().getItemById(player.getItem3());
        Item fifthItem = StaticData.getItemList().getItemById(player.getItem4());
        Item sixthItem = StaticData.getItemList().getItemById(player.getItem5());
        Item trinket = StaticData.getItemList().getItemById(player.getItem6());

        if(firstItem != null)
            firstItem.loadImageFromDDragon(matchDetailAdapterViewHolder.item1View);
        if(secondItem != null)
            secondItem.loadImageFromDDragon(matchDetailAdapterViewHolder.item2View);
        if(thirdItem != null)
            thirdItem.loadImageFromDDragon(matchDetailAdapterViewHolder.item3View);
        if(fourthItem != null)
            fourthItem.loadImageFromDDragon(matchDetailAdapterViewHolder.item4View);
        if(fifthItem != null)
            fifthItem.loadImageFromDDragon(matchDetailAdapterViewHolder.item5View);
        if(sixthItem != null)
            sixthItem.loadImageFromDDragon(matchDetailAdapterViewHolder.item6View);
        if(trinket != null)
            trinket.loadImageFromDDragon(matchDetailAdapterViewHolder.trinketView);

        //Setteo de las stats
        int gold = player.getGoldEarned();
        matchDetailAdapterViewHolder.goldView.setText(String.valueOf(gold));
        int cs = player.getTotalMinionsKilled();
        matchDetailAdapterViewHolder.totalCsView.setText(String.valueOf(cs));

    }


    @Override
    public int getItemCount() {
        if (null == players)
            return 0;
        return players.size();
    }

    public void setData(List<Player> playerList, Summoner mSummoner) {
        players = playerList;
        summoner = mSummoner;
        notifyDataSetChanged();
    }
}
