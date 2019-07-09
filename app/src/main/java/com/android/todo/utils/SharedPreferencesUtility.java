package com.android.todo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtility {

    public static final String LOGGED_IN_PREF = "logged_in_status";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String FIRST_TIME ="first_time";


    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // saved in SQLite DB
    public static void savedUser(Context context, boolean isSaved){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(FIRST_TIME,isSaved);
        editor.apply();
    }

    public static boolean isSaved(Context context){
        return getPreferences(context).getBoolean(FIRST_TIME,false);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static void saveUserData(Context context, String userName, int userId){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(USER_NAME,userName);
        editor.putInt(USER_ID,userId);
        editor.apply();
    }

    public static int getUserId(Context context){
        return getPreferences(context).getInt(USER_ID,0);
    }

    public static void getUserData(Context context, String userName, int userId){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(USER_NAME,userName);
        editor.putInt(USER_ID,userId);
        editor.apply();
    }
}

