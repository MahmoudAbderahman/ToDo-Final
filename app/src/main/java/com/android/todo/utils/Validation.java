package com.android.todo.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class Validation {

    private static final int PASSWORD_LENGTH = 6;

    public static boolean isValidEmailAddress(CharSequence emailAddress) {
        return (!TextUtils.isEmpty(emailAddress) && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches());
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_LENGTH;
    }

    public static boolean isValidConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
