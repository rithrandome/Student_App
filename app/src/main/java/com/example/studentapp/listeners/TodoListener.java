package com.example.studentapp.listeners;

import com.example.studentapp.entities.TodoEntity;

public interface TodoListener {

    void onTodoClicked(TodoEntity todo, int position);
    void onTodoDelete(TodoEntity todo, int position);
    void onCheckBoxStateChanged(TodoEntity todo, int position, boolean checkBoxState);
}
