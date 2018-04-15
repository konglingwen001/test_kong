package com.example.rmd2k.guitarstudio_android;

import android.content.Context;
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
        return NotesModel.getInstance(this.context).getBarNum();
    }

    @Override
    public Object getItem(int position) {
        return NotesModel.getInstance(this.context).getBarNoArray().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.guitar_notes_view, null);
        }

        GuitarNotesView guitarNoteView = convertView.findViewById(R.id.guitarNotesView);
        guitarNoteView.setLineNo(position);
        return convertView;
    }

}
