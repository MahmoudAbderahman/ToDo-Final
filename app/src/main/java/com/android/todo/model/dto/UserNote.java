package com.android.todo.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserNote {

    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("note")
    @Expose
    private NoteDTO note;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public NoteDTO getNote() {
        return note;
    }

    public void setNote(NoteDTO note) {
        this.note = note;
    }
}
