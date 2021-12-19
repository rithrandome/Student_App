package com.example.studentapp.adapters;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.R;
import com.example.studentapp.entities.NotesEntity;
import com.example.studentapp.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<NotesEntity> notes;
    private List<NotesEntity> notesSource;
    private NotesListener notesListener;
    private Timer timer;

    public NotesAdapter(List<NotesEntity> notes, NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;
        this.notesSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.note_element,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {

        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textDateTime;
        LinearLayout layoutNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutNote = itemView.findViewById(R.id.layoutNote);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
        }

        void setNote(NotesEntity note){
            textTitle.setText(note.getTitle());
            textDateTime.setText(note.getDateTime());
        }
    }

    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()){
                    notes = notesSource;
                }else{
                    ArrayList<NotesEntity> temp = new ArrayList<>();
                    for(NotesEntity note: notesSource){
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getContent().toLowerCase().contains(searchKeyword.toLowerCase()))
                            temp.add(note);
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

}
