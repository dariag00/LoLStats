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

    public ArrayList<ParticipantFrame> getParticipantFrames() {
        return participantFrames;
    }

    /*
        Metodo que devuelve el frame de un participante concreto
     */
    public ParticipantFrame getParticipantFrames(int participantId){

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
    public int getParticipantCsFrames(int participantId){
        ParticipantFrame participantFrame = this.getParticipantFrames(participantId);
        return participantFrame.getMinionsKilled() + participantFrame.getJungleMinionsKilled();
    }
}
