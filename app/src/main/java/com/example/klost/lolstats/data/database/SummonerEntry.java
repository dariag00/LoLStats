package com.example.klost.lolstats.data.database;

import android.util.Log;
import android.widget.ImageView;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.leagueposition.LeaguePosition;
import com.example.klost.lolstats.models.leagueposition.LeaguePositionList;
import com.example.klost.lolstats.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_summoner")
public class SummonerEntry{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String puuid;
    private String summonerName;
    private String accoundId;
    private String summonerId;
    private int profileIconId;
    private long summonerLevel;

    @Embedded(prefix = "solo_")
    private LeaguePosition soloQ;

    @Embedded(prefix = "flex_")
    private LeaguePosition flexQ;

    @Embedded(prefix = "flexqtt_")
    private LeaguePosition flexQTT;

    @Ignore
    public SummonerEntry(String puuid, String accoundId, String summonerId) {
        this.puuid = puuid;
        this.accoundId = accoundId;
        this.summonerId = summonerId;
    }

    public SummonerEntry(int id, String puuid, String accoundId, String summonerId) {
        this.id = id;
        this.puuid = puuid;
        this.accoundId = accoundId;
        this.summonerId = summonerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public String getAccoundId() {
        return accoundId;
    }

    public void setAccoundId(String accoundId) {
        this.accoundId = accoundId;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public LeaguePosition getSoloQ() {
        return soloQ;
    }

    public void setSoloQ(LeaguePosition soloQ) {
        this.soloQ = soloQ;
    }

    public LeaguePosition getFlexQ() {
        return flexQ;
    }

    public void setFlexQ(LeaguePosition flexQ) {
        this.flexQ = flexQ;
    }

    public LeaguePosition getFlexQTT() {
        return flexQTT;
    }

    public void setFlexQTT(LeaguePosition flexQTT) {
        this.flexQTT = flexQTT;
    }

    public void loadImageFromDDragon(ImageView imageView) {
        URL url = NetworkUtils.buildUrl(String.valueOf(this.profileIconId), NetworkUtils.GET_DDRAGON_PROFILE_ICON);
        Log.d("Icon Image", "URL: " + url.toString());
        Picasso.get().load(url.toString()).into(imageView);
    }

}
