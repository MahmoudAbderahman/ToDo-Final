package com.android.todo.activity.home.presenter;

import com.android.todo.model.dto.NoteDTO;

import java.util.List;

public interface HomeDelegate {

    void fetchedData(List<NoteDTO> notes);
}