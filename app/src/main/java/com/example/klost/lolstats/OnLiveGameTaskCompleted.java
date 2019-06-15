package com.example.klost.lolstats;

import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.utilities.LiveGameBean;

public interface OnLiveGameTaskCompleted {
    void onTaskCompleted(LiveGameBean result);
}
