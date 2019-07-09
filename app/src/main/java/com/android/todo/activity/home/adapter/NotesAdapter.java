package com.android.todo.activity.home.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.todo.R;
import com.android.todo.activity.home.HomeActivity;
import com.android.todo.activity.todo.NoteActivity;
import com.android.todo.model.dto.NoteDTO;
import com.android.todo.model.sqlite.NotesDataBaseManager;
import com.android.todo.network.NetworkRouter;
import com.android.todo.utils.SharedPreferencesUtility;
import com.android.todo.utils.Utility;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Fragment mFragment;
    private Context mContext;
    private List<NoteDTO> notes;
    private NotesDataBaseManager notesDataBaseManager;

    public NotesAdapter(Fragment mFragment, Context mContext, List<NoteDTO> notes) {
        this.mFragment = mFragment;
        this.mContext = mContext;
        this.notes = notes;
        notesDataBaseManager = new NotesDataBaseManager(mContext);
        notesDataBaseManager.open();
    }

    public void updateList(List<NoteDTO> notes) {
        this.notes = notes;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NoteDTO note = notes.get(position);
        if (note != null) {
            if (note.getNoteState() > 0) {
                holder.finishButton.setVisibility(View.GONE);
            }
            if (note.getNoteDate() < new Date().getTime() && note.getNoteState() == 0) {
                holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
            }

            holder.prioritySwitch.setChecked(note.getNotePriority() > 0);
            if (holder.prioritySwitch.isChecked()) {
                holder.priorityTextView.setText("High");
            } else
                holder.priorityTextView.setText("Low");

            holder.noteNameTextView.setText(note.getNoteName());

            String date = Utility.formatDate(new Date(note.getNoteDate()));
            holder.noteDateTextView.setText(date);

            String time = Utility.formatTime(new Date(note.getNoteDate()));
            holder.noteTimeTextView.setText(time);

            //holder.tripStartPointTextView.setText(trip.getStartPoint());
            //holder.tripEndPointTextView.setText(trip.getEndPoint());

//            Glide
//                    .with(mContext)
//                    .load(Const.IMAGE_URL + males.getMales_image_path())
//                    .placeholder(R.drawable.mealplaceholder)
//                    .override(80, 80)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .bitmapTransform(new CropCircleTransformation(mContext))
//                    .into(holder.maleImage);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView noteNameTextView;
        private TextView noteDateTextView;
        private TextView noteTimeTextView;
        private TextView priorityTextView;
        private ImageView editImageView;
        private ImageView deleteNoteImageView;
        private Switch prioritySwitch;
        private Button finishButton;
        private View parent;

        public ViewHolder(View view) {
            super(view);
            parent = view;
            noteNameTextView = view.findViewById(R.id.note_name_text_view);
            noteDateTextView = view.findViewById(R.id.note_date_text_view);
            noteTimeTextView = view.findViewById(R.id.note_time_text_view);
            priorityTextView = view.findViewById(R.id.priority_text_view);
            editImageView = view.findViewById(R.id.edit_image_view);
            deleteNoteImageView = view.findViewById(R.id.delete_note_image_view);
            prioritySwitch = view.findViewById(R.id.priority_switch);
            finishButton = view.findViewById(R.id.finish_button);

            editImageView.setOnClickListener(this);
            deleteNoteImageView.setOnClickListener(this);
            itemView.setOnClickListener(this);
            finishButton.setOnClickListener(this);
            prioritySwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.finish_button:
                    int position = getAdapterPosition();
                    NoteDTO noteDTO = notes.get(position);
                    noteDTO.setNoteState(1);
                    try {
                        updateNote(noteDTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    removeAt(position);
                    ((HomeActivity) mContext).finishedNoteFragment.loadPageContent();
                    break;
                case R.id.priority_switch:
                    NoteDTO noteDTo = notes.get(getAdapterPosition());
                    noteDTo.setNotePriority(prioritySwitch.isChecked() ? 1 : 0);
                    if (prioritySwitch.isChecked()) {
                        priorityTextView.setText("High");
                    } else
                        priorityTextView.setText("Low");

                    try {
                        updateNote(noteDTo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.edit_image_view:
                    NoteDTO note = notes.get(getAdapterPosition());
                    redirectNoteScreen(note);
                    break;
                case R.id.delete_note_image_view:
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete This Note")
                            .setMessage("Are you sure you want to delete this note?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NoteDTO note = notes.get(getAdapterPosition());
                                    //delete note in SQLite DB
                                    notesDataBaseManager.delete(note.getNoteId(), SharedPreferencesUtility.getUserId(mContext));
                                    removeAt(getAdapterPosition());
                                    updateList(notes);

                                    // delete note on server
                                    AndroidNetworking.delete(NetworkRouter.buildURLRequest("delete"))
                                            .addQueryParameter("noteId", note.getNoteId() + "")
                                            .addHeaders("Content-Type", "application/json")
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // do anything with response
                                                    System.out.println(response);
//                                    removeAt(getAdapterPosition());
//                                    updateList(notes);

                                                }

                                                @Override
                                                public void onError(ANError error) {
                                                    // handle error
                                                    System.out.println(error.getErrorBody());
                                                }
                                            });
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
            }
        }

        private void redirectNoteScreen(NoteDTO note) {
            Intent intent = new Intent(mContext, NoteActivity.class);
            intent.putExtra("actionType", 2);
            intent.putExtra("noteId", note.getNoteId());
            intent.putExtra("noteName", note.getNoteName());
            intent.putExtra("noteDesc", note.getNoteDesc());
            intent.putExtra("notePriority", note.getNotePriority());
            intent.putExtra("noteDate", note.getNoteDate());
            mContext.startActivity(intent);
        }

        public void removeAt(int position) {
            notes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notes.size());
        }

        public void updateNote(NoteDTO noteDTO) throws JSONException {
            //update note in SQLite DB
            notesDataBaseManager.update(noteDTO.getNoteId(), noteDTO, SharedPreferencesUtility.getUserId(mContext));
            updateList(notes);
            /*
            JSONObject noteJSON = new JSONObject();
            noteJSON.put("noteId", noteDTO.getNoteId());
            noteJSON.put("noteName", noteDTO.getNoteName());
            noteJSON.put("noteDesc", noteDTO.getNoteDesc());
            noteJSON.put("notePriority", noteDTO.getNotePriority());
            noteJSON.put("noteState", noteDTO.getNoteState());
            noteJSON.put("noteDate", noteDTO.getNoteDate());

            // delete note on server
            AndroidNetworking.patch(NetworkRouter.buildURLRequest("updateNote"))
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(noteJSON)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            System.out.println(response);
//                                    removeAt(getAdapterPosition());
//                                    updateList(notes);

                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            System.out.println(error.getErrorBody());
                        }
                    });
                    */
        }
    }
}