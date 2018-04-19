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
}
