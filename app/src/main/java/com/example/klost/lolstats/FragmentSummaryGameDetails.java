package com.example.klost.lolstats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.StaticData;
import com.github.mikephil.charting.charts.LineChart;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentSummaryGameDetails  extends Fragment{


    private Player currentPlayer;

    private static final String SAVED_GOLD_MAP_KEY = "gold";
    private static final String SAVED_PLAYER_KEY = "player";

    private LineChart goldChart;

    public FragmentSummaryGameDetails(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_game_details, container, false);

        Bundle args = getArguments();
        if (args == null) {
            Toast.makeText(getActivity(), "arguments is null " , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "text " + args , Toast.LENGTH_LONG).show();
        }

        currentPlayer = (Player) this.getArguments().getSerializable(SAVED_PLAYER_KEY);

        CircleImageView championImageView = view.findViewById(R.id.iv_champion_played);

        ChampionList championList = StaticData.getChampionList();
        Champion championPlayed = championList.getChampionById(currentPlayer.getChampionId());
        championPlayed.loadImageFromDDragon(championImageView);

        TextView killsView = view.findViewById(R.id.tv_kills);
        TextView deathsView = view.findViewById(R.id.tv_deaths);
        TextView assistsView = view.findViewById(R.id.tv_assists);
        TextView kdaView = view.findViewById(R.id.tv_kda);

        int numberOfKills = currentPlayer.getKills();
        int numberOfDeaths = currentPlayer.getDeaths();
        int numberOfAssists = currentPlayer.getAssists();

        killsView.setText(String.valueOf(numberOfKills));
        deathsView.setText(String.valueOf(numberOfDeaths));
        assistsView.setText(String.valueOf(numberOfAssists));
        double kda = LoLStatsUtils.calculateKDA(numberOfKills, numberOfAssists, numberOfDeaths);
        LoLStatsUtils.setKdaAndTextColorInView(kdaView, kda, view.getContext());

        ImageView itemView1 = view.findViewById(R.id.iv_item1);
        ImageView itemView2 = view.findViewById(R.id.iv_item2);
        ImageView itemView3 = view.findViewById(R.id.iv_item3);
        ImageView itemView4 = view.findViewById(R.id.iv_item4);
        ImageView itemView5 = view.findViewById(R.id.iv_item5);
        ImageView itemView6 = view.findViewById(R.id.iv_item6);
        CircleImageView trinketView = view.findViewById(R.id.iv_trinket);

        ItemList itemList = StaticData.getItemList();
        Item firstItem = itemList.getItemById(currentPlayer.getItem0());
        Item secondItem = itemList.getItemById(currentPlayer.getItem1());
        Item thirdItem = itemList.getItemById(currentPlayer.getItem2());
        Item fourthItem = itemList.getItemById(currentPlayer.getItem3());
        Item fifthItem = itemList.getItemById(currentPlayer.getItem4());
        Item sixthItem = itemList.getItemById(currentPlayer.getItem5());
        Item trinket = itemList.getItemById(currentPlayer.getItem6());

        if (firstItem != null)
            firstItem.loadImageFromDDragon(itemView1);
        if (secondItem != null)
            secondItem.loadImageFromDDragon(itemView2);
        if (thirdItem != null)
            thirdItem.loadImageFromDDragon(itemView3);
        if (fourthItem != null)
            fourthItem.loadImageFromDDragon(itemView4);
        if (fifthItem != null)
            fifthItem.loadImageFromDDragon(itemView5);
        if (sixthItem != null)
            sixthItem.loadImageFromDDragon(itemView6);
        if (trinket != null)
            trinket.loadImageFromDDragon(trinketView);

        CircleImageView mainPathIconView = view.findViewById(R.id.iv_rune_path);
        CircleImageView mainPathKeystoneIconView = view.findViewById(R.id.iv_rune1);
        CircleImageView mainPathRune2IconView = view.findViewById(R.id.iv_rune2);
        CircleImageView mainPathRune3IconView = view.findViewById(R.id.iv_rune3);
        CircleImageView mainPathRune4IconView = view.findViewById(R.id.iv_rune4);

        TextView mainPathNameView = view.findViewById(R.id.tv_rune_path_name);
        TextView mainPathKeystoneNameView = view.findViewById(R.id.tv_rune1_name);
        TextView mainPathRune2NameView = view.findViewById(R.id.tv_rune2_name);
        TextView mainPathRune3NameView = view.findViewById(R.id.tv_rune3_name);
        TextView mainPathRune4NameView = view.findViewById(R.id.tv_rune4_name);

        RuneList runeList = StaticData.getRuneList();
        RunePath mainPath = runeList.getRunePathById(currentPlayer.getRunePrimaryStyle());
        mainPath.loadImageFromDDragon(mainPathIconView);
        mainPathNameView.setText(mainPath.getName());

        Rune keystone = runeList.getRuneById(currentPlayer.getRune0());
        keystone.loadImageFromDDragon(mainPathKeystoneIconView);
        mainPathKeystoneNameView.setText(keystone.getName());

        Rune rune2 = runeList.getRuneById(currentPlayer.getRune1());
        rune2.loadImageFromDDragon(mainPathRune2IconView);
        mainPathRune2NameView.setText(rune2.getName());

        Rune rune3 = runeList.getRuneById(currentPlayer.getRune2());
        rune3.loadImageFromDDragon(mainPathRune3IconView);
        mainPathRune3NameView.setText(rune3.getName());

        Rune rune4 = runeList.getRuneById(currentPlayer.getRune3());
        rune4.loadImageFromDDragon(mainPathRune4IconView);
        mainPathRune4NameView.setText(rune4.getName());

        CircleImageView secondaryPathIconView = view.findViewById(R.id.iv_rune_path2);
        CircleImageView secondaryPathRune1IconView = view.findViewById(R.id.iv_rune2_1);
        CircleImageView secondaryPathRune2IconView = view.findViewById(R.id.iv_rune2_2);

        TextView secondaryPathNameView = view.findViewById(R.id.tv_rune_path2_name);
        TextView secondaryPathRune1NameView = view.findViewById(R.id.tv_rune2_1_name);
        TextView secondaryPathRune2NameView = view.findViewById(R.id.tv_rune2_2_name);


        RunePath secondaryPath = runeList.getRunePathById(currentPlayer.getRuneSecondaryStyle());
        secondaryPath.loadImageFromDDragon(secondaryPathIconView);
        secondaryPathNameView.setText(secondaryPath.getName());

        Rune rune5 = runeList.getRuneById(currentPlayer.getRune5());
        rune5.loadImageFromDDragon(secondaryPathRune1IconView);
        secondaryPathRune1NameView.setText(rune5.getName());

        Rune rune6 = runeList.getRuneById(currentPlayer.getRune4());
        rune6.loadImageFromDDragon(secondaryPathRune2IconView);
        secondaryPathRune2NameView.setText(rune6.getName());

        return view;
    }
}
