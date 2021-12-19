package com.example.studentapp;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studentapp.adapters.TodoAdapter;
import com.example.studentapp.database.TodoDatabase;
import com.example.studentapp.entities.TodoEntity;
import com.example.studentapp.listeners.TodoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class TodoFragment extends Fragment implements TodoListener {

    public static final int REQUEST_CODE_ADD_TODO = 1;
    public static final int REQUEST_CODE_UPDATE_TODO = 2;
    public static final int REQUEST_CODE_SHOW_TODO = 3;

    private boolean todoElementClicked = false;
    private EditText inputTodoTitle;
    private EditText inputTodoDesc;

    private RecyclerView todoRecyclerView;
    private List<TodoEntity> todoList;
    private TodoAdapter todoAdapter;
    private AlertDialog dialogDeleteTodo;

    private int todoClickedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_todo,container,false);

        inputTodoDesc = view.findViewById(R.id.textTodoDescription);
        inputTodoTitle = view.findViewById(R.id.textTodoTitle);

        ImageView addImage = view.findViewById(R.id.image_add_todo);

        addImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Log.e("hello","clicked ");
                Intent intent = new Intent(getActivity(), CreateTodoActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TODO);

            }
        });


        todoRecyclerView = view.findViewById(R.id.todo_recycler_view);
        todoRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList, this);
        todoRecyclerView.setAdapter(todoAdapter);

        getTodo(REQUEST_CODE_SHOW_TODO, false);

        return view;
    }


    private void getTodo(int requestCode, boolean isTodoDeleted) {

        List<TodoEntity> todo = TodoDatabase.getDatabase(getActivity().getApplicationContext()).todoDao().getAllTodo();

        if (requestCode == REQUEST_CODE_SHOW_TODO) {
            todoList.addAll(todo);
            todoAdapter.notifyDataSetChanged();
        } else if(requestCode == REQUEST_CODE_ADD_TODO){
            todoList.add(0, todo.get(0));
            todoAdapter.notifyItemInserted(0);
            todoRecyclerView.smoothScrollToPosition(0);
        }else if(requestCode == REQUEST_CODE_UPDATE_TODO){
            todoList.remove(todoClickedPosition);
            if(isTodoDeleted){
                todoAdapter.notifyItemRemoved(todoClickedPosition);
            }else{
                todoList.add(todoClickedPosition, todo.get(todoClickedPosition));
                todoAdapter.notifyItemChanged(todoClickedPosition);
            }
        }

    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void showDialog(View v) {
//        Log.e("hello","called 1 ");
//
//        if (addTodoDialog == null) {
//            Log.e("hello","called 2 ");
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
//            View view = LayoutInflater.from(getActivity()).inflate(
//                    R.layout.activity_create_todo,
//                    (ViewGroup) v.findViewById(R.id.addTodoContainer));
//            builder.setView(view);
//            addTodoDialog = builder.create();
////            if (addTodoDialog.getWindow() != null) {
////                Log.e("hello","called 3 ");
////
////                addTodoDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
////            }
//            view.findViewById(R.id.addTodo).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    saveTodo();
//                }
//            });
//
//            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    addTodoDialog.dismiss();
//                }
//            });
//
//
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_TODO && resultCode == RESULT_OK){
            getTodo(REQUEST_CODE_ADD_TODO, false);
        }else if(requestCode == REQUEST_CODE_UPDATE_TODO && resultCode == RESULT_OK){
            if(data!=null)getTodo(REQUEST_CODE_UPDATE_TODO, data.getBooleanExtra("isTodoDeleted", false));
        }
    }

    @Override
    public void onTodoClicked(TodoEntity todo, int position) {
        todoClickedPosition = position;
        Intent intent = new Intent(getActivity(), CreateTodoActivity.class);
        intent.putExtra("todoClicked", true);
        intent.putExtra("todo", todo);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_TODO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTodoDelete(TodoEntity todo, int position) {
        todoClickedPosition = position;
        showDeleteTodoDialog(todo);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showDeleteTodoDialog(final TodoEntity todo) {
        if(dialogDeleteTodo == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_delete_todo,
                    (ViewGroup) Objects.requireNonNull(getView()).findViewById(R.id.deleteNoteContainer));
            builder.setView(view);
            dialogDeleteTodo = builder.create();
            if(dialogDeleteTodo.getWindow() != null) {
                dialogDeleteTodo.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
            }
            view.findViewById(R.id.deleteTodoText).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TodoDatabase.getDatabase(getActivity()).todoDao().deleteTodo(todo);
                    Intent intent = new Intent(getActivity(), TodoFragment.class);
                    intent.putExtra("isTodoDeleted",true);
                    onActivityResult(REQUEST_CODE_UPDATE_TODO,RESULT_OK,intent);

                    dialogDeleteTodo.dismiss();

                }
            });

            view.findViewById(R.id.cancelDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDeleteTodo.dismiss();
                }
            });
        }

        dialogDeleteTodo.show();
    }

    @Override
    public void onCheckBoxStateChanged(TodoEntity todo, int position, boolean checkBoxState) {
        Log.e("checkBox state", String.valueOf(checkBoxState));
        todo.setCompleted(checkBoxState);
        TodoDatabase.getDatabase(getActivity()).todoDao().updateTodo(todo);

    }
}
