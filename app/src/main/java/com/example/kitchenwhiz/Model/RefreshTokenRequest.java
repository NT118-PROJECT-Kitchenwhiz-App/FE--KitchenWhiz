package com.example.kitchenwhiz.Model;

public class RefreshTokenRequest {
    String refreshToken;
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
