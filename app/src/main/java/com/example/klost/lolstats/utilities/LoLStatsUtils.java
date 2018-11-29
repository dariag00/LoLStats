package com.example.klost.lolstats.utilities;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Team;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LoLStatsUtils {

    private static final String LOG_TAG = "LoLStatsUtils";

    public static void setKdaAndTextColorInView(TextView textView, double kda){

        String kdaString = String.format(Locale.ENGLISH, "%.2f", kda);
        textView.setText(kdaString);
        //TODO introducir en colors
        if(kda>=3.0 && kda<5.0){
            textView.setTextColor(Color.parseColor("#65bc94"));
        }else if(kda>=5.0 && kda<7.0){
            textView.setTextColor(Color.parseColor("#599aa3"));
        }else if(kda>=7.0){
            textView.setTextColor(Color.parseColor("#f4c542"));
        }


    }

    public static String getDaysAgo(Date date){

        String timeAgo = "Hace ";

        Date today = new Date();

        long diff = today.getTime() - date.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        //Si el game se jugo hace mas de un dia lo contamos como unidad de tiempo
        if(days>=1){
            timeAgo = timeAgo + String.valueOf(days) + " dias";
        }else{
            //Si no las horas pasan a serlo
            long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            if(hours>=1){
                timeAgo = timeAgo + String.valueOf(hours) + " horas";
            }else{
                //Si se jugó hace menos de 1 hora la unidad pasa a ser minutos
                long minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MINUTES);
                if(minutes>=1){
                    timeAgo = timeAgo + String.valueOf(minutes) + " minutos";
                }else{
                    //Si se jugó hace menos de 1 minuto la unidad pasa a ser segundos
                    long seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.SECONDS);
                    timeAgo = timeAgo + String.valueOf(seconds) +   " segundos";
                }
            }
        }

        return timeAgo;

    }

    public static String getQueueName(int queueId){
        String queueName;

        //TODO añadir mas tipos de game
        switch(queueId){
            case 400:
                queueName = "Normal Draft Pick";
                break;
            case 420:
                queueName = "Ranked Solo";
                break;
            case 430:
                queueName = "Normal Blind Pick";
                break;
            case 440:
                queueName = "Ranked Flex";
                break;
            case 450:
                queueName = "ARAM";
                break;
            case 460:
                queueName = "3v3 Blind Pick";
                break;
            case 470:
                queueName = "3v3 Ranked Flex";
                break;
                default:
                    queueName = "Not supported";
        }

        return queueName;
    }

    public String getSeasonName(int seasonId){

        String seasonName;

        switch (seasonId){

            case 0:
                seasonName = "PRESEASON 3";
                break;
            case 1:
                seasonName = "SEASON 3";
                break;
            case 2:
                seasonName = "PRESEASON 4";
                break;
            case 3:
                seasonName = "SEASON 4";
                break;
            case 4:
                seasonName = "PRESEASON 5";
                break;
            case 5:
                seasonName = "SEASON 5";
                break;
            case 6:
                seasonName = "PRESEASON 6";
                break;
            case 7:
                seasonName = "SEASON 6";
                break;
            case 8:
                seasonName = "PRESEASON 7";
                break;
            case 9:
                seasonName = "SEASON 7";
                break;
            case 10:
                seasonName = "PRESEASON 8";
                break;
            case 11:
                seasonName = "SEASON 8";
                break;

                default:
                    seasonName="season no registrada";

        }

        return seasonName;

    }

    public static double calculateKDA(int kills, int assists, int deaths){


        if(deaths == 0){
            deaths = 1;
        }
        double kda = ((double) kills + (double) assists)/ (double) deaths;
        Log.d(LOG_TAG, "Kills: " + kills + " ass " + assists + " deaths " + deaths+ " resultado: " + kda);

        return kda;
    }

    public static int getWinsOfLast20Games(List<Match> listOfGames, Summoner summoner){

        int contadorDeVictorias = 0;

        for(int i=0; i<20; i++){

            Match match = listOfGames.get(i);
            //Primero revisamos que se ha procesado el match para evitar posibles errores
            if(match.isProcessed()){
                Team currentPlayerTeam = match.getTeamOfGivenSummoner(summoner);
                if(currentPlayerTeam.isWon())
                    contadorDeVictorias++;
            }
        }

        Log.d(LOG_TAG, "Resultado total: " + contadorDeVictorias);

        return contadorDeVictorias;

    }


    //TODO añadir loadFromDDragon aqui?

}
