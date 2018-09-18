package com.example.rmd2k.guitarstudio_android.Tools;

import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;
import com.example.rmd2k.guitarstudio_android.Tools.Game.GameActivity;
import com.example.rmd2k.guitarstudio_android.Tools.PlayOgg.PlayOggActivity;
import com.example.rmd2k.guitarstudio_android.Tools.Tuner.TunerActivity;

public class ToolsFragment extends Fragment {

    Button btnTuner;
    Button btnGame;
    Button btnPlayOgg;

    NotesModel notesModel;

    public ToolsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesModel = NotesModel.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        btnTuner = view.findViewById(R.id.btnTuner);
        btnTuner.setOnClickListener(onTunerClickListener);

        btnGame = view.findViewById(R.id.btnGame);
        btnGame.setOnClickListener(onGameClickListener);


        btnPlayOgg = view.findViewById(R.id.btnPlayOgg);
        btnPlayOgg.setOnClickListener(onPlayOggClickListener);

        return view;
    }



    View.OnClickListener onTunerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), TunerActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onGameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), GameActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onPlayOggClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlayOggActivity.class);
            startActivity(intent);
        }
    };

}
