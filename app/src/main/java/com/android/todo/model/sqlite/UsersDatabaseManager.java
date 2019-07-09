package com.android.todo.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.android.todo.model.dto.UserDTO;

import java.util.ArrayList;

public class UsersDatabaseManager {

    private UsersDatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public UsersDatabaseManager(Context c) {
        context = c;
    }

    public UsersDatabaseManager open() throws SQLException {
        dbHelper = new UsersDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String username, String email, String password) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(UsersContract.UsersEntry.COLUMN_USERNAME, username);
        contentValue.put(UsersContract.UsersEntry.COLUMN_EMAIL, email);
        contentValue.put(UsersContract.UsersEntry.COLUMN_PASSWORD, password);
        database.insert(UsersContract.UsersEntry.TABLE_NAME, null, contentValue);
    }

    public ArrayList<UserDTO> fetchAll() {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;
        ArrayList<UserDTO> userDtos = new ArrayList<>();

        String[] columns = new String[]{UsersContract.UsersEntry.COLUMN_USER_ID,
                UsersContract.UsersEntry.COLUMN_USERNAME,
                UsersContract.UsersEntry.COLUMN_EMAIL,
                UsersContract.UsersEntry.COLUMN_PASSWORD};

        cursor = database.query(UsersContract.UsersEntry.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(cursor.getInt(0));
            userDTO.setUsername(cursor.getString(1));
            userDTO.setEmail(cursor.getString(2));
            userDTO.setPassword(cursor.getString(3));

            userDtos.add(userDTO);
        }
        return userDtos;
    }

}
