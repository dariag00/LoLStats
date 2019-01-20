package com.example.klost.lolstats.models.matches.matchtimeline;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchEvent implements Serializable{

    long timestamp;
    String type; //Legal values: CHAMPION_KILL, WARD_PLACED, WARD_KILL, BUILDING_KILL,
    // ELITE_MONSTER_KILL, ITEM_PURCHASED, ITEM_SOLD, ITEM_DESTROYED,
    // ITEM_UNDO, SKILL_LEVEL_UP, ASCENDED_EVENT, CAPTURE_POINT, PORO_KING_SUMMON_
    String towerType;
    int teamId;
    String ascendedType;
    int killerId;
    String levelUpType;
    ArrayList<Integer> participantIds;//TODO poner participants
    String wardType;
    String monsterType;
    String eventType;
}
