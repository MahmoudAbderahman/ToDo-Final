package com.android.todo.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.android.todo.model.dto.NoteDTO;

import java.util.ArrayList;
import java.util.List;

public class NotesDataBaseManager {

    private NotesDatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public NotesDataBaseManager(Context c) {
        context = c;
    }

    public NotesDataBaseManager open() throws SQLException {
        dbHelper = new NotesDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(NoteDTO noteDTO, int userId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_NAME, noteDTO.getNoteName());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION, noteDTO.getNoteDesc());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_DATE, noteDTO.getNoteDate());
        contentValue.put(NotesContract.NotesEntry.COLUMN_USER_ID, userId);
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_ISDONE, noteDTO.getNoteState());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY, noteDTO.getNotePriority());
        database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValue);
    }

    public void insertInOnlineMode(NoteDTO noteDTO, int userId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_ID, noteDTO.getNoteId());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_NAME, noteDTO.getNoteName());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION, noteDTO.getNoteDesc());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_DATE, noteDTO.getNoteDate());
        contentValue.put(NotesContract.NotesEntry.COLUMN_USER_ID, userId);
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_ISDONE, noteDTO.getNoteState());
        contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY, noteDTO.getNotePriority());
        database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValue);
    }

    public void insertAll(List<NoteDTO> noteDTOS, int userId) {
        ContentValues contentValue = new ContentValues();
        for (int i = 0; i < noteDTOS.size(); i++) {
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_ID, noteDTOS.get(i).getNoteId());
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_NAME, noteDTOS.get(i).getNoteName());
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION, noteDTOS.get(i).getNoteDesc());
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_DATE, noteDTOS.get(i).getNoteDate());
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_ISDONE, noteDTOS.get(i).getNoteState());
            contentValue.put(NotesContract.NotesEntry.COLUMN_USER_ID, userId);
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_ISDONE, noteDTOS.get(i).getNoteState());
            contentValue.put(NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY, noteDTOS.get(i).getNotePriority());
            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValue);
        }
    }

    public ArrayList<NoteDTO> fetchAll(int userId) {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;
        ArrayList<NoteDTO> noteDTOS = new ArrayList<>();

        String[] columns = new String[]{
                NotesContract.NotesEntry.COLUMN_NOTE_ID,
                NotesContract.NotesEntry.COLUMN_NOTE_NAME,
                NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION,
                NotesContract.NotesEntry.COLUMN_NOTE_DATE,
                NotesContract.NotesEntry.COLUMN_NOTE_ISDONE,
                NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY};
        String selection = NotesContract.NotesEntry.COLUMN_USER_ID + "= ? ORDER BY " + NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY + " ASC";
        String[] selectionArgs = {String.valueOf(userId)};

        cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setNoteId(cursor.getInt(0));
            noteDTO.setNoteName(cursor.getString(1));
            noteDTO.setNoteDesc(cursor.getString(2));
            noteDTO.setNoteDate(cursor.getLong(3));

            noteDTO.setNoteState(cursor.getInt(4));
            noteDTO.setNotePriority(cursor.getInt(5));

            noteDTOS.add(noteDTO);
        }
        return noteDTOS;
    }

    public ArrayList<NoteDTO> fetchUpcomingNotes(int userId) {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;
        ArrayList<NoteDTO> noteDTOS = new ArrayList<>();

        String[] columns = new String[]{NotesContract.NotesEntry.COLUMN_NOTE_ID,
                NotesContract.NotesEntry.COLUMN_NOTE_NAME,
                NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION,
                NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY,
                NotesContract.NotesEntry.COLUMN_NOTE_DATE};
        String selection = NotesContract.NotesEntry.COLUMN_USER_ID + "= ? AND " + NotesContract.NotesEntry.COLUMN_NOTE_ISDONE + " = 0  ORDER BY " + NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY + " ASC";
        String[] selectionArgs = {String.valueOf(userId)};

        cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setNoteId(cursor.getInt(0));
            noteDTO.setNoteName(cursor.getString(1));
            noteDTO.setNoteDesc(cursor.getString(2));
            noteDTO.setNotePriority(cursor.getInt(3));
            noteDTO.setNoteDate(cursor.getLong(4));
            noteDTOS.add(noteDTO);
        }
        return noteDTOS;
    }

    public ArrayList<NoteDTO> fetchFinishedNotes(int userId) {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;
        ArrayList<NoteDTO> noteDTOS = new ArrayList<>();

        String[] columns = new String[]{NotesContract.NotesEntry.COLUMN_NOTE_ID,
                NotesContract.NotesEntry.COLUMN_NOTE_NAME,
                NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION,
                NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY,
                NotesContract.NotesEntry.COLUMN_NOTE_DATE};
        String selection = NotesContract.NotesEntry.COLUMN_USER_ID + "= ? AND " + NotesContract.NotesEntry.COLUMN_NOTE_ISDONE + " = 1  ORDER BY " + NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY + " ASC";
        String[] selectionArgs = {String.valueOf(userId)};

        cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setNoteId(cursor.getInt(0));
            noteDTO.setNoteName(cursor.getString(1));
            noteDTO.setNoteDesc(cursor.getString(2));
            noteDTO.setNotePriority(cursor.getInt(3));
            noteDTO.setNoteDate(cursor.getLong(4));
            noteDTOS.add(noteDTO);
        }
        return noteDTOS;
    }

    public ArrayList<NoteDTO> fetchWithNoteState(int userId, int isDone) {

        database = dbHelper.getReadableDatabase();
        Cursor cursor;
        ArrayList<NoteDTO> noteDTOS = new ArrayList<>();

        String[] columns = new String[]{NotesContract.NotesEntry.COLUMN_NOTE_ID,
                NotesContract.NotesEntry.COLUMN_NOTE_NAME,
                NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION,
                NotesContract.NotesEntry.COLUMN_NOTE_DATE};
        String selection = NotesContract.NotesEntry.COLUMN_USER_ID + "=? AND " + NotesContract.NotesEntry.COLUMN_NOTE_ISDONE + "=? ";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(isDone)};

        cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setNoteId(cursor.getInt(0));
            noteDTO.setNoteName(cursor.getString(1));
            noteDTO.setNoteDesc(cursor.getString(2));
            noteDTO.setNoteDate(cursor.getLong(3));
            noteDTOS.add(noteDTO);
        }
        return noteDTOS;
    }

    public int update(int noteId, NoteDTO note, int userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.NotesEntry.COLUMN_NOTE_NAME, note.getNoteName());
        contentValues.put(NotesContract.NotesEntry.COLUMN_NOTE_DESCRIPTION, note.getNoteDesc());
        contentValues.put(NotesContract.NotesEntry.COLUMN_NOTE_ISDONE, note.getNoteState());
        contentValues.put(NotesContract.NotesEntry.COLUMN_NOTE_PRIORITY, note.getNotePriority());
        contentValues.put(NotesContract.NotesEntry.COLUMN_NOTE_DATE, Long.valueOf(note.getNoteDate()));
        String[] args = new String[]{String.valueOf(noteId), String.valueOf(userId)};

        int i = database.update(NotesContract.NotesEntry.TABLE_NAME, contentValues, NotesContract.NotesEntry.COLUMN_NOTE_ID + " =? AND " + NotesContract.NotesEntry.COLUMN_USER_ID + " =?", args);
        return i;
    }

    public int delete(int noteId, int userId) {
        String[] args = new String[]{String.valueOf(noteId), String.valueOf(userId)};
        return database.delete(NotesContract.NotesEntry.TABLE_NAME, NotesContract.NotesEntry.COLUMN_NOTE_ID + " =? AND " + NotesContract.NotesEntry.COLUMN_USER_ID + " =?", args);
    }

    public int deleteAll(int userId) {
        String[] args = new String[]{String.valueOf(userId)};
        return database.delete(NotesContract.NotesEntry.TABLE_NAME, NotesContract.NotesEntry.COLUMN_USER_ID + " =?", args);
    }
}
