package com.example.food_delivery_service.auth;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context ctx;

    public SessionManager(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveAuthToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getAuthToken() {
        return prefs.getString("token", "");
    }
}
