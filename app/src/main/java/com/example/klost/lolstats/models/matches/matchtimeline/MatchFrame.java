package com.example.klost.lolstats.models.matches.matchtimeline;

import java.util.ArrayList;

public class MatchFrame {

    private long timestamp;
    private ArrayList<MatchEvent> matchEvents;
    private ArrayList<ParticipantFrame> participantFrames;

    public MatchFrame(long timestamp, ArrayList<MatchEvent> matchEvents, ArrayList<ParticipantFrame> participantFrames) {
        this.timestamp = timestamp;
        this.matchEvents = matchEvents;
        this.participantFrames = participantFrames;
    }
}
