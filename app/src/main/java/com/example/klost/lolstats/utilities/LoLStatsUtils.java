package com.example.klost.lolstats.utilities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klost.lolstats.R;
import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.matches.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoLStatsUtils {

    private static final String LOG_TAG = "LoLStatsUtils";

    public static void setKdaAndTextColorInView(TextView textView, double kda, Context context){

        String kdaString = String.format(Locale.ENGLISH, "%.2f", kda);
        textView.setText(kdaString);

        if(kda>=2.0 && kda<3.0){
            textView.setTextColor(ContextCompat.getColor(context, R.color.lowKdaColor));
        }else if(kda>=3.0 && kda<4.0){
            textView.setTextColor(ContextCompat.getColor(context, R.color.lowToMidKdaColor));
        }else if(kda>=4.0 && kda<5.0){
            textView.setTextColor(ContextCompat.getColor(context, R.color.midKdaColor));
        }else if(kda>=7.0){
            textView.setTextColor(ContextCompat.getColor(context, R.color.highKdaColor));
        }


    }

    public static double getDamagePercentOfGivenPlayer(List<Player> players, Player givenPlayer){

        double totalDamage = 0;
        for(Player player:players){
            totalDamage = totalDamage + player.getTotalDamageDealtToChampions();
        }
        return (givenPlayer.getTotalDamageDealtToChampions() / totalDamage) * 100;
    }

    public static double getGoldPercentOfGivenPlayer(List<Player> players, Player givenPlayer){
        double totalGold = 0;
        for(Player player:players){
            totalGold = totalGold + player.getGoldEarned();
        }
        return (givenPlayer.getGoldEarned() / totalGold) * 100;
    }

    public static String getDaysAgo(Date date){

        String timeAgo = "Hace ";

        Date today = new Date();

        long diff = today.getTime() - date.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        //TODO poner meses
        //Si el game se jugo hace mas de un dia lo contamos como unidad de tiempo
        if(days==1){
            timeAgo = timeAgo + String.valueOf(days) + " dia";
        }else if(days>=2){
            timeAgo = timeAgo + String.valueOf(days) + " dias";
        }else{
            //Si no las horas pasan a serlo
            long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            if(hours>=1){
                if(hours == 1){
                    timeAgo = timeAgo + String.valueOf(hours) + " hora";
                }else{
                    timeAgo = timeAgo + String.valueOf(hours) + " horas";
                }
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
            case 1200:
                queueName ="Nexus Blitz";
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

    public static double calculateKDA(double kills, double assists, double deaths){


        if(deaths == 0){
            deaths = 1;
        }
        double kda = ( kills +  assists)/  deaths;
        Log.d(LOG_TAG, "Kills: " + kills + " ass " + assists + " deaths " + deaths+ " resultado: " + kda);

        return kda;
    }
    //TODO revisar que peta con RayPrince0709
    public static int getWinsOfLast20Games(List<Match> listOfGames, Summoner summoner){

        int contadorDeVictorias = 0;

        for(int i=0; i<20; i++){

            Match match = listOfGames.get(i);
            //Primero revisamos que se ha procesado el match para evitar posibles errores
            if(match.isProcessed()){
                Team currentPlayerTeam = match.getTeamOfGivenSummoner(summoner);
                if(currentPlayerTeam == null){
                    Log.d("PruebaNull", "Es null");
                }
                if(currentPlayerTeam.isWon())
                    contadorDeVictorias++;
            }
        }

        Log.d(LOG_TAG, "Resultado total: " + contadorDeVictorias);

        return contadorDeVictorias;

    }

    public static double getAverageKillsOfLast20Games(List<Match> listOfGames, Summoner summoner){

        int totalKills = 0;

        for(int i=0; i<20; i++){
            Match match = listOfGames.get(i);
            if(match.isProcessed()){
                Player currentPlayer = match.getPlayer(summoner);
                totalKills = totalKills + currentPlayer.getKills();
            }
        }

        return (double) totalKills / 20.0;
    }

    public static double getAverageDeathsOfLast20Games(List<Match> listOfGames, Summoner summoner){

        int totalDeaths = 0;

        for(int i=0; i<20; i++){
            Match match = listOfGames.get(i);
            if(match.isProcessed()){
                Player currentPlayer = match.getPlayer(summoner);
                totalDeaths = totalDeaths + currentPlayer.getDeaths();
            }
        }

        return (double) totalDeaths / 20.0;

    }

    public static double getAverageAssistsOfLast20Games(List<Match> listOfGames, Summoner summoner){

        int totalAssists = 0;

        for(int i=0; i<20; i++){
            Match match = listOfGames.get(i);
            if(match.isProcessed()){
                Player currentPlayer = match.getPlayer(summoner);
                totalAssists = totalAssists + currentPlayer.getAssists();
            }
        }

        return (double) totalAssists / 20.0;
    }

    public static double getAverageKDAOfLast20Games(List<Match> listOfGames, Summoner summoner){

        double totalKills = getAverageKillsOfLast20Games(listOfGames, summoner);
        double totalAssists = getAverageAssistsOfLast20Games(listOfGames, summoner);
        double totalDeaths = getAverageDeathsOfLast20Games(listOfGames, summoner);

        return (totalKills + totalAssists) / totalDeaths;
    }

    public static double[] getRoleStats(List<Match> listOfGames, Summoner summoner, String role){

        if(role.equals("Top"))
            return getPercentageOfGamesPlayedAndWonAsTop(listOfGames, summoner);
        else if(role.equals("Jungle"))
            return getPercentageOfGamesPlayedAndWonAsJungle(listOfGames, summoner);
        else if(role.equals("Mid"))
            return getPercentageOfGamesPlayedAndWonAsMid(listOfGames, summoner);
        else if(role.equals("Bottom"))
            return getPercentageOfGamesPlayedAndWonAsBottom(listOfGames, summoner);
        else
            return getPercentageOfGamesPlayedAndWonAsSupport(listOfGames, summoner);

    }

    public static double[] getPercentageOfGamesPlayedAndWonAsTop(List<Match> listOfGames, Summoner summoner){

        double[] data = new double[2];
        int played = 0;
        int won = 0;

        for(int i=0; i<20; i++) {
            Match match = listOfGames.get(i);
            if(match.isProcessed()) {
                Log.d(LOG_TAG, "Lane: " + match.getLane() +" Role: " + match.getRole());
                if(match.getLane().equals("TOP")){
                    played++;
                    if(match.getTeamOfGivenSummoner(summoner).isWon()){
                        won++;
                    }
                }
            }
        }

        Log.d(LOG_TAG, "Nums: " + played + " " + "won");

        data[0] = ((double)played / 20.0) * 100;
        data[1] = ((double)won / played) * 100;

        return data;
    }

    public static double[] getPercentageOfGamesPlayedAndWonAsJungle(List<Match> listOfGames, Summoner summoner){
        double[] data = new double[2];
        int played = 0;
        int won = 0;

        for(int i=0; i<20; i++) {
            Match match = listOfGames.get(i);
            if(match.isProcessed()) {
                if(match.getLane().equals("JUNGLE")){
                    played++;
                    if(match.getTeamOfGivenSummoner(summoner).isWon()){
                        won++;
                    }
                }
            }
        }

        data[0] = ((double)played / 20.0) * 100;
        data[1] = ((double)won / played) * 100;


        return data;
    }

    public static double[] getPercentageOfGamesPlayedAndWonAsMid(List<Match> listOfGames, Summoner summoner){
        double[] data = new double[2];
        int played = 0;
        int won = 0;

        for(int i=0; i<20; i++) {
            Match match = listOfGames.get(i);
            if(match.isProcessed()) {
                if(match.getLane().equals("MID")){
                    played++;
                    if(match.getTeamOfGivenSummoner(summoner).isWon()){
                        won++;
                    }
                }
            }
        }

        data[0] = ((double)played / 20.0) * 100;
        data[1] = ((double)won / played) * 100;


        return data;
    }


    public static double[] getPercentageOfGamesPlayedAndWonAsBottom(List<Match> listOfGames, Summoner summoner){
        double[] data = new double[2];
        int played = 0;
        int won = 0;

        for(int i=0; i<20; i++) {
            Match match = listOfGames.get(i);
            if(match.isProcessed()) {
                if(match.getLane().equals("BOTTOM") && match.getRole().equals("DUO_CARRY")){
                    played++;
                    if(match.getTeamOfGivenSummoner(summoner).isWon()){
                        won++;
                    }
                }
            }
        }
        data[0] = ((double)played / 20.0) * 100;
        data[1] = ((double)won / played) * 100;

        return data;
    }


    public static double[] getPercentageOfGamesPlayedAndWonAsSupport(List<Match> listOfGames, Summoner summoner){
        double[] data = new double[2];
        int played = 0;
        int won = 0;

        for(int i=0; i<20; i++) {
            Match match = listOfGames.get(i);
            if(match.isProcessed()) {
                if(match.getLane().equals("BOTTOM") && match.getRole().equals("DUO_SUPPORT")){
                    played++;
                    if(match.getTeamOfGivenSummoner(summoner).isWon()){
                        won++;
                    }
                }
            }
        }

        data[0] = ((double)played / 20.0) * 100;
        data[1] = ((double)won / played) * 100;

        return data;
    }

    public static String[] get3MostPlayedRoles(List<Match> listOfGames, Summoner summoner){

        HashMap<String, Integer> map = new HashMap<>();

        double[] data = getPercentageOfGamesPlayedAndWonAsTop(listOfGames, summoner);
        map.put("Top", (int) data[0]);
        data = getPercentageOfGamesPlayedAndWonAsJungle(listOfGames, summoner);
        map.put("Jungle", (int) data[0]);
        data = getPercentageOfGamesPlayedAndWonAsMid(listOfGames, summoner);
        map.put("Mid", (int) data[0]);
        data = getPercentageOfGamesPlayedAndWonAsBottom(listOfGames, summoner);
        map.put("Bottom", (int) data[0]);
        data = getPercentageOfGamesPlayedAndWonAsSupport(listOfGames, summoner);
        map.put("Support", (int) data[0]);


        Object[] a = map.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });

        for (Object e : a) {
            Log.d(LOG_TAG, ((Map.Entry<String, Integer>) e).getKey() + " : "
                    + ((Map.Entry<String, Integer>) e).getValue());
        }

        String[] mostPlayedRoles =  new String[3];
        mostPlayedRoles[0] = ((Map.Entry<String, Integer>) a[0]).getKey();
        mostPlayedRoles[1] = ((Map.Entry<String, Integer>) a[1]).getKey();
        mostPlayedRoles[2] = ((Map.Entry<String, Integer>) a[2]).getKey();

        return mostPlayedRoles;
    }
    //TODO games con bots
    public static Champion[] get3MostPlayedChampion(List<Match> listOfGames, Summoner summoner, ChampionList championList) {

        Champion[] champions = new Champion[3];

        List<Integer> usedChampions = new ArrayList<>();

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            Match match = listOfGames.get(i);
            if (match.isProcessed()) {
                Log.d(LOG_TAG, "Uno procesado ");
                usedChampions.add(match.getChampionId());
            }
        }
        Log.d(LOG_TAG, "used champions" + usedChampions.size() + " " + listOfGames.size());

        for (Integer s : usedChampions) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) + 1);
            } else {
                map.put(s, 1);
            }
        }
        //TODO error cuando no hay sufucientes campeones

        Object[] a = map.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Integer, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<Integer, Integer>) o1).getValue());
            }
        });
        for (Object e : a) {
            Log.d(LOG_TAG, ((Map.Entry<Integer, Integer>) e).getKey() + " : "
                    + ((Map.Entry<Integer, Integer>) e).getValue());
        }

        Champion champion1 = null;
        Champion champion2 = null;
        Champion champion3 = null;

        if (usedChampions.size() >= 1) {
            Log.d(LOG_TAG, "Entro en size 1" + usedChampions.size());
            champion1 = championList.getChampionById(((Map.Entry<Integer, Integer>) a[0]).getKey());
        }
        if (usedChampions.size() >= 2) {
            Log.d(LOG_TAG, "Entro en size 2");
            champion2 = championList.getChampionById(((Map.Entry<Integer, Integer>) a[1]).getKey());
        }
        if (usedChampions.size() >= 3){
            Log.d(LOG_TAG, "Entro en size 3");
            champion3 = championList.getChampionById(((Map.Entry<Integer, Integer>) a[2]).getKey());
        }

        champions[0] = champion1;
        champions[1] = champion2;
        champions[2] = champion3;

        return champions;

    }

    public static int[] getChampionStats(List<Match> matchList, Summoner summoner, Champion champion){

        int[] stats = new int[6];

        for(int i = 0; i<20; i++){
            Match match = matchList.get(i);
            if(match.isProcessed()){
                Log.d(LOG_TAG, "El summoner es" + summoner.toString());
                Player player = match.getPlayer(summoner);
                if(player == null)
                    Log.d(LOG_TAG, "Es null");
                else
                    Log.d(LOG_TAG, "Player: " + player.toString());
                //Buscamos las partidas en las que ha jugado dicho campeon
                if(player.getChampionId() == champion.getChampionId()){
                    Team playerTeam = match.getTeamOfGivenSummoner(summoner);
                    //TODO sacar tiempo total jugado
                    //Sacamos los resultados
                    stats[0] = stats[0] + player.getKills();
                    stats[1] = stats[1] + player.getDeaths();
                    stats[2] = stats[2] + player.getAssists();
                    stats[3] = stats[3] + player.getTotalMinionsKilled() + player.getNeutralMinionsKilled();
                    stats[4] = stats[4] + 1; //Games played
                    if(playerTeam.isWon()) {
                        Log.d(LOG_TAG, "ch: " + champion.getName() + " ha ganado");
                        stats[5] = stats[5] + 1;//Games won
                    }
                }
            }
        }

        return stats;

    }

    public static void setChampionTextAndSize(TextView textView, String champion){

        int textSize = 14; //TODO sps?

        Log.d(LOG_TAG, "Champion size " + champion.length());

        if(champion.length() <= 6)
            textSize = 12;
        else if(champion.length() == 7)
            textSize = 11;
        else if(champion.length() == 8)
            textSize = 10;
        else if(champion.length()>=9 && champion.length()<=10)
            textSize = 8;
        else if(champion.length()>=11)
            textSize = 6;

        Log.d(LOG_TAG, "Size: " + textSize);

        textView.setText(champion);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public static void setRoleImage(ImageView imageView, String role){

        switch(role){
            case "Top":
                imageView.setImageResource(R.drawable.top_icon);
                break;
            case "Jungle":
                imageView.setImageResource(R.drawable.jungle_icon);
                break;
            case "Mid":
                imageView.setImageResource(R.drawable.mid_icon);
                break;
            case "Bottom":
                imageView.setImageResource(R.drawable.bottom_icon);
                break;
            case "Support":
                imageView.setImageResource(R.drawable.support_icon);
                break;
        }

    }



    //TODO añadir loadFromDDragon aqui?

}

