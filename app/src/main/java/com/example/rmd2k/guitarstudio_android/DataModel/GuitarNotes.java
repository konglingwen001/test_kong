package com.example.rmd2k.guitarstudio_android.DataModel;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class GuitarNotes {

    private String guitarNotesName;
    private String speed;
    private String barNum;
    private String flat;
    private ArrayList<BarNoData> barNoDataArray;

    public GuitarNotes() {
    }

    public GuitarNotes(String guitarNotesName, String playSpeed, String barNum, String flat) {
        this.guitarNotesName = guitarNotesName;
        this.speed = playSpeed;
        this.barNum = barNum;
        this.flat = flat;
    }

    public String getGuitarNotesName() {
        return guitarNotesName;
    }

    public void setGuitarNotesName(String guitarNotesName) {
        this.guitarNotesName = guitarNotesName;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBarNum() {
        return barNum;
    }

    public void setBarNum(String barNum) {
        this.barNum = barNum;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public ArrayList<BarNoData> getBarNoDataArray() {
        return barNoDataArray;
    }

    public void setBarNoDataArray(ArrayList<BarNoData> barNoDataArray) {
        this.barNoDataArray = barNoDataArray;
    }

    @Override
    protected GuitarNotes clone() throws CloneNotSupportedException {

        int barNoDataNum = 0;
        int noteNoDataNum = 0;
        int notesNum = 0;
        BarNoData srcBarNoData;
        NoteNoData srcNoteNoData;
        Note srcNote;
        BarNoData destBarNoData;
        NoteNoData destNoteNoData;
        Note destNote;

        GuitarNotes guitarNotes = new GuitarNotes();
        guitarNotes.setGuitarNotesName(this.getGuitarNotesName());
        guitarNotes.setBarNum(this.getBarNum());
        guitarNotes.setFlat(this.getFlat());
        guitarNotes.setSpeed(this.getSpeed());
        guitarNotes.setBarNoDataArray(new ArrayList<BarNoData>());

        barNoDataNum = this.getBarNoDataArray().size();
        for (int barNo = 0; barNo < barNoDataNum; barNo++) {
            destBarNoData = new BarNoData();
            destBarNoData.setNoteNoDataArray(new ArrayList<NoteNoData>());
            srcBarNoData = this.getBarNoDataArray().get(barNo);
            noteNoDataNum = srcBarNoData.getNoteNoDataArray().size();
            for (int noteNo = 0; noteNo < noteNoDataNum; noteNo++) {
                destNoteNoData = new NoteNoData();
                destNoteNoData.setNoteArray(new ArrayList<Note>());
                srcNoteNoData = this.getBarNoDataArray().get(barNo).getNoteNoDataArray().get(noteNo);
                destNoteNoData.setNoteType(srcNoteNoData.getNoteType());
                notesNum = srcNoteNoData.getNoteArray().size();
                for (int index = 0; index < notesNum; index++) {
                    destNote = new Note();
                    srcNote = srcNoteNoData.getNoteArray().get(index);
                    destNote.setStringNo(srcNote.getStringNo());
                    destNote.setPlayType(srcNote.getPlayType());
                    destNote.setFretNo(srcNote.getFretNo());
                    destNoteNoData.getNoteArray().add(destNote);
                }
                destBarNoData.getNoteNoDataArray().add(destNoteNoData);
            }
            guitarNotes.getBarNoDataArray().add(destBarNoData);
        }

        return guitarNotes;
    }

    @Override
    public boolean equals(Object obj) {
        int oldBarNoDataNum = 0;
        int oldNoteNoDataNum = 0;
        int oldNotesNum = 0;
        int barNoDataNum = 0;
        int noteNoDataNum = 0;
        int notesNum = 0;
        BarNoData oldBarNoData;
        NoteNoData oldNoteNoData;
        Note oldNote;
        BarNoData barNoData;
        NoteNoData noteNoData;
        Note note;

        GuitarNotes oldGuitarNotes = (GuitarNotes)obj;
        if (!(oldGuitarNotes.getGuitarNotesName().equals(this.getGuitarNotesName()) &&
                oldGuitarNotes.getBarNum().equals(this.getBarNum()) &&
                oldGuitarNotes.getFlat().equals(this.getFlat()) &&
                oldGuitarNotes.getSpeed().equals(this.getSpeed()))) {
            return false;
        }

        oldBarNoDataNum = oldGuitarNotes.getBarNoDataArray().size();
        barNoDataNum = this.getBarNoDataArray().size();
        if (oldBarNoDataNum != barNoDataNum) {
            return false;
        }
        for (int barNo = 0; barNo < barNoDataNum; barNo++) {
            oldBarNoData = oldGuitarNotes.getBarNoDataArray().get(barNo);
            barNoData = this.getBarNoDataArray().get(barNo);
            oldNoteNoDataNum = oldBarNoData.getNoteNoDataArray().size();
            noteNoDataNum = barNoData.getNoteNoDataArray().size();
            if (oldNoteNoDataNum != noteNoDataNum) {
                return false;
            }
            for (int noteNo = 0; noteNo < noteNoDataNum; noteNo++) {
                oldNoteNoData = oldBarNoData.getNoteNoDataArray().get(noteNo);
                noteNoData = this.getBarNoDataArray().get(barNo).getNoteNoDataArray().get(noteNo);
                if (!oldNoteNoData.getNoteType().equals(noteNoData.getNoteType())) {
                    return false;
                }
                oldNotesNum = oldNoteNoData.getNoteArray().size();
                notesNum = noteNoData.getNoteArray().size();
                if (oldNotesNum != notesNum) {
                    return false;
                }
                for (int index = 0; index < notesNum; index++) {
                    oldNote = oldNoteNoData.getNoteArray().get(index);
                    note = noteNoData.getNoteArray().get(index);
                    if (!(oldNote.getStringNo().equals(note.getStringNo()) &&
                          oldNote.getPlayType().equals(note.getPlayType()) &&
                          oldNote.getFretNo().equals(note.getFretNo()))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
