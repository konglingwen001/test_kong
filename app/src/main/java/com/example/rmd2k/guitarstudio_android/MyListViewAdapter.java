package com.example.rmd2k.guitarstudio_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by CHT1HTSH3236 on 2018/3/29.
 */

public class MyListViewAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater;

    public MyListViewAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return NotesModel.getInstance().getBarNum();
    }

    @Override
    public Object getItem(int position) {
        return NotesModel.getInstance().getBar(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = new GuitarNotesView(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.guitar_notes_view, null);
        }
        View guitarNoteView = convertView.findViewById(R.id.guitarNotesView);
        guitarNoteView.setBackgroundColor(Color.RED);
        return convertView;
    }
}
