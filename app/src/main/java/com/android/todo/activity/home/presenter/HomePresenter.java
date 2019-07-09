package com.android.todo.activity.home.presenter;

import android.content.Context;

import com.android.todo.model.dto.NoteDTO;
import com.android.todo.model.dto.UserNotesDTO;
import com.android.todo.model.sqlite.NotesDataBaseManager;
import com.android.todo.model.sqlite.UsersDatabaseManager;
import com.android.todo.network.NetworkRouter;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePresenter {

    private HomeDelegate view;
    private final List<NoteDTO> notes = new ArrayList<>();
    private ArrayList<NoteDTO> localNotes = new ArrayList<>();
    private NotesDataBaseManager notesDataBaseManager;
    private UsersDatabaseManager usersDatabaseManager;

    public HomePresenter(HomeDelegate view, Context context) {
        this.view = view;
        // SQLite data configuration
        notesDataBaseManager = new NotesDataBaseManager(context);
        notesDataBaseManager.open();
    }

    public void fetchData(){
        // fetching data from SQLite DB
        localNotes = notesDataBaseManager.fetchAll(1);
        /*
        // fetching data from server
        AndroidNetworking.get(NetworkRouter.buildURLRequest("getNotes"))
                .addQueryParameter("userId", "1")
                .addHeaders("Content-Type", "application/json")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        NoteDTO[] navigationArray = new Gson().fromJson(response.toString(), NoteDTO[].class);
                        notes.addAll(Arrays.asList(navigationArray));
                       // view.fetchedData(notes);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error);
                    }
                });

        if(localNotes.size() >= notes.size()) {
            // display local notes
            view.fetchedData(localNotes);


            // delete data on the server
            AndroidNetworking.delete(NetworkRouter.buildURLRequest("deleteAllNotes"))
                    .addQueryParameter("userId", "1")
                    .addHeaders("Content-Type", "application/json")
                    .setTag("test")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                        }
                        @Override
                        public void onError(ANError error) {
                            System.out.println(error);
                        }
                    });

            UserNotesDTO userNotesDTO = new UserNotesDTO();
            userNotesDTO.setUserId(1);
            userNotesDTO.setNotes(localNotes);

            // save all local data on the server
            AndroidNetworking.post(NetworkRouter.buildURLRequest("uploadNotes"))
                    .addBodyParameter(userNotesDTO)
                    .addHeaders("Content-Type", "application/json")
                    .setTag("test")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                            System.out.println(error);
                        }
                    });
        }


            // display remote data
            view.fetchedData(notes);
            // delete all local data
            notesDataBaseManager.deleteAll(1);
            // save all remote data locally
            notesDataBaseManager.insertAll(notes, 1);
            */

    }


}
