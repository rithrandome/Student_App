package com.example.studentapp.listeners;

import com.example.studentapp.entities.TodoEntity;

public interface TodoListener {

    void onTodoClicked(TodoEntity todo, int position);
}
