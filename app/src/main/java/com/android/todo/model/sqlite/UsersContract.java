package com.android.todo.model.sqlite;

import android.provider.BaseColumns;

public class UsersContract {
    public static final class UsersEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }
}
