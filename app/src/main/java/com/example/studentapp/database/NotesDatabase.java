package com.example.studentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;

import com.example.studentapp.dao.NotesDao;
import com.example.studentapp.entities.NotesEntity;

@Database(entities = NotesEntity.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getDatabase(Context context) {
        if(notesDatabase == null){
            notesDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase.class,
                    "notes_db"
            ).allowMainThreadQueries().build();
        }
        return notesDatabase;
    }

    public abstract NotesDao notesDao();
}
