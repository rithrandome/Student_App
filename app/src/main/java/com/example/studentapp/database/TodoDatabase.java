package com.example.studentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studentapp.dao.TodoDao;
import com.example.studentapp.entities.TodoEntity;


@Database(entities = TodoEntity.class, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase todoDatabase;

    public static synchronized TodoDatabase getDatabase(Context context) {
        if(todoDatabase == null){
            todoDatabase = Room.databaseBuilder(
                    context,
                    TodoDatabase.class,
                    "todo_db"
            ).allowMainThreadQueries().build();
        }
        return todoDatabase;
    }

    public abstract TodoDao todoDao();

}
