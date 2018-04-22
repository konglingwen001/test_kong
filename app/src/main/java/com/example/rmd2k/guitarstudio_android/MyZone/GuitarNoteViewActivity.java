package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.NoteEditView;
import com.example.rmd2k.guitarstudio_android.R;

import java.lang.ref.WeakReference;

public class GuitarNoteViewActivity extends AppCompatActivity {


    ListView lstGuitarNoteView;
    NoteEditView noteEditView;
    Context mContext;
    GuitarNoteViewAdapter adapter;
    private final MyHandler myHandler = new MyHandler(this);

    boolean editMode = false;
    Drawable oldSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar_note_view);

        mContext = getApplicationContext();
        NotesModel notesModel = NotesModel.getInstance(mContext);
        notesModel.setGuitarNotesWithNotesTitle("天空之城");
        lstGuitarNoteView = findViewById(R.id.lstGuitarNoteView);
        noteEditView = findViewById(R.id.noteEditView);
        adapter = new GuitarNoteViewAdapter(mContext, myHandler);
        lstGuitarNoteView.setAdapter(adapter);

        // 保存ListView点击效果
        oldSelector = lstGuitarNoteView.getSelector();
    }

    public static class MyHandler extends Handler {
        private final WeakReference<GuitarNoteViewActivity> mActivity;

        private MyHandler(GuitarNoteViewActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            GuitarNoteViewActivity activity = mActivity.get();
            activity.adapter.refreshVisibleItem(activity.lstGuitarNoteView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guitar_note_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                if (!editMode) {
                    editMode = true;
                    noteEditView.setMaxHeight(getWindow().getDecorView().getHeight());
                    lstGuitarNoteView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                } else {
                    editMode = false;
                    noteEditView.setMaxHeight(0);
                    lstGuitarNoteView.setSelector(oldSelector);
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
