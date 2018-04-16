package com.example.rmd2k.guitarstudio_android.DataModel;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class BarNoData {

    private int barNo;
    private ArrayList<NoteNoData> noteNoDataArray;

    public BarNoData(int barNo, ArrayList<NoteNoData> noteNoArray) {
        this.barNo = barNo;
        this.noteNoDataArray = noteNoArray;
    }

    public int getBarNo() {
        return barNo;
    }

    public void setBarNo(int barNo) {
        this.barNo = barNo;
    }

    public ArrayList<NoteNoData> getNoteNoDataArray() {
        return noteNoDataArray;
    }

    public void setNoteNoDataArray(ArrayList<NoteNoData> noteNoArray) {
        this.noteNoDataArray = noteNoArray;
    }
}
