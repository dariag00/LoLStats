package com.example.klost.lolstats;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
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

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RiotAdapter extends RecyclerView.Adapter<RiotAdapter.RiotAdapterViewHolder>{

    private Match[] matchesData;
    private Summoner summoner;
    private SummonerSpellList summonerSpellList;
    private RuneList runeList;
    private ItemList itemList;
    private ChampionList championList;

    public class RiotAdapterViewHolder extends RecyclerView.ViewHolder{


        public final LinearLayout matchLinearLayout;

        public final CircleImageView championImageView;

        public final ImageView firstSummonerSpellIconView;
        public final ImageView secondSummonerSpellIconView;

        public final ImageView mainRuneView;
        public final ImageView secondaryRuneView;

        public final TextView killsTextView;
        public final TextView deathsTextView;
        public final TextView assistsTextView;
        public final TextView kdaTextView;

        public final ImageView firstItemView;
        public final ImageView secondItemView;
        public final ImageView thirdItemView;
        public final ImageView fourthItemView;
        public final ImageView fifthItemView;
        public final ImageView sixthItemView;
        public final ImageView trinketItemView;

        public final TextView gameDateView;
        public final TextView queueTypeView;
        public final TextView gameDurationTextView;

        public final View gameResultIndicatorView;



        public RiotAdapterViewHolder(View view){
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

        }

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public RiotAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context =  viewGroup.getContext();
        int layoutIdForListItem = R.layout.match_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new RiotAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the match
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param riotAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RiotAdapterViewHolder riotAdapterViewHolder, int position) {

        Match match = matchesData[position];

        boolean gameWon = match.hasGivenSummonerWon(summoner);

        if(gameWon) {
            riotAdapterViewHolder.gameResultIndicatorView.setBackgroundColor(Color.parseColor("#4286f4"));
        }else {
            riotAdapterViewHolder.gameResultIndicatorView.setBackgroundColor(Color.parseColor("#f44d41"));
        }

        Player player = match.getPlayer(summoner);
        int championId = player.getChampionId();

        //TODO revisar posibles problemas con asyncronia(Firebase?)
        //Seteo de la imagen del campeon jugado
        Champion champion = championList.getChampionById(championId);
        champion.loadImageFromDDragon(riotAdapterViewHolder.championImageView);

        //Seteo de los hechizos de invocador jugados
        SummonerSpell firstSummonerSpell = summonerSpellList.getSpellById(player.getSpell1Id());
        firstSummonerSpell.loadImageFromDDragon(riotAdapterViewHolder.firstSummonerSpellIconView);

        SummonerSpell secondSummonerSpell = summonerSpellList.getSpellById(player.getSpell2Id());
        secondSummonerSpell.loadImageFromDDragon(riotAdapterViewHolder.secondSummonerSpellIconView);

        //Seteo de la keystone y de la rama de runas secundaria jugadas
        Rune mainRune = runeList.getRuneById(player.getRune0());
        mainRune.loadImageFromDDragon(riotAdapterViewHolder.mainRuneView);

        RunePath secondaryRune = runeList.getRunePathById(player.getRuneSecondaryStyle());
        secondaryRune.loadImageFromDDragon(riotAdapterViewHolder.secondaryRuneView);

        //Seteo del resultado del jugador
        riotAdapterViewHolder.killsTextView.setText(String.valueOf(player.getKills()));
        riotAdapterViewHolder.deathsTextView.setText(String.valueOf(player.getDeaths()));
        riotAdapterViewHolder.assistsTextView.setText(String.valueOf(player.getAssists()));

        double kda = LoLStatsUtils.calculateKDA(player.getKills(), player.getAssists(), player.getDeaths());
        riotAdapterViewHolder.kdaTextView.setText(String.format(Locale.ENGLISH, "KDA: %.2f", kda));

        //Seteo de los items jugados
        Item firstItem = itemList.getItemById(player.getItem0());
        Item secondItem = itemList.getItemById(player.getItem1());
        Item thirdItem = itemList.getItemById(player.getItem2());
        Item fourthItem = itemList.getItemById(player.getItem3());
        Item fifthItem = itemList.getItemById(player.getItem4());
        Item sixthItem = itemList.getItemById(player.getItem5());
        Item seventhItem = itemList.getItemById(player.getItem6());
        //TODO creat item vacio
        if(firstItem != null)
            firstItem.loadImageFromDDragon(riotAdapterViewHolder.firstItemView);
        if(secondItem != null)
            secondItem.loadImageFromDDragon(riotAdapterViewHolder.secondItemView);
        if(thirdItem != null)
            thirdItem.loadImageFromDDragon(riotAdapterViewHolder.thirdItemView);
        if(fourthItem != null)
            fourthItem.loadImageFromDDragon(riotAdapterViewHolder.fourthItemView);
        if(fifthItem != null)
            fifthItem.loadImageFromDDragon(riotAdapterViewHolder.fifthItemView);
        if(sixthItem != null)
            sixthItem.loadImageFromDDragon(riotAdapterViewHolder.sixthItemView);
        //TODO mirar trinket en aram
        if(seventhItem != null)
            seventhItem.loadImageFromDDragon(riotAdapterViewHolder.trinketItemView);

        //Seteo del resto de datos
        riotAdapterViewHolder.gameDurationTextView.setText(match.getGameDurationInMinutesAndSeconds());
        riotAdapterViewHolder.gameDateView.setText(LoLStatsUtils.getDaysAgo(match.getGameCreation()));
        riotAdapterViewHolder.queueTypeView.setText(LoLStatsUtils.getQueueName(match.getQueue()));

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our match list
     */
    @Override
    public int getItemCount() {
        if(matchesData == null)
            return 0;
        return matchesData.length;
    }


    public void setData(Match[] matchesData, Summoner summoner, ChampionList championList, RuneList runeList, SummonerSpellList summonerSpellList, ItemList itemList) {
        this.matchesData = matchesData;
        this.summoner = summoner;
        this.runeList = runeList;
        this.summonerSpellList = summonerSpellList;
        this.itemList = itemList;
        this.championList = championList;
        notifyDataSetChanged();
    }

}
