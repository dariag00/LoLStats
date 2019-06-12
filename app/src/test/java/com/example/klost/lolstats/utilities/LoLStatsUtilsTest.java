package com.example.klost.lolstats.utilities;

import android.util.Log;

import com.example.klost.lolstats.models.matches.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


public class LoLStatsUtilsTest{


    @Test
    public void formatDoubleValue() {
        double bigNumber = 1200;
        assertEquals("1.2k", LoLStatsUtils.formatDoubleValue(bigNumber));
        bigNumber = 200;
        assertEquals("200", LoLStatsUtils.formatDoubleValue(bigNumber));
        bigNumber = 23500;
        assertEquals("23.5k", LoLStatsUtils.formatDoubleValue(bigNumber));
    }

    @Test
    public void getDamagePercentOfGivenPlayer() {

        List<Player> playerList = new ArrayList<>();

        Player player = new Player();
        player.setTotalDamageDealtToChampions(750);

        Player player2 = new Player();
        player2.setTotalDamageDealtToChampions(250);

        playerList.add(player);
        playerList.add(player2);

        assertEquals(25, LoLStatsUtils.getDamagePercentOfGivenPlayer(playerList, player2), 0.1);

        Player player3 = new Player();

        assertEquals(0, LoLStatsUtils.getDamagePercentOfGivenPlayer(playerList, player3), 0.1);
    }

    @Test
    public void getGoldPercentOfGivenPlayer() {

        List<Player> playerList = new ArrayList<>();

        Player player = new Player();
        player.setGoldEarned(750);

        Player player2 = new Player();
        player2.setGoldEarned(250);

        playerList.add(player);
        playerList.add(player2);

        assertEquals(25, LoLStatsUtils.getGoldPercentOfGivenPlayer(playerList, player2), 0.1);

        Player player3 = new Player();

        assertEquals(0, LoLStatsUtils.getGoldPercentOfGivenPlayer(playerList, player3), 0.1);

    }

    @Test
    public void getDaysAgo(){

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.SECOND, - 10);
        assertEquals("Hace 10 segundos", LoLStatsUtils.getDaysAgo(calendar.getTime()));

        calendar.add(Calendar.MINUTE, - 10);
        assertEquals("Hace 10 minutos", LoLStatsUtils.getDaysAgo(calendar.getTime()));

        calendar.add(Calendar.HOUR, - 1);
        assertEquals("Hace 1 hora", LoLStatsUtils.getDaysAgo(calendar.getTime()));

        calendar.add(Calendar.HOUR, - 1);
        assertEquals("Hace 2 horas", LoLStatsUtils.getDaysAgo(calendar.getTime()));

        calendar.add(Calendar.DATE, - 1);
        assertEquals("Hace 1 dia", LoLStatsUtils.getDaysAgo(calendar.getTime()));

        calendar.add(Calendar.DATE, - 1);
        assertEquals("Hace 2 dias", LoLStatsUtils.getDaysAgo(calendar.getTime()));

    }

    @Test
    public void calculateKDA() {

        assertEquals(10.0, LoLStatsUtils.calculateKDA(5, 5, 0), 0.1);
        assertEquals(10.0, LoLStatsUtils.calculateKDA(5, 5, 1), 0.1);
        assertEquals(5.0, LoLStatsUtils.calculateKDA(5, 5, 2), 0.1);
    }

    @Test
    public void calculateDPM() {
        assertEquals(10, LoLStatsUtils.calculateDPM(6000, 1000), 0.1);
        assertEquals(200, LoLStatsUtils.calculateDPM(60000, 200000), 0.1);
    }
}