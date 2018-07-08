package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;

/**
 * Created by CHT1HTSH3236 on 2018/4/20.
 */

public class GuitarNoteListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private NotesModel notesModel;
    private Handler myFragment_Handler;
    private NoteTitleListView noteTitleListView;

    public GuitarNoteListAdapter(Context context, MyFragment.MyHandler myHandler, NoteTitleListView noteTitleListView) {
        this.mContext = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myFragment_Handler = myHandler;
        this.noteTitleListView = noteTitleListView;
        notesModel = NotesModel.getInstance(context);
    }

    @Override
    public int getCount() {
        return notesModel.getGuitarNotesFilesNum();
    }

    @Override
    public Object getItem(int position) {
        return notesModel.getGuitarNotesFile(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.note_title_list_view_cell, null);
        }
        SpringBackScrollView svCell = convertView.findViewById(R.id.svCell);
        svCell.setHorizontalScrollBarEnabled(false);
        svCell.setMyFragment_Handler(myFragment_Handler);
        svCell.setParentListView(noteTitleListView);
        TextView tvNoteTitle = convertView.findViewById(R.id.tvNoteTitle);
        int width = getScreenWidth();
        ViewGroup.LayoutParams lp = tvNoteTitle.getLayoutParams();
        lp.width = width;
        tvNoteTitle.setLayoutParams(lp);
        tvNoteTitle.setText(notesModel.getGuitarNotesFile(position));
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        //btnDelete.setMaxWidth(0);

        return convertView;
    }

    int getScreenWidth() {
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


}
