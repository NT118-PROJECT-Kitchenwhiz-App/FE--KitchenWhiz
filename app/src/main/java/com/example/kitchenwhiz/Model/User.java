package com.example.kitchenwhiz.Model;

import java.io.Serializable;

public class User implements Serializable {
    String _id;
    String username;
    String accessToken;
    String email;
    String refreshToken;
    String accessTokenExpire;

    public User(String id, String username, String accesstoken, String email, String refreshToken, String accessTokenExpire) {
        this._id = id;
        this.email = email;
        this.accessToken = accesstoken;
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessTokenExpire = accessTokenExpire;
    }

    public String getId() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccesstoken() {
        return accessToken;
    }

    public String getUsername() {
        return username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    public String getAccessTokenExpire(){
        return accessTokenExpire;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
