package com.example.klost.lolstats.utilities;

import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.runes.RuneList;
import com.example.klost.lolstats.models.summoners.SummonerSpellList;

public class StaticData {

    private static ChampionList championList;
    private static ItemList itemList;
    private static SummonerSpellList spellList;
    private static RuneList runeList;

    public static ChampionList getChampionList() {
        return championList;
    }

    public static void setChampionList(ChampionList list) {
        championList = list;
    }

    public static ItemList getItemList() {
        return itemList;
    }

    public static void setItemList(ItemList list) {
        itemList = list;
    }

    public static SummonerSpellList getSpellList() {
        return spellList;
    }

    public static void setSpellList(SummonerSpellList list) {
        spellList = list;
    }

    public static RuneList getRuneList() {
        return runeList;
    }

    public static void setRuneList(RuneList list) {
        runeList = list;
    }
}
