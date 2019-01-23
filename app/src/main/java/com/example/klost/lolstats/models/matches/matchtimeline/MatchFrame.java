package com.example.klost.lolstats.models.matches.matchtimeline;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchFrame implements Serializable{

    private long timestamp;
    private ArrayList<MatchEvent> matchEvents;
    private ArrayList<ParticipantFrame> participantFrames;

    public MatchFrame(long timestamp, ArrayList<MatchEvent> matchEvents, ArrayList<ParticipantFrame> participantFrames) {
        this.timestamp = timestamp;
        this.matchEvents = matchEvents;
        this.participantFrames = participantFrames;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<MatchEvent> getMatchEvents() {
        return matchEvents;
    }

    public ArrayList<ParticipantFrame> getParticipantsFrames() {
        return participantFrames;
    }

    /*
        Metodo que devuelve el frame de un participante concreto
     */
    private ParticipantFrame getParticipantFrame(int participantId){

        for(ParticipantFrame participantFrame: participantFrames){
            if(participantFrame.getParticipantId() == participantId){
                return participantFrame;
            }
        }

        return null;
    }
    /*
        Metodo que devuelve los CS que ha tenido el participante en este frame concreto
     */
    public int getParticipantCsFrame(int participantId){
        ParticipantFrame participantFrame = this.getParticipantFrame(participantId);
        return participantFrame.getMinionsKilled() + participantFrame.getJungleMinionsKilled();
    }

    public int getParticipantGoldFrame(int participantId){
        ParticipantFrame participantFrame = this.getParticipantFrame(participantId);
        return participantFrame.getTotalGold();
    }

}
