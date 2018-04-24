package com.example.rmd2k.guitarstudio_android.DataModel;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class BarNoData {

    private ArrayList<NoteNoData> noteNoDataArray;

    public BarNoData(ArrayList<NoteNoData> noteNoArray) {
        this.noteNoDataArray = noteNoArray;
    }

    public BarNoData() {
    }

    public ArrayList<NoteNoData> getNoteNoDataArray() {
        return noteNoDataArray;
    }

    public void setNoteNoDataArray(ArrayList<NoteNoData> noteNoArray) {
        this.noteNoDataArray = noteNoArray;
    }
}
