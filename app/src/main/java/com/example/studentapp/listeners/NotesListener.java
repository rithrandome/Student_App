package com.example.studentapp.listeners;

import com.example.studentapp.entities.NotesEntity;

public interface NotesListener {

    void onNoteClicked(NotesEntity note, int position);
}

