package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;
import com.example.rmd2k.guitarstudio_android.MyZone.GuitarNoteViewActivity.MyHandler;

/**
 * Created by CHT1HTSH3236 on 2018/3/29.
 */

public class GuitarNoteViewAdapter extends BaseAdapter {

    private static final String TAG = "GuitarNoteViewAdapter";

    private Context context;
    private LayoutInflater mInflater;
    private MyHandler myHandler;

    public GuitarNoteViewAdapter(Context context, MyHandler myHandler) {
        this.context = context;
        this.myHandler = myHandler;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshVisibleItem(ListView listView) {
        GuitarNotesViewCell guitarNotesViewCell;
        for (int i = 0; i < listView.getChildCount(); i++) {
            guitarNotesViewCell = listView.getChildAt(i).findViewById(R.id.guitarNotesViewCell);
            guitarNotesViewCell.refresh();
        }
    }

    @Override
    public int getCount() {
        int count = NotesModel.getInstance(this.context).getBarNum();
        //Log.d(TAG, "lineCount : " + count);
        return count;
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
            convertView = mInflater.inflate(R.layout.guitar_notes_view_cell, null);
        }

        GuitarNotesViewCell guitarNoteViewCell = convertView.findViewById(R.id.guitarNotesViewCell);
        guitarNoteViewCell.setLineNo(position);
        guitarNoteViewCell.setMyHandler(myHandler);
        return convertView;
    }



}
