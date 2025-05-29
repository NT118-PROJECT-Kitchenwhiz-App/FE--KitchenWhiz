package com.example.kitchenwhiz.Model;

public class AccessTokenResponse {
    String accessToken;
    String accessTokenExpire;
    public AccessTokenResponse(String accessToken, String accessTokenExpire){
        this.accessToken = accessToken;
        this.accessTokenExpire = accessTokenExpire;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenExpire() {
        return accessTokenExpire;
    }
}
