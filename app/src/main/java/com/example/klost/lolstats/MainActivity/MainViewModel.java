package com.example.klost.lolstats.MainActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.models.matches.Match;
import com.example.klost.lolstats.models.matches.MatchList;
import com.example.klost.lolstats.models.matches.matchtimeline.MatchTimeline;
import com.example.klost.lolstats.utilities.JsonUtils;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.google.common.util.concurrent.RateLimiter;

import java.net.URL;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    private final SummonerLiveData summonerMutableLiveData;
    private final URL riotSearchUrl;

    public MainViewModel(URL url){
        Log.d(LOG_TAG, "Creo un nuevo viewmodel");
        this.riotSearchUrl = url;
        this.summonerMutableLiveData = new SummonerLiveData();
    }

    public LiveData<Summoner>  getData(){
        return summonerMutableLiveData;
    }


    public class SummonerLiveData extends LiveData<Summoner>{

        private RateLimiter throttler;
        private int MATCHLIST_SIZE = 20;

        public SummonerLiveData(){
            throttler = RateLimiter.create(0.7);
            loadData();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadData(){
            new AsyncTask<URL, Void, Summoner>(){

                @Override
                protected Summoner doInBackground(URL... urls) {
                    URL searchURL = urls[0];
                    String summonerSearchResults;
                    String matchListSearchResults;
                    String matchSearchResults;
                    String matchTimelineSearchResults;
                    Summoner summoner = null;
                    MatchList matchList;
                    try {
                        summonerSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL, throttler);
                        Log.d(LOG_TAG, "summonerSearchResults: " + summonerSearchResults);
                        if(summonerSearchResults.charAt(0) != '{'){
                            //Entonces significa que la respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error.
                            Log.e(LOG_TAG, "Error: La respuesta no es un JSON y getResponseFromHttpUrl ha devuelto un Error");
                            return null;
                        }

                        summoner = JsonUtils.getSummonerFromJSON(summonerSearchResults);
                        URL leaguesURL = NetworkUtils.buildUrl(String.valueOf(summoner.getEncryptedSummonerId()), NetworkUtils.GET_LEAGUES_POSITIONS);
                        if(leaguesURL != null){
                            String leaguesSearchResult = NetworkUtils.getResponseFromHttpUrl(leaguesURL, throttler);
                            LeaguePositionList positionList = JsonUtils.getLeaguePositionListFromJSON(leaguesSearchResult);
                            summoner.setPositionList(positionList);
                        }

                        URL matchListURL = NetworkUtils.buildUrl(String.valueOf(summoner.getEncryptedAccountId()), NetworkUtils.GET_MATCHLIST);
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
                                    JsonUtils.getMatchFromJSON(matchSearchResults, match);

                                    URL getMatchTimelineURL = NetworkUtils.buildUrl(String.valueOf(match.getGameId()), NetworkUtils.GET_MATCH_TIMELINE);
                                    if(getMatchTimelineURL != null){
                                        matchTimelineSearchResults = NetworkUtils.getResponseFromHttpUrl(getMatchTimelineURL, throttler);
                                        MatchTimeline matchTimeline = JsonUtils.getMatchTimeLine(matchTimelineSearchResults);
                                        match.setMatchTimeline(matchTimeline);
                                        Log.d(LOG_TAG, "Procesado con exito");
                                    }
                                }else{
                                    Log.d(LOG_TAG, "Devuelvo null en getMatchURL");
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
                    setValue(summoner);
                }
            }.execute(riotSearchUrl);
        }

    }

}
