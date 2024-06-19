package com.example.bookdom.tools.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static TokenManager instance = null;
    private String token;

    private TokenManager() {}

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void loadTokenFromPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        this.token = sharedPreferences.getString("auth_token", null);
    }

    public void saveTokenToPreferences(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    public void clearToken() {
        this.token = null;
    }
}
