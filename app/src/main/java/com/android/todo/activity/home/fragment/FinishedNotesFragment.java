package com.android.todo.activity.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.todo.R;
import com.android.todo.activity.home.adapter.NotesAdapter;
import com.android.todo.model.dto.NoteDTO;
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

public class FinishedNotesFragment extends Fragment {

    private List<NoteDTO> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private RecyclerView recyclerView;

    private static final String TAG = "FinishedNotesFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NotesAdapter(this, getActivity(), notes);
        adapter.updateList(notes);
        recyclerView.setAdapter(adapter);

        loadPageContent();

    }

    public void getData(ArrayList<NoteDTO> noteDTOS) {
        notes = noteDTOS;
        if (adapter != null)
            adapter.updateList(notes);
    }

    public void loadPageContent() {

        adapter.updateList(notes);
        recyclerView.setAdapter(adapter);
    /*
        AndroidNetworking.get(NetworkRouter.buildURLRequest("getAllFinished"))
                .addQueryParameter("userId", "1")
                .addHeaders("Content-Type", "application/json")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        System.out.println(response);
                        NoteDTO[] navigationArray = new Gson().fromJson(response.toString(), NoteDTO[].class);
                        notes.clear();
                        notes.addAll(Arrays.asList(navigationArray));
                        adapter.updateList(notes);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error);
                    }
                });
                */
    }


}
