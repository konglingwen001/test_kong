package com.example.rmd2k.guitarstudio_android.DataModel;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class Note {

    private int barNo;
    private int noteNo;
    private int stringNo;
    private int fretNo;
    private int playType;

    public Note(int barNo, int noteNo, int stringNo, int fretNo, int playType) {
        this.barNo = barNo;
        this.noteNo = noteNo;
        this.stringNo = stringNo;
        this.fretNo = fretNo;
        this.playType = playType;
    }

    public int getStringNo() {
        return stringNo;
    }

    public void setStringNo(int stringNo) {
        this.stringNo = stringNo;
    }

    public int getFretNo() {
        return fretNo;
    }

    public void setFretNo(int fretNo) {
        this.fretNo = fretNo;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int getBarNo() {
        return barNo;
    }

    public void setBarNo(int barNo) {
        this.barNo = barNo;
    }

    public int getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(int noteNo) {
        this.noteNo = noteNo;
    }
}
