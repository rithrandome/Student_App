package com.example.studentapp;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.studentapp.adapters.NotesAdapter;
import com.example.studentapp.database.NotesDatabase;
import com.example.studentapp.entities.NotesEntity;
import com.example.studentapp.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;


import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment implements NotesListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private List<NotesEntity> notesList;

    private int noteClickedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_notes,container,false);

        ImageView addNote = view.findViewById(R.id.image_add_note);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);

            }
        });

        notesRecyclerView = view.findViewById(R.id.recycler_view);

        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES, false);

        EditText searchBar = view.findViewById(R.id.inputSearch);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(notesList.size() !=0)notesAdapter.searchNotes(editable.toString());
            }
        });

        return view;
    }

    @Override
    public void onNoteClicked(NotesEntity note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
        intent.putExtra("noteClicked", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);

    }
    private void getNotes(int requestCode, boolean isNotedDeleted) {

        List<NotesEntity> notes = NotesDatabase.getDatabase(getActivity().getApplicationContext()).notesDao().getAllNotes();

        if (requestCode == REQUEST_CODE_SHOW_NOTES) {
            notesList.addAll(notes);
            notesAdapter.notifyDataSetChanged();
        } else if(requestCode == REQUEST_CODE_ADD_NOTE){
            notesList.add(0, notes.get(0));
            notesAdapter.notifyItemInserted(0);
            notesRecyclerView.smoothScrollToPosition(0);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
            notesList.remove(noteClickedPosition);
            if(isNotedDeleted){
                notesAdapter.notifyItemRemoved(noteClickedPosition);
            }else{
                notesList.add(noteClickedPosition, notes.get(noteClickedPosition));
                notesAdapter.notifyItemChanged(noteClickedPosition);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if(data!=null)getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
        }
    }
}
