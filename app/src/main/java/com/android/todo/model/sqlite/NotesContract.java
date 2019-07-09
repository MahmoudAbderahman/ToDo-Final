package com.android.todo.model.sqlite;

import android.provider.BaseColumns;

public class NotesContract {
    public static final class NotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NOTE_ID = "note_id";
        public static final String COLUMN_NOTE_NAME = "name";
        public static final String COLUMN_NOTE_DESCRIPTION = "description";
        public static final String COLUMN_NOTE_DATE = "date";
        public static final String COLUMN_NOTE_PRIORITY = "note_priority";
        public static final String COLUMN_NOTE_ISDONE = "isDone";
        public static final String COLUMN_USER_ID = "user_Id";
    }
}
