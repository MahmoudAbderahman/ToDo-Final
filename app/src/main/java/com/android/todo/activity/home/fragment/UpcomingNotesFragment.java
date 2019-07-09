package com.android.todo.activity.home.fragment;

import android.content.Context;
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

public class UpcomingNotesFragment extends Fragment {

    private static final String TAG = "UpComingTripsFragment";
    private List<NoteDTO> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        ((HomeActivity) getActivity()).registerDataUpdateListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String url = "https://to-do-web-service.herokuapp.com/note/all";

        adapter = new NotesAdapter(this, getActivity(), notes);
        adapter.updateList(notes);
        recyclerView.setAdapter(adapter);

        AndroidNetworking.get(NetworkRouter.buildURLRequest("getAllUpcoming"))
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

                        notes.addAll(Arrays.asList(navigationArray));
                        adapter.updateList(notes);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error);
                    }
                });
    }

    public void getData(ArrayList<NoteDTO> noteDTOS) {
        notes = noteDTOS;
    }
}