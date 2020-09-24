package com.example.studentapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.studentapp.entities.NotesEntity;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<NotesEntity> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NotesEntity note);

    @Update(entity = NotesEntity.class)
    void updateNote(NotesEntity... note);

    @Delete
    void deleteNode(NotesEntity note);

}
