package com.android.todo.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    static final String DB_NAME = "Users_DataBase.DB";

    // database version
    static final int DB_VERSION = 1;

    public UsersDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "create table " + UsersContract.UsersEntry.TABLE_NAME + "( " +
                UsersContract.UsersEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UsersContract.UsersEntry.COLUMN_USERNAME + " TEXT ," +
                UsersContract.UsersEntry.COLUMN_EMAIL + " TEXT, " +
                UsersContract.UsersEntry.COLUMN_PASSWORD + " TEXT );";
        // Creating table query
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UsersContract.UsersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
