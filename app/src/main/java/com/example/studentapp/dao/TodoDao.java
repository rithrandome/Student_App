package com.example.studentapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentapp.entities.TodoEntity;

import java.util.List;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo ORDER BY id DESC")
    List<TodoEntity> getAllTodo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodo(TodoEntity todo);

    @Update(entity = TodoEntity.class)
    void updateTodo(TodoEntity... todo);

    @Delete
    void deleteTodo(TodoEntity todo);
}
