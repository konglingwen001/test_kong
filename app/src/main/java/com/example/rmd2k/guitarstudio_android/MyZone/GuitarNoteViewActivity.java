package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;

public class GuitarNoteViewActivity extends AppCompatActivity {


    ListView lstGuitarNoteView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar_note_view);

        mContext = getApplicationContext();
        NotesModel notesModel = NotesModel.getInstance(mContext);
        notesModel.setGuitarNotesWithNotesTitle("天空之城");
        lstGuitarNoteView = findViewById(R.id.lstGuitarNoteView);
        GuitarNoteViewAdapter adapter = new GuitarNoteViewAdapter(mContext);
        lstGuitarNoteView.setAdapter(adapter);
    }
}
