package com.example.rmd2k.guitarstudio_android.DataModel;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/16.
 */

public class GuitarNotes {

    private String guitarNotesName;
    private int playSpeed;
    private int barNum;
    private String flat;
    private ArrayList<BarNoData> barNoDataArray;

    public GuitarNotes(String guitarNotesName, int playSpeed, int barNum, String flat) {
        this.guitarNotesName = guitarNotesName;
        this.playSpeed = playSpeed;
        this.barNum = barNum;
        this.flat = flat;
    }

    public String getGuitarNotesName() {
        return guitarNotesName;
    }

    public void setGuitarNotesName(String guitarNotesName) {
        this.guitarNotesName = guitarNotesName;
    }

    public int getPlaySpeed() {
        return playSpeed;
    }

    public void setPlaySpeed(int playSpeed) {
        this.playSpeed = playSpeed;
    }

    public int getBarNum() {
        return barNum;
    }

    public void setBarNum(int barNum) {
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
}
