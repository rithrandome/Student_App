 package com.example.studentapp.adapters;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.R;
import com.example.studentapp.entities.TodoEntity;
import com.example.studentapp.listeners.TodoListener;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>{

    private List<TodoEntity> todo;
    private TodoListener todoListener;


    public TodoAdapter(List<TodoEntity> todo, TodoListener todoListener) {
        this.todo = todo;
        this.todoListener = todoListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.todo_element,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoViewHolder holder, final int position) {
        holder.setTodo(todo.get(position));
        holder.layoutTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position", String.valueOf(position));
                todoListener.onTodoClicked(todo.get(position), position);
            }
        });
        holder.deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position - delete", String.valueOf(position));
                todoListener.onTodoDelete(todo.get(position),position);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkBoxAction(holder.checkBox.isChecked());
                Log.e("position - checkBox", String.valueOf(position)+ holder.checkBox.isChecked());
                todoListener.onCheckBoxStateChanged(todo.get(position),position, holder.checkBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return todo.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder{

        TextView todoTitle, todoDesc;
        RelativeLayout layoutTodo;
        CheckBox checkBox;
        RelativeLayout deleteTodo;


        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            layoutTodo = itemView.findViewById(R.id.layoutTodo);
            todoTitle = itemView.findViewById(R.id.todoTitle);
            todoDesc = itemView.findViewById(R.id.todoDescription);
            deleteTodo = itemView.findViewById(R.id.deleteTodo);

        }


        void setTodo(TodoEntity todo){
            todoTitle.setText(todo.getTitle());
            todoDesc.setText(todo.getDescription());
            checkBox.setChecked(todo.isCompleted());
            checkBoxAction(todo.isCompleted());
        }

        void checkBoxAction(boolean checkBoxState){
            if(checkBoxState){
                todoTitle.setTextColor(itemView.getResources().getColor(R.color.colorTextHint));
                todoDesc.setTextColor(itemView.getResources().getColor(R.color.colorTextHint));
                todoTitle.setPaintFlags(todoTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                todoDesc.setPaintFlags(todoDesc.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            }
            else{
                todoTitle.setTextColor(itemView.getResources().getColor(R.color.colorWhite));
                todoDesc.setTextColor(itemView.getResources().getColor(R.color.colorWhite));
                todoTitle.setPaintFlags(todoTitle.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG));
                todoDesc.setPaintFlags(todoDesc.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG));

            }
        }

    }

}
