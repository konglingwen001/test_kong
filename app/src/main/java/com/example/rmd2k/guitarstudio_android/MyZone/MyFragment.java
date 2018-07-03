package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    private static final int REFRESH_LIST = 0;
    private static final int SET_CLICKABLE = 1;
    private static final int CLICKABLE = 0;
    private static final int NOT_CLICKABLE = 1;

    Context mContext;
    NotesModel notesModel;

    NoteTitleListView lstGuitarNotes;
    GuitarNoteListAdapter adapter;
    private MyHandler myHandler = null;

    public MyFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesModel = NotesModel.getInstance(this.getActivity().getApplicationContext());
        mContext = this.getActivity().getApplicationContext();
        myHandler = new MyHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        lstGuitarNotes = view.findViewById(R.id.lstGuitarNotes);
        lstGuitarNotes.setMyFragment_Handler(myHandler);

        notesModel.copyAssetFilesToFileDir(this.getActivity());

        notesModel.reloadGuitarNotesFiles();

        lstGuitarNotes.setOnItemClickListener(itemClickListener);
        lstGuitarNotes.setLongClickable(false);
        //lstGuitarNotes.setOnItemLongClickListener(itemLongClickListener);
        adapter = new GuitarNoteListAdapter(getContext(), myHandler);
        lstGuitarNotes.setAdapter(adapter);

        return view;
    }

    public static class MyHandler extends Handler {
        private final WeakReference<MyFragment> mFragment;

        private MyHandler(MyFragment mFragment) {
            this.mFragment = new WeakReference<>(mFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MyFragment activity = mFragment.get();
            switch (msg.what) {
                case REFRESH_LIST:
                    activity.adapter.refreshVisibleItem(activity.lstGuitarNotes);
                    break;
                case SET_CLICKABLE:
                    if (msg.arg1 == CLICKABLE) {
                        Log.i("KONG", "clickable");
                        //activity.lstGuitarNotes.setFocusable(true);
                        //activity.lstGuitarNotes.setClickable(true);
                        activity.lstGuitarNotes.setOnItemClickListener(activity.itemClickListener);
                    } else {
                        Log.i("KONG", "not clickable");
                        //activity.lstGuitarNotes.setFocusable(false);
                        //activity.lstGuitarNotes.setClickable(false);
                        activity.lstGuitarNotes.setOnItemClickListener(null);
                    }
                    break;
            }
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mContext, "onItemClick", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.putExtra("fileName", notesModel.getGuitarNotesFile(position));
//            intent.setClass(getActivity(), GuitarNoteViewActivity.class);
//            startActivity(intent);
        }
    };

    List<String> list;

    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            list = new ArrayList<>();
            list.add("删除");
            list.add("重命名");
            ListView listView = new ListView(getContext());
            listView.setBackgroundColor(Color.GRAY);
            listView.setAdapter(new PopupAdapter(getContext(), list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("TAG", list.get(position));
                }
            });

            PopupWindow popupWindow = new PopupWindow(listView, 400, 400);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int xPos = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, xPos, 300);
            return true;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
