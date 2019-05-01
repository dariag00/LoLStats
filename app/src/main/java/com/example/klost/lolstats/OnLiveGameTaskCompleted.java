package com.example.klost.lolstats;

import com.example.klost.lolstats.models.matches.Match;

public interface OnLiveGameTaskCompleted {
    void onTaskCompleted(Match result);
}
