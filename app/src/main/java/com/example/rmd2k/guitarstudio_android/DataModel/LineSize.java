package com.example.rmd2k.guitarstudio_android.DataModel;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/4/19.
 */

public class LineSize {

    private int startBarNo;
    private int barNum;
    private ArrayList<Float> barWidthArray;
    private float minimWidth;
    private float crotchetaWidth;
    private float quaverWidth;
    private float demiquaverWidth;

    public LineSize() {
    }

    public int getStartBarNo() {
        return startBarNo;
    }

    public void setStartBarNo(int startBarNo) {
        this.startBarNo = startBarNo;
    }

    public int getBarNum() {
        return barNum;
    }

    public void setBarNum(int barNum) {
        this.barNum = barNum;
    }

    public ArrayList<Float> getBarWidthArray() {
        return barWidthArray;
    }

    public void setBarWidthArray(ArrayList<Float> barWidthArray) {
        this.barWidthArray = barWidthArray;
    }

    public float getMinimWidth() {
        return minimWidth;
    }

    public void setMinimWidth(float minimWidth) {
        this.minimWidth = minimWidth;
    }

    public float getCrotchetaWidth() {
        return crotchetaWidth;
    }

    public void setCrotchetaWidth(float crotchetaWidth) {
        this.crotchetaWidth = crotchetaWidth;
    }

    public float getQuaverWidth() {
        return quaverWidth;
    }

    public void setQuaverWidth(float quaverWidth) {
        this.quaverWidth = quaverWidth;
    }

    public float getDemiquaverWidth() {
        return demiquaverWidth;
    }

    public void setDemiquaverWidth(float demiquaverWidth) {
        this.demiquaverWidth = demiquaverWidth;
    }

    public float getNoteWidth(String noteType) {
        float currentWidth = 0;
        switch (noteType) {
            case NotesModel.TYPE_MINIM:
                currentWidth = minimWidth;
                break;
            case NotesModel.TYPE_CROTCHET:
                currentWidth = crotchetaWidth;
                break;
            case NotesModel.TYPE_QUAVER:
                currentWidth = quaverWidth;
                break;
            case NotesModel.TYPE_DEMIQUAVER:
                currentWidth =  demiquaverWidth;
                break;
        }
        return currentWidth;
    }
}
