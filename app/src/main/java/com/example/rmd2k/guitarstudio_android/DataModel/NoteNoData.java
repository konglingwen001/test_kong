package com.example.rmd2k.guitarstudio_android.DataModel;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class NoteNoData {

    private String noteNo;
    private String noteType;
    private ArrayList<Note> noteArray;

    public NoteNoData(String noteType, ArrayList<Note> noteArray) {
        this.noteType = noteType;
        this.noteArray = noteArray;
    }

    public NoteNoData(String noteNo, String noteType, ArrayList<Note> noteArray) {
        this.noteNo = noteNo;
        this.noteType = noteType;
        this.noteArray = noteArray;
    }

    public NoteNoData() {
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public ArrayList<Note> getNoteArray() {
        return noteArray;
    }

    public void setNoteArray(ArrayList<Note> noteArray) {
        this.noteArray = noteArray;
    }

    public String getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(String noteNo) {
        this.noteNo = noteNo;
    }
}
