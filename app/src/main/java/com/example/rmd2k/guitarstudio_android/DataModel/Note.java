package com.example.rmd2k.guitarstudio_android.DataModel;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class Note {

    private String stringNo;
    private String fretNo;
    private String playType;

    public Note(String stringNo, String fretNo, String playType) {
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

    @Override
    public boolean equals(Object obj) {
        Note note = (Note)obj;
        if (!this.stringNo.equals(note.getStringNo())) {
            return false;
        }
        return true;
    }
}
