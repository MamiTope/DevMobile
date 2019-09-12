package com.mamitope.projects.devmobile.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mamitope.projects.devmobile.activities.MainActivity;
import com.mamitope.projects.devmobile.activities.SignInActivity;
import com.mamitope.projects.devmobile.models.User;

public class SessionManager {

    private static SessionManager sessionManager;
    private static String preferenceFile = "DobraPreferenceFile";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private int mode = 0;

    private static String keyToken = "Token";
    private static String keyLoggedIn = "LoggedIn";
    private static String keyProfile = "Profile";

    private Gson gson = new Gson();

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(preferenceFile, mode);
        editor = preferences.edit();
        editor.apply();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    public void signIn(String token) {
        editor.putString(keyToken, token);
        editor.putBoolean(keyLoggedIn, true);
        editor.commit();
    }

    public String getToken() {
        return preferences.getString(keyToken, null);
    }

    public void setProfile(User user) {
        String userJson = gson.toJson(user);
        editor.putString(keyProfile, userJson);
        editor.commit();
    }

    public User getProfile() {
        User profile = null;
        String userJson = preferences.getString(keyProfile, null);
        if (userJson != null) {
            profile = gson.fromJson(userJson, User.class);
        }

        return profile;
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(keyLoggedIn, false);
    }

    public void checkLoggedIn() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(context, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void signOut() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
