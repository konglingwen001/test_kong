package com.example.rmd2k.guitarstudio_android.Tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rmd2k.guitarstudio_android.R;
import com.example.rmd2k.guitarstudio_android.Tools.Game.GameActivity;
import com.example.rmd2k.guitarstudio_android.Tools.Tuner.TunerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ToolsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ToolsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolsFragment extends Fragment {

    Button btnTuner;
    Button btnGame;

    public ToolsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
