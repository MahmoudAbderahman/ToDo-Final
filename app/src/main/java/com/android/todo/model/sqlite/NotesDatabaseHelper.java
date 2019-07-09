package com.android.todo.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    static final String DB_NAME = "Notes_DataBase.DB";

    // database version
    static final int DB_VERSION = 1;

    public NotesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME + "( " +
                NotesContract.NotesEntry.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NotesContract.NotesEntry.COLUMN_NOTE_NAME + " TEXT NOT NULL," +
                NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION + " TEXT, " +
                NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY + " INTEGER DEFAULT 0, " +
                NotesContract.NotesEntry.COLUMN_NOTE_ISDONE + " INTEGER DEFAULT 0, " +
                NotesContract.NotesEntry.COLUMN_NOTE_DATE + " TEXT, " +
                NotesContract.NotesEntry.COLUMN_USER_ID + " INTEGER," + " FOREIGN KEY (" +
                NotesContract.NotesEntry.COLUMN_USER_ID + ") REFERENCES " +
                UsersContract.UsersEntry.TABLE_NAME + "(" + UsersContract.UsersEntry.COLUMN_USER_ID + ")" + " );";

        // Creating table query
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotesContract.NotesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
