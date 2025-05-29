package com.example.kitchenwhiz.Util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.kitchenwhiz.Activity.Home;
import com.example.kitchenwhiz.Activity.Login;

import java.sql.Time;

public class JWT {
    private String accessToken;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "KitchenWhizToken";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_TOKEN_EXPIRY = "token_expiry";
    public JWT(Context context) {
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public void saveToken(String accessToken, String expireTime) {
        if (accessToken != null) {
            long expiryTimestamp = calculateExpiry(expireTime);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_ACCESS_TOKEN, accessToken);
            editor.putLong(KEY_TOKEN_EXPIRY, expiryTimestamp);
            editor.apply();
        } else {
            Log.d("JWT", "Loi Token");
        }
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.apply();
    }

    public long changeTime(String time) {
        if (time == null) return 0;
        try {
            String num = time.replaceAll("[^0-9]", "");
            String unit = time.replaceAll("[0-9]", "").toLowerCase();
            int value = Integer.parseInt(num);

            switch(unit){
                case "s": return value;
                case "m": return value*60;
                case "h": return value*3600;
                default: return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public long calculateExpiry(String time){
        long expireSeconds = changeTime(time);
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds + expireSeconds;
    }
    public boolean isTokenExpired(String token){
        String acesstoken = getToken();
        if (token == null) return true;
        long expiryTimestamp = sharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0);
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if (expiryTimestamp > 0 && expiryTimestamp < currentTimeSeconds) {
            return true;
        }

        return false;
    }
}
