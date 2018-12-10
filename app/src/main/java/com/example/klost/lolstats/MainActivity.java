package com.example.klost.lolstats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.champions.Champion;
import com.example.klost.lolstats.models.champions.ChampionList;
import com.example.klost.lolstats.models.items.Item;
import com.example.klost.lolstats.models.items.ItemList;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.models.matches.Player;
import com.example.klost.lolstats.models.runes.Rune;
import com.example.klost.lolstats.models.runes.RuneList;
import com.example.klost.lolstats.models.runes.RunePath;
import com.example.klost.lolstats.models.summoners.SummonerSpell;
import com.example.klost.lolstats.models.summoners.SummonerSpellList;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.LoLStatsUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, RiotAdapter.RiotAdapterOnClickHandler{

    //TODO comprobar conexión a internet
    //TODO crear clase que ejecute todas las requests de datos estaticos

    private static final String LOG_TAG = "MainActivity";

    private String summonerName;

    private static ChampionList championList;
    private static SummonerSpellList summonerSpellList;
    private static RuneList runeList;
    private static ItemList itemList;

    private static RecyclerView recyclerView;
    private static LinearLayout mainLayout;
    private static RiotAdapter riotAdapter;
    private static PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview_matches);
        mainLayout = findViewById(R.id.main_layout);

        //Obtencion de los datos estaticos de campeones
        URL championsUrl = NetworkUtils.buildUrl("champion",NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + championsUrl.toString());

        ReadTextTask championsTextTask = new ReadTextTask(this);
        championsTextTask.execute(championsUrl);

        //Obtencion de los datos estaticos de hechizos de invocador
        URL summonerSpellsUrl = NetworkUtils.buildUrl("summoner", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + summonerSpellsUrl.toString());

        ReadTextTask spellsTextTask = new ReadTextTask(this);
        spellsTextTask.execute(summonerSpellsUrl);

        //Obtencion de los datos estaticos de las runas
        URL runesUrl = NetworkUtils.buildUrl("runesReforged", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + runesUrl.toString());

        ReadTextTask runesTextTask = new ReadTextTask(this);
        runesTextTask.execute(runesUrl);

        //Obtencion de los datos estaticos de los items
        URL itemsUrl = NetworkUtils.buildUrl("item", NetworkUtils.GET_DDRAGON_DATA);
        Log.d(LOG_TAG, "URL: " + itemsUrl.toString());

        ReadTextTask itemsTextTask = new ReadTextTask(this);
        itemsTextTask.execute(itemsUrl);


        Intent previousIntent = getIntent();
        summonerName = previousIntent.getStringExtra(InitialActivity.EXTRA_SUMMONER_NAME);
        makeRiotSearchQuery(summonerName);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayoutManager) layoutManager).setReverseLayout(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        riotAdapter = new RiotAdapter(this);
        recyclerView.setAdapter(riotAdapter);

    }

    @Override
    public void onTaskCompleted(String result, String dataType) {

        Log.d(LOG_TAG, "DATA TYPE: " + dataType);

        try {
            //TODO problema con las runes
            if(dataType != null) {
                switch (dataType) {
                    case "summoner":
                        summonerSpellList = JsonUtils.getSpellListFromJSON(result);
                        break;
                    case "champion":
                        championList = JsonUtils.getChampionListFromJSON(result);
                        break;
                    case "item":
                        itemList = JsonUtils.getItemListFromJSON(result);
                        break;
                }
            }else{
                //TODO fix this
                Log.d(LOG_TAG, "dataType es null");
                runeList = JsonUtils.getRuneListFromJSON(result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void makeRiotSearchQuery(String searchQuery){
        URL riotSearchUrl = NetworkUtils.buildUrl(searchQuery, NetworkUtils.GET_SUMMONER);
        new RiotQueryTask(this).execute(riotSearchUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.action_refresh){
            makeRiotSearchQuery(summonerName);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Match clickedMatch, Summoner givenSummoner) {
        Context context = this;
        Toast.makeText(context, "Clicked " + clickedMatch.getGameId() + " summ name " + givenSummoner.getSummonerName(), Toast.LENGTH_SHORT)
                .show();
    }

    public static class RiotQueryTask extends AsyncTask<URL, Void, Summoner> {

        private WeakReference<MainActivity> activityReference;
        private RateLimiter throttler;
        private RateLimiter throttler2;
        private final static int MATCHLIST_SIZE = 20;

        RiotQueryTask(MainActivity context){
            //TODO estudiar el impacto que tiene esto en la memoria y si no usar SoftReference
            activityReference = new WeakReference<>(context);
            //Seteamos la cantidad de requests que pueden salir por segundo.
            throttler = RateLimiter.create(0.7);
            //throttler2 = throttler2.create(0.1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityReference.get();
            ProgressBar loadingIndicator = activity.findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Summoner doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String summonerSearchResults;
            String matchListSearchResults;
            String matchSearchResults;
            Summoner summoner = null;
            MatchList matchList;
            try {
                summonerSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL, throttler);
                Log.d(LOG_TAG, "summonerSearchResults: " + summonerSearchResults);

                if(summonerSearchResults.charAt(0) != '{'){
                    //Entonces significa que la respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error.
                    Log.e(LOG_TAG, "Error: La respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error");
                    //TODO conseguir diferenciar los errores
                    return null;
                }

                //TODO Multiple async task
                summoner = JsonUtils.getSummonerFromJSON(summonerSearchResults);

                URL leaguesURL = NetworkUtils.buildUrl(String.valueOf(summoner.getSummonerId()), NetworkUtils.GET_LEAGUES_POSITIONS);
                if(leaguesURL != null){
                    String leaguesSearchResult = NetworkUtils.getResponseFromHttpUrl(leaguesURL, throttler);
                    LeaguePositionList positionList = JsonUtils.getLeaguePositionListFromJSON(leaguesSearchResult);
                    summoner.setPositionList(positionList);
                }

                URL matchListURL = NetworkUtils.buildUrl(String.valueOf(summoner.getAccountId()), NetworkUtils.GET_MATCHLIST);
                if(matchListURL != null){
                    matchListSearchResults = NetworkUtils.getResponseFromHttpUrl(matchListURL, throttler);
                    matchList = JsonUtils.getMatchListFromJSON(matchListSearchResults);
                    summoner.setMatchList(matchList);

                    List<Match> matchListToProcess = matchList.getMatches();
                    Log.d(LOG_TAG, " MatchList Total Games: " + matchList.getMatches().size());
                    for(int i = 0;i<MATCHLIST_SIZE; i++){
                        Match match = matchListToProcess.get(i);
                        URL getMatchURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH);
                        if(getMatchURL != null){
                            matchSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchURL, throttler);
                            JsonUtils.getMatchFromJSON(matchSearchResults, match);//TODO revisar esto
                        }else{
                            return null;
                        }
                    }


                }else{
                    return null;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            if(summoner != null)
                return summoner;
            else
                return null;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {

            MainActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;
            ProgressBar loadingIndicator = activity.findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.INVISIBLE);
            if(summoner != null){
                //TODO crear un Task para analizar partidos y otro para informacion del summoner
                //Setteamos los datos del perfil
                ImageView profileIcon = activity.findViewById(R.id.iv_profile_icon);
                summoner.loadImageFromDDragon(profileIcon);

                TextView summonerLevelView = activity.findViewById(R.id.tv_summoner_level);
                summonerLevelView.setText(String.valueOf(summoner.getSummonerLevel()));

                ImageView soloQIcon = activity.findViewById(R.id.iv_rank_icon_solo);
                ImageView flexQIcon = activity.findViewById(R.id.iv_rank_icon_flex);
                ImageView flexQTTIcon = activity.findViewById(R.id.iv_rank_icon_3v3);

                LeaguePositionList list = summoner.getPositionList();
                LeaguePosition soloQ = list.getRankedSoloPosition();
                LeaguePosition flexQ = list.getRankedFlexPosition();
                LeaguePosition flexTTQ = list.getRankedFlexTTPosition();

                soloQ.setLeagueIconOnImageView(soloQIcon);
                flexQ.setLeagueIconOnImageView(flexQIcon);
                flexTTQ.setLeagueIconOnImageView(flexQTTIcon);

                //TODO no settear si es unranked
                //TODO arreglar warnings al concatenar strings
                //Setteamos los valores de SoloQ
                TextView winsSoloQView = activity.findViewById(R.id.tv_solo_wins);
                TextView lossesSoloQView = activity.findViewById(R.id.tv_solo_losses);
                TextView winRateSoloQView = activity.findViewById(R.id.tv_wr_soloq);
                TextView rankSoloQView = activity.findViewById(R.id.tv_summoner_rank_solo);
                TextView lpsSoloQView = activity.findViewById(R.id.tv_summoner_rank_lps_solo);

                double winRateSoloQ = (double) soloQ.getWins() / ((double) soloQ.getWins() + soloQ.getLosses()) * 100;
                String wRSQ = String.format(Locale.ENGLISH, "%.1f", winRateSoloQ);
                wRSQ = wRSQ.concat("%");

                winsSoloQView.setText(String.valueOf(soloQ.getWins()));
                lossesSoloQView.setText(String.valueOf(soloQ.getLosses()));
                winRateSoloQView.setText(wRSQ);
                rankSoloQView.setText(soloQ.getTier() + " " + soloQ.getRank());
                lpsSoloQView.setText(String.valueOf(soloQ.getLeaguePoints())+ " lps");

                //Setteamos los valores de FlexQ
                TextView winsFlexQView = activity.findViewById(R.id.tv_flex_wins);
                TextView lossesFlexQView = activity.findViewById(R.id.tv_flex_losses);
                TextView winRateFlexQView = activity.findViewById(R.id.tv_wr_flexq);
                TextView rankFlexQView = activity.findViewById(R.id.tv_summoner_rank_flex);
                TextView lpsFlexQView = activity.findViewById(R.id.tv_summoner_rank_lps_flex);

                double winRateFlexQ = (double) flexQ.getWins() / ((double) flexQ.getWins() + flexQ.getLosses()) * 100;
                String wRFQ = String.format(Locale.ENGLISH, "%.1f", winRateFlexQ).concat("%");

                winsFlexQView.setText(String.valueOf(flexQ.getWins()));
                lossesFlexQView.setText(String.valueOf(flexQ.getLosses()));
                winRateFlexQView.setText(wRFQ);
                rankFlexQView.setText(flexQ.getTier() + " " + flexQ.getRank());
                lpsFlexQView.setText(String.valueOf(flexQ.getLeaguePoints())+ " lps");

                //Setteamos los valores de 3v3
                TextView winsFlexTTQView = activity.findViewById(R.id.tv_3v3_wins);
                TextView lossesFlexTTQView = activity.findViewById(R.id.tv_3v3_losses);
                TextView winRate3v3View = activity.findViewById(R.id.tv_wr_3v3);
                TextView rank3v3View = activity.findViewById(R.id.tv_summoner_rank_3v3);
                TextView lps3v3View = activity.findViewById(R.id.tv_summoner_rank_lps_3v3);

                double winRate3v3 = (double) flexTTQ.getWins() / ((double) flexTTQ.getWins() + flexTTQ.getLosses()) * 100;
                String wRFTTQ = String.format(Locale.ENGLISH, "%.1f", winRate3v3).concat("%");

                winsFlexTTQView.setText(String.valueOf(flexTTQ.getWins()));
                lossesFlexTTQView.setText(String.valueOf(flexTTQ.getLosses()));
                winRate3v3View.setText(wRFTTQ);
                rank3v3View.setText(flexTTQ.getTier() + " " + flexTTQ.getRank());
                lps3v3View.setText(String.valueOf(flexTTQ.getLeaguePoints()) + " lps");


                showData(activity);
                MatchList matchList = summoner.getMatchList();
                List<Match> matches = matchList.getMatches();
                int contador = 0;
                Match[] processedMatches = new Match[MATCHLIST_SIZE];

                for(Match m : matches){
                    if(m.isProcessed()){
                        processedMatches[contador] = m;
                        contador++;
                    }
                    if(contador>=MATCHLIST_SIZE){
                        break;
                    }

                    Log.d(LOG_TAG, "Meto un match: " + contador);
                }

                Log.d(LOG_TAG, "Contador: " + contador);

                //TODO procesar N matches

                riotAdapter.setData(processedMatches, summoner, championList, runeList, summonerSpellList, itemList);

                //TODO grafico de victorias
                chart = activity.findViewById(R.id.pie_chart);

                //chart.setBackgroundColor(Color.WHITE);

                //moveOffScreen();

                chart.setUsePercentValues(true);
                chart.getDescription().setEnabled(false);

        /*chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generateCenterSpannableText());*/

                chart.setDrawHoleEnabled(true);
                //chart.setHoleColor(Color.WHITE);

                chart.setTransparentCircleColor(Color.WHITE);
                chart.setTransparentCircleAlpha(110);

                chart.setHoleRadius(5f);
                chart.setTransparentCircleRadius(20f);

                chart.setDrawCenterText(false);

                chart.setRotationEnabled(false);
                chart.setHighlightPerTapEnabled(true);

                chart.setMaxAngle(360f); // HALF CHART
                chart.setRotationAngle(180f);

                int wins = LoLStatsUtils.getWinsOfLast20Games(matches, summoner);

                setData(wins);

                chart.animateY(1400, Easing.EaseInOutQuad);

                // entry label styling
                chart.setEntryLabelColor(Color.WHITE);
                chart.setEntryLabelTextSize(8f); //Tamaño del label de dentro

                Legend legend = chart.getLegend();
                legend.setEnabled(false);

                String[] roles = LoLStatsUtils.get3MostPlayedRoles(matches, summoner);
                setMostPlayedRoles(activity, roles, matches, summoner);

                //Setteamos los datos globales de resultados
                TextView killsView = activity.findViewById(R.id.tv_global_kills);
                String kills = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageKillsOfLast20Games(matchList.getMatches(), summoner));
                killsView.setText(kills);

                TextView deathsView = activity.findViewById(R.id.tv_global_deaths);
                String deaths = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageDeathsOfLast20Games(matchList.getMatches(), summoner));
                deathsView.setText(deaths);

                TextView assistsView = activity.findViewById(R.id.tv_global_assists);
                String assists = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageAssistsOfLast20Games(matchList.getMatches(), summoner));
                assistsView.setText(assists);

                TextView kdaView = activity.findViewById(R.id.tv_global_kda);
                String kda = String.format(Locale.ENGLISH, "%.2f", LoLStatsUtils.getAverageKDAOfLast20Games(matchList.getMatches(), summoner));
                kdaView.setText(kda);

                //Setteo de los mejores champions
                Champion[] top3Champions = LoLStatsUtils.get3MostPlayedChampion(matches, summoner, championList);
                int[] stats1 = LoLStatsUtils.getChampionStats(matches, summoner, top3Champions[0]);
                int[] stats2 = LoLStatsUtils.getChampionStats(matches, summoner, top3Champions[1]);
                int[] stats3 = LoLStatsUtils.getChampionStats(matches, summoner, top3Champions[2]);

                setBestChampionData(activity, stats1, 1, top3Champions[0]);
                setBestChampionData(activity, stats2, 2, top3Champions[1]);
                setBestChampionData(activity, stats3, 3, top3Champions[2]);

            }else{
                showErrorMessage(activity);
            }

        }

        private void showData(MainActivity activity){
            TextView errorMessageDisplay = activity.findViewById(R.id.tv_error_message);
            errorMessageDisplay.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage(MainActivity activity){
            TextView errorMessageDisplay = activity.findViewById(R.id.tv_error_message);

            recyclerView.setVisibility(View.INVISIBLE);
            errorMessageDisplay.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.INVISIBLE);
        }

        private void setMostPlayedRoles(MainActivity activity, String[] roles, List<Match> listOfGames, Summoner summoner){

            TextView winRateView1 = activity.findViewById(R.id.tv_role_win_rate1);
            TextView playRateView1 = activity.findViewById(R.id.tv_role_played1);
            TextView roleNameView1 = activity.findViewById(R.id.tv_role_name_1);

            TextView winRateView2 = activity.findViewById(R.id.tv_role_win_rate2);
            TextView playRateView2 = activity.findViewById(R.id.tv_role_played2);
            TextView roleNameView2 = activity.findViewById(R.id.tv_role_name_2);

            TextView winRateView3 = activity.findViewById(R.id.tv_role_win_rate3);
            TextView playRateView3 = activity.findViewById(R.id.tv_role_played3);
            TextView roleNameView3 = activity.findViewById(R.id.tv_role_name_3);

            ImageView roleIcon1 = activity.findViewById(R.id.iv_role_1);
            ImageView roleIcon2 = activity.findViewById(R.id.iv_role_2);
            ImageView roleIcon3 = activity.findViewById(R.id.iv_role_3);

            LoLStatsUtils.setRoleImage(roleIcon1, roles[0]);
            LoLStatsUtils.setRoleImage(roleIcon2, roles[1]);
            LoLStatsUtils.setRoleImage(roleIcon3, roles[2]);

            roleNameView1.setText(roles[0]);
            roleNameView2.setText(roles[1]);
            roleNameView3.setText(roles[2]);

            double[] stats1 = LoLStatsUtils.getRoleStats(listOfGames, summoner, roles[0]);

            Log.d(LOG_TAG, "Played: " + stats1[0] + " Won: " + stats1[1]);

            String winRate1 = String.format(Locale.ENGLISH, "%.1f", stats1[1]).concat("%");
            String playRate1 = String.valueOf((int) stats1[0]).concat("%");
            winRateView1.setText(winRate1);
            playRateView1.setText(playRate1);

            double[] stats2 = LoLStatsUtils.getRoleStats(listOfGames, summoner, roles[1]);

            String winRate2 = String.format(Locale.ENGLISH, "%.1f", stats2[1]).concat("%");
            String playRate2 = String.valueOf((int) stats2[0]).concat("%");
            winRateView2.setText(winRate2);
            playRateView2.setText(playRate2);

            double[] stats3 = LoLStatsUtils.getRoleStats(listOfGames, summoner, roles[2]);

            String winRate3 = String.format(Locale.ENGLISH, "%.1f", stats3[1]).concat("%");
            String playRate3 =String.valueOf((int) stats3[0]).concat("%");
            winRateView3.setText(winRate3);
            playRateView3.setText(playRate3);

        }

        //TODO meter en stats el championid
        private void setBestChampionData(MainActivity activity, int[] stats, int championRank, Champion champion){

            ImageView championView;
            TextView championCs;
            TextView kdaChampionView;
            TextView gamesPlayedView;
            TextView gamesWonView;

            if(championRank == 1){
                championView = activity.findViewById(R.id.iv_champion1_image);
                championCs = activity.findViewById(R.id.tv_champion1_cs);
                kdaChampionView = activity.findViewById(R.id.tv_champion1_kda);
                gamesPlayedView = activity.findViewById(R.id.tv_champion1_games_played);
                gamesWonView = activity.findViewById(R.id.tv_champion1_win_rate);
            }else if(championRank == 2){
                championView = activity.findViewById(R.id.iv_champion2_image);
                championCs = activity.findViewById(R.id.tv_champion2_cs);
                kdaChampionView = activity.findViewById(R.id.tv_champion2_kda);
                gamesPlayedView = activity.findViewById(R.id.tv_champion2_games_played);
                gamesWonView = activity.findViewById(R.id.tv_champion2_win_rate);
            }else{
                championView = activity.findViewById(R.id.iv_champion3_image);
                championCs = activity.findViewById(R.id.tv_champion3_cs);
                kdaChampionView = activity.findViewById(R.id.tv_champion3_kda);
                gamesPlayedView = activity.findViewById(R.id.tv_champion3_games_played);
                gamesWonView = activity.findViewById(R.id.tv_champion3_win_rate);
            }

            champion.loadImageFromDDragon(championView);

            int numberOfKills = stats[0];
            int numberOfDeaths = stats[1];
            int numberOfAssists = stats[2];
            int totalMinionsKilled = stats[3];
            int gamesPlayed = stats[4];
            int gamesWon = stats[5];
            Log.d(LOG_TAG, "Games won: " + gamesWon);

            double averageMinionsKilled = (double) totalMinionsKilled / (double) gamesPlayed;
            double averageKills = (double) numberOfKills / (double) gamesPlayed;
            double averageDeaths = (double) numberOfDeaths / (double) gamesPlayed;
            double averageAssists = (double) numberOfAssists / (double) gamesPlayed;

            championCs.setText(String.valueOf(averageMinionsKilled));

            double kdaChampion = LoLStatsUtils.calculateKDA(averageKills, averageAssists, averageDeaths);
            LoLStatsUtils.setKdaAndTextColorInView(kdaChampionView, kdaChampion);

            double winRate = ((double) gamesWon / (double) gamesPlayed) *100;
            Log.d(LOG_TAG, "WR: " + winRate + " dividiendo " + gamesWon + " entre "+ gamesPlayed);

            gamesPlayedView.setText("Games: " + String.valueOf(gamesPlayed));
            gamesWonView.setText(String.valueOf((int) winRate).concat("%"));

        }

        private SpannableString generateCenterSpannableText() {

            SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by klosote");
            s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length(), 0);
            return s;
        }

        private void setData(int wins) {

            ArrayList<PieEntry> values = new ArrayList<>();

            int losses = 20- wins;

            values.add(new PieEntry(wins, "Victories"));
            values.add(new PieEntry(losses, "Defeats"));

            PieDataSet dataSet = new PieDataSet(values, "Win/Losses of last Games");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            int[] colors = {
                    rgb("#4286f4"), rgb("#f44d41")};
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(7f);
            data.setValueTextColor(Color.WHITE);
            chart.setData(data);

            chart.invalidate();
        }



    }


    private static class ReadTextTask extends AsyncTask<URL, Void, String> {

        OnTaskCompleted listener;

        ReadTextTask(OnTaskCompleted listener){
            this.listener=listener;
        }

        @Override
        protected String doInBackground(URL... urls) {
            String str = null;
            try {
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(urls[0].openStream()));
                str = in.readLine();
                in.close();
            }
            catch (IOException e) {
                // ** do something here **
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("ReadTextTask", "resultado" + result);

            String dataType = null;

            try {
                dataType = JsonUtils.getDataTypeFromJSON(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listener.onTaskCompleted(result, dataType);

        }

    }

}
