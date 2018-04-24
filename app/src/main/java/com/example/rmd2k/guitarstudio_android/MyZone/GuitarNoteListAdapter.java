package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/20.
 */

public class GuitarNoteListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> guitarNoteList;

    public GuitarNoteListAdapter(Context context, ArrayList<String> guitarNoteNames) {
        this.mContext = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        guitarNoteList = guitarNoteNames;
    }

    @Override
    public int getCount() {
        return guitarNoteList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return guitarNoteList.get(position);
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
        TextView tvName = convertView.findViewById(android.R.id.text1);
        if (position == guitarNoteList.size()) {
            tvName.setText("Add GuitarNote");
        } else {
            tvName.setText(guitarNoteList.get(position));
        }
        return convertView;
    }
}
