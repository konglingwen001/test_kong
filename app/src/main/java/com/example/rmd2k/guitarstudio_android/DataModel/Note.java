package com.example.rmd2k.guitarstudio_android.DataModel;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class Note {

    private String barNo;
    private String noteNo;
    private String stringNo;
    private String fretNo;
    private String playType;

    public Note(String barNo, String noteNo, String stringNo, String fretNo, String playType) {
        this.barNo = barNo;
        this.noteNo = noteNo;
        this.stringNo = stringNo;
        this.fretNo = fretNo;
        this.playType = playType;
    }

    public Note() {
    }

    public String getStringNo() {
        return stringNo;
    }

    public void setStringNo(String stringNo) {
        this.stringNo = stringNo;
    }

    public String getFretNo() {
        return fretNo;
    }

    public void setFretNo(String fretNo) {
        this.fretNo = fretNo;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getBarNo() {
        return barNo;
    }

    public void setBarNo(String barNo) {
        this.barNo = barNo;
    }

    public String getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(String noteNo) {
        this.noteNo = noteNo;
    }

    @Override
    public boolean equals(Object obj) {
        Note note = (Note)obj;
        if (!this.barNo.equals(note.getBarNo())) {
            return false;
        }
        if (!this.noteNo.equals(note.getNoteNo())) {
            return false;
        }
        if (!this.stringNo.equals(note.getStringNo())) {
            return false;
        }
        return true;
    }
}
