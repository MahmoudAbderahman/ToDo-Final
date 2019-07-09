package com.android.todo.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserNotesDTO {

    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("notes")
    @Expose
    private List<NoteDTO> notes;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDTO> notes) {
        this.notes = notes;
    }
}