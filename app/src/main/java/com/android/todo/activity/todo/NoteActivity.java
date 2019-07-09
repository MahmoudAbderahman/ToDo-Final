package com.android.todo.activity.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.todo.R;
import com.android.todo.activity.todo.presenter.NoteDelegate;
import com.android.todo.activity.todo.presenter.NotePresenter;
import com.android.todo.model.dto.NoteDTO;
import com.android.todo.model.dto.UserNote;
import com.android.todo.model.sqlite.NotesDataBaseManager;
import com.android.todo.utils.EditTextDatePicker;
import com.android.todo.utils.EditTextTimePicker;
import com.android.todo.utils.SharedPreferencesUtility;
import com.android.todo.utils.Utility;

import java.sql.SQLOutput;
import java.util.Date;

public class NoteActivity extends AppCompatActivity implements NoteDelegate {

    private EditText mNoteNameEditText;
    private EditText mNoteDescEditText;
    private EditText mNoteDueDateEditText;
    private EditText mNoteTimeEditText;
    private EditTextDatePicker datePicker;
    private EditTextTimePicker timePicker;
    private Spinner spinner;
    private NotePresenter notePresenter;
    private int actionType;
    private int noteId;
    private NotesDataBaseManager notesDataBaseManager;
    private int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        notePresenter = new NotePresenter(this, this);
        actionType = getIntent().getExtras().getInt("actionType");
        noteId = getIntent().getExtras().getInt("noteId");

        notesDataBaseManager = new NotesDataBaseManager(this);
        notesDataBaseManager.open();
        initializeViews();
        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

        if (actionType == 2) {
            String noteName = getIntent().getExtras().getString("noteName");
            String noteDesc = getIntent().getExtras().getString("noteDesc");
            int notePriority = getIntent().getExtras().getInt("notePriority");
            String date = Utility.formatDate(new Date(getIntent().getExtras().getLong("noteDate")));

            mNoteNameEditText.setText(noteName);
            mNoteDescEditText.setText(noteDesc);
            spinner.setSelection(notePriority);
            mNoteDueDateEditText.setText(date);
            String time = Utility.formatTime(new Date(getIntent().getExtras().getLong("noteDate")));
            mNoteTimeEditText.setText(time);
        }
    }

    public void addItemsOnSpinner() {
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.priority_array));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        priority = 0;
                        break;
                    case 1:
                        priority = 1;
                        break;
                    case 2:
                        priority = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_done:
                switch (actionType) {
                    case 1:
                        addNote();
                        break;
                    case 2:
                        editNote(noteId);
                        break;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNote() {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setNoteName(mNoteNameEditText.getText().toString());
        noteDTO.setNoteDesc(mNoteDescEditText.getText().toString());
        noteDTO.setNotePriority(priority);
        //TimePicker selected = timePicker.getPickedDate();
        noteDTO.setNoteDate(timePicker.getPickedDate(datePicker.getPickedDate()));

        UserNote userNote = new UserNote();
        userNote.setNote(noteDTO);
        userNote.setUserId(SharedPreferencesUtility.getUserId(this));
        notePresenter.addNewNote(this, userNote);
    }

    private void editNote(int noteId) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setNoteName(mNoteNameEditText.getText().toString());
        noteDTO.setNoteDesc(mNoteDescEditText.getText().toString());
        noteDTO.setNotePriority(priority);
        noteDTO.setNoteDate(timePicker.getPickedDate(datePicker.getPickedDate()));
        UserNote userNote = new UserNote();
        userNote.setNote(noteDTO);
        userNote.setUserId(SharedPreferencesUtility.getUserId(this));
        notePresenter.editNote(userNote, noteId);
    }

    private void initializeViews() {
        mNoteNameEditText = findViewById(R.id.noteName);
        mNoteDescEditText = findViewById(R.id.noteDesc);
        mNoteDueDateEditText = findViewById(R.id.note_due_date_edit_text);
        mNoteTimeEditText = findViewById(R.id.note_time_edit_text);


        datePicker = new EditTextDatePicker(this, R.id.note_due_date_edit_text);
        timePicker = new EditTextTimePicker(this, R.id.note_time_edit_text);
    }

    @Override
    public void noteAddedSuccessfully() {
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
    }

    @Override
    public void noteEditedSuccessfully() {
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
