package com.android.todo.activity.todo.presenter;

public interface NoteDelegate {

    public abstract void noteAddedSuccessfully();

    public abstract void noteEditedSuccessfully();

    public abstract void showToast(String message);
}
