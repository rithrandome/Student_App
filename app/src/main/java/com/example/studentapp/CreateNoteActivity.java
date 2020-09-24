package com.example.studentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.studentapp.database.NotesDatabase;
import com.example.studentapp.entities.NotesEntity;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteContent;
    private TextView textDateTime;
    private ImageView deleteImage;
    private NotesEntity alreadyAvailableNote;
    private AlertDialog dialogDeleteNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        deleteImage = findViewById(R.id.image_delete);

        ImageView imageBack = findViewById(R.id.image_back);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inputNoteContent = findViewById(R.id.note_content);
        inputNoteTitle = findViewById(R.id.note_title);
        textDateTime = findViewById(R.id.text_date_time);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        if(getIntent().getBooleanExtra("noteClicked", false)){
            alreadyAvailableNote = (NotesEntity) getIntent().getSerializableExtra("note");
            setUpdateNote();
        }

        ImageView imageSave = findViewById(R.id.image_save);

        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        if(alreadyAvailableNote != null) {

            deleteImage.setVisibility(View.VISIBLE);
            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteNoteDialog();
                }
            });
        }
    }

    private void setUpdateNote(){
        inputNoteContent.setText(alreadyAvailableNote.getContent());
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        textDateTime.setText(alreadyAvailableNote.getDateTime());

        Log.e("title",inputNoteTitle.getText().toString());
    }

    private void showDeleteNoteDialog() {
        if(dialogDeleteNote == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.deleteNoteContainer));
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if(dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
            }
            view.findViewById(R.id.deleteNoteText).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotesDatabase.getDatabase(getApplicationContext()).notesDao().deleteNode(alreadyAvailableNote);

                    Intent intent = new Intent(CreateNoteActivity.this, NotesFragment.class);
                    intent.putExtra("isNoteDeleted",true);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });

            view.findViewById(R.id.cancelDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDeleteNote.dismiss();
                }
            });
        }

        dialogDeleteNote.show();
    }

    private void saveNote() {
        if(inputNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }else if(inputNoteContent.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final NotesEntity note = new NotesEntity();

        note.setContent(inputNoteContent.getText().toString());
        note.setTitle(inputNoteTitle.getText().toString());
        note.setDateTime(textDateTime.getText().toString());

        Log.e("title",note.getTitle());
        if(alreadyAvailableNote == null){
            NotesDatabase.getDatabase(getApplicationContext()).notesDao().insertNote(note);
        }else {
            note.setId(alreadyAvailableNote.getId());
            NotesDatabase.getDatabase(getApplicationContext()).notesDao().updateNote(note);
        }


        Intent intent = new Intent(CreateNoteActivity.this, NotesFragment.class);
        setResult(RESULT_OK, intent);
        finish();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialogDeleteNote != null) {
            dialogDeleteNote.dismiss();
            dialogDeleteNote = null;
        }
    }
}