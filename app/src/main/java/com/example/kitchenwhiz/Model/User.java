package com.example.kitchenwhiz.Model;

import java.io.Serializable;

public class User implements Serializable {
    String _id;
    String username;
    String accessToken;
    String email;
    String refreshToken;

    public User(String id, String username, String accesstoken, String email, String refreshToken) {
        this._id = id;
        this.email = email;
        this.accessToken = accesstoken;
        this.email = email;
        this.refreshToken = refreshToken;
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
}
