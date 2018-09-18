package com.example.rmd2k.guitarstudio_android.Tools.PlayOgg;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;

public class PlayOggActivity extends AppCompatActivity {

    NotesModel notesModel;
    ListView lstOggFiles;
    Button btnStop;

    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ogg);

        notesModel = NotesModel.getInstance(getApplicationContext());
        notesModel.initOggFileList("main");

        mInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        lstOggFiles = findViewById(R.id.lstOggFiles);
        MyAdapter myAdapter = new MyAdapter();
        lstOggFiles.setAdapter(myAdapter);
        lstOggFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notesModel.playNote(position);
            }
        });

        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notesModel.getOggFiles().size();
        }

        @Override
        public Object getItem(int position) {
            return notesModel.getOggFiles().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
            }
            TextView tvFileName = convertView.findViewById(android.R.id.text1);
            tvFileName.setTextColor(Color.WHITE);
            tvFileName.setText(notesModel.getOggFiles().get(position));
            return convertView;
        }
    }
}
