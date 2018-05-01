package com.example.rmd2k.guitarstudio_android.MyZone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.DataModel.BarNoData;
import com.example.rmd2k.guitarstudio_android.DataModel.EditNoteInfo;
import com.example.rmd2k.guitarstudio_android.DataModel.Note;
import com.example.rmd2k.guitarstudio_android.DataModel.NoteNoData;
import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.NoteEditView;
import com.example.rmd2k.guitarstudio_android.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GuitarNoteViewActivity extends AppCompatActivity {

    private static final String TAG = "GuitarNoteViewActivity";

    ListView lstGuitarNoteView;
    NoteEditView noteEditView;
    Context mContext;
    GuitarNoteViewAdapter adapter;
    NotesModel notesModel;
    private final MyHandler myHandler = new MyHandler(this);

    boolean isNoteChanged = false;

    boolean editMode = false;
    Drawable oldSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar_note_view);

        mContext = getApplicationContext();
        notesModel = NotesModel.getInstance(mContext);
        notesModel.setGuitarNotesWithNotesTitle(getIntent().getStringExtra("GuitarNoteName"));
        lstGuitarNoteView = findViewById(R.id.lstGuitarNoteView);
        noteEditView = findViewById(R.id.noteEditView);
        adapter = new GuitarNoteViewAdapter(mContext, myHandler);
        lstGuitarNoteView.setAdapter(adapter);

        lstGuitarNoteView.setOnScrollListener(scrollListener);

        // 保存ListView点击效果
        oldSelector = lstGuitarNoteView.getSelector();
    }

    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING ||
                    scrollState == SCROLL_STATE_TOUCH_SCROLL) {

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View my_view = lstGuitarNoteView.getChildAt(visibleItemCount - 1);
            if (my_view == null) {
                return;
            }
            int bottom = my_view.getBottom();
            Log.d(TAG, "bottom : " + bottom);
        }
    };

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
            case R.id.menu_save:
                if (notesModel.isNoteChanged()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("保存");
                    builder.setMessage("吉他谱已更改，是否保存？");
                    final EditText et = new EditText(mContext);
                    et.setHint(notesModel.getGuitarNoteName());
                    builder.setView(et);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notesModel.saveGuitarNotes(et.getText().toString());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeNoteType(View view) {
        String tag = view.getTag().toString();
        notesModel.changeEditNoteType(tag);

        refreshGuitarNoteView();
    }

    public void changeFretNo(View view) {
        int pushNum = Integer.parseInt(view.getTag().toString());
        notesModel.changeEditNoteFretNo(pushNum);

        refreshGuitarNoteView();
    }

    public void addBlankBarNo(View view) {
        int barNo = notesModel.getCurrentEditNote().getBarNo();
        notesModel.addBlankBarNoData(barNo);

        refreshGuitarNoteView();
    }

    public void removeBarNoData(View view) {
        EditNoteInfo editNoteInfo = notesModel.getCurrentEditNote();
        int barNo = editNoteInfo.getBarNo();
        notesModel.removeBarNoData(barNo);

        refreshGuitarNoteView();
    }

    public void addNoteNo(View view) {
        EditNoteInfo editNoteInfo = notesModel.getCurrentEditNote();
        int barNo = editNoteInfo.getBarNo();
        int noteNo = editNoteInfo.getNoteNo();

        notesModel.addBlankNoteNoData(barNo, noteNo + 1);

        refreshGuitarNoteView();
    }

    public void removeNoteNoData(View view) {
        EditNoteInfo editNoteInfo = notesModel.getCurrentEditNote();
        int barNo = editNoteInfo.getBarNo();
        int noteNo = editNoteInfo.getNoteNo();

        notesModel.removeNoteNoData(barNo, noteNo);

        refreshGuitarNoteView();
    }

    public void finishEdit(View view) {

    }

    public void cancelEdit(View view) {

    }

    private void refreshGuitarNoteView() {
        Log.d(TAG, "before calc lineCount : " + notesModel.getNotesSizeArray().size());
        notesModel.calNotesSize();
        Log.d(TAG, "after calc lineCount : " + notesModel.getNotesSizeArray().size());

        // remove bar之后重新加载listview
        adapter.notifyDataSetChanged();

        myHandler.sendEmptyMessage(0);
    }
}
