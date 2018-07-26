package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;

public class MyFragment extends Fragment {

    Context mContext;
    NotesModel notesModel;

    Button btnCreateGuitarNotes;
    GuitarNoteListAdapter adapter;

    private MySwipeListView lstGuitarNotes;

    public MyFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesModel = NotesModel.getInstance(this.getActivity().getApplicationContext());
        mContext = this.getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        lstGuitarNotes = view.findViewById(R.id.lstGuitarNotes);
        btnCreateGuitarNotes = view.findViewById(R.id.btnCreateGuitarNotes);

        notesModel.copyAssetFilesToFileDir(this.getActivity());
        notesModel.reloadGuitarNotesFiles();
        adapter = new GuitarNoteListAdapter(getContext());
        lstGuitarNotes.setAdapter(adapter);
        lstGuitarNotes.setOnItemClickListener(onItemClickListener);

        btnCreateGuitarNotes.setOnClickListener(onClickListener);

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), GuitarNoteViewActivity.class);
            intent.putExtra("filename", notesModel.getGuitarNotesFile(position));
            startActivity(intent);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

}
