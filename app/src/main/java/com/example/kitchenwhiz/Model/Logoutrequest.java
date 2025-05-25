package com.example.kitchenwhiz.Model;

public class Logoutrequest {
    String refreshToken;
    public Logoutrequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
}
