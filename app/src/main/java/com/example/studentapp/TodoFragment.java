package com.example.studentapp;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.studentapp.adapters.NotesAdapter;
import com.example.studentapp.database.TodoDatabase;
import com.example.studentapp.entities.TodoEntity;
import com.example.studentapp.listeners.TodoListener;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment implements TodoListener {

    private AlertDialog addTodoDialog;
    private boolean todoElementClicked = false;
    private EditText inputTodoTitle;
    private EditText inputTodoDesc;
    private RecyclerView todoRecyclerView;
    private List<TodoEntity> todoList;
    private TodoEntity alreadyAvailableTodo;
    private RecyclerView.Adapter todoAdapter;

    private int todoClickedPosition = -1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_todo,container,false);

        inputTodoDesc = v.findViewById(R.id.textTodoDescription);
        inputTodoTitle = v.findViewById(R.id.textTodoTitle);

        ImageView addImage = v.findViewById(R.id.image_add_todo);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(v);
            }
        });

        todoRecyclerView = v.findViewById(R.id.todo_recycler_view);
        todoRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        todoList = new ArrayList<>();
        todoAdapter = new NotesAdapter(todoList);
        todoRecyclerView.setAdapter(todoAdapter);

        getTodo();

        return v;
    }

    private void showDialog(View v) {
        if (addTodoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_add_todo,
                    (ViewGroup) v.findViewById(R.id.addTodoContainer));
            builder.setView(view);
            addTodoDialog = builder.create();
            if (addTodoDialog.getWindow() != null) {
                addTodoDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
            }
            view.findViewById(R.id.addTodo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveTodo();
                }
            });

            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTodoDialog.dismiss();
                }
            });



        }
    }

    private void setUpdateTodo(){
        inputTodoTitle.setText(alreadyAvailableTodo.getTitle());
        inputTodoDesc.setText(alreadyAvailableTodo.getDescription());

    }

    private void saveTodo(){
        if(inputTodoTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), "Todo title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }else if(inputTodoDesc.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Todo can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final TodoEntity todo = new TodoEntity();

        todo.setTitle(inputTodoTitle.getText().toString());
        todo.setDescription(inputTodoDesc.getText().toString());

        if(alreadyAvailableTodo == null){
            TodoDatabase.getDatabase(getActivity().getApplicationContext()).todoDao().insertTodo(todo);
        }else {
            todo.setId(alreadyAvailableTodo.getId());
            TodoDatabase.getDatabase(getActivity().getApplicationContext()).todoDao().updateTodo(todo);
        }

        addTodoDialog.dismiss();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (addTodoDialog != null) {
            addTodoDialog.dismiss();
            addTodoDialog = null;
        }
    }

    @Override
    public void onTodoClicked(TodoEntity todo, int position) {

        todoClickedPosition = position;
        todoElementClicked = true;
        alreadyAvailableTodo = todo;

    }
}
