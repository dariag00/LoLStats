package com.example.klost.lolstats.utilities;

import java.net.URL;

public class ReadTaskToken {
    private URL url;
    private String token;
    private String response;

    public ReadTaskToken(URL url, String token){
        this.token = token;
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
