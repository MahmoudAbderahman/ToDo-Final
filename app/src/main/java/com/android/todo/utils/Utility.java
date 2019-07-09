package com.android.todo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String formatTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aa");
        return dateFormat.format(date);
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
