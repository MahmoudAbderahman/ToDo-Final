package com.android.todo.activity.todo.presenter;

import android.content.Context;
import android.util.Log;

import com.android.todo.model.dto.NoteDTO;
import com.android.todo.model.dto.UserNote;
import com.android.todo.model.sqlite.NotesDataBaseManager;
import com.android.todo.network.NetworkRouter;
import com.android.todo.utils.Utility;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NotePresenter {

    NoteDelegate view;
    private NotesDataBaseManager notesDataBaseManager;


    public NotePresenter(NoteDelegate view, Context context) {
        this.view = view;
        notesDataBaseManager = new NotesDataBaseManager(context);
        notesDataBaseManager.open();
    }

    public void addNewNote(Context mContext, final UserNote userNote) {

        // add note on server
        JSONObject jsonObject = new JSONObject();
        try {

            NoteDTO noteDTO = userNote.getNote();

            JSONObject noteJSON = new JSONObject();
            noteJSON.put("noteName", noteDTO.getNoteName());
            noteJSON.put("noteDesc", noteDTO.getNoteDesc());
            noteJSON.put("noteState", 0);
            noteJSON.put("notePriority", noteDTO.getNotePriority());
            noteJSON.put("noteDate", noteDTO.getNoteDate());

            jsonObject.put("userId", userNote.getUserId());
            jsonObject.put("note", noteJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JSON", userNote.toString());

        if (Utility.isNetworkAvailable(mContext)) {
            AndroidNetworking.post(NetworkRouter.buildURLRequest("addNote"))
                    .addJSONObjectBody(jsonObject)
                    .addHeaders("Content-Type", "application/json")
                    .setTag("test")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int noteId = response.getInt("noteId");
                                NoteDTO note = userNote.getNote();
                                note.setNoteId(noteId);

                                // add note in SQLite DB
                                notesDataBaseManager.insertInOnlineMode(note, userNote.getUserId());
                                view.noteAddedSuccessfully();
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            System.out.println(error);
                            view.showToast(String.valueOf(error.getErrorCode()));
                        }
                    });
        } else {
            // add note in SQLite DB
            NoteDTO note = userNote.getNote();
            notesDataBaseManager.insert(note, userNote.getUserId());
            view.noteAddedSuccessfully();
        }
    }

    public void editNote(UserNote userNote, int noteId) {

        // update note in SQLite DB
        notesDataBaseManager.update(noteId, userNote.getNote(), userNote.getUserId());

        // update note on server
        JSONObject noteJSON = new JSONObject();
        try {
            NoteDTO noteDTO = userNote.getNote();
            noteJSON.put("noteId", noteId);
            noteJSON.put("noteName", noteDTO.getNoteName());
            noteJSON.put("noteDesc", noteDTO.getNoteDesc());
            noteJSON.put("notePriority", noteDTO.getNotePriority());
            noteJSON.put("noteState", noteDTO.getNoteState());
            noteJSON.put("noteDate", Utility.formatDate(new Date(noteDTO.getNoteDate())));

            //jsonObject.put("userId", userNote.getUserId());
            //jsonObject.put("note", noteJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.patch(NetworkRouter.buildURLRequest("updateNote"))
                .addJSONObjectBody(noteJSON)
                .addHeaders("Content-Type", "application/json")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        view.noteEditedSuccessfully();
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.getErrorDetail());
                        view.showToast(String.valueOf(anError.getErrorCode()));
                    }
                });
    }
}
