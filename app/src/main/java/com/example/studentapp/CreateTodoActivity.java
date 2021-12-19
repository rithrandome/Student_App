package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentapp.database.TodoDatabase;
import com.example.studentapp.entities.TodoEntity;

public class CreateTodoActivity extends AppCompatActivity {


    private EditText inputTodoTitle, inputTodoDesc;
    private TodoEntity alreadyAvailableTodo;
    private AlertDialog dialogDeleteTodo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        this.setFinishOnTouchOutside(true);

        inputTodoDesc = findViewById(R.id.textTodoDescription);
        inputTodoTitle = findViewById(R.id.textTodoTitle);
        TextView cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().getBooleanExtra("todoClicked", false)) {
            alreadyAvailableTodo = (TodoEntity) getIntent().getSerializableExtra("todo");
            setUpdateTodo();


        }

        ImageView saveTodo = findViewById(R.id.addTodo);
        saveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });
    }

    private void setUpdateTodo(){
        inputTodoTitle.setText(alreadyAvailableTodo.getTitle());
        inputTodoDesc.setText(alreadyAvailableTodo.getDescription());

    }

    private void saveTodo(){
        if(inputTodoTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Todo title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final TodoEntity todo = new TodoEntity();

        todo.setTitle(inputTodoTitle.getText().toString());
        todo.setDescription(inputTodoDesc.getText().toString());

        if(alreadyAvailableTodo == null){
            TodoDatabase.getDatabase(getApplicationContext()).todoDao().insertTodo(todo);
        }else {
            todo.setId(alreadyAvailableTodo.getId());
            TodoDatabase.getDatabase(getApplicationContext()).todoDao().updateTodo(todo);
        }

        Intent intent = new Intent(CreateTodoActivity.this, TodoFragment.class);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialogDeleteTodo != null) {
            dialogDeleteTodo.dismiss();
            dialogDeleteTodo = null;
        }
    }

}
