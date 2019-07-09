package com.android.todo.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoteDTO {

    @SerializedName("noteId")
    @Expose
    private int noteId;
    @SerializedName("noteName")
    @Expose
    private String noteName;
    @SerializedName("noteDesc")
    @Expose
    private String noteDesc;
    @SerializedName("noteState")
    @Expose
    private int noteState;
    @SerializedName("notePriority")
    @Expose
    private int notePriority;
    @SerializedName("noteDate")
    @Expose
    private long noteDate;

    public NoteDTO() {
    }

    public NoteDTO(int noteId, String noteName, String noteDesc, int noteState, int notePriority, long noteDate) {
        this.noteId = noteId;
        this.noteName = noteName;
        this.noteDesc = noteDesc;
        this.noteState = noteState;
        this.notePriority = notePriority;
        this.noteDate = noteDate;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public int getNoteState() {
        return noteState;
    }

    public void setNoteState(int noteState) {
        this.noteState = noteState;
    }

    public int getNotePriority() {
        return notePriority;
    }

    public void setNotePriority(int notePriority) {
        this.notePriority = notePriority;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
                "noteId=" + noteId +
                ", noteName='" + noteName + '\'' +
                ", noteDesc='" + noteDesc + '\'' +
                ", noteState=" + noteState +
                ", noteDate=" + noteDate +
                '}';
    }
}