package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.MyZone.SwipeMenuListView.SwipeMenu;
import com.example.rmd2k.guitarstudio_android.MyZone.SwipeMenuListView.SwipeMenuCreator;
import com.example.rmd2k.guitarstudio_android.MyZone.SwipeMenuListView.SwipeMenuItem;
import com.example.rmd2k.guitarstudio_android.MyZone.SwipeMenuListView.SwipeMenuListView;
import com.example.rmd2k.guitarstudio_android.R;

import java.util.List;

public class MyFragment extends Fragment {

    private static final int HIDE_ALL_BTNDELETE = 0;
    private static final int SET_CLICKABLE = 1;
    private static final int CLICKABLE = 0;
    private static final int NOT_CLICKABLE = 1;

    Context mContext;
    NotesModel notesModel;

    Button btnCreateGuitarNotes;
    GuitarNoteListAdapter adapter;

    private SwipeMenuListView lstGuitarNotes;

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

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set item title
                deleteItem.setTitle("Delete");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lstGuitarNotes.setMenuCreator(creator);

        // step 2. listener item click event
        lstGuitarNotes.setOnMenuItemClickListener(onMenuItemClickListener);

        // set SwipeListener
        lstGuitarNotes.setOnSwipeListener(onSwipeListener);

        // set MenuStateChangeListener
        lstGuitarNotes.setOnMenuStateChangeListener(onMenuStateChangeListener);

        btnCreateGuitarNotes.setOnClickListener(onClickListener);

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO
        }
    };

    SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            String guitarNotes = notesModel.getGuitarNotesFile(position);
            switch (index) {
                case 0:
                    // delete
                    notesModel.deleteGuitarNotes(guitarNotes, position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    };

    SwipeMenuListView.OnSwipeListener onSwipeListener = new SwipeMenuListView.OnSwipeListener() {

        @Override
        public void onSwipeStart(int position) {
            // swipe start
        }

        @Override
        public void onSwipeEnd(int position) {
            // swipe end
        }
    };

    SwipeMenuListView.OnMenuStateChangeListener onMenuStateChangeListener = new SwipeMenuListView.OnMenuStateChangeListener() {
        @Override
        public void onMenuOpen(int position) {
        }

        @Override
        public void onMenuClose(int position) {
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

}
